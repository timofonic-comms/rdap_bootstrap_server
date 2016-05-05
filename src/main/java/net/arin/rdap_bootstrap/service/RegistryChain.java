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
import net.arin.rdap_bootstrap.lookup.ServiceUrls;
import net.arin.rdap_bootstrap.lookup.Store;
import net.ripe.ipresource.IpResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Implementation of Registry Chains
 */
public abstract class RegistryChain
{
    public static final String AS_7484      = "as_7487";
    public static final String IPV4_7484    = "ipv4_7484";
    public static final String IPV6_7484    = "ipv6_7484";
    public static final String DOMAIN_7484  = "domain_7484";
    public static final String ENTITY_7484  = "entity_7484";
    public static final String DEFAULT_7484 = "default_7484";

    private static final String rcPropPrefix  = Constants.PROPERTY_PREFIX + "registry_chain.";
    private static final String regPropPrefix = Constants.PROPERTY_PREFIX + "registry.";

    protected String[] configedRegistries = null;
    protected Properties properties = null;

    private static Logger logger = Logger.getLogger( "RegistryChain" );

    public RegistryChain( String configedRegistries, Properties properties )
    {
        this.configedRegistries = configedRegistries( configedRegistries );
        this.properties = properties;
    }

    public static List<RegistryChain> makeRegistryChains( Properties properties )
    {

        ArrayList<RegistryChain> chains = new ArrayList<RegistryChain>(  );
        for ( Entry<Object, Object> entry : properties.entrySet() )
        {
            String propName =  (String)entry.getKey();
            String propValue = (String)entry.getValue();
            if( propName.startsWith( rcPropPrefix ) )
            {
                String rcType = propName.substring( rcPropPrefix.length() );
                if( rcType.equals( "as" ) )
                {
                    As as = new As( propValue, properties );
                    chains.add( as );
                }
                else if( rcType.equals( "ipv4" ) )
                {
                    Ipv4 ipV4 = new Ipv4( propValue, properties );
                    chains.add( ipV4 );
                }
                else if( rcType.equals( "ipv6" ) )
                {
                    Ipv6 ipV6 = new Ipv6( propValue, properties );
                    chains.add( ipV6 );
                }
                else if( rcType.equals( "domain" ) )
                {
                    Domain domain = new Domain( propValue, properties );
                    chains.add( domain );
                }
                else if( rcType.equals( "entity" ) )
                {
                    Entity entity = new Entity( propValue, properties );
                    chains.add( entity );
                }
                else
                {
                    logger.warning( "Unknown registry chain '" + rcType + "'" );
                }
            }
        }
        return chains;
    }

    public static String[] configedRegistries( String configedRegistries )
    {
        return configedRegistries.split( "(,|\\s+)" );
    }

    public static List<Registry> makeRegistries( String[] configedRegistries, Properties properties )
    {
        ArrayList<Registry> registries = new ArrayList<Registry>();
        for ( String s : configedRegistries )
        {
            Properties regProps = new Properties();
            String prefix = regPropPrefix + s;
            for ( Object key : properties.keySet() )
            {
                String propName = (String)key;
                if( propName.startsWith( prefix ) )
                {
                    regProps.setProperty( propName, properties.getProperty( propName ) );
                }
            }
            Registry r = new Registry( s, regProps );
            registries.add( r );
        }
        return registries;
    }

    public static class As extends RegistryChain implements Lookup.As
    {
        private ArrayList<Lookup.As> lookups = new ArrayList<Lookup.As>();

        public As( String configedRegistries, Properties properties )
        {
            super( configedRegistries, properties );
        }

        @Override
        public ServiceUrls getServiceUrlsForAs( IpResource ipResource )
        {
            ServiceUrls retval = null;
            for ( Lookup.As lookup : lookups )
            {
                retval = lookup.getServiceUrlsForAs( ipResource );
                if( retval != null )
                {
                    break;
                }
            }
            return retval;
        }
    }

    public static class Ipv4 extends RegistryChain implements Lookup.IpV4
    {
        private ArrayList<Lookup.IpV4> lookups = new ArrayList<Lookup.IpV4>();

        public Ipv4( String configedRegistries, Properties properties )
        {
            super( configedRegistries, properties );
        }

        @Override
        public ServiceUrls getServiceUrlsForIpV4( IpResource ipResource )
        {
            ServiceUrls retval = null;
            for ( Lookup.IpV4 lookup : lookups )
            {
                retval = lookup.getServiceUrlsForIpV4( ipResource );
                if( retval != null )
                {
                    break;
                }
            }
            return retval;
        }
    }

    public static class Ipv6 extends RegistryChain implements Lookup.IpV6
    {
        private ArrayList<Lookup.IpV6> lookups = new ArrayList<Lookup.IpV6>();

        public Ipv6( String configedRegistries, Properties properties )
        {
            super( configedRegistries, properties );
        }

        @Override
        public ServiceUrls getServiceUrlsForIpV6( IpResource ipResource )
        {
            ServiceUrls retval = null;
            for ( Lookup.IpV6 lookup : lookups )
            {
                retval = lookup.getServiceUrlsForIpV6( ipResource );
                if( retval != null )
                {
                    break;
                }
            }
            return retval;
        }
    }

    public static class Domain extends RegistryChain implements Lookup.Domain
    {
        private ArrayList<Lookup.Domain> lookups = new ArrayList<Lookup.Domain>();

        public Domain( String configedRegistries, Properties properties )
        {
            super( configedRegistries, properties );
        }

        @Override
        public ServiceUrls getServiceUrlsForDomain( String domain )
        {
            ServiceUrls retval = null;
            for ( Lookup.Domain lookup : lookups )
            {
                retval = lookup.getServiceUrlsForDomain( domain );
                if( retval != null )
                {
                    break;
                }
            }
            return retval;
        }
    }

    public static class Entity extends RegistryChain implements Lookup.Entity
    {
        private ArrayList<Lookup.Entity> lookups = new ArrayList<Lookup.Entity>();

        public Entity( String configedRegistries, Properties properties )
        {
            super( configedRegistries, properties );
        }

        @Override
        public ServiceUrls getServiceUrlsForEntity( String entity )
        {
            ServiceUrls retval = null;
            for ( Lookup.Entity lookup : lookups )
            {
                retval = lookup.getServiceUrlsForEntity( entity );
                if( retval != null )
                {
                    break;
                }
            }
            return retval;
        }
    }

    public static class Registry
    {
        private String name;
        private Properties properties;
        private Lookup lookup;
        private Store  store;
        private Source source;
        private Format format;

        public Registry( String name, Properties properties )
        {
            this.name = name;
            this.properties = properties;
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
}

