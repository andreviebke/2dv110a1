package inventory;

import java.util.LinkedList;
import java.util.List;

public class StorageLocation {

	public static double MAX_WIDTH = 1000;
	public static int MAX_ARTICLES = 10;

	private String name;
	private List<Article> articles;

	public StorageLocation(String name) {
		
		if (null == name)
			throw new IllegalArgumentException();

		this.name = name;
		this.articles = new LinkedList<Article>();
	}

	public StorageLocation(String name, List<Article> input) {

		// Set articles
		if (input.size() > StorageLocation.MAX_ARTICLES)
			throw new IllegalArgumentException();
		
		this.articles = input;

		// Check width
		double totWidth = 0;

		for (Article a : input)
			totWidth += a.getWidth();

		if (totWidth >= StorageLocation.MAX_WIDTH)
			throw new IllegalArgumentException();
		
	}

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
		{
			if(a.getArtNr().equalsIgnoreCase(string))
			{
				tmpList.add(a);
			}
		}
		
		return tmpList;
	}

	public void insert(Article article) {
		if(null == article)
			throw new IllegalArgumentException();
		
		this.articles.add(article);
	}

	public void insertMany(LinkedList<Article> articles) {
		if(null == articles)
			throw new IllegalArgumentException();
		
		this.articles.addAll(articles);		
	}

	public LinkedList<Article> pickAll(String string) {

		if(null == string)
			throw new IllegalArgumentException();
		
		LinkedList<Article> tmpList = new LinkedList<Article>();
		
		for(Article a : this.articles)
		{
			if(a.getArtNr().equalsIgnoreCase(string))
			{
				tmpList.add(a);
			}
		}
		
		for(Article tmpArticle : tmpList)
		{
			this.articles.remove(tmpArticle);
		}
		
		return tmpList;
	}

	public LinkedList<Article> pickAll() {
		LinkedList<Article> tmpList = new LinkedList<Article>(this.articles);
		this.articles.clear();
		return tmpList;
	}
}
