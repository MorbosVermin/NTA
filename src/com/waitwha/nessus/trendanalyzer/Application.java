package com.waitwha.nessus.trendanalyzer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.waitwha.logging.LogManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: Application<br/>
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
public class Application implements Runnable {
	
	public static class Console  {
		
		public static final synchronized void write(String line)  {
			System.out.print(line);
		}
		
		public static final synchronized void writeLine(String line)  {
			System.out.println(line);
		}
		
	}
	
	private static final Logger log = LogManager.getLogger(Application.class);
	private JFrame mainFrame;
	private Thread runner;
	
	public Application(JFrame mainFrame)  {
		this.mainFrame = mainFrame;
		this.runner = null;
	}
	
	/**
	 * Returns the ConfigurationManager
	 * 
	 * @return	ConfigurationManager
	 */
	public ConfigurationManager getConfigurationManager()  {
		return ConfigurationManager.getInstance();
	}
	
	/**
	 * Provided to initialize various things before the Application starts.
	 * 
	 */
	protected void init()  {
		log.finest("Did not do anything within Application.init(). If you would like to perform something here, override the method.");
	}
	
	/**
	 * Provided to execute stuff when shutting down the Application. 
	 * 
	 */
	protected void finish()  {
		log.finest("Did not do anything within Application.finish(). If you would like to perform something here, override the method.");
	}
	
	/**
	 * Starts the Application instance.
	 */
	public void start()  {
		this.runner = new Thread(this);
		this.runner.start();
		log.finest(String.format("Started application using JFrame: %s", mainFrame.getClass().getName()));
	}
	
	/**
	 * Stops the Application instance.
	 */
	public void stop()  {
		if(this.runner != null && this.runner.isAlive())  {
			this.runner.interrupt();
			this.runner = null;
		}
	}

	public JFrame getMainFrame()  {
		return this.mainFrame;
	}
	
	@Override
	public void run() {
		this.init();
		
		log.finest(String.format("Showing dialog %s", this.mainFrame.getClass().getName()));
		this.mainFrame.setVisible(true);
		this.mainFrame.addWindowListener(new WindowAdapter()  {

			/**
			 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosed(WindowEvent e) {
				log.finest(String.format("Caught closing of %s. Shutting down.", mainFrame.getClass().getName()));
				finish();
			}
			
		});
	}
	
}
