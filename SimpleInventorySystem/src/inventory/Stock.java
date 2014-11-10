package inventory;

public class Stock {

	private double temperature;
	public static double MAX_TEMP = 30;
	public static double MIN_TEMP = 20;
	
	public void setTemperature(double d) {
		if(d < Stock.MIN_TEMP || d > Stock.MAX_TEMP)
			throw new IllegalArgumentException();
		
		this.temperature = d;
	}

	public Object getTemperature() {
		return this.temperature;
	}

}
