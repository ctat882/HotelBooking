package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;

public class HotelOccupancyDAOImpl implements HotelOccupancyDAO{
	 
	
	static Logger logger = Logger.getLogger(HotelOccupancyDAOImpl.class.getName());
	private Connection connection;
	
	public HotelOccupancyDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");		
	}
	

	@Override
	public List<HotelOccupancyDTO> getRooms() {
		
		ArrayList<HotelOccupancyDTO> hotelOccupancyInfo = new ArrayList<HotelOccupancyDTO>();
		
		try{			
			
			int noHotels = 0;
			String HotelLoc = "";
			int noAvail = 0;
			int noOccupied = 0;
						
			// Retrieve number of hotels in database
			noHotels = getTotalHotels();
			
			for (int hotelID = 1; hotelID <= noHotels; ++hotelID){ 						
				// get 'Occupied' and 'Available' amounts for each type of Room ('Single', 'Twin', 'Queen', 'Executive' and 'Suite')			
				
				// Get Hotel Location
				HotelLoc = getHotelLoc(hotelID);			
				
				/*******************************************  SINGLE ROOMS  *****************************************************/
				
				// Get 'Occupied' 'Single' Room amount
				noOccupied = getRoomAvailability(hotelID, "Single", "Occupied");		
				
			
				// Get 'Available' 'Single' Room amount 
				noAvail = getRoomAvailability(hotelID, "Single", "Available");
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Single", noOccupied, noAvail));							
			
				/***********************************************************************************************************************/
			
				/****************************************************  TWIN ROOMS  *****************************************************/
			
				// Get 'Occupied' 'Twin' Room amount
				noOccupied = getRoomAvailability(hotelID, "Twin", "Occupied");	
				
				// Get 'Available' 'Twin' Room amount
				noAvail = getRoomAvailability(hotelID, "Twin", "Available");
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Twin", noOccupied, noAvail));
			
				/***********************************************************************************************************************/
				
				/****************************************************  QUEEN ROOMS  *****************************************************/
				// Get 'Occupied' 'Queen' Room amount
				noOccupied = getRoomAvailability(hotelID, "Queen", "Occupied");	
				
				// Get 'Available' 'Queen' Room amount
				noAvail = getRoomAvailability(hotelID, "Queen", "Available");
				
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Queen", noOccupied, noAvail));
				/***********************************************************************************************************************/
				
				/***********************************************  EXECUTIVE ROOMS  *****************************************************/
				// Get 'Occupied' 'Executive' Room amount
				noOccupied = getRoomAvailability(hotelID, "Executive", "Occupied");	
				
				// Get 'Available' 'Executive' Room amount
				noAvail = getRoomAvailability(hotelID, "Executive", "Available");
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Executive", noOccupied, noAvail));
				/***********************************************************************************************************************/
				
				/***************************************************  SUITE ROOMS  *****************************************************/
				// Get 'Occupied' 'Suite' Room amount
				noOccupied = getRoomAvailability(hotelID, "Suite", "Occupied");	
				
				// Get 'Available' 'Suite' Room amount
				noAvail = getRoomAvailability(hotelID, "Suite", "Available");
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Suite", noOccupied, noAvail));
				/***********************************************************************************************************************/
			}
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		
		return hotelOccupancyInfo;
	}
	
	@Override
	public ArrayList<Integer> getAvailableRoomsForRoomType(String checkIn, String checkOut, String roomType, int userHotelID) {
		
				
		ArrayList<Integer> availableRooms = new ArrayList<Integer>();
		Date unassignedCheckInDate = null;
		Date unassignedCheckOutDate = null;
		try {
			unassignedCheckInDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkIn);
			unassignedCheckOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkOut);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		ArrayList<BookingDTO> allBookedRooms = new ArrayList<BookingDTO>();
		try{
			Statement stmnt = connection.createStatement();
			
			// Find Incompatible Bookings
			// 1) Find all assigned bookings particular RoomTYpe
			// 2) Find all incompatible booked rooms
			// 3) Get all rooms of a particular type 
			// 4) 3) - 2) = Available rooms for (Type)
			
			// 1)
			String query = "SELECT * FROM BOOKINGS WHERE ASSIGNED = " + "'Yes'" + "AND HOTEL = " + userHotelID + "AND SIZE = " + "'" + roomType + "'";
			ResultSet res = stmnt.executeQuery(query);	
						
			while (res.next()){
				
				BookingDTO bookingDTO = new BookingDTO();
				
				// set check-in date
				bookingDTO.setCheckIn(res.getDate(3));
				// set check-out date
				bookingDTO.setCheckOut(res.getDate(4));
				// get room type
				bookingDTO.setSize(res.getString(5));
				// set assignedRoom number
				bookingDTO.setAssignedRoom(res.getInt(10));
				allBookedRooms.add(bookingDTO);
			}
			
			stmnt.close();
			
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
			
			// 2) Find all incompatible booking rooms 
			
			ArrayList<Integer> incompatibleBookedRooms = new ArrayList<Integer>();
			
			for (Iterator<BookingDTO> i = (Iterator<BookingDTO>) allBookedRooms.iterator(); i.hasNext();){
				BookingDTO item = i.next();
				Date bookedCheckInDate = item.getCheckIn();
				Date bookedCheckOutDate = item.getCheckOut();
				int assignedRoom = item.getAssignedRoom();
				
				
				// TODO: CHECK THIS
		
				if ( (unassignedCheckInDate.after(bookedCheckInDate) && unassignedCheckInDate.before(bookedCheckOutDate))
																	||
					(unassignedCheckOutDate.after(bookedCheckInDate) && unassignedCheckOutDate.before(bookedCheckOutDate))
																	||
					(unassignedCheckInDate.equals(bookedCheckInDate) || unassignedCheckInDate.equals(bookedCheckOutDate))
																	||
					(unassignedCheckOutDate.equals(bookedCheckInDate) || unassignedCheckOutDate.equals(bookedCheckOutDate))){
					
					incompatibleBookedRooms.add(assignedRoom);					
				}
			}
			
			// 3) Get ALL rooms of a particular type
			try{
				Statement stmnt = connection.createStatement();
				String query = "SELECT ROOM_NUM FROM ROOMS WHERE SIZE = " + "'" + roomType + "'" + "AND HOTEL = " + userHotelID;
				ResultSet res = stmnt.executeQuery(query);	
				while (res.next()){
					availableRooms.add(res.getInt(1));
				}
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
			
			// 4) Remove incompatible rooms from ALL available rooms
			
			availableRooms.removeAll(incompatibleBookedRooms);
		
		return availableRooms;
	}
	
	// Get the number of hotels in the database
	public int getTotalHotels(){
		int noHotels = 0;
		
		try{			
			Statement stmnt = connection.createStatement();
			// Retrieve number of hotels in database
			String query_NoHotels = "SELECT MAX(ID) FROM HOTELS";	
			ResultSet res = stmnt.executeQuery(query_NoHotels);
			logger.info("The result set size is "+res.getFetchSize());
			if (res.next()){
				noHotels = res.getInt(1);
			}
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		return noHotels;
	}
	
	// Get a list of all the locations of the hotels in the database
		public ArrayList<String> getHotelLocList(){
			ArrayList<String> hotelLoc = new ArrayList<String>();
			
			int noHotels = getTotalHotels();
			
						
			for (int hotelID = 1; hotelID <= noHotels; ++hotelID){ 							
				hotelLoc.add(getHotelLoc(hotelID));
			}
					
			return hotelLoc;
		}

		
		// Get the Location of a Hotel using its hotelID.
		public String getHotelLoc(int hotelID){
			
			String hotelLoc = "";
			
			try{			
				Statement stmnt = connection.createStatement();
				
				String query_HotelLoc = "SELECT CITY from HOTELS where ID = " + hotelID;
				ResultSet res = stmnt.executeQuery(query_HotelLoc);				
				if (res.next()){
					hotelLoc = res.getString(1);	
				}
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
			
			return hotelLoc;
			
		}
		// Retrieve the number of 'Available' and 'Occupied' rooms.
		public int getRoomAvailability(int hotelID, String roomType, String availability){
			int result = 0;
			try{			
				Statement stmnt = connection.createStatement();
				
				String query = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = " + "'" + roomType + "'" +  " AND AVAILABILITY = " + "'" + availability + "'"  ;
				
				ResultSet res = stmnt.executeQuery(query);				
				if (res.next()){
					result = res.getInt(1);					
				}
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
			
			return result;
		}


		
		

}
