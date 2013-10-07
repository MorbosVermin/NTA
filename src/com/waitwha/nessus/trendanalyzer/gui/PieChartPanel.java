package com.waitwha.nessus.trendanalyzer.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: PieChartPanel<br/>
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
 * Reusable PieChart panel.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer.gui
 */
public class PieChartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public PieChartPanel(String title, PieDataset dataset)  {
		super(new BorderLayout());
		
		JFreeChart chart = ChartFactory.createPieChart(
        title,
        dataset,
        true,
        true,
        false
    );

    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setSectionOutlinesVisible(true);
    plot.setNoDataMessage("No data available");
    
    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    this.add(panel, BorderLayout.CENTER);
    this.validate();
	}

}
