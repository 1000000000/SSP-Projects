package net.clonecomputers.louis.ssp;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

import java.util.Calendar;
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
	 * @return
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
	 * @return
	 */
	public static double getAltitudeHourAngle(double latitude, double declenation, double altitude) {
		latitude = toRadians(latitude);
		declenation = toRadians(declenation);
		altitude = toRadians(altitude);
		return AngleConverter.radiansToHours(Math.acos(  (sin(altitude) - sin(latitude)*sin(declenation))  /  (cos(latitude)*cos(declenation))  ));
	}
	
}
