package inventory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Stock {

	private double temperature;
	private List<StorageLocation> storageLocations;
	public static double MAX_TEMP = 30;
	public static double MIN_TEMP = 20;
	public static int MAX_STORAGE_LOCATIONS = 3;

	public Stock() {
		this.storageLocations = new LinkedList<StorageLocation>();
		this.temperature = Double.MIN_VALUE;
	}

	public void setTemperature(double d) {
		if (d < Stock.MIN_TEMP || d > Stock.MAX_TEMP)
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
		if (null == loc)
			throw new IllegalArgumentException();

		if (this.removeDuplications(new LinkedList<StorageLocation>() {
			{
				add(loc);
			}
		}).size() == 1) {
			
			if(this.storageLocations.size() +1 > Stock.MAX_STORAGE_LOCATIONS)
				throw new TooManyStorageLocationsException();
			
			this.storageLocations.add(loc);
		}


	}

	public void addStorageLocations(List<StorageLocation> locs) {
		if (null == locs)
			throw new IllegalArgumentException();

		LinkedList<StorageLocation> toInsert = removeDuplications(locs);

		if (toInsert.size() + this.storageLocations.size() > Stock.MAX_STORAGE_LOCATIONS)
			throw new TooManyStorageLocationsException();

		this.storageLocations.addAll(toInsert);

	}

	private LinkedList<StorageLocation> removeDuplications(
			List<StorageLocation> locs) {
		LinkedList<StorageLocation> toInsert = new LinkedList<StorageLocation>();

		boolean exists;
		for (StorageLocation insertLoc : locs) {
			exists = false;

			for (StorageLocation currentLoc : this.storageLocations) {
				if (currentLoc == insertLoc) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				toInsert.add(insertLoc);
			}
		}

		return toInsert;
	}

}
