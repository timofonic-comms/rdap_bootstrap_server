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
package net.arin.rdap_bootstrap.format;

import net.arin.rdap_bootstrap.lookup.ServiceUrls;
import net.arin.rdap_bootstrap.lookup.Store;
import net.ripe.ipresource.IpResource;

import java.io.InputStream;

/**
 * @version $Rev$, $Date$
 */
public class IpV67484 implements Format, Rfc7484.Handler
{
    private Store.IpV6 store;

    private ServiceUrls serviceUrls;
    private String publication;
    private String description;

    @Override
    public void startServices()
    {
        store = store.createLoadContext();
    }

    @Override
    public void endServices()
    {
        store.loadWithContext( store, true );
    }

    @Override
    public void startService()
    {
        serviceUrls = new ServiceUrls();
    }

    @Override
    public void endService()
    {
        // Nothing to do
    }

    @Override
    public void addServiceEntry( String entry )
    {
        store.store( IpResource.parse( entry ), serviceUrls );
    }

    @Override
    public void addServiceUrl( String url )
    {
        serviceUrls.addUrl( url );
    }

    @Override
    public void loadData( InputStream inputStream, Store store )
    {
        this.store = (Store.IpV6)store;
        Rfc7484 bsFile = new Rfc7484();
        try
        {
            bsFile.loadData( inputStream, this );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }

    }

    @Override
    public void setPublication( String publication )
    {
        this.publication = publication;
    }

    public String getPublication()
    {
        return publication;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public void setDescription( String description )
    {
        this.description = description;
    }
}
