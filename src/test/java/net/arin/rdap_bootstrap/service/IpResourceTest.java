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

import net.ripe.ipresource.Asn;
import net.ripe.ipresource.IpAddress;
import net.ripe.ipresource.IpRange;
import net.ripe.ipresource.IpResource;
import net.ripe.ipresource.IpResourceRange;
import net.ripe.ipresource.Ipv4Address;
import net.ripe.ipresource.Ipv6Address;
import net.ripe.ipresource.UniqueIpResource;
import net.ripe.ipresource.etree.IpResourceIntervalStrategy;
import net.ripe.ipresource.etree.NestedIntervalMap;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for understanding the RIPE IPResource library
 */
public class IpResourceTest
{
    @Test
    public void testIp() throws Exception
    {
        assertEquals( "1.0.0.0", UniqueIpResource.parse( "1.0.0.0" ).toString() );
        assertEquals( "1.0.0.0", IpAddress.parse( "1.0.0.0" ).toString() );
        assertEquals( "1.0.0.0", Ipv4Address.parse( "1.0.0.0" ).toString() );
        assertEquals( "1.0.0.0", IpRange.parse( "1.0.0.0/8" ).getStart().toString() );
        assertEquals( "1.255.255.255", IpRange.parse( "1.0.0.0/8" ).getEnd().toString() );
        assertEquals( 8, IpRange.parse( "1.0.0.0/8" ).getPrefixLength() );
        assertEquals( 32, IpRange.parse( "1.0.0.0/32" ).getPrefixLength() );
        assertEquals( "1.0.0.0", IpResource.parse( "1.0.0.0" ).toString() );
        assertEquals( "1.0.0.0", IpResource.parse( "1.0.0.0/8" ).getStart().toString() );
        assertEquals( "1.255.255.255", IpResource.parse( "1.0.0.0/8" ).getEnd().toString() );

        assertEquals( "2620::", UniqueIpResource.parse( "2620::" ).toString() );
        assertEquals( "2620::", UniqueIpResource.parse(
            "2620:0000:0000:0000:0000:0000:0000:0000" ).toString() );
        assertEquals( "2620::", UniqueIpResource.parse( "2620::0" ).toString() );
        assertEquals( "2620::", IpAddress.parse( "2620::" ).toString() );
        assertEquals( "2620::", IpAddress.parse( "2620:0000:0000:0000:0000:0000:0000:0000" ).toString() );
        assertEquals( "2620::", IpAddress.parse( "2620::0" ).toString() );
        assertEquals( "2620::", Ipv6Address.parse( "2620::" ).toString() );
        assertEquals( "2620::", Ipv6Address.parse( "2620:0000:0000:0000:0000:0000:0000:0000" ).toString() );
        assertEquals( "2620::", Ipv6Address.parse( "2620::0" ).toString() );
        assertEquals( "2620::", IpRange.parse( "2620::0/16" ).getStart().toString() );
        assertEquals( "2620:ffff:ffff:ffff:ffff:ffff:ffff:ffff", IpRange.parse( "2620::0/16" ).getEnd().toString() );
        assertEquals( 16, IpRange.parse( "2620::0/16" ).getPrefixLength() );
        assertEquals( 48, IpRange.parse( "2620::0/48" ).getPrefixLength() );
        assertEquals( "2620::", IpResource.parse( "2620::" ).toString() );
        assertEquals( "2620::", IpResource.parse( "2620::0/16" ).getStart().toString() );
        assertEquals( "2620:ffff:ffff:ffff:ffff:ffff:ffff:ffff", IpResource.parse( "2620::0/16" ).getEnd().toString() );
    }

    @Test
    public void testAsn() throws Exception
    {
        assertEquals( "AS1", UniqueIpResource.parse( "1" ).toString() );
        assertEquals( "AS1", UniqueIpResource.parse( "AS1" ).toString() );
        assertEquals( "AS1", Asn.parse( "1" ).toString() );
        assertEquals( "AS1", Asn.parse( "AS1" ).toString() );
        assertEquals( "AS1", IpResource.parse( "1-3" ).getStart().toString() );
        assertEquals( "AS3", IpResource.parse( "1-3" ).getEnd().toString() );
        assertEquals( "AS1", IpResourceRange.parse( "1-3" ).getStart().toString() );
        assertEquals( "AS3", IpResourceRange.parse( "1-3" ).getEnd().toString() );
        assertEquals( "AS1", IpResourceRange.parse( "AS1-AS3" ).getStart().toString() );
        assertEquals( "AS3", IpResourceRange.parse( "AS1-AS3" ).getEnd().toString() );
    }

