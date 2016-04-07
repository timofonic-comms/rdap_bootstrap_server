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
package net.arin.rdap_bootstrap.service;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;
import net.arin.rdap_bootstrap.lookup.Lookup;
import net.arin.rdap_bootstrap.lookup.Lookup.ServiceUrls;
import net.arin.rdap_bootstrap.lookup.Lookup.Type;
import net.arin.rdap_bootstrap.service.ResourceFiles.BootFiles;

import java.util.HashMap;

public class DefaultBootstrap implements Bootstrap, Lookup.As, Lookup.Domain, Lookup.Entity,
    Lookup.IpV4, Lookup.IpV6, Rfc7484.Handler
{

    private volatile HashMap<String,ServiceUrls> allocations = new HashMap<String, ServiceUrls>(  );
    private HashMap<String,ServiceUrls> _allocations;

    private ServiceUrls serviceUrls;
    private String publication;
    private String description;

    public void loadData( ResourceFiles resourceFiles )
        throws Exception
    {
        Rfc7484 bsFile = new Rfc7484();
        bsFile.loadData( resourceFiles.getInputStream( BootFiles.DEFAULT.getKey() ), this );
    }

    @Override
    public void startServices()
    {
        _allocations = new HashMap<String, ServiceUrls>(  );
    }

    @Override
    public void endServices()
    {
        allocations = _allocations;
    }

    @Override
    public void startService()
    {
        serviceUrls = new ServiceUrls();
    }

    @Override
    public void endService()
    {
        //Nothing to do
    }

    @Override
    public void addServiceEntry( String entry )
    {
        _allocations.put( entry, serviceUrls );
    }

    @Override
    public void addServiceUrl( String url )
    {
        serviceUrls.addUrl( url );
    }

    public ServiceUrls getServiceUrlsForDefault( Type type )
    {
        return allocations.get( type.getPValue() );
    }

    @Override
    public ServiceUrls getServiceUrlsForAs( String autnum )
    {
        return allocations.get( Type.AUTNUM.getPValue() );
    }

    @Override
    public ServiceUrls getServiceUrlsForDomain( String domain )
    {
        return allocations.get( Type.DOMAIN.getPValue() );
    }

    @Override
    public ServiceUrls getServiceUrlsForEntity( String entity )
    {
        return allocations.get( Type.ENTITY.getPValue() );
    }

    @Override
    public ServiceUrls getServiceUrlsForIpV4( String prefix )
    {
        return allocations.get( Type.NAMESERVER.getPValue() );
    }

    @Override
    public ServiceUrls getServiceUrlsForIpV6( IPv6Address addr )
    {
        return allocations.get( Type.IP.getPValue() );
    }

    @Override
    public ServiceUrls getServiceUrlsForIpV6( IPv6Network net )
    {
        return allocations.get( Type.IP.getPValue() );
    }

    @Override
    public void setPublication( String publication ) { this.publication = publication; }
    public String getPublication() { return publication; }

    @Override
    public void setDescription( String description )
    {
        this.description = description;
    }
    public String getDescription()
    {
        return description;
    }
}
