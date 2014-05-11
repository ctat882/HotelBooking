package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;
import java.util.List;

public interface HotelOccupancyDAO {

	// holds a list containing information about the hotel's occupancy
	public List<HotelOccupancyDTO> getRooms();
	// Get number of available rooms for a particular room type
	public ArrayList<Integer> getAvailableRoomsForRoomType(String checkIn, String checkOut, String roomType, int userHotelID);
	// get the total number of hotels in the database
	public int getTotalHotels();
	// get a list of all the hotel locations
	public ArrayList<String> getHotelLocList();
	
}
