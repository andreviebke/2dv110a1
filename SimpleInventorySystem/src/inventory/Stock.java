package inventory;

import java.util.LinkedHashSet;
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

	/**
	 * Sets the temperature in stock
	 * 
	 * @param d
	 *            - temperature
	 */
	public void setTemperature(double d) {
		if (d < Stock.MIN_TEMP || d > Stock.MAX_TEMP)
			throw new IllegalArgumentException();

		this.temperature = d;
	}

	/**
	 * Returns stock temperature
	 * 
	 * @return stock temperature
	 */
	public Object getTemperature() {
		return this.temperature;
	}

	/**
	 * Returns all storage locations in stock
	 * 
	 * @return all storage locations in stock
	 */
	public List<StorageLocation> getStorageLocations() {
		return this.storageLocations;
	}

	/**
	 * Adds a storage location to the stock
	 * 
	 * @param loc
	 *            - storage location
	 */
	public void addStorageLocation(StorageLocation loc) {
		if (null == loc)
			throw new IllegalArgumentException();

		if (!this.duplicationExists(loc)) {
			checkStorageCount(1);

			this.storageLocations.add(loc);
		}
	}

	/**
	 * Adds a list of storage locations to stock
	 * 
	 * @param locs
	 *            - locations
	 */
	public void addStorageLocations(List<StorageLocation> locs) {
		if (null == locs)
			throw new IllegalArgumentException();

		LinkedList<StorageLocation> noInternalDuplicates = this.removeInternalDuplications(
				locs);
		LinkedList<StorageLocation> toInsert = this.removeExternalDuplications(
				noInternalDuplicates, this.storageLocations);

		this.checkStorageCount(toInsert.size());

		this.storageLocations.addAll(toInsert);

	}

	/**
	 * Returns true if duplication exists, false otherwise
	 * 
	 * @param loc
	 *            - location
	 * @return true if duplication exists, false otherwise
	 */
	private boolean duplicationExists(StorageLocation loc) {
		boolean exists = false;

		for (StorageLocation s : this.storageLocations)
			if (loc == s)
				exists = true;

		return exists;
	}

	/**
	 * Returns a list with non duplicated storage locations
	 * 
	 * @param locs
	 *            - storage locations
	 * @return a list with non duplicated storage locations
	 */
	private LinkedList<StorageLocation> removeInternalDuplications(
			List<StorageLocation> toInsert) {

		LinkedList<StorageLocation> toReturn = new LinkedList<StorageLocation>(new LinkedHashSet<StorageLocation>(toInsert));

		return toReturn;
	}
	
	private LinkedList<StorageLocation> removeExternalDuplications(
			List<StorageLocation> toInsert, List<StorageLocation> toCheckAgainst) {
		LinkedList<StorageLocation> toReturn = new LinkedList<StorageLocation>();

		boolean exists;
		for (int i = 0; i < toInsert.size(); i++) {
			exists = false;

			for (int j = 0; j < toCheckAgainst.size(); j++) {
				if (toInsert.get(i) == toCheckAgainst.get(j)) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				toReturn.add(toInsert.get(i));
			}
		}

		return toReturn;
	}
	
	/**
	 * Checks the storage count including numbersTOAdd
	 * 
	 * @param numbersToAdd
	 *            - the number of storage locations to add
	 */
	private void checkStorageCount(int numbersToAdd) {
		if (this.storageLocations.size() + numbersToAdd > Stock.MAX_STORAGE_LOCATIONS)
			throw new TooManyStorageLocationsException();
	}

}
