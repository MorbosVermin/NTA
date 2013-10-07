package com.waitwha.nessus.trendanalyzer.event;

import java.util.EventObject;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: BackgroundWorkerEvent<br/>
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
 * @package com.waitwha.nessus.trendanalyzer.event
 */
public class BackgroundWorkerEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	private String status;
	private int progress;
	
	public BackgroundWorkerEvent(Object worker, String status, int progress)  {
		super(worker);
		this.status = status;
		this.progress = progress;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

}
