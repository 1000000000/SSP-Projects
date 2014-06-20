package net.clonecomputers.louis.ssp;

public class AngleConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * This is for converting values of the form x minutes seconds into decimals
	 * (usually for degrees-minutes-seconds to decimal degrees or hours-minutes-seconds to decimal hours)
	 * @param n The ones place of the number (usually hours or degrees)
	 * @param min the number of minutes
	 * @param sec the number of seconds
	 * @return the inputted value in decimal form
	 */
	public static double toDecimal(int n, int min, double sec) {
		double signum = Math.signum(n);
		return signum*(Math.abs(n) + (min + sec/60)/60);
	}
	
	/**
	 * Converts decimal degrees to decimal hours
	 * @param degrees a decimal value in degrees
	 * @return an equivalent decimal value in hours
	 */
	public static double degreesToHours(double degrees) {
		return degrees/15;
	}
	
	/**
	 * Converts decimal hours to decimal degrees
	 * @param hours a decimal value in hours
	 * @return an equivalent decimal value in degrees
	 */
	public static double hoursToDegrees(double hours) {
		return 15*hours;
	}
	
	/**
	 * Converts radians to decimal hours
	 * @param radians a value in radians
	 * @return an equivalent decimal value in hours
	 */
	public static double radiansToHours(double radians) {
		return 12*radians/Math.PI;
	}
	
	/**
	 * Converts decimal hours to radians
	 * @param hours a value in decimal hours
	 * @return an equivalent value in radians
	 */
	public static double hoursToRadians(double hours) {
		return Math.PI*hours/12;
	}

}
