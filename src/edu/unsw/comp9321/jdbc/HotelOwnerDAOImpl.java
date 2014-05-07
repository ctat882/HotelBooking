package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;

public class HotelOwnerDAOImpl implements HotelOwnerDAO{
	
	
	static Logger logger = Logger.getLogger(HotelOwnerDAOImpl.class.getName());
	private Connection connection;
	
	public HotelOwnerDAOImpl() throws ServiceLocatorException, SQLException{
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
