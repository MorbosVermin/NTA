package com.waitwha.nessus.trendanalyzer;

import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.waitwha.logging.LogManager;
import com.waitwha.nessus.trendanalyzer.gui.TrendAnalyzerFrame;
import com.waitwha.util.ArrayUtils;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: TrendAnalyzer<br/>
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
 * TrendAnalyzer Application implementation. We will use the TrendAnalyzerFrame class
 * as the main window and take care of the Configuration loading and saving here.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer
 */
public class TrendAnalyzer extends Application {

	private static final Logger log = LogManager.getLogger(TrendAnalyzer.class);
	
	/**
	 * Constructor. This will use the TrendAnalyzerFrame class 
	 * as the main window/dialog of the application.
	 *
	 */
	public TrendAnalyzer()  {
		super(new TrendAnalyzerFrame());
	}
	
	/**
	 * Setup the ConfigurationManager and GUI.
	 * 
	 * @see com.waitwha.nessus.trendanalyzer.Application#init()
	 */
	@Override
	protected void init() {
		ConfigurationManager.getInstance();
		Configuration gui = ConfigurationManager.getInstance().getConfiguration("gui");
		String defaultLaf = gui.getProperty("laf");
		if(defaultLaf == null)
			defaultLaf = "Nimbus";
		
		/*
		 * Setup LaF
		 */
		for(LookAndFeelInfo info : TrendAnalyzer.getAllLookAndFeels())  {
			log.finest("Found installed LaF: "+ info.getName());
			if(info.getName().equals(defaultLaf))  {
				try {
					UIManager.setLookAndFeel(info.getClassName());
					SwingUtilities.updateComponentTreeUI(getMainFrame());
					
					log.finest("Successfully set LaF: "+ info.getName());
					gui.setProperty("laf", info.getName());
					
				}catch(ClassNotFoundException | InstantiationException
						| IllegalAccessException | UnsupportedLookAndFeelException e) {
					log.warning("Could not set LaF to "+ info.getName() +": "+ e.getClass().getName() +" "+ e.getMessage());
				}
				
			}
		}
	}
	
	/**
	 * Returns an array of LookAndFeelInfo objects for the installed LaFs 
	 * with the jgoodies looks included.
	 * 
	 * @return	LookAndFeelInfo[]
	 */
	public static final LookAndFeelInfo[] getAllLookAndFeels()  {
		String[] jgoodies = new String[] {
				"com.jgoodies.looks.windows.WindowsLookAndFeel",
				"com.jgoodies.looks.plastic.Plastic3DLookAndFeel",
				"com.jgoodies.looks.plastic.PlasticLookAndFeel",
				"com.jgoodies.looks.plastic.PlasticXPLookAndFeel"
		};

		LookAndFeelInfo[] jgoodiesLafs = new LookAndFeelInfo[4];
		int c = 0;
		for(String jgoodie : jgoodies)  {
			try  {
				String name = jgoodie.substring(jgoodie.lastIndexOf('.') + 1);
				jgoodiesLafs[c] = new LookAndFeelInfo(name, jgoodie);
				c++;

			}catch(Exception e)  {}
		}

		return ArrayUtils.concat(jgoodiesLafs, UIManager.getInstalledLookAndFeels());
	}

	/**
	 * Save the Configuration before exiting.
	 * 
	 * @see com.waitwha.nessus.trendanalyzer.Application#finish()
	 */
	@Override
	protected void finish() {
		try {
			ConfigurationManager.getInstance().saveAll();
			
		}catch(IOException e) {
			log.warning("Could not save all of the configurations: "+ e.getMessage());
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TrendAnalyzer().start();
	}

}
