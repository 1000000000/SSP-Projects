package net.clonecomputers.louis.ssp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AstronomyAlgorithms {

	public static void main(String[] args) {
		//Defining Stuff
		double latitude = AngleConverter.toDecimal(34, 04, 21.46);
		double longitude = 360 - AngleConverter.toDecimal(106, 54, 51.80); // in degrees east of prime meridian
		EquatorialLocation mbx = new EquatorialLocation(AngleConverter.toDecimal(22, 15, 37), AngleConverter.toDecimal(10, 13, 49));
		Calendar june19 = Calendar.getInstance(TimeZone.getTimeZone("GMT-6"));
		june19.clear();
		june19.set(2014, 5, 19, 12, 0);
		Calendar transit = Calendar.getInstance();
		Calendar rise = Calendar.getInstance();
		Calendar set = Calendar.getInstance();
		Calendar secondObservatoryTransit = Calendar.getInstance();
		Calendar thirdObservatoryTransit = Calendar.getInstance();
		Calendar fourthObservatoryTransit = Calendar.getInstance();
		Calendar testTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		testTime.clear();
		testTime.set(2014, 0, 1, 6, 0);
		
		//
		// Astro 1
		//Problem 5
		double hourAngle = mbx.getHourAngle(june19, longitude);
		transit.setTimeInMillis(june19.getTimeInMillis() - DateTimeConverter.decimalHourToMillis(hourAngle));
		long horizonHourAngleMillis = DateTimeConverter.decimalHourToMillis(mbx.getAltitudeHourAngle(latitude, 0));
		rise.setTimeInMillis(transit.getTimeInMillis() - horizonHourAngleMillis);
		set.setTimeInMillis(transit.getTimeInMillis() + horizonHourAngleMillis);
		System.out.println("Rising: " + rise.getTime());
		System.out.println("Transitting: " + transit.getTime());
		System.out.println("Setting: " + set.getTime());
		
		//Problem 6
		double soupHourAngle = mbx.getAltitudeHourAngle(latitude, 30);
		System.out.println("Time out of soup: " + 2*soupHourAngle);
		
		//Problem 7
		double minDiff = 25;
		Date best = null;
		while(testTime.get(Calendar.YEAR) == 2014) {
			double diff = Math.abs(AngleConverter.degreesToHours(DateTimeConverter.getLST(testTime, longitude)) - mbx.getRA());
			if(diff < minDiff) {
				minDiff = diff;
				best = testTime.getTime();
			}
			testTime.add(Calendar.DAY_OF_YEAR, 1);
		}
		DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println("MbX transits at 6UT around " + format.format(best) + " (diff: " + minDiff + " hours)");
		
		//Problem 9
		secondObservatoryTransit.setTimeInMillis(transit.getTimeInMillis() + DateTimeConverter.decimalHourToMillis(2*soupHourAngle));
		System.out.println("Second Observatory Longitude: " + DateTimeConverter.getLongitude(secondObservatoryTransit, AngleConverter.hoursToDegrees(mbx.getRA())));
		thirdObservatoryTransit.setTimeInMillis(secondObservatoryTransit.getTimeInMillis() + DateTimeConverter.decimalHourToMillis(2*soupHourAngle));
		System.out.println("Third Observatory Longitude: " + DateTimeConverter.getLongitude(thirdObservatoryTransit, AngleConverter.hoursToDegrees(mbx.getRA())));
		fourthObservatoryTransit.setTimeInMillis(thirdObservatoryTransit.getTimeInMillis() + DateTimeConverter.decimalHourToMillis(2*soupHourAngle));
		System.out.println("Unnecessary Fourth Observatory Longitude: " + DateTimeConverter.getLongitude(fourthObservatoryTransit, AngleConverter.hoursToDegrees(mbx.getRA())));
		
		//
		// Astro 2
		System.out.println();
		System.out.println();
		System.out.println("====================Astro 2====================");
		System.out.println();
		//Problem 7
		mbx.changeEpoch(2014.5);
		System.out.println("MbX (equinox 2014.5): " + mbx);
		
		//Problem 9
		System.out.println("Ecliptic coords: (" + mbx.getEclipticLongitude() + ", " + mbx.getEclipticLatitude() + ")");
		
		//Problem 7
		mbx.accountForProperMotion(0.425, -1.004);
		System.out.println("MbX (epoch 2014.5): " + mbx);
		
		//Problem 9
		System.out.println("Ecliptic coords: (" + mbx.getEclipticLongitude() + ", " + mbx.getEclipticLatitude() + ")");
		
		//
		// Astro 5
		System.out.println();
		System.out.println();
		System.out.println("====================Astro 5====================");
		System.out.println();
		//Problem 2e
		Calendar timeOfObs = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		timeOfObs.clear();
		timeOfObs.set(2005, 2, 4, 2, 50);
		System.out.println("GMST at parallax observation: " + DateTimeConverter.getGMST(timeOfObs));
	}
	
}
