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
 *
 */
package net.arin.rdap_bootstrap.service;

import net.arin.rdap_bootstrap.format.Format;
import net.arin.rdap_bootstrap.lookup.Store;
import net.arin.rdap_bootstrap.source.FileSource;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link net.arin.rdap_bootstrap.source.FileSource}
 */
public class FileSourceTest
{
    @Test
    public void testMakeRegistries() throws Exception
    {
        Path tmpDir = Files.createTempDirectory( null );
        InputStream in = getClass().getResourceAsStream( "/entity_bootstrap.json" );
        Files.copy( in, Paths.get( tmpDir.toString(), "entity_bootstrap.json" ) );

        class MockFormat implements Format
        {
            boolean loadDataCommanded = false;
            public boolean isLoadDataCommanded()
            {
                return loadDataCommanded;
            }

            @Override
            public void loadData( InputStream inputStream, Store store )
            {
               loadDataCommanded = true;
            }
        }
        Format mockFormat = new MockFormat();

        Properties props = new Properties();
        props.setProperty( "arin.rdapbootstrap.data_directory",tmpDir.toString() );
        props.setProperty( "arin.rdapbootstrap.registry.test_registry.source","entity_bootstrap.json" );
        props.setProperty( "arin.rdapbootstrap.registry.test_registry.recheck","500" );

        Registry myRegistry = new Registry( "test_registry", "arin.rdapbootstrap.registry.test_registry.", props, new HashMap<String,Registry>() );
        myRegistry.setFormat( mockFormat );

        FileSource fileSource = new FileSource();
        myRegistry.setSource( fileSource );
        fileSource.configFromRegistry( myRegistry );
        fileSource.execute( false );
        assertTrue( ((MockFormat)mockFormat).isLoadDataCommanded() );
    }
}
