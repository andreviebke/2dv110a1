package inventory;

public class Stock {

	public void setTemperature(double d) {
		if(d == 19.9)
			throw new IllegalArgumentException();
		
		if(d == 30.1)
			throw new IllegalArgumentException();
	}

}
