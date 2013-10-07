package com.waitwha.nessus.trendanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import com.waitwha.logging.LogManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: Configuration<br/>
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
public class Configuration extends Properties {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(Configuration.class);
	
	/**
	 * Configuration Directory. This is a directory within the $HOME of 
	 * the user running the application. This directory is automatically 
	 * created on the first run and all configurations are saved to this
	 * directory when save(File file) is called.
	 */
	public static final String CONFDIR = 
			System.getProperty("user.home") + File.separator + ".config" + File.separator + "nta";
	
	private File file;
	
	private Configuration(File configFile) throws FileNotFoundException, IOException  {
		super();
		this.file = configFile;
		this.load(new FileReader(file));
		log.finest(String.format("Loaded configuration from file %s.", file));
	}
	
	public Configuration()  {
		super();
	}

	/**
	 * Saves the Configuration to a given File. If the given File does 
	 * not have a parent directory set, this will save the File within 
	 * the CONFDIR.
	 * 
	 * @param file		File to save Configuration to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see #CONFDIR
	 */
	public void save(File file) throws FileNotFoundException, IOException  {
		if(file.getParent() == null)
			file = new File(CONFDIR, file.toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
		Date now = new Date();
		this.store(new FileOutputStream(file), String.format("Lasted updated %s", sdf.format(now)));
		log.finest(String.format("Successfully saved configuration to %s", file));
	}
	
	/**
	 * Saves the Configuration to the filesystem at the path of the given File in 
	 * the constructor.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void save() throws FileNotFoundException, IOException  {
		if(this.file == null)
			throw new IOException("Cannot save to a file without a filename. Please use Configuration.save(File file) since this instance was derived not from a file but dynamically.");
		
		this.save(this.file);
	}
	
	/**
	 * Returns an instance of Configuration for the given File.
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final Configuration getInstance(File file) throws FileNotFoundException, IOException  {
		return new Configuration(file); 
	}
	
	public static final Configuration getInstance(String name)  {
		Configuration c = new Configuration();
		
		String path = "/" + Configuration.class.getPackage().getName().replace(".", "/") +"/"+ name +".properties";
		try {
			c.load(Configuration.class.getResourceAsStream(path));
			log.finest(String.format("Successfully loaded configuration from resource %s", path));
			
		}catch(IOException e) {
			log.warning(String.format("Could not load configuration from resource %s: %s", path, e.getMessage()));
			
		}
		
		return c;
	}
	
}
