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

import java.util.HashMap;

/**
 * Implements "Default" lookup using a HashMap
 */
public class DefaultHashMap implements Lookup.As, Lookup.IpV4, Lookup.IpV6, Lookup.Domain, Lookup.Entity,
    Store.Default
{
    private volatile HashMap<Type,ServiceUrls> allocations = new HashMap<Type, ServiceUrls>(  );

    @Override
    public ServiceUrls getServiceUrlsForAs( IpResource ipResource )
    {
        return allocations.get( Type.AUTNUM );
    }

    @Override
    public ServiceUrls getServiceUrlsForDomain( String domain )
    {
        return allocations.get( Type.DOMAIN );
    }

    @Override
    public ServiceUrls getServiceUrlsForEntity( String entity )
    {
        return allocations.get( Type.ENTITY );
    }

    @Override
    public ServiceUrls getServiceUrlsForIpV4( IpResource ipRange )
    {
        return allocations.get( Type.NAMESERVER );
    }

    @Override
    public ServiceUrls getServiceUrlsForIpV6( IpResource ipResource )
    {
        return allocations.get( Type.IP );
    }

    @Override
    public void store( Type type, ServiceUrls serviceUrls )
    {
        allocations.put( type, serviceUrls );
    }

    @Override
    public Default createLoadContext()
    {
        return new DefaultHashMap();
    }

    @Override
    public void loadWithContext( Default aDefault, boolean success )
    {
        if( success )
        {
            allocations = ((DefaultHashMap)aDefault).allocations;
        }
    }
}
