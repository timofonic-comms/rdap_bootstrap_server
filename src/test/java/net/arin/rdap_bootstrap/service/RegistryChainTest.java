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
}
