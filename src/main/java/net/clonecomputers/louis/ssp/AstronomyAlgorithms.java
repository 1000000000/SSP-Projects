package net.clonecomputers.louis.ssp;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

import java.util.Calendar;

public class AstronomyAlgorithms {

	public static void main(String[] args) {
		double latitude = AngleConverter.toDec(34, 04, 21.46);
		double longitude = 360 - AngleConverter.toDec(106, 54, 51.80);
		double ra = AngleConverter.toDec(22, 15, 37);
		double declenation = AngleConverter.toDec(10, 13, 49);
		Calendar june19 = Calendar.getInstance();
		june19.clear();
		june19.set(2014, 5, 19, 12, 0);
		double hourAngle = getHourAngle(june19, longitude, ra);
		System.out.println("Hour Angle: " + hourAngle);
		Calendar transit = Calendar.getInstance();
		Calendar rise = Calendar.getInstance();
		Calendar set = Calendar.getInstance();
		transit.setTimeInMillis(june19.getTimeInMillis() - DateTimeConverter.decimalHourToMillis(hourAngle));
		long horizonHourAngleMillis = DateTimeConverter.decimalHourToMillis(getAltitudeHourAngle(latitude, declenation, 0));
		rise.setTimeInMillis(transit.getTimeInMillis() - horizonHourAngleMillis);
		set.setTimeInMillis(transit.getTimeInMillis() + horizonHourAngleMillis);
		double soupHourAngle = getAltitudeHourAngle(latitude, declenation, 30);
		System.out.println("Rising: " + rise.getTime());
		System.out.println("Transitting: " + transit.getTime());
		System.out.println("Setting: " + set.getTime());
		System.out.println("Time out of soup: " + 2*soupHourAngle);
	}
	
	public static double getHourAngle(Calendar time, double eastLongitude, double ra) {
		return AngleConverter.degreeToHour(DateTimeConverter.getLST(time, eastLongitude)) - ra;
	}
	
	public static double getAltitude(double latitude, double declenation, double hourAngle) {
		latitude = toRadians(latitude);
		declenation = toRadians(declenation);
		hourAngle = toRadians(hourAngle);
		return Math.toDegrees(Math.asin(sin(latitude)*sin(declenation) + cos(latitude)*cos(declenation)*cos(hourAngle)));
	}
	
	public static double getAltitudeHourAngle(double latitude, double declenation, double altitude) {
		latitude = toRadians(latitude);
		declenation = toRadians(declenation);
		altitude = toRadians(altitude);
		return AngleConverter.radiansToHour(Math.acos(  (sin(altitude) - sin(latitude)*sin(declenation))  /  (cos(latitude)*cos(declenation))  ));
	}
	
}
