package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;

public class BookingDAOImpl implements BookingDAO {
	
	static Logger logger = Logger.getLogger(BookingDAOImpl.class.getName());
	private Connection connection;
	
	public BookingDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
		
	}

	@Override
	public ArrayList<BookingDTO> getBookings(Boolean assigned, UserDTO user) {
		
		ArrayList<BookingDTO> bookingInfo = new ArrayList<BookingDTO>();	
		int userHotelID = user.getUserHotelId();
		String hotelLoc = user.getHoteLoc();
		
		// Get bookings that have NOT BEEN assigned yet
		if (!assigned){
			
			try{
				Statement stmnt = connection.createStatement();
				// Get all Bookings that are unassigned
				String query = "SELECT * FROM BOOKINGS WHERE ASSIGNED = " + "'No'" + "AND HOTEL = " + userHotelID;
				ResultSet res = stmnt.executeQuery(query);
				while (res.next()){
					BookingDTO bookingDTO = new BookingDTO();
					//Get Booking ID
					int bookingID = res.getInt(1);
					// Get check-in Date
					Date check_in = res.getDate(3);
					// Get check-out Date
					Date check_out = res.getDate(4);
					// Get type of room
					String type = res.getString(5);
					
					// **** Add Items to BookingDTO ****
					// Add Booking ID
					bookingDTO.setId(bookingID);
					// Add Check-In 
					bookingDTO.setCheckIn(check_in);
					// Add Check-Out
					bookingDTO.setCheckOut(check_out);
					// Add Type of Room
					bookingDTO.setSize(type);
										
					// Add BookingDTO to bookingInfo 
					bookingInfo.add(bookingDTO);
				
				}
				
				stmnt.close();
				
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
			
		}
		// Get bookings that HAVE been assigned
		else if (assigned){
			
			try{
				Statement stmnt = connection.createStatement();
				// Get all Bookings that are assigned
				String query = "SELECT * FROM BOOKINGS WHERE ASSIGNED = " + "'Yes'" + "AND HOTEL = " + userHotelID;
				ResultSet res = stmnt.executeQuery(query);
				
				while (res.next()){
					BookingDTO bookingDTO = new BookingDTO();
					//Get Booking ID
					int bookingID = res.getInt(1);
					// Get check-in Date
					Date check_in = res.getDate(3);
					// Get check-out Date
					Date check_out = res.getDate(4);
					// Get type of room
					String type = res.getString(5);
					// Get assignedRoomNumber
					int assignedRoomNo = res.getInt(10);
					
					// **** Add Items to BookingDTO ****
					
					// Add hotel location
					//bookingDTO.setHotelLoc(hotelLoc);
					
					
					
					// Add Booking ID
					bookingDTO.setId(bookingID);
					// Add Check-In 
					bookingDTO.setCheckIn(check_in);
					// Add Check-Out
					bookingDTO.setCheckOut(check_out);
					// Add Type of Room
					bookingDTO.setSize(type);
					// Add Assigned Room Number
					bookingDTO.setAssignedRoom(assignedRoomNo);
										
					// Add BookingDTO to bookingInfo 
					bookingInfo.add(bookingDTO);
				}
				
				
				stmnt.close();
				
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
		}
		
		return bookingInfo; 
	}

	@Override
	public void assignRoom(int bookingID, int roomNumber, int hotelID) {
		
		try{			
			Statement stmnt = connection.createStatement();
									// *** Update Booking ***
			// Add AssignedRoom AND Change 'Assigned' to 'Yes'
			String query = "UPDATE BOOKINGS SET ASSIGNED = 'Yes', ASSIGNEDROOM = " + roomNumber + " WHERE BOOKING_ID = " + bookingID;
			stmnt.executeUpdate(query);			
			
									// *** Update Rooms ***
			// Change Room Availability to 'Occupied' 
			query = "UPDATE ROOMS SET AVAILABILITY = 'Occupied' WHERE ROOM_NUM = " + roomNumber + "AND HOTEL = " + hotelID;
			stmnt.executeUpdate(query);
			
			stmnt.close();
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
	}
	public void unassignRoom(int bookingID, int roomNumber, int hotelID) {
		try{
			Statement stmnt = connection.createStatement();
						// *** Remove Booking ***
			String query = "DELETE FROM BOOKINGS WHERE BOOKING_ID = " + bookingID;
			stmnt.executeUpdate(query);
						// *** Update Rooms ***
			// Change room Availability to 'Available'
			query = "UPDATE ROOMS SET AVAILABILITY = 'Available' WHERE ROOM_NUM = " + roomNumber + "AND HOTEL = " + hotelID;
			stmnt.executeUpdate(query);
			
			stmnt.close();
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
	}	
}
