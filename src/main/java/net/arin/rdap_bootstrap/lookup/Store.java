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

import net.ripe.ipresource.IpResource;

/**
 * Defines the interfaces for storing data.
 */
public interface Store
{
    interface Load<T>
    {
        T createLoadContext();
        void loadWithContext( T t, boolean success);
    }

    interface As extends Load<As>
    {
        void store( IpResource ipResource, ServiceUrls serviceUrls );
    }

    interface Domain extends Load<Domain>
    {
        void store( String domain, ServiceUrls serviceUrls );
    }

    interface Entity extends Load<Entity>
    {
        void store( String entity, ServiceUrls serviceUrls );
    }

    interface IpV4 extends Load<IpV4>
    {
        void store( IpResource ipResource, ServiceUrls serviceUrls );
    }

    interface IpV6 extends Load<IpV6>
    {
        void store( IpResource ipResource, ServiceUrls serviceUrls );
    }
}
