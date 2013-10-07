package com.waitwha.nessus.trendanalyzer.plugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.waitwha.nessus.NessusClientData;
import com.waitwha.nessus.Report.ReportHost;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: HostDiffPlugin<br/>
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
 * @package com.waitwha.nessus.trendanalyzer.plugin
 */
public class HostDiffPlugin extends BasePlugin {

	private static final I18n i18n = I18nManager.getI18n(HostDiffPlugin.class);
	
	/**
	 * All newly added HostDiffTableRow's should be set to Added status. Upon future 
	 * NessusClientData objects given, the status of the Added should initially be 
	 * changed to Unchanged, but if found again, they should be updated to Improved
	 * or Degraded depending on the overall severity of the ReportHost. However, at 
	 * the end of the scan analysis, all Unchanged status should be updated to 
	 * NotScanned before going through and again setting all Added to Unchanged again.
	 */
	public enum HostDiffStatus  {
		
		Unchanged,
		Added,
		Improved,
		StayedSame,
		Degraded,
		NotScanned;
		
		/**
		 * Returns the background Color to be used to represent this 
		 * row's status.
		 * 
		 * @return	Color
		 */
		public Color getColor()  {
			switch(this)  {
				
				case Improved:
					return Color.green;
					
				case Degraded:
					return Color.red;
					
				case NotScanned:
					return Color.lightGray;
				
				default:
					return Color.white;
					
			}
			
		}
		
		@Override
		public String toString()  {
			return i18n.trans(super.toString().toUpperCase());
		}
		
	}
	
	/**
	 * Represents a row within the table.
	 * 
	 */
	private class HostDiffTableRow  {
		
		private Date scanned;
		private String name;
		private String address;
		private String os;
		private int severity;
		private int oldSeverity;
		private HostDiffStatus status;
		
		/**
		 * Constructor initialy contructed with the Added status.
		 *
		 * @param host ReportHost
		 */
		public HostDiffTableRow(ReportHost host)  {
			this.scanned = host.getEndDate();
			this.name = host.getName();
			this.address = host.getAddress();
			this.os = host.getOS();
			this.severity = host.getOverallSeverity();
			this.oldSeverity = 0;
			this.status = HostDiffStatus.Added;
		}

		/**
		 * @return the oldSeverity
		 */
		public int getOldSeverity() {
			return oldSeverity;
		}

		/**
		 * @return the scanned
		 */
		public Date getScanned() {
			return scanned;
		}

		/**
		 * @param scanned the scanned to set
		 */
		public void setScanned(Date scanned) {
			this.scanned = scanned;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the address
		 */
		public String getAddress() {
			return address;
		}

		/**
		 * @return the os
		 */
		public String getOs() {
			return os;
		}

		/**
		 * @return the severity
		 */
		public int getSeverity() {
			return severity;
		}

		/**
		 * @param severity the severity to set
		 */
		public void setSeverity(int severity) {
			if(this.oldSeverity == 0)
				this.oldSeverity = this.severity;
			
			this.severity = severity;
			if(this.severity > this.oldSeverity)
				this.status = HostDiffStatus.Degraded;
			else if(this.severity < this.oldSeverity)
				this.status = HostDiffStatus.Improved;
			else
				this.status = HostDiffStatus.StayedSame;
			
		}

		/**
		 * @return the status
		 */
		public HostDiffStatus getStatus() {
			return status;
		}
		
		public void setStatus(HostDiffStatus status)  {
			this.status = status;
		}
		
	}

	/**
	 * Used to map ReportHost to a row within the table.
	 * 
	 */
	private class Model extends ArrayList<HostDiffTableRow> implements TableModel  {

		private static final long serialVersionUID = 1L;
		private EventListenerList eventListenerList;

		public Model()  {
			super();
			this.eventListenerList = new EventListenerList();
		}
		
		public HostDiffTableRow get(String name)  {
			for(HostDiffTableRow row : this)
				if(row.getName().equals(name))
					return row;
			
			return null;
		}
		
		/**
		 * IF you send a String here, we will search for a ReportHost based
		 * on a name you have given. Otherwise, the default functionality 
		 * from ArrayList.contains() is followed.
		 * 
		 * @see java.util.ArrayList#contains(java.lang.Object)
		 */
		@Override
		public boolean contains(Object o) {
			if(!(o instanceof String))
				return super.contains(o);
			
			boolean found = false;
			for(HostDiffTableRow row : this)  {
				if(row.getName().equals((String)o))  {
					found = true;
					break;
					
				}
			}
			
			return found;
		}

