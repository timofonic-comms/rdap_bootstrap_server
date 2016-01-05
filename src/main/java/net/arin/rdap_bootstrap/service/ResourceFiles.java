/*
 * Copyright (C) 2013-2015 American Registry for Internet Numbers (ARIN)
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arin.rdap_bootstrap.Constants;

/**
 * Manages getting resource files.
 * 
 * @version $Rev$, $Date$
 */
public class ResourceFiles
{
	public enum BootFiles
	{
		DEFAULT("default_bootstrap"),
		AS("as_bootstrap"),
		DOMAIN("domain_bootstrap"),
		V4("v4_bootstrap", "v4_*_bootstrap"),
		V6("v6_bootstrap", "v6_*_bootstrap"),
		ENTITY("entity_bootstrap");

		private String key;
		private String regex;

		public String getKey()
		{
			return key;
		}

		private BootFiles(String key)
		{
			this(key, key);
		}

		private BootFiles(String key, String regex)
		{
			this.key = key;
			this.regex = regex;
		}

		public static BootFiles fromString(String string)
		{
			for (BootFiles bootFiles : values())
			{
				if (string.equalsIgnoreCase(bootFiles.getKey()))
					return bootFiles;
			}

			throw new IllegalArgumentException("No constant with text " + string + " found");
		}

		public boolean hasRegex()
		{
			return key != regex;
		}

		public String getRegex()
		{
			return regex;
		}

		public void setRegex(String regex)
		{
			this.regex = regex;
		}
	}

	private Properties resourceFiles;
	private HashMap<String, Boolean> isFile;

	public ResourceFiles() throws IOException
	{
		String extFileName = System.getProperty(Constants.PROPERTY_PREFIX + "resource_files");
		resourceFiles = new Properties();
		File file;
		if (extFileName == null)
		{
			InputStream inputStream = getClass().getResourceAsStream("/resource_files.properties");
			resourceFiles.load(inputStream);
		} else if ((file = new File(extFileName)).isFile())
		{
			InputStream inputStream = new FileInputStream(file);
			resourceFiles.load(inputStream);
		}
		// override with explicitly set system properties
		for (BootFiles bootFiles : BootFiles.values())
		{
			String value = System.getProperty(Constants.PROPERTY_PREFIX + "bootfile." + bootFiles.key);
			if (value != null && value.length() > 0)
			{
				resourceFiles.put(bootFiles.key, value);
			}
		}
		isFile = new HashMap<String, Boolean>();
		for (Entry<Object, Object> entry : resourceFiles.entrySet())
		{
			file = new File(entry.getValue().toString());
			isFile.put(entry.getKey().toString(), file.exists());
		}
	}

	public InputStream getInputStream(String key) throws FileNotFoundException
	{
		if (isFile.get(key))
		{
			File file = new File(resourceFiles.getProperty(key));
			return new FileInputStream(file);
		}
		// else
		return getClass().getResourceAsStream(resourceFiles.getProperty(key));
	}

	/**
	 * @param key
	 * @return All InputStreams, including wildcard keys associated to that key.
	 *         For 'v4_bootstrap' returns the 'v4_bootstrap' InputStream and the
	 *         'v4_*_bootstrap' InputStreams, if any.
	 * @throws FileNotFoundException
	 */
	public List<InputStream> getInputStreams(String key) throws FileNotFoundException
	{
		final ArrayList<InputStream> res = new ArrayList<InputStream>();

		res.add(getInputStream(key));

		// add wildcard entries
		final String regex = BootFiles.fromString(key).getRegex().replace("*", "\\w+");
		final Pattern pattern = java.util.regex.Pattern.compile(regex);
		for (Entry<Object, Object> entry : resourceFiles.entrySet())
		{
			final Matcher matcher = pattern.matcher(entry.getKey().toString());
			if (key != entry.getKey() && matcher.matches())
			{
				res.add(getInputStream(entry.getKey().toString()));
			}
		}

		return res;
	}

	public long getLastModified(String key)
	{
		if (!isFile.get(key))
		{
			return System.currentTimeMillis();
		}
		// else
		File file = new File(resourceFiles.getProperty(key));
		return file.lastModified();
	}

	public String getKeyLocation(String key)
	{
		return (String) resourceFiles.get(key);
	}
}
