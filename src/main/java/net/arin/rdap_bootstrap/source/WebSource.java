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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * A source type for a file on the web.
 */
public class WebSource implements Source
{
    private Registry registry;
    private File dataFile;
    private URL url;
    private Timer timer;
    private long refetchMillis;
    private boolean initialRun = false;

    private static Logger logger = Logger.getLogger( "WebSource" );

    @Override
    public void configFromRegistry( Registry registry )
    {
        this.registry = registry;
        String dirName = registry.getProperty( Constants.DATA_DIR_PROP_NAME );
        File dataDirectory = new File( dirName );
        if( !dataDirectory.exists() )
        {
            throw new RuntimeException( "'" + dirName + "' does not exist."  );
        }
        if( !dataDirectory.isDirectory() )
        {
            throw new RuntimeException( "'" + dirName + "' is not a directory."  );
        }
        try
        {
            url = new URL( registry.getProperty( Constants.SOURCE_SUBPROPNAME ) );
        }
        catch ( MalformedURLException e )
        {
            throw new RuntimeException( e );
        }
        String dataFileName = url.getFile();
        String s[] = dataFileName.split( File.pathSeparator );
        dataFileName = s[ s.length - 1 ];
        dataFile = new File( dataDirectory, dataFileName );
        String s2 = registry.getProperty( "refetch" );
        refetchMillis = Long.parseLong( s2 );
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
            if( !initialRun )
            {
                if( dataFile.exists() )
                {
                    InputStream in = new FileInputStream( dataFile );
                    registry.getFormat().loadData( in, registry.getStore() );
                }
                initialRun = true;
            }
            else
            {
                File tmp = File.createTempFile( registry.getName(), "tmp" );
                ReadableByteChannel read = Channels.newChannel( url.openStream() );
                FileOutputStream out = new FileOutputStream( tmp );
                out.getChannel().transferFrom( read, 0L, Long.MAX_VALUE );
                out.close();
                InputStream in = new FileInputStream( tmp );
                registry.getFormat().loadData( in, registry.getStore() );
                // assuming we got this far, let's copy the file from the tmp spot
                FileInputStream fin = new FileInputStream( tmp );
                read = Channels.newChannel( fin );
                out = new FileOutputStream( dataFile );
                out.getChannel().transferFrom( read, 0L, Long.MAX_VALUE );
                out.close();
                fin.close();
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
