/*
 * Copyright (C) 2016 American Registry for Internet Numbers (ARIN)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package net.arin.rdap_bootstrap.source;

import net.arin.rdap_bootstrap.Constants;
import net.arin.rdap_bootstrap.service.Registry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * A source type for a file on the web.
 */
public class WebSource implements Source
{
    private Registry registry;
    private Path dataFile;
    private URL url;
    private Timer timer;
    private long refetchMillis;
    private boolean initialRun = false;

    private CloseableHttpClient httpClient;
    private HttpGet httpGet;

    private static Logger logger = Logger.getLogger( "WebSource" );

    @Override
    public void configFromRegistry( Registry registry )
    {
        this.registry = registry;
        String dirName = registry.getProperties().getProperty( Constants.DATA_DIR_PROP_NAME );
        if( dirName == null || dirName.isEmpty() )
        {
            throw new RuntimeException( "Property for data directory does not exist" );
        }
        Path dataDirectory = Paths.get( dirName );
        if( !Files.exists( dataDirectory ) )
        {
            throw new RuntimeException( "'" + dirName + "' does not exist."  );
        }
        if( !Files.isDirectory( dataDirectory ) )
        {
            throw new RuntimeException( "'" + dirName + "' is not a directory."  );
        }
        try
        {
            url = new URL( registry.getProperty( Constants.SOURCE_SUBPROPNAME ) );
            httpGet = new HttpGet( url.toURI() );
            httpClient = HttpClients.createDefault();
        }
        catch ( MalformedURLException | URISyntaxException e )
        {
            throw new RuntimeException( e );
        }
        String dataFileName = url.getFile();
        String s[] = dataFileName.split( File.pathSeparator );
        dataFileName = s[ s.length - 1 ];
        dataFile = Paths.get( dataDirectory.toString(), dataFileName );
        String s2 = registry.getProperty( "refetch" );
        refetchMillis = Long.parseLong( s2 );
    }

    void setHttpClient( CloseableHttpClient httpClient )
    {
        this.httpClient = httpClient;
    }

    void setHttpGet( HttpGet httpGet )
    {
        this.httpGet = httpGet;
    }

    @Override
    public void execute( boolean background )
    {
        if( !background )
        {
            loadData();
        }
        else
        {
            if( timer != null )
            {
                timer = new Timer();
                timer.schedule( new RefetchTask(), 0L, refetchMillis );
            }
            // else it was already setup to run
        }
    }

    private void loadData()
    {
        try
        {
            // On the initial run, load the data that we have locally if we have it.
            if( !initialRun )
            {
                if( Files.exists( dataFile ) )
                {
                    InputStream in = Files.newInputStream( dataFile );
                    registry.getFormat().loadData( in, registry.getStore() );
                }
                initialRun = true;
            }
            else
            {
                //find a temporary place to put the data
                //do not overwrite what we already have
                Path tmp = Files.createTempFile( registry.getName(), "tmp" );

                //download from web
                CloseableHttpResponse httpResponse = httpClient.execute( httpGet );
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if( statusCode != HttpStatus.SC_OK )
                {
                    logger.warning( "cannot download source: status " + statusCode + " for " + url.toString() );
                }
                else
                {
                    OutputStream out = Files.newOutputStream( tmp );
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if( httpEntity != null )
                    {
                        httpEntity.writeTo( out );
                    }
                    else
                    {
                        logger.warning( "no entity provided in response for " + url.toString() );
                    }
                    out.close();
                    InputStream in = Files.newInputStream( tmp );
                    registry.getFormat().loadData( in, registry.getStore() );
                    in.close();

                    // assuming we got this far, let's move the file from the tmp spot
                    Files.move( tmp, dataFile, StandardCopyOption.REPLACE_EXISTING );
                }
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    private class RefetchTask extends TimerTask
    {

        @Override
        public void run()
        {
            try
            {
                loadData();
            }
            catch ( Exception e )
            {
                logger.severe(
                    "Problem loading source for registry '" + registry.getName() + "': " + e
                        .getMessage() );
            }
        }
    }
}
