package com.waitwha.nessus.trendanalyzer.i18n;

import java.util.Hashtable;
import java.util.Locale;
import java.util.logging.Logger;

import com.waitwha.logging.LogManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: I18nManager<br/>
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
public class I18nManager extends Hashtable<Class<?>, I18n>  {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(I18nManager.class.getName());
	
	private static I18nManager manager;
	
	private I18nManager()  {
		super();
	}
	
	/**
	 * Returns a I18n object for the given Class. 
	 * 
	 * @param clazz
	 * @return
	 */
	public static final I18n getI18n(Class<?> clazz)  {
		I18nManager manager = I18nManager.getInstance();
		if(manager.containsKey(clazz))  {
			log.finest(String.format("Retrieving cached i18n for %s", clazz.getName()));
			return manager.get(clazz);
		}
		
		//  -->        /com/waitwha/nessus/trenanalyzer/gui/DialogName-en_US.properties
		String file = "/" + clazz.getName().replace(".", "/") +"-" + Locale.getDefault() +".properties";
		I18n i18n = new I18n();
		
		try  {
			i18n.load(clazz.getResourceAsStream(file));
			log.finest(String.format("Successully loaded %d translations from %s", i18n.keySet().size(), file));
			
		}catch(Exception e)  {
			log.warning(String.format("Could not load i18n translations from resource '%s': %s %s", file, e.getClass().getName(), e.getMessage()));
			
		}
		
		I18nManager.cache(clazz, i18n);
		return i18n;
	}
	
	private static final I18nManager getInstance()  {
		if(manager == null)
			manager = new I18nManager();
		
		return manager;
	}
	
	private static final void cache(Class<?> clazz, I18n i18n)  {
		I18nManager manager = I18nManager.getInstance();
		if(!manager.containsKey(clazz))  {
			log.finest(String.format("successfully cached i18n translations for %s.", clazz.getName()));
			manager.put(clazz, i18n);
		}
	}
	
}