    @Test
    public void testETreeV4() throws Exception
    {
        IpRange net192_0_0_0_0 = IpRange.parse( "192.0.0.0/8" );
        IpRange net192_0_0_0_1 = IpRange.parse( "192.0.0.0/8" );
        IpRange net192_0_0_0_2 = IpRange.parse( "192.0.0.0/16" );
        IpRange net192_1_0_0_0 = IpRange.parse( "192.1.0.0/16" );

        /*
            The thing to notice here is that NestedIntervalMap allows children to be nested
            so long as the children are not equal. In other words, the child cannot have the
            same start and end, but it can have the same or the same end.
         */

        NestedIntervalMap<IpResource,String> v4Etree = new NestedIntervalMap<IpResource, String>( IpResourceIntervalStrategy.getInstance() );
        v4Etree.put( net192_0_0_0_0, "net192_0_0_0_0");
        v4Etree.put( net192_0_0_0_1, "net192_0_0_0_1");
        v4Etree.put( net192_0_0_0_2, "net192_0_0_0_2" );
        v4Etree.put( net192_1_0_0_0, "net192_1_0_0_0" );
        assertEquals( null, v4Etree.findFirstLessSpecific( net192_0_0_0_0 ) );
        assertEquals( "net192_0_0_0_1", v4Etree.findExactOrFirstLessSpecific( net192_0_0_0_0 ) );
        assertEquals( "net192_0_0_0_1", v4Etree.findExact( net192_0_0_0_0 ) );
        assertEquals( "net192_0_0_0_2", v4Etree.findFirstMoreSpecific( net192_0_0_0_0 ).get( 0 ) );
        assertEquals( "net192_0_0_0_2", v4Etree.findAllMoreSpecific( net192_0_0_0_0 ).get( 0 ) );
        assertEquals( "net192_1_0_0_0", v4Etree.findAllMoreSpecific( net192_0_0_0_0 ).get( 1 ) );
    }

    @Test
    public void testETreeV6() throws Exception
    {
        IpRange net2620_0 = IpRange.parse( "2620::0/16" );
        IpRange net2620_1 = IpRange.parse( "2620::0/48" );
        IpRange net2620_2 = IpRange.parse( "2620:1::0/48" );

        NestedIntervalMap<IpResource,String> v6Etree = new NestedIntervalMap<IpResource, String>( IpResourceIntervalStrategy.getInstance() );
        v6Etree.put( net2620_0, "net2620_0" );
        v6Etree.put( net2620_1, "net2620_1" );
        v6Etree.put( net2620_2, "net2620_2" );
        assertEquals( "net2620_0", v6Etree.findExactOrFirstLessSpecific( net2620_0 ) );
        assertEquals( "net2620_0", v6Etree.findExact( net2620_0 ) );
        assertEquals( "net2620_1", v6Etree.findExact( net2620_1 ) );
        assertEquals( "net2620_1", v6Etree.findFirstMoreSpecific( net2620_0 ).get( 0 ) );
        assertEquals( "net2620_2", v6Etree.findFirstMoreSpecific( net2620_0 ).get( 1 ) );
        assertEquals( "net2620_1", v6Etree.findAllMoreSpecific( net2620_0 ).get( 0 ) );
        assertEquals( "net2620_2", v6Etree.findAllMoreSpecific( net2620_0 ).get( 1 ) );
    }

    @Test
    public void testETreeAsn() throws Exception
    {
        IpResource as1_0 = IpResource.parse( "1" );
        IpResource as2_0 = IpResource.parse( "2-4" );
        IpResource as2_1 = IpResource.parse( "3" );

        NestedIntervalMap<IpResource,String> asnEtree = new NestedIntervalMap<IpResource, String>( IpResourceIntervalStrategy.getInstance() );
        asnEtree.put( as1_0, "as1_0" );
        asnEtree.put( as2_0, "as2_0" );
        asnEtree.put( as2_1, "as2_1" );
        assertEquals( "as1_0", asnEtree.findExactOrFirstLessSpecific( as1_0 ) );
        assertEquals( "as2_0", asnEtree.findExactOrFirstLessSpecific( as2_0 ) );
        assertEquals( "as2_0", asnEtree.findFirstLessSpecific( as2_1 ) );
        assertEquals( "as2_1", asnEtree.findExactOrFirstLessSpecific( as2_1 ) );
    }
}
