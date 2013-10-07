package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: TopPanel<br/>
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
public final class TopPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TopPanel(String message)  {
		super(new FlowLayout(FlowLayout.LEFT, 10, 10));
		this.setBackground(Color.white);
		JTextArea txtArea = new JTextArea(message);
		txtArea.setBorder(BorderFactory.createEmptyBorder());
		txtArea.setEditable(false);
		txtArea.setWrapStyleWord(true);
		txtArea.setLineWrap(true);
		txtArea.setColumns(30);
		txtArea.setRows(2);
		this.add(txtArea);
		this.setPreferredSize(new Dimension(300, 60));
	}
	
}
