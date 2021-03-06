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

package net.marcomerli.xpfp.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.marcomerli.xpfp.core.Context;
import net.marcomerli.xpfp.core.data.Settings;
import net.marcomerli.xpfp.error.DataException;
import net.marcomerli.xpfp.fn.GeoFn;
import net.marcomerli.xpfp.fn.GuiFn;
import net.marcomerli.xpfp.gui.comp.EnableablePanel;
import net.marcomerli.xpfp.gui.comp.FormPanel;
import net.marcomerli.xpfp.gui.comp.TextInput;
import net.marcomerli.xpfp.gui.comp.ValidateFormAction;

/**
 * @author Marco Merli
 * @since 1.0
 */
public class SettingsWindow extends Window {

	private static final long serialVersionUID = 5454273820569518074L;

	private JTextField fmsDirText;
	private JButton fmsDirBtn;
	private JFileChooser fmsDirFileChooser;
	private TextInput geoApiText;
	private EnableablePanel proxyForm;
	private JTextField proxyHostnameText;
	private JTextField proxyPortText;
	private EnableablePanel authForm;
	private JTextField proxyAuthUsername;
	private JPasswordField proxyAuthPassword;

	public SettingsWindow() {

		super(TITLE_COMPACT + " :: Settings");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fmsDirFileChooser = new JFileChooser();
		fmsDirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Main
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		mainPane.setBorder(PADDING_BORDER);

		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPane.add(fmsDirPanel());
		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPane.add(geoApiKey());
		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPane.add(proxyPanel());
		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));

		// Save
		JPanel savePanel = new JPanel();
		JButton save = new JButton("Save");
		save.addActionListener(new OnSave(geoApiText));
		savePanel.add(save);

		mainPane.add(savePanel);
		mainPane.add(Box.createGlue());

		setContentPane(mainPane);
		pack();

		setResizable(false);
		setLocationByPlatform(true);
		setVisible(true);
	}

	private JPanel fmsDirPanel()
	{
		FormPanel form = new FormPanel();
		form.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Export Directory"),
			BorderFactory.createEmptyBorder()));

		fmsDirText = new JTextField();
		fmsDirText.setEnabled(false);
		fmsDirText.setText(Context.getSettings()
			.getProperty(Settings.DIR_EXPORT, File.class).getAbsolutePath());

		fmsDirBtn = new JButton("Choose");
		fmsDirBtn.addActionListener(new OnChooseDir());

		form.addLast(fmsDirText);
		form.addSpace(50);
		form.addLast(fmsDirBtn, .25);

		return form;
	}

	private JPanel geoApiKey()
	{
		FormPanel form = new FormPanel();
		form.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Google API"),
			BorderFactory.createEmptyBorder()));

		geoApiText = new TextInput(25);
		geoApiText.setText(Context.getSettings().getProperty(Settings.GEOAPI_KEY));
		form.addLast(geoApiText);

		return form;
	}

	private JPanel proxyPanel()
	{
		Settings settings = Context.getSettings();
		proxyForm = new EnableablePanel("Proxy");

		proxyHostnameText = new JTextField();
		proxyHostnameText.setText(settings.getProperty(Settings.PROXY_HOSTNAME));
		proxyForm.addLabel("Hostname", .25).setLabelFor(proxyHostnameText);
		proxyForm.addLast(proxyHostnameText, 1);

		proxyPortText = new JTextField();
		proxyPortText.setText(settings.getProperty(Settings.PROXY_PORT));
		proxyForm.addLabel("Port", .25).setLabelFor(proxyPortText);
		proxyForm.addLast(proxyPortText, 1);

		proxyForm.setEnabled(settings.getProperty(Settings.PROXY_ACTIVE, Boolean.class));
		authForm = new EnableablePanel("Authentication");

		proxyAuthUsername = new JTextField();
		proxyAuthUsername.setText(settings.getProperty(Settings.PROXY_AUTH_USERNAME));
		authForm.addLabel("Username", .25).setLabelFor(proxyAuthUsername);
		authForm.addLast(proxyAuthUsername, 1);

		proxyAuthPassword = new JPasswordField();
		proxyAuthPassword.setText(settings.getProperty(Settings.PROXY_AUTH_PASSWORD));
		authForm.addLabel("Password", .25).setLabelFor(proxyAuthPassword);
		authForm.addLast(proxyAuthPassword, 1);

		authForm.setEnabled(settings.getProperty(Settings.PROXY_AUTH, Boolean.class));
		proxyForm.addLast(authForm);

		return proxyForm;
	}

	private class OnChooseDir implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int returnVal = fmsDirFileChooser.showOpenDialog(fmsDirBtn);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File fpl = fmsDirFileChooser.getSelectedFile();
				fmsDirText.setText(fpl.getAbsolutePath());
			}
		}
	}

	private class OnSave extends ValidateFormAction {

		public OnSave(JComponent... fields) {

			super(fields);
		}

		@Override
		public void perform(ActionEvent e)
		{
			try {
				Settings settings = Context.getSettings();
				settings.setProperty(Settings.DIR_EXPORT, fmsDirText.getText());
				settings.setProperty(Settings.GEOAPI_KEY, geoApiText.getText());
				settings.setProperty(Settings.PROXY_ACTIVE, String.valueOf(proxyForm.isEnabled()));
				settings.setProperty(Settings.PROXY_HOSTNAME, proxyHostnameText.getText());
				settings.setProperty(Settings.PROXY_PORT, proxyPortText.getText());
				settings.setProperty(Settings.PROXY_AUTH, String.valueOf(authForm.isEnabled()));
				settings.setProperty(Settings.PROXY_AUTH_USERNAME, proxyAuthUsername.getText());
				settings.setProperty(Settings.PROXY_AUTH_PASSWORD, new String(proxyAuthPassword.getPassword()));

				settings.save();
				Context.refresh();
				GeoFn.init();
			}
			catch (DataException ee) {
				GuiFn.errorDialog(ee, SettingsWindow.this);
			}
			catch (Exception ee) {
				logger.error("onSave", ee);
				GuiFn.errorDialog(ee, SettingsWindow.this);
			}
			finally {
				dispose();
			}
		}
	}
}
