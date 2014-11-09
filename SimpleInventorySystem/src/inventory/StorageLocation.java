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
	 * @param name of storage location
	 * @param articles
	 */
	public StorageLocation(String name, List<Article> articles) {

		// Set articles
		if (articles.size() > StorageLocation.MAX_ARTICLES)
			throw new IllegalArgumentException();
		
		this.articles = articles;

		// Check width
		double totWidth = 0;

		for (Article a : articles)
			totWidth += a.getWidth();

		if (totWidth >= StorageLocation.MAX_WIDTH)
			throw new IllegalArgumentException();
		
	}

	/**
	 * Returns name
	 * @return name
	 */
	public String getName() {
		
		return this.name;
	}

	/**
	 * Returns all articles at this location
	 * @return all articles at location
	 */
	public List<Article> getArticles() {		
		return this.articles;
	}

	/**
	 * Returns all articles with given article id
	 * @param string - article id
	 * @return all articles with given article id
	 */
	public LinkedList<Article> getArticles(String string) {
		
		if(null == string)
			throw new IllegalArgumentException();
		
		LinkedList<Article> tmpList = new LinkedList<Article>();
		
		for(Article a : this.articles)
			if(a.getArtNr().equalsIgnoreCase(string))
				tmpList.add(a);
		
		return tmpList;
	}

	/**
	 * Inserts one article
	 * @param article to insert
	 */
	public void insert(Article article) {
		if(null == article)
			throw new IllegalArgumentException();

		if(article.getWidth() > StorageLocation.MAX_WIDTH)
			throw new IllegalArgumentException();
		
		if(article.getWidth() < StorageLocation.MIN_WIDTH)
			throw new IllegalArgumentException();
		
		this.articles.add(article);
	}

	/**
	 * Inserts a list of articles
	 * @param articles to insert
	 */
	public void insertMany(LinkedList<Article> articles) {
		if(null == articles)
			throw new IllegalArgumentException();

		double totWidth = 0;

		for (Article a : articles)
			totWidth += a.getWidth();

		if (totWidth > StorageLocation.MAX_WIDTH)
			throw new IllegalArgumentException();
		
		if(totWidth < StorageLocation.MIN_WIDTH)
			throw new IllegalArgumentException();
		
		this.articles.addAll(articles);		
	}

	/**
	 * Picks all articles with given article id
	 * @param string - article id
	 * @return all articles with given article id
	 */
	public LinkedList<Article> pickAll(String string) {

		if(null == string)
			throw new IllegalArgumentException();
		
		LinkedList<Article> tmpList = new LinkedList<Article>();
		
		for(Article a : this.articles)
			if(a.getArtNr().equalsIgnoreCase(string))
				tmpList.add(a);
		
		for(Article tmpArticle : tmpList)
			this.articles.remove(tmpArticle);
		
		return tmpList;
	}

	/**
	 * Picks all articles
	 * @return all articles
	 */
	public LinkedList<Article> pickAll() {
		LinkedList<Article> tmpList = new LinkedList<Article>(this.articles);
		this.articles.clear();
		return tmpList;
	}
}
