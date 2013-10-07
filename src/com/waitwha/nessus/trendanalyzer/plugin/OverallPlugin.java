package com.waitwha.nessus.trendanalyzer.plugin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.waitwha.nessus.NessusClientData;
import com.waitwha.nessus.Report.ReportHost;
import com.waitwha.nessus.Report.ReportItem;
import com.waitwha.nessus.trendanalyzer.Configuration;
import com.waitwha.nessus.trendanalyzer.ConfigurationManager;
import com.waitwha.nessus.trendanalyzer.gui.PieChartPanel;
import com.waitwha.nessus.trendanalyzer.gui.TimeChartPanel;
import com.waitwha.nessus.trendanalyzer.i18n.I18n;
import com.waitwha.nessus.trendanalyzer.i18n.I18nManager;
import com.waitwha.util.Scoreboard;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: OverallPlugin<br/>
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
 * A plugin to display various stats from the Nessus scan including totals of 
 * hosts and vulns as well as plugins used.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.plugin
 */
public class OverallPlugin
	extends BasePlugin
	implements Plugin {

	private static final I18n i18n = I18nManager.getI18n(OverallPlugin.class);
	
	public OverallPlugin()  {
		super();
	}

	@Override
	public JPanel getContents(ArrayList<NessusClientData> data) {
		TimeZone tzone = TimeZone.getDefault();
		
		TimeSeries numberOfHosts = new TimeSeries(i18n.trans("number.of.hosts"));
		TimeSeries numberOfVulns = new TimeSeries(i18n.trans("number.of.vulns"));
		TimeSeries numberOfPlugins = new TimeSeries(i18n.trans("number.of.plugins"));
		
		Hashtable<String, Integer> hostScores = new Hashtable<String,Integer>();
		Scoreboard familyScores = new Scoreboard();
		
		//Sort the scan data (by end date)
		Collections.sort(data);
		for(NessusClientData s : data)  {
			numberOfHosts.add(RegularTimePeriod.createInstance(Minute.class, s.getReport().getEndDate(), tzone), s.getReport().getReportHosts().size());
			numberOfVulns.add(RegularTimePeriod.createInstance(Minute.class, s.getReport().getEndDate(), tzone), s.getReport().getTotalVulnerabilities());
			numberOfPlugins.add(RegularTimePeriod.createInstance(Minute.class, s.getReport().getEndDate(), tzone), s.getPolicy().getFamilySelection().size());
			
			for(ReportHost host : s.getReport().getReportHosts())  {
				hostScores.put(host.getName(), host.getOverallSeverity());
				
				for(ReportItem item : host.getReportItems())
					if(item.getSeverity() > 0) //skip the Info/None checks.
						familyScores.score(item.getPluginFamily());
				
			}
			
		}
		
		DefaultPieDataset dsHosts = new DefaultPieDataset();
		DefaultPieDataset dsFamilies = new DefaultPieDataset();
		ArrayList<Map.Entry<String, Integer>> scores = 
				new ArrayList<Map.Entry<String, Integer>>(hostScores.entrySet());
		ArrayList<Map.Entry<String, Integer>> families =
				new ArrayList<Map.Entry<String, Integer>>(familyScores.entrySet());
		Collections.sort(scores, new Comparator<Map.Entry<String, Integer>>()  {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
			
		});
		Collections.sort(families, new Comparator<Map.Entry<String, Integer>>()  {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
			
		});
		
		for(int i = 0; i < ((scores.size() >= 5) ? 5 : scores.size()); i++)
			dsHosts.setValue(scores.get(i).getKey(), scores.get(i).getValue());
		
		for(int i = 0; i < ((families.size() >= 5) ? 5 : families.size()); i++)
			dsFamilies.setValue(families.get(i).getKey(), families.get(i).getValue());
		
		JPanel top = new JPanel(new GridLayout(1,2,1,1));
		top.add(new PieChartPanel(i18n.trans("piechart.hosts.label"), dsHosts));
		top.add(new PieChartPanel(i18n.trans("piechart.families.label"), dsFamilies)); //TODO Need to do this!
		
		TimeSeriesCollection c = new TimeSeriesCollection();
		c.addSeries(numberOfHosts);
		c.addSeries(numberOfVulns);
		c.addSeries(numberOfPlugins);
		TimeChartPanel panel = 
				new TimeChartPanel(i18n.trans("timechart.title"), i18n.trans("timechart.xaxis.label"), i18n.trans("timechart.yaxis.label"), c);
		
		Configuration gui = ConfigurationManager.getInstance().getConfiguration("gui");
		if(gui.getProperty("overall.splitloc") == null)
			gui.setProperty("overall.splitloc", "150");
		
		int splitLoc = Integer.parseInt(gui.getProperty("overall.splitloc"));
		
		JSplitPane jsp = new JSplitPane();
		jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jsp.setDividerLocation(splitLoc);
		jsp.setDividerSize(3);
		jsp.setTopComponent(top);
		jsp.setBottomComponent(panel);
		
		JPanel r = new JPanel(new BorderLayout());
		r.add(jsp, BorderLayout.CENTER);
		return r;
	}

	@Override
	public String getName() {
		return i18n.trans("plugin.name");
	}

}
