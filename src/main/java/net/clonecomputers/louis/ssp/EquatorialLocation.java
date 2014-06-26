package net.clonecomputers.louis.ssp;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.util.Calendar;

public class EquatorialLocation {
	
	public static final double EPSILON = toRadians(23.5);
	
	private double ra;
	private double dec;
	
	private double epoch;

	/**
	 * Constructs an EquatorialLocation from a given R.A. and declenation with an epoch of J2000
	 * @see #EquatorialLocation(double, double, double)
	 */
	public EquatorialLocation(double rightAscension, double declenation) {
		this(rightAscension, declenation, 2000);
	}
	
	/**
	 * Constructs an EquatorialLocation from a given R.A., declenation with an epoch of J2000
	 * @param rightAscension the right ascension in decimal hours
	 * @param declenation the declenation in decimal degrees
	 * @param epoch the epoch in Julian years
	 */
	public EquatorialLocation(double rightAscension, double declenation, double epoch) {
		ra = AngleConverter.hoursToRadians(rightAscension);
		dec = toRadians(declenation);
		this.epoch = epoch;
	}
	
	/**
	 * Changes the epoch to the new epoch and precess the coordinates
	 * @param newEpoch the new epoch in Julian years
	 */
	public void changeEpoch(double newEpoch) {
			if(epoch != 2000) throw new IllegalStateException("The epoch cannot be changed from anything but 2000");
			double deltaT = newEpoch - epoch;
			double m = 46.1244 + (2.79e-4)*deltaT;
			double n = 20.0431 - (8.5e-15)*deltaT;
			double deltaRA = toRadians(deltaT*(m + n*sin(ra)*tan(dec))/3600);
			dec += toRadians(deltaT*n*cos(ra)/3600);
			ra += deltaRA;
			epoch = newEpoch;
	}
	
	/**
	 * Accounts for the proper motion of a body this location represents\n
	 * Be warned! You can call this method multiple times and it will not complain!
	 * @param raMotion the proper motion of the R.A. in seconds per year
	 * @param decMotion the proper motion of the declenation in \u2033/yr
	 */
	public void accountForProperMotion(double raMotion, double decMotion) {
		ra += AngleConverter.hoursToRadians(raMotion*(epoch - 2000)/3600);
		dec += toRadians(decMotion*(epoch - 2000)/3600);
	}
	
	/**
	 * Calculates hour angle of this location at a time given the longitude of the observation site
	 * and the R.A. of this location
	 * @param time the time of observation (this does not need to be the local time as it is converted by its time zone to UTC)
	 * @param eastLongitude the longitude of the observation site in decimal degrees east of the prime meridian
	 * @return the hour angle of this location in decimal hours
	 */
	public double getHourAngle(Calendar time, double eastLongitude) {
		return AngleConverter.degreesToHours(DateTimeConverter.getLST(time, eastLongitude)) - AngleConverter.radiansToHours(ra);
	}
	
	/**
	 * Calculates the positive hour angle that this location will have when it is at a certain altitude
	 * when observed from a particular latitude
	 * @param latitude the latitude of the observation site in decimal degrees above the equator
	 * @param altitude the altitude of this location in decimal degrees
	 * @return the hour angle of this location in decimal hours
	 */
	public double getAltitudeHourAngle(double latitude, double altitude) {
		latitude = toRadians(latitude);
		altitude = toRadians(altitude);
		return AngleConverter.radiansToHours(Math.acos(  (sin(altitude) - sin(latitude)*sin(dec))  /  (cos(latitude)*cos(dec))  ));
	}
	
	/**
	 * Calculates the altitude of this location given the latitude of the observation site
	 * and the hour angle of this location
	 * @param latitude the latitude of the observation site in decimal degrees above the equator
	 * @param hourAngle the hour angle of this location in decimal hours
	 * @return the altitude of the body in decimal degrees
	 */
	public double getAltitude(double latitude, double hourAngle) {
		latitude = toRadians(latitude);
		hourAngle = AngleConverter.hoursToRadians(hourAngle);
		return Math.toDegrees(Math.asin(sin(latitude)*sin(dec) + cos(latitude)*cos(dec)*cos(hourAngle)));
	}
	
	/**
	 * Calculates the ecliptic latitude of this location
	 * @return the ecliptic latitude of this location in decimal degrees
	 */
	public double getEclipticLatitude() {
		return toDegrees(asin(sin(dec)*cos(EPSILON) - cos(dec)*sin(EPSILON)*sin(ra)));
	}
	
	/**
	 * Calculates the ecliptic longitude of this location
	 * @return the ecliptic longitude of this location in decimal degrees
	 */
	public double getEclipticLongitude() {
		return toDegrees(atan2(sin(ra)*cos(EPSILON) + tan(dec)*sin(EPSILON), cos(ra)));
	}
	
	/**
	 * Returns the R.A. of this location
	 * @return the R.A. in decimal hours
	 */
	public double getRA() {
		return AngleConverter.radiansToHours(ra);
	}
	
	/**
	 * Returns the declenation of this location
	 * @return the declenation in decimal degrees
	 */
	public double getDec() {
		return toDegrees(dec);
	}
	
	@Override
	/**
	 * Returns a string of the form "((-)rah ram ras, \u00B1dec\u00B0 dec\u2032 dec\u2033)"
	 */
	public String toString() {
		StringBuilder str = new StringBuilder('(');
		double ra = AngleConverter.radiansToHours(this.ra);
		double dec = toDegrees(this.dec);
		if(ra < 0) {
			str.append('-');
			ra *= -1;
		}
		str.append((int) ra);
		str.append("h ");
		ra = (ra - (int) ra)*60;
		str.append((int) ra);
		str.append("m ");
		ra = (ra - (int) ra)*60;
		str.append(Math.round(ra));
		str.append("s, ");
		if(dec < 0) {
			str.append('-');
			dec *= -1;
		} else {
			str.append('+');
		}
		str.append((int) dec);
		str.append("\u00B0 ");
		dec = (dec - (int) dec)*60;
		str.append((int) dec);
		str.append("\u2032 ");
		dec = (dec - (int) dec)*60;
		str.append(Math.round(dec));
		str.append("\u2033)");
		return str.toString();
	}
}
