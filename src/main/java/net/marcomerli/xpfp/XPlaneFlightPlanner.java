/**
 *   Copyright (c) 2017 Marco Merli <yohji@marcomerli.net>
 *   This file is part of XPFP.
 *
 *   XPFP is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   XPFP is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with XPFP.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.marcomerli.xpfp;

import java.awt.Toolkit;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.marcomerli.xpfp.core.Context;
import net.marcomerli.xpfp.core.data.Preferences;
import net.marcomerli.xpfp.core.data.Settings;
import net.marcomerli.xpfp.error.DataException;
import net.marcomerli.xpfp.fn.GeoFn;
import net.marcomerli.xpfp.fn.GuiFn;
import net.marcomerli.xpfp.gui.MainWindow;
import net.marcomerli.xpfp.gui.Window;

/**
 * @author Marco Merli
 * @since 1.0
 */
public class XPlaneFlightPlanner {

	protected static final Logger logger = LoggerFactory.getLogger(XPlaneFlightPlanner.class);

	private static JFrame mainWindow;

	public static void main(String[] args) throws Exception
	{
		try {
			Context.init();

			Settings settings = new Settings();
			settings.load();
			Context.setSettings(settings);

			GeoFn.init();

			Preferences prefs = new Preferences();
			prefs.load();
			Context.setPreferences(prefs);

			if (SystemUtils.IS_OS_LINUX) {
				Toolkit xToolkit = Toolkit.getDefaultToolkit();
				Field awtAppClassNameField = xToolkit.getClass().getDeclaredField("awtAppClassName");
				awtAppClassNameField.setAccessible(true);
				awtAppClassNameField.set(xToolkit, Window.TITLE_FULL);
			}

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run()
				{
					mainWindow = new MainWindow();
				}
			});
		}
		catch (DataException e) {
			GuiFn.errorDialog(e, getMainWindow());
		}
		catch (Exception e) {
			logger.error("main", e);
			GuiFn.fatalDialog(e, getMainWindow());
		}
	}

	public static MainWindow getMainWindow()
	{
		if (mainWindow == null)
			mainWindow = new JFrame(MainWindow.TITLE_FULL);

		return (MainWindow) mainWindow;
	}
}
