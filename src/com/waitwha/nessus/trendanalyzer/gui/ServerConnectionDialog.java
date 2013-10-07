package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.waitwha.nessus.trendanalyzer.Configuration;
import com.waitwha.nessus.trendanalyzer.ConfigurationManager;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: ServerConnectionDialog<br/>
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
public class ServerConnectionDialog extends BaseDialog {
	
	private static final long serialVersionUID = 1L;
	private static final I18n i18n = I18nManager.getI18n(ServerConnectionDialog.class);
	
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private ServersComboBox cboServers;
	
	public ServerConnectionDialog(JFrame parent)  {
		super(parent, i18n.trans("dialog.title"), i18n.trans("dialog.subtitle"));
		
		this.txtUsername = new JTextField(20);
		Configuration server = ConfigurationManager.getInstance().getConfiguration("servers");
		String lastUsername = server.getProperty("last.username");
		this.txtUsername.setText(lastUsername);
		
		this.txtPassword = new JPasswordField(20);
		this.cboServers = new ServersComboBox();
		
		FormLayout layout = new FormLayout(
				"right:pref,$lcgap,pref", 
				"pref,$lgap,pref,$lgap,pref");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel(i18n.trans("label.username"), cc.xy(1, 1));
		builder.add(this.txtUsername, cc.xy(3, 1));
		builder.addLabel(i18n.trans("label.password"), cc.xy(1, 3));
		builder.add(this.txtPassword, cc.xy(3, 3));
		builder.addLabel(i18n.trans("label.server"), cc.xy(1, 5));
		builder.add(this.cboServers, cc.xy(3, 5));
		JPanel contents = builder.getPanel();
		this.getContentPane().add(contents, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 1));
		JButton btnOk = new JButton(i18n.trans("button.ok"));
		btnOk.addActionListener(new ActionListener()  {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		buttons.add(btnOk);
		buttons.add(btnCancel);
		btnCancel.addActionListener(new ActionListener()  {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		
		this.getContentPane().add(buttons, BorderLayout.PAGE_END);
		this.setResizable(false);
		this.pack();
		this.getRootPane().setDefaultButton(btnOk);
	}
	
	public String getUsername()  {
		return this.txtUsername.getText();
	}
	
	public String getPassword()  {
		return this.txtPassword.getText();
	}
	
	public String getServer()  {
		return (String)this.cboServers.getSelectedItem();
	}

}
