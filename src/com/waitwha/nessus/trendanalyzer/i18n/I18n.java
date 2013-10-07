package com.waitwha.nessus.trendanalyzer.i18n;

import java.util.Properties;
import java.util.logging.Logger;

import com.waitwha.logging.LogManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: I18n<br/>
 * <small>Copyright (c)2013 Mike Duncan &lt;<a href="mailto:mike.duncan@waitwha.com">mike.duncan@waitwha.com</a>&gt;</small><p />
 *
 * <pre>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * </pre>
 *
 * TODO Document this class/interface.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.i18n
 */
public class I18n extends Properties {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(I18n.class.getName());

	/**
	 * Returns the value for the key given. However, if the key does not exist, 
	 * we will log the issue and return the key.
	 * 
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		if(this.containsKey(key))
			return super.getProperty(key);
		
		log.warning(String.format("Translation not found for key '%s'.", key));
		return key;
	}

	
	/**
	 * Alias call for getProperty.
	 * 
	 * @param key	String string to look up translation for. 
	 * @return String translation or blank if no translation exists.
	 * @see #getProperty(String)
	 */
	public String trans(String key)  {
		return this.getProperty(key);
	}
	
}
