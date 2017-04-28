package net.marcomerli.xpfp.fn;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.junit.BeforeClass;
import org.junit.Test;

import net.marcomerli.xpfp.UnitTestSupport;
import net.marcomerli.xpfp.model.Location;

public class GeoFnTest extends UnitTestSupport {

	@BeforeClass
	public static void before() throws Exception
	{
		GeoFn.context.setApiKey("AIzaSyC96Ww5A-wNmGFLfVpbr61eLr_JZv9cjuQ");
		GeoFn.context.setProxy(new Proxy(Type.HTTP, new InetSocketAddress(
			"127.0.0.1", 3128)));
	}

	@Test
	public void distance()
	{
		Location a = new Location(45.086389, 7.601944);
		Location b = new Location(45.0601, 7.3886);
		double dist = GeoFn.distance(a, b);

		assertNumberNotZero(dist);
		assertNumberBetween(dist, 16999, 17010);
	}

	@Test
	public void point()
	{
		Location start = new Location(45.086389, 7.601944);
		Location expected = new Location(45.0601, 7.3886);
		Location actual = GeoFn.point(start, 17, 260);

		assertNotNull(actual);
		assertNumberBetween(GeoFn.distance(expected, actual), 0, 100);
	}

	@Test
	public void elevation() throws Exception
	{
		Location loc = new Location(45.086389, 7.601944);
		GeoFn.elevation(loc);

		assertNumberNotZero(loc.alt);
		assertNumberBetween(loc.alt, 282, 284);
	}

	@Test
	public void elevations() throws Exception
	{
		double[] elevs = GeoFn.elevations(
			new Location(45.0146, 5.5828),
			new Location(44.5347, 6.1221));

		assertNotNull(elevs);
		assertNotEquals(0, elevs.length);

		for (int i = 0; i < elevs.length; i++)
			assertNumberNotZero(elevs[i]);
	}

	@Test
	public void declination()
	{
		Location loc = new Location(45.0, 7.5);
		double decl = GeoFn.declination(loc);

		assertNumberNotZero(decl);
		assertNumberBetween(decl, 2, 3);
	}
}
