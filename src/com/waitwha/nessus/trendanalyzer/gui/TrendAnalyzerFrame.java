package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.waitwha.nessus.server.Server;
import com.waitwha.nessus.trendanalyzer.Configuration;
import com.waitwha.nessus.trendanalyzer.ConfigurationManager;
import com.waitwha.nessus.trendanalyzer.TrendAnalyzer;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;
import com.waitwha.nessus.trendanalyzer.plugin.Plugin;
import com.waitwha.nessus.trendanalyzer.plugin.PluginManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: TrendAnalyzerFrame<br/>
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
 * Primary interface for the application.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.gui
 */
public class TrendAnalyzerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final I18n i18n = I18nManager.getI18n(TrendAnalyzerFrame.class);

	/**
	 * Menubar used in the main frame.
	 */
	private class Menubar extends JMenuBar  {
		
		private static final long serialVersionUID = 1L;

		public Menubar()  {
			super();
			
			JMenu mnuFile = new JMenu(i18n.trans("menu.file"));
			
			JMenuItem mnuFileOpen = new JMenuItem(i18n.trans("menu.file.open"));
			mnuFileOpen.addActionListener(new ActionListener()  {

				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
				
			});
			mnuFileOpen.addMouseListener(new MouseAdapter()  {

				/**
				 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseEntered(MouseEvent e) {
					statusbar.setText(i18n.trans("menu.file.open.tooltip"));
				}
				
			});
			mnuFile.add(mnuFileOpen);
			
			JMenuItem mnuFileRemoteOpen = new JMenuItem(i18n.trans("menu.file.remote_open"));
			mnuFileRemoteOpen.addActionListener(new ActionListener()  {

				@Override
				public void actionPerformed(ActionEvent e) {
					ServerConnectionDialog dlg = new ServerConnectionDialog(ref());
					dlg.setVisible(true);
					if(!dlg.isCanceled())  {
						setWait(true);
						Server nessus = new Server(dlg.getServer());
						if(! nessus.login(dlg.getUsername(), dlg.getPassword()))  {
							setWait(false);
							JOptionPane.showMessageDialog(ref(), i18n.trans("error.login"), i18n.trans("error"), JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						setWait(false);
						Configuration server = ConfigurationManager.getInstance().getConfiguration("servers");
						server.setProperty("last.username", dlg.getUsername());
						server.setProperty("last.server", dlg.getServer());
						
						ReportListDialog rld = new ReportListDialog(ref(), nessus);
						rld.setVisible(true);
						
						if((! rld.isCanceled()) && rld.getSelectedUuids().size() > 0)  {
							DownloadReportsDialog drd = new DownloadReportsDialog(ref(), nessus, rld.getSelectedUuids());
							drd.setVisible(true);
							
							if(! drd.isCanceled())
								analysisPane.update(drd.getScanData());
							
						}
						
						nessus.logout();
					}
				}
				
			});
			mnuFileRemoteOpen.addMouseListener(new MouseAdapter()  {

				/**
				 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseEntered(MouseEvent e) {
					statusbar.setText(i18n.trans("menu.file.remote_open.tooltip"));
				}
				
			});
			mnuFile.add(mnuFileRemoteOpen);
			
			mnuFile.addSeparator();
			
			JMenuItem mnuFileExit = new JMenuItem(i18n.trans("menu.file.exit"));
			mnuFileExit.addActionListener(new ActionListener()  {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
				
			});
			mnuFileExit.addMouseListener(new MouseAdapter()  {

				/**
				 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseEntered(MouseEvent e) {
					statusbar.setText(i18n.trans("menu.file.exit.tooltip"));
				}
				
			});
			mnuFile.add(mnuFileExit);
			
			this.add(mnuFile);
			
			JMenu mnuEdit = new JMenu(i18n.trans("menu.edit"));
			JMenuItem mnuEditOptions = new JMenuItem(i18n.trans("menu.edit.options"));
			mnuEditOptions.addActionListener(new ActionListener()  {

				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO Open Options dialog.
				}
				
			});
			mnuEdit.add(mnuEditOptions);
			JMenu mnuEditPlugins = new JMenu(i18n.trans("menu.edit.plugins"));
			if(PluginManager.getInstance().size() == 0)  {
				JMenuItem noplugins = new JMenuItem(i18n.trans("menu.edit.plugins.noplugins"));
				noplugins.setEnabled(false);
				mnuEditPlugins.add(noplugins);
				
			}else{
				for(final Plugin p : PluginManager.getInstance())  {
					final JCheckBoxMenuItem mnuItem = new JCheckBoxMenuItem(p.getName());
					mnuItem.setSelected(p.isActive());
					mnuItem.addActionListener(new ActionListener()  {
	
						@Override
						public void actionPerformed(ActionEvent e) {
							PluginManager.getInstance().setActive(p, mnuItem.isSelected());
						}
						
					});
					mnuEditPlugins.add(mnuItem);
				}
			}
			mnuEdit.add(mnuEditPlugins);
			
			JMenu mnuEditTheme = new JMenu(i18n.trans("menu.edit.themes"));
			ButtonGroup themes = new ButtonGroup();
			final Configuration gui = ConfigurationManager.getInstance().getConfiguration("gui");
			for(final LookAndFeelInfo info : TrendAnalyzer.getAllLookAndFeels())  {
				JRadioButtonMenuItem mnuItem = new JRadioButtonMenuItem(info.getName());
				mnuItem.addActionListener(new ActionListener()  {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							UIManager.setLookAndFeel(info.getClassName());
							SwingUtilities.updateComponentTreeUI(ref());
							gui.setProperty("laf", info.getName());
							
						} catch (ClassNotFoundException | InstantiationException
								| IllegalAccessException | UnsupportedLookAndFeelException e1) {
							
							JOptionPane.showMessageDialog(ref(), 
									String.format(i18n.trans("error.theme"), info.getName(), e1.getMessage()), 
									i18n.trans("error"), 
									JOptionPane.ERROR_MESSAGE);
							
						}
					}
					
				});
				
				if(info.getName().equals(gui.getProperty("laf")))
					mnuItem.setSelected(true);
				
				themes.add(mnuItem);
				mnuEditTheme.add(mnuItem);
			}
			mnuEdit.add(mnuEditTheme);
			this.add(mnuEdit);
			
			//TODO Help menu.
		
		}
		
	}
	
	private Menubar menubar;
	private TrendAnalysisPane analysisPane;
	private StatusBar statusbar;
	
	public TrendAnalyzerFrame()  {
		super(i18n.trans("dialog.title"));
		this.getContentPane().setLayout(new BorderLayout());
		
		this.menubar = new Menubar();
		this.getContentPane().add(this.menubar, BorderLayout.PAGE_START);
		
		this.analysisPane = new TrendAnalysisPane();
		this.getContentPane().add(analysisPane, BorderLayout.CENTER);
		
		this.statusbar = new StatusBar("");
		this.statusbar.setPreferredSize(new Dimension(this.getSize().width, 20));
		this.getContentPane().add(this.statusbar, BorderLayout.PAGE_END);
		
		/*
		 * Listen to component events to properly update the Configuration of the "gui". 
		 */
		this.addComponentListener(new ComponentAdapter()  {

			/**
			 * @see java.awt.event.ComponentAdapter#componentMoved(java.awt.event.ComponentEvent)
			 */
			@Override
			public void componentMoved(ComponentEvent e) {
				int x = getLocation().x;
				int y = getLocation().y;
				ConfigurationManager.getInstance().getConfiguration("gui").setProperty("window.x", ""+ x);
				ConfigurationManager.getInstance().getConfiguration("gui").setProperty("window.y", ""+ y);
			}

			/**
			 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
			 */
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension size = getSize();
				ConfigurationManager.getInstance().getConfiguration("gui").setProperty("window.width", ""+ size.width);
				ConfigurationManager.getInstance().getConfiguration("gui").setProperty("window.height", ""+ size.height);
			}
			
		});
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private JFrame ref()  {
		return this;
	}
	
	private void setWait(boolean wait)  {
		this.statusbar.setText( (wait) ? i18n.trans("status.wait") : "");
		this.getRootPane().setCursor( (wait) ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor() );
	}
	
	/**
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		if(b)  {
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			Configuration gui = ConfigurationManager.getInstance().getConfiguration("gui");
			
			int w = Integer.parseInt(gui.getProperty("window.width"));
			int h = Integer.parseInt(gui.getProperty("window.height"));
			Dimension window = new Dimension(w, h);
			
			this.setSize(window);

			int x = Integer.parseInt(gui.getProperty("window.x"));
			int y = Integer.parseInt(gui.getProperty("window.y"));
			if(x >= screen.width || y >= screen.height)  {
				x = (screen.width / 2) - (window.width / 2);
				y = (screen.height / 2) - (window.height / 2);
			}

			this.setLocation(x, y);
		}
		
		super.setVisible(b);
	}
	
}
