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
package net.arin.rdap_bootstrap.lookup;

import net.ripe.ipresource.IpResource;
import net.ripe.ipresource.etree.IpResourceIntervalStrategy;
import net.ripe.ipresource.etree.NestedIntervalMap;

/**
 * @version $Rev$, $Date$
 */
public class AsEMap implements Lookup.As, Store.As
{

    NestedIntervalMap<IpResource,ServiceUrls> allocations = new NestedIntervalMap<IpResource, ServiceUrls>(
        IpResourceIntervalStrategy.getInstance() );

    @Override
    public void store( IpResource ipResource, ServiceUrls serviceUrls )
    {
        allocations.put( ipResource, serviceUrls );
    }

    @Override
    public ServiceUrls getServiceUrlsForAs( IpResource ipResource )
    {
        return allocations.findExactOrFirstLessSpecific( ipResource );
    }

    @Override
    public Store.As createLoadContext()
    {
        return new AsEMap();
    }

    @Override
    public void loadWithContext( Store.As as, boolean success )
    {
        if( success )
        {
            allocations = ((AsEMap)as).allocations;
        }
    }
}
