package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: BaseDialog<br/>
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
public class BaseDialog extends JDialog {

	private static final I18n i18n = I18nManager.getI18n(BaseDialog.class);
	private static final long serialVersionUID = 1L;
	private boolean canceled;
	protected JButton btnCancel;

	public BaseDialog(JFrame parent, String title, String subtitle)  {
		super(parent, title, true);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new TopPanel(subtitle), BorderLayout.PAGE_START);
		
		this.canceled = false;
		this.btnCancel = new JButton(i18n.trans("button.cancel"));
		this.btnCancel.addActionListener(new ActionListener()  {

			@Override
			public void actionPerformed(ActionEvent e) {
				canceled = true;
			}
			
		});
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	protected void setCanceled(boolean canceled)  {
		this.canceled = canceled;
	}
	
	public boolean isCanceled()  {
		return this.canceled;
	}
	
	public void setWait(boolean wait)  {
		this.getRootPane().setCursor( (wait) ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor() );
	}
	
	/**
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		if(b)  {
			JFrame parent = (JFrame)this.getParent();
			Dimension p = parent.getSize();
			Dimension w = this.getSize();
			int x = (p.width / 2) - (w.width / 2);
			int y = (p.height / 2) - (w.height / 2);
			setLocation(x,y);
		}
		
		super.setVisible(b);
	}
	
	

}
