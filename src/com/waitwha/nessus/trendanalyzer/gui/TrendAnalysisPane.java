package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.Cursor;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.waitwha.nessus.NessusClientData;
import com.waitwha.nessus.trendanalyzer.BackgroundWorker;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener;
import com.waitwha.nessus.trendanalyzer.plugin.Plugin;
import com.waitwha.nessus.trendanalyzer.plugin.PluginManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: TrendAnalysisPane<br/>
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
 * @package com.waitwha.nessus.trendanalyzer.gui
 */
public class TrendAnalysisPane 
	extends JTabbedPane 
	implements Runnable, BackgroundWorkerListener {

	private static final long serialVersionUID = 1L;

	private ArrayList<NessusClientData> scanData;
	
	public TrendAnalysisPane()  {
		super();
	}
	
	private void update()  {
		SwingUtilities.invokeLater(new Runnable()  {

			@Override
			public void run() {
				invalidate();
				repaint();
			}
			
		});
	}
	
	public void update(ArrayList<NessusClientData> scanData)  {
		this.scanData = scanData;
		BackgroundWorker.addBackgroundWorkerListener(this);
		BackgroundWorker.execute(this);
	}

	@Override
	public void started(BackgroundWorkerEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	@Override
	public void stopped(BackgroundWorkerEvent e) {
		BackgroundWorker.removeBackgroundWorkerListener(this);
		this.setCursor(Cursor.getDefaultCursor());
		update();
	}

	@Override
	public void progress(BackgroundWorkerEvent e) {}

	@Override
	public void run() {
		BackgroundWorker.threadStart(new BackgroundWorkerEvent(this, "", 0));
		this.removeAll();
		
		/*
		 * Send data to each plugin.
		 */
		for(Plugin plugin : PluginManager.getInstance())  {
			if(plugin.isActive())  {
				JPanel content = plugin.getContents(scanData);
				this.add(plugin.getName(), content);
			}
		}
		
		BackgroundWorker.threadStopped(new BackgroundWorkerEvent(this, "", 100));
	}

}
