package edu.unsw.comp9321;

import java.util.ArrayList;

public interface HotelDAO {

	
	/*
	 * What we need from the database.
	 */
	
	// Search results
	//TODO the return type must be changed to something more suitable
	public ArrayList<VacancyQueryDTO> findVacantRooms (VacancyQueryDTO query);
	
	// Make booking
	//TODO create Booking object and parse as a parameter. (Change Object to BookingObject)
	public void makeBooking (Object booking);
	
	
	
}
