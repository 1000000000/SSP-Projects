package net.clonecomputers.louis.ssp;

import java.util.Calendar;
import java.util.TimeZone;

public class DateTimeConverter {

	public static void main(String[] args) {
		Calendar june19 = Calendar.getInstance();
		june19.clear();
		june19.set(2014, 5, 19);
		double lst = AngleConverter.degreesToHours(getLST(june19, 360 - AngleConverter.toDecimal(106, 54, 51.80)));
		System.out.println(lst - AngleConverter.toDecimal(22, 15, 37));
	}
	
	/**
	 * Calculates the LST from a time and the longitude of the location
	 * @param time a time equivalent to local time
	 * @param eastLongitude the longitude of the observation site in decimal degrees east of the prime meridian
	 * @return the LST in decimal degrees
	 */
	public static double getLST(Calendar time, double eastLongitude) {
		return (getGMST(time) + eastLongitude) % 360;
	}
	
	/**
	 * Gets the longitude of a location from its lst and an equivalent civil time
	 * @param time a civil time equivalent to the lst
	 * @param lst lst in decimal degrees
	 * @return the longitude of the location in decimal degrees east of the prime meridian
	 */
	public static double getLongitude(Calendar time, double lst) {
		return lst - getGMST(time);
	}
	
	/**
	 * Converts a time to GMST
	 * @param time a time
	 * @return the GMST in decimal degrees
	 */
	public static double getGMST(Calendar time) {
		double j_0 = getJ_0(time);
		double hourOfDay = getDecimalHourOfDay(getUTC(time));
		double jCent = (j_0 - 2451545.0)/36525;
		double theta_0 = 100.46061837 + 36000.77053608*jCent + 3.87933e-4*Math.pow(jCent, 2) - Math.pow(jCent, 3)/3.871e7;
		return (theta_0 + 360.985647366*hourOfDay/24) % 360;
		
	}
	
	/**
	 * Calculates J<sub>0</sub> from the time
	 * @param time a time sometime between 1901 and 2099
	 * @return J<sub>0</sub>
	 */
	public static double getJ_0(Calendar time) {
		Calendar utc = getUTC(time);
		int year = utc.get(Calendar.YEAR);
		int month = utc.get(Calendar.MONTH) + 1; //January is 0 so add 1
		int day = utc.get(Calendar.DATE);
		
		// Truncation automatically done by integer division
		return 367*year - 7*( year + (month + 9)/12 )/4 + 275*month/9 + day + 1721013.5;
	}
	
	/**
	 * Returns the UTC time given a time
	 * @param time a time
	 * @return UTC time
	 */
	public static Calendar getUTC(Calendar time) {
		Calendar newTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		newTime.setTimeInMillis(time.getTimeInMillis());
		return newTime;
	}
	
	/**
	 * Calculates the number of decimal hours since the start of the day
	 * @param time the time
	 * @return the decimal hours of the day
	 */
	public static double getDecimalHourOfDay(Calendar time) {
		return time.get(Calendar.HOUR_OF_DAY) +
			   (1/60d)*(time.get(Calendar.MINUTE) +
			   (1/60d)*(time.get(Calendar.SECOND) +
			   0.001*time.get(Calendar.MILLISECOND)));
	}
	
	/**
	 * Converts decimal hours to milliseconds
	 * @param hour decimal hours
	 * @return milliseconds
	 */
	public static long decimalHourToMillis(double hour) {
		return Math.round(hour*3600000);
	}
}
