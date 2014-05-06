package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;
import java.util.List;

public interface HotelOwnerDAO {

	// holds a list containing information about the hotel's occupancy
	public List<HotelOccupancyDTO> getRooms();
	// get the total number of hotels in the database
	public int getTotalHotels();
	// get a list of all the hotel locations
	public ArrayList<String> getHotelLocList();
	
}
