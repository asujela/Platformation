package entities;

public class TimeEntity {
	
	
	// a value from 0 -> 1
	public static double TIME_RATE = 1;
	
	
	public static void setTimeRate(double time) {
		if(time < 0) {
			System.out.println("Bad time value, time cannot be less than 0. TimePassedIn: " + time);
		}
		else if(time > 4) {
			System.out.println("Bad time value, time cannot be greater than 4. TimePassedIn: " + time);
		}
		else {
			TIME_RATE = time;
		}
	}
	
}
