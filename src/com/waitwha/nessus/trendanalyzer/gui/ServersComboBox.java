package com.waitwha.nessus.trendanalyzer.gui;

import javax.swing.JComboBox;

import com.waitwha.nessus.trendanalyzer.Configuration;
import com.waitwha.nessus.trendanalyzer.ConfigurationManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: ServersComboBox<br/>
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
 * Looks to Configuration for entries.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.gui
 */
public class ServersComboBox extends JComboBox<String> {

	private static final long serialVersionUID = 1L;

	public ServersComboBox()  {
		super();
		Configuration servers = ConfigurationManager.getInstance().getConfiguration("servers");
		String[] s = servers.getProperty("servers").split(",");
		int i = 0;
		for(String server : s)  {
			this.addItem(server);
			if(server.equals(servers.getProperty("last.server")))
				this.setSelectedIndex(i);
			
			i++;
		}
		
		this.setEditable(false);
	}
	
}
