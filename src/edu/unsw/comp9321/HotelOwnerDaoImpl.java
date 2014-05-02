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
			Statement stmnt = connection.createStatement();
			
			int noHotels = 0;
			String HotelLoc = "";
			int noAvail = 0;
			int noOccupied = 0;
			
			// Retrieve number of hotels in database
			String query_NoHotels = "SELECT MAX(ID) FROM HOTELS";	
		
			ResultSet res = stmnt.executeQuery(query_NoHotels);
			logger.info("The result set size is "+res.getFetchSize());
			if (res.next()){
				noHotels = res.getInt(1);
			}
			
			for (int hotelID = 1; hotelID <= noHotels; ++hotelID){ 						
				// get 'Occupied' and 'Available' amounts for each type of Room ('Single', 'Twin', 'Queen', 'Executive' and 'Suite')			
				
				// Get Hotel Location
				HotelLoc = getHotelLoc(hotelID);					
				System.out.println("Hotel Loc: " + HotelLoc);
				/*******************************************  SINGLE ROOMS  *****************************************************/
				
				// Get 'Occupied' 'Single' Room amount
				String query_SingleOccupied = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Single' AND AVAILABILITY = 'Occupied'" ;
				res = stmnt.executeQuery(query_SingleOccupied);
				if (res.next()){
					noOccupied = res.getInt(1);					
					System.out.println("there are " + noOccupied + "single occupied rooms");
				}
			
				// Get 'Available' 'Single' Room amount 
				String query_SingleAvailable = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Single' AND AVAILABILITY = 'Available'" ;
				res = stmnt.executeQuery(query_SingleAvailable);
				if (res.next()){
					noAvail = res.getInt(1);					
					System.out.println("there are " + noAvail + "single available rooms");
				}
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Single", noOccupied, noAvail));
				
			
			
				/***********************************************************************************************************************/
			
				/****************************************************  TWIN ROOMS  *****************************************************/
			
				// Get 'Occupied' 'Twin' Room amount
				String query_TwinOccupied = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Twin' AND AVAILABILITY = 'Occupied'" ;
				res = stmnt.executeQuery(query_TwinOccupied);
				if (res.next()){
					noOccupied = res.getInt(1);					
					System.out.println("there are " + noOccupied + "twin occupied rooms");
				}
				
				// Get 'Available' 'Twin' Room amount
				String query_TwinAvailable = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Twin' AND AVAILABILITY = 'Available'" ;
				res = stmnt.executeQuery(query_TwinAvailable);
				if (res.next()){
					noAvail = res.getInt(1);					
					System.out.println("there are " + noAvail + "twin available rooms");
				}
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Twin", noOccupied, noAvail));
			
				/***********************************************************************************************************************/
				
				/****************************************************  QUEEN ROOMS  *****************************************************/
				// Get 'Occupied' 'Queen' Room amount
				String query_QueenOccupied = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Queen' AND AVAILABILITY = 'Occupied'" ;
				res = stmnt.executeQuery(query_QueenOccupied);
				if (res.next()){
					noOccupied = res.getInt(1);					
					System.out.println("there are " + noOccupied + "Queen occupied rooms");
				}
				
				// Get 'Available' 'Queen' Room amount
				String query_QueenAvailable = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Queen' AND AVAILABILITY = 'Available'" ;
				res = stmnt.executeQuery(query_QueenAvailable);
				if (res.next()){
					noAvail = res.getInt(1);					
					System.out.println("there are " + noAvail + "Queen available rooms");
				}
				
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Queen", noOccupied, noAvail));
				/***********************************************************************************************************************/
				
				/***********************************************  EXECUTIVE ROOMS  *****************************************************/
				// Get 'Occupied' 'Executive' Room amount
				String query_ExecutiveOccupied = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Executive' AND AVAILABILITY = 'Occupied'" ;
				res = stmnt.executeQuery(query_ExecutiveOccupied);
				if (res.next()){
					noOccupied = res.getInt(1);					
					System.out.println("there are " + noOccupied + "Executive occupied rooms");
				}
				
				// Get 'Available' 'Executive' Room amount
				String query_ExecutiveAvailable = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Executive' AND AVAILABILITY = 'Available'" ;
				res = stmnt.executeQuery(query_ExecutiveAvailable);
				if (res.next()){
					noAvail = res.getInt(1);					
					System.out.println("there are " + noAvail + "Executive available rooms");
				}
				
				hotelOccupancyInfo.add(new HotelOccupancyDTO(hotelID, HotelLoc, "Executive", noOccupied, noAvail));
				/***********************************************************************************************************************/
				
				/***************************************************  SUITE ROOMS  *****************************************************/
				// Get 'Occupied' 'Suite' Room amount
				String query_SuiteOccupied = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Suite' AND AVAILABILITY = 'Occupied'" ;
				res = stmnt.executeQuery(query_SuiteOccupied);
				if (res.next()){
					noOccupied = res.getInt(1);					
					System.out.println("there are " + noOccupied + "Suite occupied rooms");
				}
				
				// Get 'Available' 'Suite' Room amount
				String query_SuiteAvailable = "SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = " + hotelID + " AND SIZE = 'Suite' AND AVAILABILITY = 'Available'" ;
				res = stmnt.executeQuery(query_SuiteAvailable);
				if (res.next()){
					noAvail = res.getInt(1);					
					System.out.println("there are " + noAvail + "Suite available rooms");
				}
				
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
				res = stmnt.executeQuery(query_HotelLoc);
				if (res.next()){
					hotelLoc = res.getString(1);	
				}
			}catch(Exception e){
				System.out.println("Caught Exception");
				e.printStackTrace();
			}
			
			return hotelLoc;
			
		}
		

}
