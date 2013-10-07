package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.waitwha.nessus.server.Report;
import com.waitwha.nessus.server.Server;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: ReportListDialog<br/>
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
public class ReportListDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;
	private static I18n i18n = I18nManager.getI18n(ReportListDialog.class);
	
	private class TableRow  {
		
		private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		private boolean selected;
		private String name;
		private String uuid;
		private String status;
		private String timestamp;
		
		public TableRow(Report report)  {
			this.selected = false;
			this.name = report.getName();
			this.status = report.getStatus();
			this.uuid = report.getUuid();
			this.timestamp = sdf.format(report.getTimestamp());
		}

		/**
		 * @return the selected
		 */
		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected)  {
			this.selected = selected;
		}
		
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the uuid
		 */
		public String getUuid() {
			return uuid;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @return the timestamp
		 */
		public String getTimestamp() {
			return timestamp;
		}
		
	}
	
	private class Model extends ArrayList<TableRow> implements TableModel  {

		private static final long serialVersionUID = 1L;
		private EventListenerList eventListenerList;

		public Model()  {
			this(new ArrayList<Report>());
			this.eventListenerList = new EventListenerList();
		}
		
		public Model(ArrayList<Report> reports)  {
			super();
			for(Report report : reports)
				this.add(new TableRow(report));
			
		}
		
		public synchronized void updateFromServer() {
			Thread t = new Thread(new Runnable()  {

				@Override
				public void run() {
					clear();
					ArrayList<Report> reports = nessus.getReports();
					for(Report report : reports)
						add(new TableRow(report));
					
					setWait(false);
				}
				
			});
			setWait(true);
			t.start();
			try {
				t.join();
			}catch(InterruptedException e) {}
			
		}
		
		/**
		 * @see java.util.ArrayList#add(java.lang.Object)
		 */
		@Override
		public boolean add(TableRow e) {
			int size = this.size();
			boolean ok = super.add(e);
			if(ok)  {
				int nSize = this.size();
				Object[] listeners = this.eventListenerList.getListenerList();
				for(int i = 0; i < listeners.length; i += 2)
					((TableModelListener)listeners[(i + 1)]).tableChanged(new TableModelEvent(this, size, nSize, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
				
			}
			
			return ok;
		}

		
		/**
		 * @see java.util.ArrayList#clear()
		 */
		@Override
		public void clear() {
			super.clear();
			Object[] listeners = this.eventListenerList.getListenerList();
			for(int i = 0; i < listeners.length; i += 2)
				((TableModelListener)listeners[(i + 1)]).tableChanged(new TableModelEvent(this));
			
		}
		

		@Override
		public int getRowCount() {
			return this.size();
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return i18n.trans("list.column.name"+ columnIndex);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return (columnIndex == 0) ? Boolean.class : String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 0);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			TableRow row = this.get(rowIndex);
			switch(columnIndex)  {
				case 0:
					return new Boolean(row.isSelected());
					
				case 1:
					return row.getName();
					
				case 2:
					return row.getUuid();
					
				case 3:
					return row.getStatus().toUpperCase();
					
				default:
					return row.getTimestamp();
					
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			((TableRow)this.get(rowIndex)).setSelected((boolean)aValue);
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
			this.eventListenerList.add(TableModelListener.class, l);
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			this.eventListenerList.remove(TableModelListener.class, l);
		}
		
	}
	
	private class Table extends JTable  {
		
		private static final long serialVersionUID = 1L;

		public Table()  {
			super(new Model());
			this.setAutoCreateColumnsFromModel(true);
			this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			//this.setAutoCreateRowSorter(true);
			this.setBackground(Color.white);
			this.setGridColor(Color.lightGray);
		}
		
		public void updateFromServer()  {
			((Model)this.getModel()).updateFromServer();
		}
		
		public ArrayList<String> getSelectedUuids()  {
			ArrayList<String> r = new ArrayList<String>();
			for(TableRow row : (Model)this.getModel()) {
				if(row.isSelected())
					r.add(row.getUuid());
				
			}
			
			return r;
		}
		
	}
	
	private Server nessus;
	private JButton btnOk;
	private JButton btnRefresh;
	private Table table;
	
	public ReportListDialog(JFrame parent, Server nessus)  {
		super(parent, i18n.trans("dialog.title"), i18n.trans("dialog.subtitle"));
		this.nessus = nessus;
		
		this.table = new Table();
		JScrollPane jsp = new JScrollPane(table, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBorder(BorderFactory.createEmptyBorder());
		this.getContentPane().add(jsp, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 1));
		this.btnRefresh = new JButton(i18n.trans("button.refresh"));
		this.btnRefresh.addActionListener(new ActionListener()  {

			@Override
			public void actionPerformed(ActionEvent e) {
				table.updateFromServer();
			}
			
		});
		buttons.add(btnRefresh);
		this.btnOk = new JButton(i18n.trans("button.ok"));
		this.btnOk.addActionListener(new ActionListener()  {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
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
		
		this.addWindowListener(new WindowAdapter()  {

			/**
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				table.updateFromServer();
			}
			
		});
	}
	
	public ArrayList<String> getSelectedUuids()  {
		return table.getSelectedUuids();
	}
	
	@Override
	public void setWait(boolean wait)  {
		this.btnOk.setEnabled(!wait);
		this.btnCancel.setEnabled(!wait);
		this.btnRefresh.setEnabled(!wait);
		super.setWait(wait);
	}

}
