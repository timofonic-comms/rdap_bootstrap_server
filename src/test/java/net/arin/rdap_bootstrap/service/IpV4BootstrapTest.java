/*
 * Copyright (C) 2013-2016 American Registry for Internet Numbers (ARIN)
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

import static junit.framework.Assert.assertEquals;

import net.ripe.ipresource.IpRange;
import net.ripe.ipresource.IpResource;
import org.junit.Test;

/**
 * @version $Rev$, $Date$
 */
public class IpV4BootstrapTest
{
    @Test
    public void testAllocations() throws Exception
    {
        IpV4Bootstrap v4 = new IpV4Bootstrap();
        v4.loadData( new ResourceFiles() );

        assertEquals( "https://rdap.apnic.net", v4.getServiceUrlsForIpV4( IpResource.parse(
            "1.0.0.0/8" ) ).getHttpsUrl() );
        assertEquals( "https://rdap.apnic.net",
            v4.getServiceUrlsForIpV4( IpResource.parse( "27.0.0.0/8" ) ).getHttpsUrl() );
        assertEquals( "https://rdap.db.ripe.net",
            v4.getServiceUrlsForIpV4( IpResource.parse( "31.0.0.0/8" ) ).getHttpsUrl() );
        assertEquals( "http://rdap.afrinic.net/rdap",
            v4.getServiceUrlsForIpV4( IpResource.parse( "41.0.0.0/8" ) ).getHttpUrl() );
        assertEquals( "https://rdap.lacnic.net/rdap",
            v4.getServiceUrlsForIpV4( IpResource.parse( "177.0.0.0/8" ) ).getHttpsUrl() );
        assertEquals( "https://rdap.db.ripe.net",
            v4.getServiceUrlsForIpV4( IpResource.parse( "188.0.0.0/8" ) ).getHttpsUrl() );
        assertEquals( "https://rdap.lacnic.net/rdap",
            v4.getServiceUrlsForIpV4( IpResource.parse( "191.0.0.0/8" ) ).getHttpsUrl() );

        // Testing for full prefixes
        assertEquals( "https://rdap.lacnic.net/rdap",
            v4.getServiceUrlsForIpV4( IpResource.parse( "177.0.0.0/8" ) ).getHttpsUrl() );

        // Testing for host addresses
        assertEquals( "https://rdap.lacnic.net/rdap",
            v4.getServiceUrlsForIpV4( IpResource.parse( "177.0.0.1/32" ) ).getHttpsUrl() );
        assertEquals( "https://rdap.lacnic.net/rdap",
            v4.getServiceUrlsForIpV4( IpResource.parse( "177.0.0.1/32" ) ).getHttpsUrl() );
    }
}
