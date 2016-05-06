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
import net.arin.rdap_bootstrap.format.Format;
import net.arin.rdap_bootstrap.lookup.AsEMap;
import net.arin.rdap_bootstrap.lookup.DefaultHashMap;
import net.arin.rdap_bootstrap.lookup.DomainHashMap;
import net.arin.rdap_bootstrap.lookup.EntityHashMap;
import net.arin.rdap_bootstrap.lookup.IpV4EMap;
import net.arin.rdap_bootstrap.lookup.IpV6EMap;
import net.arin.rdap_bootstrap.lookup.Lookup;
import net.arin.rdap_bootstrap.lookup.Store;
import net.arin.rdap_bootstrap.source.Source;

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
    private static final String typePropName  = "type";

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

    public void config()
    {
        String type = getType();
        switch ( type )
        {
            case AS_7484:
                AsEMap asEMap = new AsEMap();
                store = asEMap;
                lookup = asEMap;
                break;
            case IPV4_7484:
                IpV4EMap ipV4EMap = new IpV4EMap();
                store = ipV4EMap;
                lookup = ipV4EMap;
                break;
            case IPV6_7484:
                IpV6EMap ipV6EMap = new IpV6EMap();
                store = ipV6EMap;
                lookup = ipV6EMap;
                break;
            case DOMAIN_7484:
                DomainHashMap domainHashMap = new DomainHashMap();
                store = domainHashMap;
                lookup = domainHashMap;
                break;
            case ENTITY_7484:
                EntityHashMap entityHashMap = new EntityHashMap();
                store = entityHashMap;
                lookup = entityHashMap;
                break;
            case DEFAULT_7484:
                DefaultHashMap defaultHashMap = new DefaultHashMap();
                store = defaultHashMap;
                lookup = defaultHashMap;
                break;
        }
    }

    public String getType()
    {
        String retval = getProperty( typePropName );
        if( retval == null || retval.length() == 0 )
        {
            throw new RuntimeException( "Registry type is empty or null" );
        }
        return retval;
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
