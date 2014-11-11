package inventory;

import java.util.LinkedList;
import java.util.List;

public class StorageLocation {

	public static double MAX_WIDTH = 999;
	public static int MAX_ARTICLES = 10;

	private String name;
	private List<Article> articles;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            of storage location
	 */
	public StorageLocation(String name) {

		if (null == name)
			throw new IllegalArgumentException();

		this.name = name;
		this.articles = new LinkedList<Article>();
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            of storage location
	 * @param articles
	 */
	public StorageLocation(String name, List<Article> articles) {
		this.checkNumArticles(articles);
		this.checkWidth((LinkedList<Article>) articles);
		this.articles = articles;
	}

	/**
	 * Returns name
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns all articles at this location
	 * 
	 * @return all articles at location
	 */
	public List<Article> getArticles() {
		return this.articles;
	}

	/**
	 * Returns all articles with given article id
	 * 
	 * @param string
	 *            - article id
	 * @return all articles with given article id
	 */
	public LinkedList<Article> getArticles(String string) {

		if (null == string)
			throw new IllegalArgumentException();

		LinkedList<Article> tmpList = findArticlesWithId(string);

		return tmpList;
	}

	/**
	 * Inserts one article
	 * 
	 * @param article
	 *            to insert
	 */
	public void insert(Article article) {
		if (null == article)
			throw new IllegalArgumentException();

		LinkedList<Article> articles = new LinkedList<Article>(this.articles);
		articles.add(article);

		this.checkWidth(articles);
		this.checkNumArticles(articles);

		this.articles.add(article);
	}

	/**
	 * Inserts a list of articles
	 * 
	 * @param articles
	 *            to insert
	 */
	public void insertMany(LinkedList<Article> articles) {
		if (null == articles)
			throw new IllegalArgumentException();

		LinkedList<Article> allArticles = new LinkedList<Article>(articles);
		allArticles.addAll(this.articles);

		this.checkWidth(allArticles);
		this.checkNumArticles(allArticles);

		this.articles.addAll(articles);
	}

	/**
	 * Picks all articles with given article id
	 * 
	 * @param string
	 *            - article id
	 * @return all articles with given article id
	 */
	public LinkedList<Article> pickAll(String string) {

		if (null == string)
			throw new IllegalArgumentException();

		LinkedList<Article> tmpList = findArticlesWithId(string);

		for (Article tmpArticle : tmpList)
			this.articles.remove(tmpArticle);

		return tmpList;
	}

	/**
	 * Picks all articles
	 * 
	 * @return all articles
	 */
	public LinkedList<Article> pickAll() {
		LinkedList<Article> tmpList = new LinkedList<Article>(this.articles);
		this.articles.clear();
		return tmpList;
	}

	/**
	 * Picks a count articles with number
	 * 
	 * @param string
	 *            - article number
	 * @param count
	 *            - number to pick
	 * @return picked articles
	 */
	public LinkedList<Article> pick(String string, int count) {
		LinkedList<Article> allArticles = this.getArticles(string);
		LinkedList<Article> pickedArticles = new LinkedList<Article>();

		for (int i = 0; i < count && i < allArticles.size(); i++)
			pickedArticles.add(allArticles.get(i));
		
		this.articles.removeAll(pickedArticles);

		return pickedArticles;
	}

	/**
	 * Checks the the total width of a set of articles
	 * 
	 * @param articles
	 */
	private void checkWidth(LinkedList<Article> articles) {
		double totWidth = 0;

		for (Article a : articles)
			totWidth += a.getWidth();

		if (totWidth > StorageLocation.MAX_WIDTH)
			throw new IllegalArgumentException();
	}

	/**
	 * Returns all articles with the given id
	 * 
	 * @param string
	 *            - article id
	 * @param tmpList
	 *            - list
	 */
	private LinkedList<Article> findArticlesWithId(String string) {
		LinkedList<Article> articles = new LinkedList<Article>();

		for (Article a : this.articles)
			if (a.getArtNr().equalsIgnoreCase(string))
				articles.add(a);

		return articles;
	}

	/**
	 * Checks the number of articles
	 * 
	 * @param articles
	 *            - articles
	 */
	private void checkNumArticles(List<Article> articles) {
		if (articles.size() > StorageLocation.MAX_ARTICLES)
			throw new IllegalArgumentException();
	}
}
