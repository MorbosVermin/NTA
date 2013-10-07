package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.waitwha.nessus.trendanalyzer.BackgroundWorker;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: StatusBar<br/>
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
public class StatusBar 
	extends JPanel 
	implements BackgroundWorkerListener {

	private static final long serialVersionUID = 1L;
	private String text;
	private JProgressBar progBar;
	
	public StatusBar(String text)  {
		super(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.text = text;
		this.progBar = new JProgressBar();
		this.add(this.progBar);
		BackgroundWorker.addBackgroundWorkerListener(this);
	}
	
	private void update()  {
		try  {
			SwingUtilities.invokeLater(new Runnable()  {

				@Override
				public void run() {
					invalidate();
					repaint();
				}
				
			});
		}catch(Exception e) {}
	}
	
	public void setText(String text)  {
		this.text = text;
		update();
	}
	
	public void setProgress(int progress)  {
		this.progBar.setValue(progress);
		update();
	}
	
	public void reset()  {
		this.setText("");
		this.setProgress(0);
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawString(text, 5, 12);
	}
	
	/**
	 * @see com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener#started(com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent)
	 */
	@Override
	public void started(BackgroundWorkerEvent e) {
		this.setText(e.getStatus());
		this.setProgress(e.getProgress());
	}

	/**
	 * @see com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener#stopped(com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent)
	 */
	@Override
	public void stopped(BackgroundWorkerEvent e) {
		this.setText(e.getStatus());
		this.setProgress(e.getProgress());
	}

	/**
	 * @see com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener#progress(com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent)
	 */
	@Override
	public void progress(BackgroundWorkerEvent e) {
		this.setText(e.getStatus());
		this.setProgress(e.getProgress());
	}

}
