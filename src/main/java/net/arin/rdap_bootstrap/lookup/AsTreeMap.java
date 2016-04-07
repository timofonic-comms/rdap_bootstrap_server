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

import net.arin.rdap_bootstrap.lookup.Lookup;
import net.arin.rdap_bootstrap.lookup.Lookup.ServiceUrls;
import net.arin.rdap_bootstrap.lookup.Store;

import java.util.Map;
import java.util.TreeMap;

/**
 * @version $Rev$, $Date$
 */
public class AsTreeMap implements Lookup.As, Store.As
{

    private volatile TreeMap<Long,AsRangeInfo> allocations = new TreeMap<Long, AsRangeInfo>(  );

    @Override
    public void store( AsRangeInfo asRangeInfo )
    {
        allocations.put( asRangeInfo.getAsStart(), asRangeInfo );
    }

    public ServiceUrls getServiceUrlsForAs( String autnum )
    {
        long number = Long.parseLong( autnum );
        Map.Entry<Long,AsRangeInfo> entry = allocations.floorEntry( number );
        if( entry != null )
        {
            AsRangeInfo asRangeInfo = entry.getValue();
            if( number <= asRangeInfo.getAsEnd() )
            {
                return asRangeInfo.getServiceUrls();
            }
        }
        //else
        return null;
    }

}
