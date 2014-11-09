package inventory;

import java.util.LinkedList;
import java.util.List;

public class StorageLocation {

	public static double MAX_WIDTH = 999;
	public static double MIN_WIDTH = 0;
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

		checkNumArticles(articles);
		this.articles = articles;
		this.checkWidth((LinkedList<Article>) this.articles);
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

		LinkedList<Article> tmpList = new LinkedList<Article>();
		getArticlesWithId(string, tmpList);

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

		this.checkWidth(new LinkedList<Article>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				add(article);
			}
		});

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

		this.checkWidth(articles);

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

		LinkedList<Article> tmpList = new LinkedList<Article>();

		getArticlesWithId(string, tmpList);

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

		if (totWidth < StorageLocation.MIN_WIDTH)
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
	private void getArticlesWithId(String string, LinkedList<Article> tmpList) {
		for (Article a : this.articles)
			if (a.getArtNr().equalsIgnoreCase(string))
				tmpList.add(a);
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

	public LinkedList<Article> pick(String string, int count) {
		LinkedList<Article> allArticles = this.getArticles(string);
		LinkedList<Article> pickedArticles = new LinkedList<Article>();
		
		for(int i=0; i<count; i++)
			pickedArticles.add(allArticles.get(i));
			
		return pickedArticles;
	}
}
