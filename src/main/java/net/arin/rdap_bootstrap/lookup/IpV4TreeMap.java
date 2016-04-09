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
import net.ripe.ipresource.IpRange;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

/**
 * @version $Rev$, $Date$
 */
public class IpV4TreeMap implements Lookup.IpV4, Store.IpV4
{
    private TreeMap<IpRange, ServiceUrls> allocations = new TreeMap<IpRange, ServiceUrls>();

    @Override
    public void store( IpRange ipRange, ServiceUrls serviceUrls )
    {
        allocations.put( ipRange, serviceUrls );
    }

    public ServiceUrls getServiceUrlsForIpV4( IpRange ipRange )
    {
        ServiceUrls retval = null;
        Map.Entry<IpRange, ServiceUrls> entry = allocations.floorEntry( ipRange );
        if( entry != null )
        {
            retval = entry.getValue();
        }
        return retval;
    }

}
