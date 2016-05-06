/* Copyright (C) 2016 American Registry for Internet Numbers (ARIN)
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
package net.arin.rdap_bootstrap.service;

import net.arin.rdap_bootstrap.Constants;
import net.arin.rdap_bootstrap.lookup.Lookup;
import net.arin.rdap_bootstrap.lookup.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Implements a registry
 */
public class Registry
{
    public static final String AS_7484      = "as_7487";
    public static final String IPV4_7484    = "ipv4_7484";
    public static final String IPV6_7484    = "ipv6_7484";
    public static final String DOMAIN_7484  = "domain_7484";
    public static final String ENTITY_7484  = "entity_7484";
    public static final String DEFAULT_7484 = "default_7484";

    private static final String regPropPrefix = Constants.PROPERTY_PREFIX + "registry.";

    private String name;
    private String propertyPrefix;
    private Properties properties;
    private Lookup lookup;
    private Store store;
    private Source source;
    private Format format;
    private Map<String,Registry> registries;

    public Registry( String name, String propertyPrefix, Properties properties,
                     Map<String, Registry> registries )
    {
        this.name = name;
        this.propertyPrefix = propertyPrefix;
        this.properties = properties;
        this.registries = registries;
    }

    public static Map<String,Registry> makeRegistries( Map<String,Registry> registries, String[] registryNames, Properties properties )
    {
        if( registries == null )
        {
            registries = new HashMap<String, Registry>();
        }
        for ( String s : registryNames )
        {
            String prefix = regPropPrefix + s + ".";
            Registry r = new Registry( s, prefix, properties, registries );
            registries.put( s, r );
        }
        return registries;
    }

    public Map<String, Registry> getRegistries()
    {
        return registries;
    }

    public void setRegistries(
        Map<String, Registry> registries )
    {
        this.registries = registries;
    }

    public String getProperty( String subPropName )
    {
        return properties.getProperty( propertyPrefix + subPropName );
    }

    public String getPropertyPrefix()
    {
        return propertyPrefix;
    }

    public void setPropertyPrefix( String propertyPrefix )
    {
        this.propertyPrefix = propertyPrefix;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties( Properties properties )
    {
        this.properties = properties;
    }

    public Lookup getLookup()
    {
        return lookup;
    }

    public void setLookup( Lookup lookup )
    {
        this.lookup = lookup;
    }

    public Store getStore()
    {
        return store;
    }

    public void setStore( Store store )
    {
        this.store = store;
    }

    public Source getSource()
    {
        return source;
    }

    public void setSource( Source source )
    {
        this.source = source;
    }

    public Format getFormat()
    {
        return format;
    }

    public void setFormat( Format format )
    {
        this.format = format;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
