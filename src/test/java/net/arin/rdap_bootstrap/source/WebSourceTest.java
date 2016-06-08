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
package net.arin.rdap_bootstrap.source;

import net.arin.rdap_bootstrap.format.Format;
import net.arin.rdap_bootstrap.lookup.Store;
import net.arin.rdap_bootstrap.service.Registry;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FileSource}
 */
public class WebSourceTest
{
    @Test
    public void testDataLoading() throws Exception
    {
        Path tmpDir = Files.createTempDirectory( null );
        Files.write( Paths.get( tmpDir.toString(), "foo.txt" ), Arrays.asList( "foo", "foo" ),
            StandardCharsets.UTF_8 );

        class MockFormat implements Format
        {
            boolean fooRead = false;
            boolean barRead = false;

            @Override
            public void loadData( InputStream inputStream, Store store )
            {
                try
                {
                    String line = new BufferedReader( new InputStreamReader( inputStream, StandardCharsets.UTF_8 ) ).readLine();
                    if( line.equals( "foo" ) )
                    {
                        fooRead = true;
                    }
                    if( line.equals( "bar" ) )
                    {
                        barRead = true;
                    }
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( e );
                }
            }
        }
        Format mockFormat = new MockFormat();

        Properties props = new Properties();
        props.setProperty( "arin.rdapbootstrap.data_directory",tmpDir.toString() );
        props.setProperty( "arin.rdapbootstrap.registry.test_registry.source","http://localhost/foo.txt" );
        props.setProperty( "arin.rdapbootstrap.registry.test_registry.refetch","500" );

        Registry myRegistry = new Registry( "test_registry", "arin.rdapbootstrap.registry.test_registry.", props, new HashMap<String,Registry>() );
        myRegistry.setFormat( mockFormat );

        WebSource webSource = new WebSource();
        myRegistry.setSource( webSource );
        webSource.configFromRegistry( myRegistry );

        // first execution should cause the existing file to load
        webSource.execute( false );
        assertTrue( ((MockFormat)mockFormat).fooRead );

        CloseableHttpClient httpClient = mock( CloseableHttpClient.class );
        HttpGet httpGet = mock( HttpGet.class );
        CloseableHttpResponse httpResponse = mock( CloseableHttpResponse.class );
        StatusLine statusLine = mock( StatusLine.class );
        StringEntity stringEntity = new StringEntity( "bar\nbar\n" );

        when( statusLine.getStatusCode() ).thenReturn( HttpStatus.SC_OK );
        when( httpResponse.getStatusLine() ).thenReturn( statusLine );
        when( httpResponse.getEntity() ).thenReturn( stringEntity );
        when( httpClient.execute( httpGet ) ).thenReturn( httpResponse );
        webSource.setHttpClient( httpClient );
        webSource.setHttpGet( httpGet );

        // the second execution should cause the http fetch which will get a different result
        webSource.execute( false );
        assertTrue( ((MockFormat)mockFormat).barRead );
    }

}
