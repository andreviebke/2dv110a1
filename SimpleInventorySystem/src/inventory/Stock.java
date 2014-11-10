package inventory;

import java.util.LinkedList;
import java.util.List;

public class Stock {

	private double temperature;
	private List<StorageLocation> storageLocations;
	public static double MAX_TEMP = 30;
	public static double MIN_TEMP = 20;
	
	public Stock()
	{
		this.storageLocations = new LinkedList<StorageLocation>();
	}
	
	public void setTemperature(double d) {
		if(d < Stock.MIN_TEMP || d > Stock.MAX_TEMP)
			throw new IllegalArgumentException();
		
		this.temperature = d;		
	}

	public Object getTemperature() {
		return this.temperature;
	}

	public List<StorageLocation> getStorageLocations() {
		return this.storageLocations;
	}

	public void addStorageLocation(StorageLocation loc) {
		if(null == loc)
			throw new IllegalArgumentException();
		
		this.storageLocations.add(loc);
	}

}
