package com.waitwha.nessus.trendanalyzer;

import javax.swing.event.EventListenerList;

import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerEvent;
import com.waitwha.nessus.trendanalyzer.event.BackgroundWorkerListener;

/**
 * <b>Nessus Trend Analyzer (Desktop)</b>: BackgroundWorker<br/>
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
 * Much like the BackgroundWorker in .Net, this class will execute a Runnable 
 * and allow the implementation to use eventing to listen for various events.
 *
 * @author Mike Duncan <mike.duncan@waitwha.com>
 * @version $Id$
 * @package com.waitwha.nessus.trendanalyzer
 */
public class BackgroundWorker {

	private static EventListenerList eventListenerList;
	
	/**
	 * Adds a BackgroundWorkerListener implementation to the list of 
	 * listeners.
	 * 
	 * @param l BackgroundWorkerListener
	 */
	public static final void addBackgroundWorkerListener(BackgroundWorkerListener l)  {
		if(eventListenerList == null)
			eventListenerList = new EventListenerList();
		
		eventListenerList.add(BackgroundWorkerListener.class, l);
	}
	
	/**
	 * Removes the given BackgroundWorkerListener from the list of
	 * listeners.
	 * 
	 * @param l BackgroundWorkerListener
	 */
	public static final void removeBackgroundWorkerListener(BackgroundWorkerListener l)  {
		eventListenerList.remove(BackgroundWorkerListener.class, l);
	}
	
	/**
	 * Executes the given Runnable.
	 * 
	 * @param r	Runnable
	 */
	public static final void execute(Runnable r)  {
		new Thread(r).start();
	}
	
	/**
	 * Notifies listeners that the Runnable has started.
	 * 
	 * @param e	BackgroundWorkerEvent
	 */
	public static final void threadStart(BackgroundWorkerEvent e)  {
		Object[] listeners = eventListenerList.getListenerList();
		for(int i = 0; i < listeners.length; i += 2)
			((BackgroundWorkerListener)listeners[(i + 1)]).started(e);
		
	}
	
	/**
	 * Notifies listeners that the Runnable has stopped.
	 * 
	 * @param e	BackgroundWorkerEvent
	 */
	public static final void threadStopped(BackgroundWorkerEvent e)  {
		Object[] listeners = eventListenerList.getListenerList();
		for(int i = 0; i < listeners.length; i += 2)
			((BackgroundWorkerListener)listeners[(i + 1)]).stopped(e);
		
	}
	
	/**
	 * Notifies listeners that the Runnable has progressed.
	 * 
	 * @param e	BackgroundWorkerEvent
	 */
	public static final void threadProgress(BackgroundWorkerEvent e)  {
		Object[] listeners = eventListenerList.getListenerList();
		for(int i = 0; i < listeners.length; i += 2)
			((BackgroundWorkerListener)listeners[(i + 1)]).progress(e);
		
	}
	
}
