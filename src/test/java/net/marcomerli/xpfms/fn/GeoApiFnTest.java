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

package net.marcomerli.xpfms.fn;

import org.junit.Test;

import net.marcomerli.xpfms.UnitTestSupport;
import net.marcomerli.xpfms.model.Location;

public class GeoApiFnTest extends UnitTestSupport {

	@Test
	public void distance()
	{
		Location a = new Location(45.0, 7.5);
		Location b = new Location(45.0, 8.5);
		double dist = GeoApiFn.distanceOf(a, b);

		assertNumberNotZero(dist);
		assertNumberBetween(dist, 78000, 79000);
	}
}
