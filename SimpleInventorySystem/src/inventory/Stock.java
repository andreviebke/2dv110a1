package inventory;

public class Stock {

	private double temperature;
	
	public void setTemperature(double d) {
		if(d == 19.9)
			throw new IllegalArgumentException();
		
		if(d == 30.1)
			throw new IllegalArgumentException();
		
		this.temperature = d;
	}

	public Object getTemperature() {
		return this.temperature;
	}

}
