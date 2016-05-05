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
 *
 */
package net.arin.rdap_bootstrap.service;

import net.arin.rdap_bootstrap.service.RegistryChain.Registry;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@link RegistryChain}
 */
public class RegistryChainTest
{
    @Test
    public void testMakeRegistryChains() throws Exception
    {
        Properties props = new Properties(  );
        props.setProperty( "arin.rdapbootstrap.registry_chain.as", "iana_as,nro_stats,default" );
        props.setProperty( "arin.rdapbootstrap.registry_chain.ipv4", "iana_as,nro_stats,default" );
        props.setProperty( "arin.rdapbootstrap.registry_chain.ipv6", "iana_ipv6,nro_stats,default" );
        props.setProperty( "arin.rdapbootstrap.registry_chain.domain",
            "iana_dns,nro_stats,default" );
        props.setProperty( "arin.rdapbootstrap.registry_chain.entity", "my_entity,default" );

        List<RegistryChain> chains = RegistryChain.makeRegistryChains( props );
        assertEquals( chains.size(), 5 );
        boolean asFound = false;
        boolean ipv4Found = false;
        boolean ipv6Found = false;
        boolean domainFound = false;
        boolean entityFound = false;
        for ( RegistryChain chain : chains )
        {
            if( chain instanceof RegistryChain.As )
            {
                asFound = true;
            }
            else if( chain instanceof RegistryChain.Ipv4 )
            {
                ipv4Found = true;
            }
            else if( chain instanceof RegistryChain.Ipv6 )
            {
                ipv6Found = true;
            }
            else if( chain instanceof RegistryChain.Domain )
            {
                domainFound = true;
            }
            else if( chain instanceof RegistryChain.Entity )
            {
                entityFound = true;
            }
        }
        assertTrue( asFound );
        assertTrue( ipv4Found );
        assertTrue( ipv6Found );
        assertTrue( domainFound );
        assertTrue( entityFound );
    }

    @Test
    public void testConfigedRegistries() throws Exception
    {
        String[] registries = RegistryChain.configedRegistries( "as_7484,ipv4_7484" );
        assertEquals( registries.length, 2 );
        assertEquals( registries[0], "as_7484" );
        assertEquals( registries[1], "ipv4_7484" );

        registries = RegistryChain.configedRegistries( "as_7484  ipv4_7484" );
        assertEquals( registries.length, 2 );
        assertEquals( registries[0], "as_7484" );
        assertEquals( registries[1], "ipv4_7484" );
    }

    @Test
    public void testMakeRegistries() throws Exception
    {
        Properties props = new Properties();
        props.setProperty( "arin.rdapbootstrap.registry.as_7484.type","foo" );
        props.setProperty( "arin.rdapbootstrap.registry.as_7484.source","thing.txt" );
        props.setProperty( "arin.rdapbootstrap.registry.ipv4_7484.type","bar" );
        props.setProperty( "arin.rdapbootstrap.registry.ipv4_7484.source","thing.json" );
        props.setProperty( "arin.rdapbootstrap.registry.ipv4_7484.recheck","now" );

        List<Registry> registries = RegistryChain.makeRegistries( new String[]{ "as_7484", "ipv4_7484" }, props );
        assertEquals( registries.size(), 2 );
        assertEquals( registries.get( 0 ).getName(), "as_7484" );
        assertEquals( registries.get( 0 ).getProperties().size(), 2 );
        assertEquals( registries.get( 1 ).getName(), "ipv4_7484" );
        assertEquals( registries.get( 1 ).getProperties().size(), 3 );
    }
}
