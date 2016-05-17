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
import net.arin.rdap_bootstrap.format.Format;
import net.arin.rdap_bootstrap.lookup.Store;
import net.arin.rdap_bootstrap.service.Registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * A source type for a file on disk
 */
public class FileSource implements Source
{
    private Registry registry;
    private File dataFile;
    private Timer timer;
    private long recheckMillis;

    private static Logger logger = Logger.getLogger( "FileSource" );

    @Override
    public void configFromRegistry( Registry registry )
    {
        this.registry = registry;
        String dirName = registry.getProperties().getProperty( Constants.DATA_DIR_PROP_NAME );
        if( dirName == null || dirName.isEmpty() )
        {
            throw new RuntimeException( "Property for data directory does not exist" );
        }
        File dataDirectory = new File( dirName );
        if( !dataDirectory.exists() )
        {
            throw new RuntimeException( "'" + dirName + "' does not exist."  );
        }
        if( !dataDirectory.isDirectory() )
        {
            throw new RuntimeException( "'" + dirName + "' is not a directory."  );
        }
        String dataFileName = registry.getProperty( Constants.SOURCE_SUBPROPNAME );
        dataFile = new File( dataDirectory, dataFileName );
        if( !dataFile.exists() )
        {
            throw new RuntimeException( "'" + dataFileName + "' does not exist."  );
        }
        if( !dataFile.isFile() )
        {
            throw new RuntimeException( "'" + dataFileName + "' is not a file."  );
        }
        if( !dataFile.canRead() )
        {
            throw new RuntimeException( "'" + dataFileName + "' is not readable."  );
        }
        String s = registry.getProperty( "recheck" );
        recheckMillis = Long.parseLong( s );
    }

    @Override
    public void execute( boolean background )
    {
        // if not backgrounding, just load the data.
        // even if backgrounding, do the initial load in the foreground
        loadData();
        if( background )
        {
            // now schedule it to be read in the background
            if( timer != null )
            {
                timer = new Timer();
                timer.schedule( new RecheckTask(), recheckMillis, recheckMillis );
            }
            // else it was already setup to run
        }
    }

    private void loadData()
    {
        try
        {
            InputStream in = new FileInputStream( dataFile );
            registry.getFormat().loadData( in, registry.getStore() );
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

    private class RecheckTask extends TimerTask
    {
        private boolean isModified( long currentTime, long lastModified )
        {
            if ( ( currentTime - recheckMillis ) < lastModified )
            {
                return true;
            }
            // else
            return false;
        }

        @Override
        public void run()
        {
            boolean load = false;
            long currentTime = System.currentTimeMillis();
            if( isModified( currentTime, dataFile.lastModified() ) )
            {
                load = true;
            }
            if ( load )
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
}
