/**
 *  Copyright (c) 2017 Marco Merli <yohji@marcomerli.net>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.marcomerli.xpfp.fn;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * @author Marco Merli
 * @since 1.0
 */
public class NumberFn {

	private static final Map<Integer, DecimalFormat> formatters = new HashMap<>();

	public static String format(double number, int precision)
	{
		DecimalFormat df = formatters.get(precision);
		if (df == null)
			formatters.put(precision,
				(df = new DecimalFormat("#." + StringUtils.repeat('0', precision),
					new DecimalFormatSymbols(Locale.US))));

		return df.format(number);
	}

	public static String time(long time)
	{
		return DurationFormatUtils.formatDurationHMS(time);
	}

	private NumberFn() {}
}
