package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;

public class SearchResults {
	private ArrayList<ArrayList<RoomDTO>> results;
	
	public SearchResults () {
		
	}

	public ArrayList<ArrayList<RoomDTO>> getResults() {
		return results;
	}

	public void setResults(ArrayList<ArrayList<RoomDTO>> results) {
		this.results = results;
	}
	
//	private int discSingle
	
}
