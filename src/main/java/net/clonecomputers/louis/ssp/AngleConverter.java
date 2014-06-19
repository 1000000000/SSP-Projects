package net.clonecomputers.louis.ssp;

public class AngleConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static double toDec(int deg, int min, double sec) {
		double signum = Math.signum(deg);
		return signum*(Math.abs(deg) + (min + sec/60)/60);
	}
	
	public static double degreeToHour(double degree) {
		return degree/15;
	}
	
	public static double radiansToHour(double rad) {
		return 12*rad/Math.PI;
	}

}
