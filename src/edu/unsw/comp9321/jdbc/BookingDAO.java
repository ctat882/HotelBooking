package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;

public interface BookingDAO {
	
	// Get Bookings
	ArrayList<BookingDTO> getBookings(Boolean assigned, UserDTO user);
	void assignRoom(int bookingID, int roomNumber, int hotelID);
	void unassignRoom(int bookingID, int roomNumber, int hotelID);
}

