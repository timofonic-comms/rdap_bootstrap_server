/*
 * Copyright (C) 2013-2015 American Registry for Internet Numbers (ARIN)
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
package net.arin.rdap_bootstrap.lookup;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;
import net.arin.rdap_bootstrap.lookup.Lookup;
import net.arin.rdap_bootstrap.lookup.Store;
import net.arin.rdap_bootstrap.service.Bootstrap;
import net.arin.rdap_bootstrap.service.ResourceFiles;
import net.arin.rdap_bootstrap.service.ResourceFiles.BootFiles;
import net.arin.rdap_bootstrap.service.Rfc7484;

import java.util.Map;
import java.util.TreeMap;

/**
 * @version $Rev$, $Date$
 */
public class IpV6TreeMap implements Lookup.IpV6, Store.IpV6
{
    private TreeMap<Long, ServiceUrls> allocations = new TreeMap<Long, ServiceUrls>();

    @Override
    public void store( IPv6Network iPv6Network, ServiceUrls serviceUrls )
    {
        long key = iPv6Network.getFirst().getHighBits();
        allocations.put( key, serviceUrls );
    }

    private ServiceUrls getServiceUrls( long prefix )
    {
        ServiceUrls retval = null;
        Map.Entry<Long, ServiceUrls> entry = allocations.floorEntry( prefix );
        if ( entry != null )
        {
            retval = entry.getValue();
        }
        return retval;
    }

    public ServiceUrls getServiceUrlsForIpV6( IPv6Address addr )
    {
        return getServiceUrls( addr.getHighBits() );
    }

    public ServiceUrls getServiceUrlsForIpV6( IPv6Network net )
    {
        return getServiceUrls( net.getFirst().getHighBits() );
    }

}
