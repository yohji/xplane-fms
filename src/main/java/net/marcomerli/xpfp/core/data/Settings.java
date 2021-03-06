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

package net.marcomerli.xpfp.core.data;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

import net.marcomerli.xpfp.core.Context;

/**
 * @author Marco Merli
 * @since 1.0
 */
public class Settings extends Data {

	private static final long serialVersionUID = 511605731858765879L;

	public static final String DIR_EXPORT = "directory.export";
	public static final String GEOAPI_KEY = "geoapi.google.key";
	public static final String PROXY_ACTIVE = "proxy.active";
	public static final String PROXY_HOSTNAME = "proxy.hostname";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_AUTH = "proxy.auth.active";
	public static final String PROXY_AUTH_USERNAME = "proxy.auth.username";
	public static final String PROXY_AUTH_PASSWORD = "proxy.auth.password";

	@Override
	protected File file()
	{
		return new File(Context.getHomedir(), "settings.properties");
	}

	@Override
	protected void init()
	{
		setProperty(DIR_EXPORT, SystemUtils.USER_HOME);
		setProperty(PROXY_ACTIVE, "false");
		setProperty(PROXY_AUTH, "false");
	}

	@Override
	protected void upgrade(String oldVersion)
	{}
}