		/**
		 * @see java.util.ArrayList#add(java.lang.Object)
		 */
		@Override
		public boolean add(HostDiffTableRow e) {
			int size = size();
			boolean ok = super.add(e);
			if(ok)  {
				int nSize = size();
				Object[] listeners = eventListenerList.getListenerList();
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
			Object[] listeners = eventListenerList.getListenerList();
			for(int i = 0; i < listeners.length; i += 2)
				((TableModelListener)listeners[(i + 1)]).tableChanged(new TableModelEvent(this));
			
		}

		@Override
		public int getRowCount() {
			return size();
		}

		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return i18n.trans(String.format("hostdiff.column%d.label", columnIndex));
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch(columnIndex)  {
				case 4:
				case 5:
					return Integer.class;
				
				default:
					return String.class;
				
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 0);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			HostDiffTableRow row = this.get(rowIndex);
			switch(columnIndex)  {
				case 1:
					return row.getName();
					
				case 2:
					return row.getAddress();
					
				case 3:
					return row.getOs();
					
				case 4:
					return row.getOldSeverity();
					
				case 5:
					return row.getSeverity();
					
				case 6:
					return row.getStatus();
					
				default:
					return row.getScanned().toString();
			
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			return;
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
	
	/**
	 * Used to change the behavior/color of the cells within the table.
	 * 
	 */
	private class CellRenderer extends JLabel implements TableCellRenderer  {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(
				JTable table, 
				Object value,
				boolean isSelected, 
				boolean hasFocus, 
				int row, 
				int column) {
			
			this.setText(value.toString());
			switch(column)  {
				case 6:
					this.setBackground(((HostDiffStatus)value).getColor());
					break;
					
				default:
					
			}
			
			return this;
		}
		
	}
	
	/**
	 * Custom JTable implementation.
	 * 
	 */
	private class Table extends JTable  {
		
		private static final long serialVersionUID = 1L;

		public Table(ArrayList<NessusClientData> data)  {
			super(new Model());
			
			this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			this.setAutoCreateColumnsFromModel(true);
			//this.setAutoCreateRowSorter(true); //This always causes issues for me :/
			this.setBackground(Color.white);
			this.setColumnSelectionAllowed(true);
			this.setAutoscrolls(true);
			this.setDefaultRenderer(String.class, new CellRenderer());
			
			Model model = (Model)this.getModel();
			boolean first = true;
			Collections.sort(data);
			for(NessusClientData d : data)  {
				if(! first)  {
					for(HostDiffTableRow row : model)  {
						if(row.getStatus() == HostDiffStatus.Added)
							row.setStatus(HostDiffStatus.Unchanged);
						
					}
				}
				
				for(ReportHost host : d.getReport().getReportHosts())  {
					if(model.contains(host.getName()))  {
						
						HostDiffTableRow row = model.get(host.getName());
						row.setSeverity(host.getOverallSeverity());
						row.setScanned(host.getEndDate());
						
					}else
						model.add(new HostDiffTableRow(host));
					
				}
				
				for(HostDiffTableRow row : model)  {
					if(row.getStatus() == HostDiffStatus.Unchanged)
						row.setStatus(HostDiffStatus.NotScanned);
					
				}
				
				if(first)
					first = false;
				
			}
			
			for(HostDiffTableRow row : model)  {
				if(row.getStatus() == HostDiffStatus.Added)
					row.setStatus(HostDiffStatus.NotScanned);
				
			}
			
		}
		
		
	}
	
	/**
	 * @see com.waitwha.nessus.trendanalyzer.plugin.BasePlugin#getName()
	 */
	@Override
	public String getName() {
		return i18n.trans("plugin.name");
	}

	/**
	 * @see com.waitwha.nessus.trendanalyzer.plugin.BasePlugin#getContents(java.util.ArrayList)
	 */
	@Override
	public JPanel getContents(ArrayList<NessusClientData> data) {
		JPanel panel = new JPanel(new BorderLayout());
		
		Table hostDiffTable = new Table(data);
		JScrollPane jsp = new JScrollPane(hostDiffTable, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.getViewport().setBackground(Color.white);
		panel.add(jsp, BorderLayout.CENTER);
		
		return panel;
	}

}
