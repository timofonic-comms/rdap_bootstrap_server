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
 */
package net.arin.rdap_bootstrap.lookup;

/**
 * Represents AS Ranges.
 */
public class AsRangeInfo
{
    private Long asStart;
    private Long asEnd;
    private ServiceUrls serviceUrls;

    public AsRangeInfo( Long asStart, Long asEnd,
                        ServiceUrls serviceUrls )
    {
        this.asStart = asStart;
        this.asEnd = asEnd;
        this.serviceUrls = serviceUrls;
    }

    public Long getAsStart()
    {
        return asStart;
    }

    public Long getAsEnd()
    {
        return asEnd;
    }

    public ServiceUrls getServiceUrls()
    {
        return serviceUrls;
    }
}
