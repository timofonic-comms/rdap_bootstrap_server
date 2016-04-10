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

import net.arin.rdap_bootstrap.lookup.Store.Domain;

import java.util.HashMap;

/**
 * @version $Rev$, $Date$
 */
public class DomainHashMap implements Lookup.Domain, Store.Domain
{
    private HashMap<String,ServiceUrls> allocations = new HashMap<String, ServiceUrls>(  );

    @Override
    public void store( String entry, ServiceUrls serviceUrls )
    {
        allocations.put( entry.toUpperCase(), serviceUrls );
    }

    public ServiceUrls getServiceUrlsForDomain( String domain )
    {
        domain = domain.toUpperCase();
        int idx = 0;
        ServiceUrls retval = null;
        while( idx != -1 )
        {
            retval = allocations.get( domain.substring( idx ) );
            if( retval != null )
            {
                break;
            }
            //else
            idx = domain.indexOf( ".", idx );
            if( idx != -1 )
            {
                idx++;
            }
        }
        return retval;
    }

    @Override
    public Domain createLoadContext()
    {
        return new DomainHashMap();
    }

    @Override
    public void loadWithContext( Domain domain, boolean success )
    {
        if( success )
        {
            allocations = ((DomainHashMap)domain).allocations;
        }
    }
}
