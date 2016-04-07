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
package net.arin.rdap_bootstrap.lookup;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;

import java.util.ArrayList;

/**
 * Defines the interfaces for storing data.
 */
public interface Store
{

    interface As
    {
        void store( AsRangeInfo asRangeInfo );
    }

    interface Domain
    {
        ServiceUrls getServiceUrlsForDomain( String domain );
    }

    interface Entity
    {
        ServiceUrls getServiceUrlsForEntity( String entity );
    }

    interface IpV4
    {
        ServiceUrls getServiceUrlsForIpV4( String prefix );
    }

    interface IpV6
    {
        ServiceUrls getServiceUrlsForIpV6( IPv6Address addr );
        ServiceUrls getServiceUrlsForIpV6( IPv6Network net );
    }

    /**
     * A utility class for referencing URLs.
     */
    class ServiceUrls
    {
        private ArrayList<String> urls = new ArrayList<String>();
        private int httpIdx = -1;
        private int httpsIdx = -1;

        public void addUrl( String url )
        {
            if( url != null )
            {
                if( url.endsWith( "/" ) )
                {
                    url = url.substring( 0, url.length() - 1 );
                }
                urls.add( url );
                if( url.startsWith( "https://" ) )
                {
                    httpsIdx = urls.size() -1;
                }
                else if( url.startsWith( "http://" ) )
                {
                    httpIdx = urls.size() - 1;
                }
            }
        }

        public ArrayList<String> getUrls()
        {
            return urls;
        }

        public void setUrls( ArrayList<String> urls )
        {
            this.urls = urls;
        }

        public String getHttpUrl()
        {
            if( httpIdx != -1 ){
                return urls.get( httpIdx );
            }
            //else
            return null;
        }

        public String getHttpsUrl()
        {
            if( httpsIdx != -1 ){
                return urls.get( httpsIdx );
            }
            //else
            return null;
        }
    }
}
