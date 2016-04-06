/*
 * Copyright (C) 2013-2016 American Registry for Internet Numbers (ARIN)
 */
package net.arin.rdap_bootstrap.service;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;

import java.util.ArrayList;

/**
 *
 */
public interface Bootstrap
{
    void loadData( ResourceFiles resourceFiles ) throws Exception;

    interface AsLookup
    {
        ServiceUrls getServiceUrlsForAs( String autnum );
    }

    interface DefaultLookup
    {
        ServiceUrls getServiceUrlsForDefault( Type type );
    }

    interface DomainLookup
    {
        ServiceUrls getServiceUrlsForDomain( String domain );
    }

    interface EntityLookup
    {
        ServiceUrls getServiceUrlsForEntity( String entity );
    }

    interface IpV4Lookup
    {
        ServiceUrls getServiceUrlsForIpV4( String prefix );
    }

    interface IpV6Lookup
    {
        ServiceUrls getServiceUrlsForIpV6( IPv6Address addr );
        ServiceUrls getServiceUrlsForIpV6( IPv6Network net );
    }

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

    /**
     * A utility class for referencing URLs.
     */
    public static class ServiceUrls
    {
        private ArrayList<String> urls = new ArrayList<String>();
        private int httpIdx = -1;
        private int httpsIdx = -1;

        public void addUrl( String url )
        {
            if( url != null )
            {
                if( url.endsWith( "/" ) )
                {
                    url = url.substring( 0, url.length() - 1 );
                }
                urls.add( url );
                if( url.startsWith( "https://" ) )
                {
                    httpsIdx = urls.size() -1;
                }
                else if( url.startsWith( "http://" ) )
                {
                    httpIdx = urls.size() - 1;
                }
            }
        }

        public ArrayList<String> getUrls()
        {
            return urls;
        }

        public void setUrls( ArrayList<String> urls )
        {
            this.urls = urls;
        }

        public String getHttpUrl()
        {
            if( httpIdx != -1 ){
                return urls.get( httpIdx );
            }
            //else
            return null;
        }

        public String getHttpsUrl()
        {
            if( httpsIdx != -1 ){
                return urls.get( httpsIdx );
            }
            //else
            return null;
        }
    }
}
