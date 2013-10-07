package com.waitwha.nessus.trendanalyzer.plugin;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.waitwha.nessus.NessusClientData;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: BasePlugin<br/>
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
 * Base plugin class which takes care of the active status flag.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.plugin
 */
public abstract class BasePlugin implements Plugin {

	private boolean active;
	
	/**
	 * Constructor (active = true)
	 *
	 */
	public BasePlugin()  {
		this.active = true;
	}
	
	/**
	 * @see com.waitwha.nessus.trendanalyzer.plugin.Plugin#getName()
	 */
	@Override
	public abstract String getName();

	/**
	 * @see com.waitwha.nessus.trendanalyzer.plugin.Plugin#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @see com.waitwha.nessus.trendanalyzer.plugin.Plugin#isActive()
	 */
	@Override
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @see com.waitwha.nessus.trendanalyzer.plugin.Plugin#getContents(java.util.ArrayList)
	 */
	@Override
	public abstract JPanel getContents(ArrayList<NessusClientData> data);

}
