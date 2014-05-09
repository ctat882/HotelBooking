package edu.unsw.comp9321.logic;

import java.io.Serializable;

import edu.unsw.comp9321.jdbc.SearchResults;

public class CartBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private SearchResults search;
	
	public CartBean () {
		this.search = new SearchResults();
	}

	public SearchResults getSearch() {
		return search;
	}

	public void setSearch(SearchResults search) {
		this.search = search;
	}

}
