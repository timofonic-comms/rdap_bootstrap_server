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
 */
package net.arin.rdap_bootstrap.source;

import net.arin.rdap_bootstrap.format.Format;
import net.arin.rdap_bootstrap.lookup.Store;

/**
 * Defines the behavior for a registry source
 */
public interface Source
{
    /*
     * Sets the pace where the values of the source are to be set.
     */
    void setStore( Store store );

    /*
     * Sets the format of the source
     */
    void setFormat( Format format );

    /*
     * Instructs the source to do what is necessary to load data into the store using the format.
     * Will only be called after the other methods have been called.
     * It is also assumed that if the source is to be regularly checked, this method will
     * initiate any background tasks.
     */
    void execute();
}
