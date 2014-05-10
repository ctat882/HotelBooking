package edu.unsw.comp9321.logic;

import java.io.Serializable;
import java.util.ArrayList;

import edu.unsw.comp9321.jdbc.RoomDTO;
import edu.unsw.comp9321.jdbc.SearchResults;

public class CartBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private SearchResults search;
	private ArrayList<RoomDTO> selection;
	
	public CartBean () {
		this.search = new SearchResults();
	}

	public SearchResults getSearch() {
		return search;
	}

	public void setSearch(SearchResults search) {
		this.search = search;
	}

	public ArrayList<RoomDTO> getSelection() {
		return selection;
	}

	public void setSelection(ArrayList<RoomDTO> selection) {
		this.selection = selection;
	}

}
