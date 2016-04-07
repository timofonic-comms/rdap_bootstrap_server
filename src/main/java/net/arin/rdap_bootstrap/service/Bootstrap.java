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

}
