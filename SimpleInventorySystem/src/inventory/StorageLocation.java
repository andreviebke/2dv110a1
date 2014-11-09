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
	}

	public StorageLocation(String name, List<Article> input) {

		if (input.size() > StorageLocation.MAX_ARTICLES)
			throw new IllegalArgumentException();

		double totWidth = 0;

		for (Article a : input)
			totWidth += a.getWidth();

		if (totWidth >= StorageLocation.MAX_WIDTH)
			throw new IllegalArgumentException();

		this.articles = input;
	}

	public String getName() {
		return this.name;
	}

	public List<Article> getArticles() {
		return this.articles;
	}
}
