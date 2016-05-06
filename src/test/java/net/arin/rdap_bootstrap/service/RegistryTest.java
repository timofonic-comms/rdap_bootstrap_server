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

import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for {@link Registry}
 */
public class RegistryTest
{
    @Test
    public void testMakeRegistries() throws Exception
    {
        Properties props = new Properties();
        props.setProperty( "arin.rdapbootstrap.registry.as_7484.type","foo" );
        props.setProperty( "arin.rdapbootstrap.registry.as_7484.source","thing.txt" );
        props.setProperty( "arin.rdapbootstrap.registry.ipv4_7484.type","bar" );
        props.setProperty( "arin.rdapbootstrap.registry.ipv4_7484.source","thing.json" );
        props.setProperty( "arin.rdapbootstrap.registry.ipv4_7484.recheck", "now" );

        Map<String,Registry> registries = Registry
            .makeRegistries( null, new String[]{"as_7484", "ipv4_7484"}, props );
        assertEquals( registries.size(), 2 );
        assertEquals( registries.get( "as_7484" ).getName(), "as_7484" );
        assertEquals( registries.get( "as_7484" ).getProperty( "type" ), "foo" );
        assertEquals( registries, registries.get( "as_7484" ).getRegistries() );
        assertEquals( registries.get( "ipv4_7484" ).getName(), "ipv4_7484" );
        assertEquals( registries.get( "ipv4_7484" ).getProperty( "type"), "bar" );
        assertEquals( registries, registries.get( "ipv4_7484" ).getRegistries() );
    }
}
