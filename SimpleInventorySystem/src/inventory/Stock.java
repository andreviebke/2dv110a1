package inventory;

import inventory.exceptions.InvalidTemperatureException;
import inventory.exceptions.TooManyStorageLocationsException;

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
			throw new InvalidTemperatureException();

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

		if (!(this.duplicationExists(loc))) {
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

		LinkedList<StorageLocation> noInternalDuplicates = this
				.removeInternalDuplications(locs);
		LinkedList<StorageLocation> nonExternalDuplicates = this
				.removeExternalDuplications(noInternalDuplicates,
						this.storageLocations);

		this.checkStorageCount(nonExternalDuplicates.size());

		this.storageLocations.addAll(nonExternalDuplicates);

	}

	/**
	 * Get storage location by name
	 * 
	 * @param storage
	 *            name
	 * @return all storage locations with name
	 */
	public LinkedList<StorageLocation> getStorageLocationsByName(
			String storageName) {

		LinkedList<StorageLocation> toReturn = new LinkedList<StorageLocation>();

		for (StorageLocation loc : this.storageLocations)
			if (loc.getName().equalsIgnoreCase(storageName))
				toReturn.add(loc);

		return toReturn;
	}

	/**
	 * Moves all articles from one storage location to another
	 * 
	 * @param s1
	 *            - to location
	 * @param s2
	 *            - from location
	 */
	public boolean moveAllArticles(StorageLocation s1, StorageLocation s2) {
		
		return this.moveAllArticles(s1, s2, null);
	}

	/**
	 * Moves all with article id from one storage location to another
	 * 
	 * @param s1
	 *            - to location
	 * @param s2
	 *            - from location
	 * @param artrnr
	 *            - article number
	 */
	public boolean moveAllArticles(StorageLocation s1, StorageLocation s2,
			String artNr) {

		if (null == s1 || null == s2)
			throw new IllegalArgumentException();

		LinkedList<Article> allArticles = new LinkedList<Article>();
		allArticles.addAll(s1.getArticles());
		allArticles.addAll(artNr == null ? s2.getArticles() : s2.getArticles(artNr));

		if (this.checkCount(allArticles) && this.checkWidth(allArticles))
		{
			s1.insertMany(artNr == null ? s2.pickAll() : s2.pickAll(artNr));
			return true;
		}
		
		return false;
	}

	/**
	 * Finds articles with given id
	 * 
	 * @param id
	 * @return all found articles
	 */
	public LinkedList<Article> findArticles(String id) {

		if (null == id)
			throw new IllegalArgumentException();

		LinkedList<Article> foundArticles = new LinkedList<Article>();

		for (StorageLocation s : this.storageLocations)
			foundArticles.addAll(s.getArticles(id));

		return foundArticles;
	}

	/**
	 * Merge storage locations
	 * 
	 * @param s1
	 *            - to location
	 * @param s2
	 *            - from location
	 */
	public void mergeStorageLocations(StorageLocation s1, StorageLocation s2) {
		if (null == s1 || null == s2)
			throw new IllegalArgumentException();

		if (this.moveAllArticles(s1, s2))
			this.storageLocations.remove(s2);

	}

	/**
	 * Verifies the total width
	 * 
	 * @param articles
	 *            - articles to verify
	 * @return true if width is within storage boundaries, false otherwise
	 */
	private boolean checkWidth(LinkedList<Article> articles) {
		double totWidth = 0;

		for (Article art : articles)
			totWidth += art.getWidth();

		if (totWidth > StorageLocation.MAX_WIDTH)
			return false;

		return true;
	}

	/**
	 * Verifies the total number of articles
	 * 
	 * @param articles
	 *            - articles
	 * @return true if number of articles less than max for storage location,
	 *         false otherwise
	 */
	private boolean checkCount(LinkedList<Article> articles) {
		if (articles.size() > StorageLocation.MAX_ARTICLES)
			return false;

		return true;
	}

	/**
	 * Removes external duplications, i.e. duplicated storage locations to be
	 * inserted
	 * 
	 * @param newLocations
	 *            - locations to insert
	 * @param existingLocations
	 *            - existing locations at stock
	 * @return List without duplications
	 */
	private LinkedList<StorageLocation> removeExternalDuplications(
			List<StorageLocation> newLocations,
			List<StorageLocation> existingLocations) {
		LinkedList<StorageLocation> toReturn = new LinkedList<StorageLocation>(
				newLocations);

		toReturn.removeAll(existingLocations);

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

		LinkedList<StorageLocation> toReturn = new LinkedList<StorageLocation>(
				new LinkedHashSet<StorageLocation>(toInsert));

		return toReturn;
	}
}
