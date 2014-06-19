package net.clonecomputers.louis.ssp;

import java.util.Calendar;
import java.util.TimeZone;

public class DateTimeConverter {

	public static void main(String[] args) {
		Calendar june19 = Calendar.getInstance();
		june19.clear();
		june19.set(2014, 5, 19);
		double lst = AngleConverter.degreeToHour(getLST(june19, 360 - AngleConverter.toDec(106, 54, 51.80)));
		System.out.println(lst - AngleConverter.toDec(22, 15, 37));
	}
	
	public static double getLST(Calendar time, double eastLongitude) {
		double j_0 = getJ_0(time);
		double hourOfDay = getDecimalHourOfDay(getUTC(time));
		double jCent = (j_0 - 2451545.0)/36525;
		double theta_0 = 100.46061837 + 36000.77053608*jCent + 3.87933e-4*Math.pow(jCent, 2) - Math.pow(jCent, 3)/3.871e7;
		double gmst = theta_0 + 360.985647366*hourOfDay/24;
		return (gmst + eastLongitude) % 360;
	}
	
	public static double getJ_0(Calendar time) {
		Calendar utc = getUTC(time);
		int year = utc.get(Calendar.YEAR);
		int month = utc.get(Calendar.MONTH) + 1; //January is 0 so add 1
		int day = utc.get(Calendar.DATE);
		return 367*year - 7*( year + (month + 9)/12 )/4 + 275*month/9 + day + 1721013.5; // Truncation automatically done by integer division
	}
	
	public static Calendar getUTC(Calendar time) {
		Calendar newTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		newTime.setTimeInMillis(time.getTimeInMillis());
		return newTime;
	}
	
	public static double getDecimalHourOfDay(Calendar time) {
		return time.get(Calendar.HOUR_OF_DAY) +
			   (1/60d)*(time.get(Calendar.MINUTE) +
			   (1/60d)*(time.get(Calendar.SECOND) +
			   0.001*time.get(Calendar.MILLISECOND)));
	}
	
	public static long decimalHourToMillis(double hour) {
		return Math.round(hour*3600000);
	}
}
