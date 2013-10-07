package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.waitwha.nessus.NessusClientData;
import com.waitwha.nessus.server.Server;
import com.waitwha.nessus.trendanalyzer.BackgroundWorker;
import com.waitwha.nessus.trendanalyzer.Configuration;
import com.waitwha.nessus.trendanalyzer.ConfigurationManager;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: DownloadReportsDialog<br/>
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
public class DownloadReportsDialog 
	extends BaseDialog 
	implements Runnable, BackgroundWorkerListener {

	private static final long serialVersionUID = 1L;
	private static final I18n i18n = I18nManager.getI18n(DownloadReportsDialog.class);
	
	private JLabel status;
	private JProgressBar progBar;
	private ArrayList<NessusClientData> scanData;
	private Server nessus;
	private ArrayList<String> uuids;
	
	public DownloadReportsDialog(JFrame parent, Server nessus, ArrayList<String> uuids)  {
		super(parent, i18n.trans("dialog.title"), i18n.trans("dialog.subtitle"));
		
		this.nessus = nessus;
		this.uuids = uuids;
		
		JPanel contents = new JPanel(new GridLayout(2,1,1,1));
		contents.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.status = new JLabel(i18n.trans("status.wait"));
		contents.add(this.status);
		this.progBar = new JProgressBar();
		contents.add(this.progBar);
		this.getContentPane().add(contents, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 1));
		buttons.add(btnCancel);
		this.getContentPane().add(buttons, BorderLayout.PAGE_END);
		
		this.setResizable(false);
		this.pack();
		
		BackgroundWorker.addBackgroundWorkerListener(this);
		this.addWindowListener(new WindowAdapter()  {

			/**
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e)  {
				start();
			}
			
		});
	}
	
	public ArrayList<NessusClientData> getScanData()  {
		return this.scanData;
	}
	

	@Override
	public void started(BackgroundWorkerEvent e) {
		setWait(true);
	}

	
	@Override
	public void stopped(BackgroundWorkerEvent e) {
		BackgroundWorker.removeBackgroundWorkerListener(this);
		setWait(false);
		dispose();
	}

	
	@Override
	public void progress(BackgroundWorkerEvent e) {
		this.status.setText(e.getStatus());
		this.progBar.setValue(e.getProgress());
		SwingUtilities.invokeLater(new Runnable()  {

			@Override
			public void run() {
				status.invalidate();
				progBar.invalidate();
				status.repaint();
				progBar.repaint();
			}
			
		});
	}
	
	private void start()  {
		BackgroundWorker.execute(this);
	}
	

	@Override
	public void run() {
		BackgroundWorker.threadStart(new BackgroundWorkerEvent(this, i18n.trans("status.wait"), 0));
		
		scanData = new ArrayList<NessusClientData>();
		Configuration system = ConfigurationManager.getInstance().getConfiguration("system");
		File tmpdir = new File(system.getProperty("java.io.tmpdir"));
		int total = uuids.size();
		int p = 0;
		int c = 0;
		for(String uuid : uuids)  {
			if(isCanceled())
				break;
			
			File file = new File(tmpdir, uuid +".nessus");
			try {
				p = (c * 100) / total;
				BackgroundWorker.threadProgress(new BackgroundWorkerEvent(this, String.format(i18n.trans("status.download"), uuid), p));
				NessusClientData scan = nessus.downloadReport(uuid, file.toString());
				scanData.add(scan);
				
			}catch(Exception e1) {
				//TODO Handle parsing/IO exceptions properly.
				
			}
			
			c++;
		}
		
		BackgroundWorker.threadStopped(new BackgroundWorkerEvent(this, i18n.trans("status.complete"), 100));
	}
	
}
