package com.waitwha.nessus.trendanalyzer.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;
import java.net.URLClassLoader;
import java.net.URL;

import com.waitwha.logging.LogManager;
import com.waitwha.nessus.trendanalyzer.Configuration;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: PluginManager<br/>
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
 * @package com.waitwha.nessus.trendanalyzer.plugin
 */
public class PluginManager 
	extends ArrayList<Plugin> {

	/**
	 * The directory which is searched for plugins. 
	 */
	public static final String PLUGINDIR = Configuration.CONFDIR + File.separator + "plugins";
	
	private static final Logger log = LogManager.getLogger(PluginManager.class);
	private static final long serialVersionUID = 1L;
	private static PluginManager manager;
	
	private PluginManager() {
		super();
		
		/*
		 * Load default/core plugins.
		 */
		this.add(new OverallPlugin());
		this.add(new HostDiffPlugin());
		log.finest(String.format("Successfully added %d default/core plugin(s).", this.size()));
		
		/*
		 * Search for .jar files in the plugins directory.
		 */
		File pluginDir = new File(PluginManager.PLUGINDIR);
		if(! pluginDir.exists())
			pluginDir.mkdirs();
		
		File[] jars = pluginDir.listFiles(new FilenameFilter()  {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
			
		});
		log.finest(String.format("Found %d jar files within %s", jars.length, PluginManager.PLUGINDIR));
		for(File jar : jars)  {
			log.finest(String.format("Inspecting %s for plugins.", jar));
			JarInputStream jis = null;
			try  {
				jis = new JarInputStream(new FileInputStream(jar));
				JarEntry entry = jis.getNextJarEntry();
				while(entry != null)  {
					if(entry.getName().endsWith(".class"))  {
						log.finest(String.format("Found class: %s", entry.getName()));
						Plugin plugin = pluginLoader(jar, entry.getName());
						if(plugin != null)
							add(plugin);
						
					}
					
					entry = jis.getNextJarEntry();
				}
				
			}catch(Exception e)  {
				log.warning(String.format("Potential issue while inspecting jar %s for plugins: %s %s", jar.toString(), e.getClass().getName(), e.getMessage()));
				
			}finally{
				if(jis != null)  {
					try  {
						jis.close();
						
					}catch(Exception e) {}
				}
			}
			
		}
		
		log.fine(String.format("Loaded %d total plugin(s).", this.size()));
	}
	
	/**
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Plugin e) {
		boolean ok = super.add(e);
		if(ok)
			log.finest(String.format("Successfully loaded plugin: %s", e.getName()));
		
		return ok;
	}

	public void setActive(Plugin p, boolean active)  {
		for(Plugin plugin : this)
			if(p.getName().equals(plugin.getName()))
				plugin.setActive(active);
		
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private final Plugin pluginLoader(File jar, String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException  {
		String path = String.format("jar:file://%s!/", jar.getAbsolutePath());
		Plugin p = null;
		URLClassLoader cl = 
				new URLClassLoader(new URL[] { new File(path).toURL() });
		log.finest(String.format("Successfully loaded JAR: %s", path));
		
		Class clazz = cl.loadClass(name);
		log.finest(String.format("Successfully loaded class: %s", name));
		Class[] interfaces = clazz.getInterfaces();
		
		for(Class i : interfaces)  {
			if(i.getName().equals("com.waitwha.nessus.trendanalyzer.plugin.Plugin"))  {
				p = (Plugin)clazz.newInstance();
				log.finest(String.format("Loaded plugin: %s", p.getName()));
				break;
			}
		}
		
		if(cl != null)
			cl.close();
		
		return p;
	}
	
	public static final PluginManager getInstance()  {
		if(manager == null)
			manager = new PluginManager();
		
		return manager;
	}
	
}
