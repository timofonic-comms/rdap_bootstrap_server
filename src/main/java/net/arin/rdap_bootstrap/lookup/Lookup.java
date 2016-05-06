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
 * Defines the interfaces for doing lookups.
 */
public interface Lookup
{
    enum Type {
        NAMESERVER("nameserver"),
        IP("ip"),
        AUTNUM("autnum"),
        ENTITY("entity"),
        DOMAIN("domain");

        private String pValue;
        private Type( String pValue )
        {
            this.pValue = pValue;
        }

        public String getPValue()
        {
            return pValue;
        }
    }

    interface As extends Lookup
    {
        ServiceUrls getServiceUrlsForAs( IpResource ipResource);
    }

    interface Domain extends Lookup
    {
        ServiceUrls getServiceUrlsForDomain( String domain );
    }

    interface Entity extends Lookup
    {
        ServiceUrls getServiceUrlsForEntity( String entity );
    }

    interface IpV4 extends Lookup
    {
        ServiceUrls getServiceUrlsForIpV4( IpResource ipResource );
    }

    interface IpV6 extends Lookup
    {
        ServiceUrls getServiceUrlsForIpV6( IpResource ipResource );
    }

}
