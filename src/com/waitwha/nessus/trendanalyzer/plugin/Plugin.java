package com.waitwha.nessus.trendanalyzer.plugin;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.waitwha.nessus.NessusClientData;
import com.waitwha.nessus.trendanalyzer.gui.TrendAnalysisPane;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: Plugin<br/>
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
 * Plugin which is called from within TrendAnalysisPane.run() to handle and provide 
 * NessusClientData scan results/analysis. getName is used to provide the tab label 
 * while getContents is used to provide the contents.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.plugin
 */
public interface Plugin {
	
	/**
	 * Returns the name of this plugin. This is String value is 
	 * used within TrendAnalysisPane to label the tabs. This value
	 * should be internationalized using I18n.
	 * 
	 * @return	String name of the plugin.
	 * @see I18n#trans(String key)
	 * @see TrendAnalysisPane#run()
	 */
	public String getName();
	
	/**
	 * Sets this plugin as (in)active.
	 * 
	 * @param active	boolean (in)activate this plugin.
	 */
	public void setActive(boolean active);
	
	/**
	 * Returns whether or not this plugin is (in)active.
	 * 
	 * @return	boolean (in)active plugin.
	 */
	public boolean isActive();
	
	/**
	 * Returns the contents of this plugin. This method is called 
	 * from within the TrendAnalysisPane to provide contents for
	 * the tabs.
	 * 
	 * @param data	ArrayList<NessusClientData> data to work with.
	 * @return	JPanel contents for this tab within the TrendAnalysisPane class.
	 * @see TrendAnalysisPane#run()
	 */
	public JPanel getContents(ArrayList<NessusClientData> data);

}
