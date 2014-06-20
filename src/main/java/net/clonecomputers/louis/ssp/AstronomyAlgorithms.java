package net.clonecomputers.louis.ssp;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AstronomyAlgorithms {

	public static void main(String[] args) {
		//Defining Stuff
		double latitude = AngleConverter.toDecimal(34, 04, 21.46);
		double longitude = 360 - AngleConverter.toDecimal(106, 54, 51.80); // in degrees east of prime meridian
		double ra = AngleConverter.toDecimal(22, 15, 37);
		double declenation = AngleConverter.toDecimal(10, 13, 49);
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
		
		//Problem 5
		double hourAngle = getHourAngle(june19, longitude, ra);
		transit.setTimeInMillis(june19.getTimeInMillis() - DateTimeConverter.decimalHourToMillis(hourAngle));
		long horizonHourAngleMillis = DateTimeConverter.decimalHourToMillis(getAltitudeHourAngle(latitude, declenation, 0));
		rise.setTimeInMillis(transit.getTimeInMillis() - horizonHourAngleMillis);
		set.setTimeInMillis(transit.getTimeInMillis() + horizonHourAngleMillis);
		System.out.println("Rising: " + rise.getTime());
		System.out.println("Transitting: " + transit.getTime());
		System.out.println("Setting: " + set.getTime());
		
		//Problem 6
		double soupHourAngle = getAltitudeHourAngle(latitude, declenation, 30);
		System.out.println("Time out of soup: " + 2*soupHourAngle);
		
		//Problem 7
		double minDiff = 25;
		Date best = null;
		while(testTime.get(Calendar.YEAR) == 2014) {
			double diff = Math.abs(AngleConverter.degreesToHours(DateTimeConverter.getLST(testTime, longitude)) - ra);
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
		System.out.println("Second Observatory Longitude: " + DateTimeConverter.getLongitude(secondObservatoryTransit, AngleConverter.hoursToDegrees(ra)));
		thirdObservatoryTransit.setTimeInMillis(secondObservatoryTransit.getTimeInMillis() + DateTimeConverter.decimalHourToMillis(2*soupHourAngle));
		System.out.println("Third Observatory Longitude: " + DateTimeConverter.getLongitude(thirdObservatoryTransit, AngleConverter.hoursToDegrees(ra)));
		fourthObservatoryTransit.setTimeInMillis(thirdObservatoryTransit.getTimeInMillis() + DateTimeConverter.decimalHourToMillis(2*soupHourAngle));
		System.out.println("Unnecessary Fourth Observatory Longitude: " + DateTimeConverter.getLongitude(fourthObservatoryTransit, AngleConverter.hoursToDegrees(ra)));
	}
	
	/**
	 * Calculates hour angle of an object at a time given the longitude of the observation site
	 * and the R.A. of the object
	 * @param time the time of observation (this does not need to be the local time as it is converted by its time zone to UTC)
	 * @param eastLongitude the longitude of the observation site in decimal degrees east of the prime meridian
	 * @param ra the R.A. of the body in decimal hours
	 * @return the hour angle of the object in decimal hours
	 */
	public static double getHourAngle(Calendar time, double eastLongitude, double ra) {
		return AngleConverter.degreesToHours(DateTimeConverter.getLST(time, eastLongitude)) - ra;
	}
	
	/**
	 * Calculates the altitude of an object given the latitude of the observation site,
	 * the declenation of the object site and the hour angle of the object
	 * @param latitude the latitude of the observation site in decimal degrees above the equator
	 * @param declenation the declenation of the object in decimal degrees
	 * @param hourAngle the hour angle of the object in decimal hours
	 * @return the altitude of the body in decimal degrees
	 */
	public static double getAltitude(double latitude, double declenation, double hourAngle) {
		latitude = toRadians(latitude);
		declenation = toRadians(declenation);
		hourAngle = AngleConverter.hoursToRadians(hourAngle);
		return Math.toDegrees(Math.asin(sin(latitude)*sin(declenation) + cos(latitude)*cos(declenation)*cos(hourAngle)));
	}
	
	/**
	 * Calculates the positive hour angle that an object will have when it is at a certain altitude
	 * when observed from a particular latitude
	 * @param latitude the latitude of the observation site in decimal degrees above the equator
	 * @param declenation the declenation of the object in decimal degrees
	 * @param altitude the altitude of the object in decimal degrees
	 * @return the hour angle of the body in decimal hours
	 */
	public static double getAltitudeHourAngle(double latitude, double declenation, double altitude) {
		latitude = toRadians(latitude);
		declenation = toRadians(declenation);
		altitude = toRadians(altitude);
		return AngleConverter.radiansToHours(Math.acos(  (sin(altitude) - sin(latitude)*sin(declenation))  /  (cos(latitude)*cos(declenation))  ));
	}
	
}
