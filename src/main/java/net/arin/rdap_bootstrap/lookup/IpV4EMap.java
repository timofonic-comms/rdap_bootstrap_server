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

import net.arin.rdap_bootstrap.lookup.Store.IpV4;
import net.ripe.ipresource.IpResource;
import net.ripe.ipresource.etree.IpResourceIntervalStrategy;
import net.ripe.ipresource.etree.NestedIntervalMap;

/**
 * @version $Rev$, $Date$
 */
public class IpV4EMap implements Lookup.IpV4, Store.IpV4
{
    private NestedIntervalMap<IpResource, ServiceUrls> allocations = new NestedIntervalMap<IpResource, ServiceUrls>(
        IpResourceIntervalStrategy.getInstance() );

    @Override
    public void store( IpResource ipResource, ServiceUrls serviceUrls )
    {
        allocations.put( ipResource, serviceUrls );
    }

    public ServiceUrls getServiceUrlsForIpV4( IpResource ipResource )
    {
        return allocations.findExactOrFirstLessSpecific( ipResource );
    }

    @Override
    public IpV4 createLoadContext()
    {
        return new IpV4EMap();
    }

    @Override
    public void loadWithContext( IpV4 ipV4, boolean success )
    {
        if( success )
        {
            allocations = ((IpV4EMap)ipV4).allocations;
        }
    }
}
