package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResults {
	private ArrayList<ArrayList<RoomDTO>> results;
	private HashMap<String,Double> prices;
	private double[] single_totals;
	private double[] twin_totals;
	private double[] queen_totals;
	private double[] executive_totals;
	private double[] suite_totals;
	
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

	public double[] getSingle_totals() {
		return single_totals;
	}

	public void setSingle_totals(double[] single_totals) {
		this.single_totals = single_totals;
	}

	public double[] getTwin_totals() {
		return twin_totals;
	}

	public void setTwin_totals(double[] twin_totals) {
		this.twin_totals = twin_totals;
	}

	public double[] getQueen_totals() {
		return queen_totals;
	}

	public void setQueen_totals(double[] queen_totals) {
		this.queen_totals = queen_totals;
	}

	public double[] getExecutive_totals() {
		return executive_totals;
	}

	public void setExecutive_totals(double[] executive_totals) {
		this.executive_totals = executive_totals;
	}

	public double[] getSuite_totals() {
		return suite_totals;
	}

	public void setSuite_totals(double[] suite_totals) {
		this.suite_totals = suite_totals;
	}
	
//	private int discSingle
	
}
