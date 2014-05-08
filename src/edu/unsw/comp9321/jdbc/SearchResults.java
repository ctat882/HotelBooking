package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResults {
	private ArrayList<ArrayList<RoomDTO>> results;
	private HashMap<String,Double> prices;
	
	public HashMap<String, Double> getPrices() {
		return prices;
	}

	public void setPrices(HashMap<String, Double> prices) {
		this.prices = prices;
	}

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
