package com.waitwha.nessus.trendanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Logger;

import com.waitwha.logging.LogManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: ConfigurationManager<br/>
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
 * @package com.waitwha.nessus.trendanalyzer
 */
public class ConfigurationManager extends Hashtable<String, Configuration> {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ConfigurationManager.class);
	private static ConfigurationManager instance;
	
	private ConfigurationManager()  {
		super();
		
		Configuration c = new Configuration();
		c.putAll(System.getProperties());
		this.put("system", c);
		log.finest("Added system configuration.");
		
		/*
		 * Look to the CONFDIR for previously saved Configuration objects or
		 * create the directory and load defaults from resources bundled with 
		 * the project.
		 */
		File confdir = new File(Configuration.CONFDIR);
		if(! confdir.exists())  {
			if(! confdir.mkdirs())
				log.warning(String.format("Could not create directory %s", confdir.toString()));
			else
				log.finest(String.format("Successfully created configuration directory %s", confdir.toString()));
			
		}else{
			File[] files = confdir.listFiles();
			for(File file : files)  {
				if(file.isDirectory())
					continue;
				
				String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
				if(name.equals("system"))
					continue;
				
				try  {
					this.put(name, Configuration.getInstance(file));
					
				}catch(Exception e)  {
					log.warning(String.format("Could not load configuration file %s: %s %s", file, e.getClass().getName(), e.getMessage()));
					
				}
			}
		}
		
		if(!this.containsKey("gui"))
			this.put("gui", Configuration.getInstance("gui"));
		
		if(!this.containsKey("servers"))
			this.put("servers", Configuration.getInstance("servers"));
		
		if(!this.containsKey("app"))
			this.put("app", Configuration.getInstance("app"));
		
	}
	
	public Configuration getConfiguration(String group)  {
		return this.get(group);
	}
	
	public String getProperty(String group, String key)  {
		if(this.containsKey(group))
			return this.get(group).getProperty(key);
		
		log.warning(String.format("Configuration group '%s' does not exist.", group));
		return "";
	}
	
	public void addConfiguration(String name, File file) throws FileNotFoundException, IOException   {
		Configuration c = Configuration.getInstance(file);
		this.put(name, c);
		log.finest(String.format("Added configuration '%s' from %s to manager: %d total config(s)", name, file, this.size()));
	}
	
	public synchronized void saveAll() throws FileNotFoundException, IOException  {
		for(String group : this.keySet())  {
			if(group.equals("system"))
				continue;
			
			Configuration c = this.getConfiguration(group);
			c.save(new File(group +".properties"));
		}
	}
	
	public static final ConfigurationManager getInstance()  {
		if(instance == null)
			instance = new ConfigurationManager();
		
		return instance;
	}

}
