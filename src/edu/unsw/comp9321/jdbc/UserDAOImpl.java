package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;

public class UserDAOImpl implements UserDAO {
	
	static Logger logger = Logger.getLogger(HotelOwnerDAOImpl.class.getName());
	private Connection connection;
	
	public UserDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");		
	}

	@Override
	public Boolean verifyLogin(UserDTO user) {
		
		// Info from login form
		String username = user.getUsername();
		String password = user.getPassword();
		String hotelLoc = user.getHoteLoc();
		
		
		
		// Get the hotel id corresponding to the hotel location
		int hotelIDCorrHotelLoc = 0;
		try{
			Statement stmnt = connection.createStatement();
			String query = "SELECT ID FROM HOTELS WHERE CITY = " + "'" + hotelLoc + "'";
			ResultSet res = stmnt.executeQuery(query);
			if (res.next()){
				hotelIDCorrHotelLoc = res.getInt(1);
			}			
			stmnt.close();
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		
		// Get password for entered username
		String passwordCorrUsername = "";
		try{
			Statement stmnt = connection.createStatement();
			String query = "SELECT PASSWORD FROM USERS WHERE USERNAME = " + "'" + username + "'";
			ResultSet res = stmnt.executeQuery(query);
			if (res.next()){
				passwordCorrUsername = res.getString(1);
			}			
			stmnt.close();
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		// Get hotelID for entered username
		int hotelIDdCorrUsername = 0;
		try{
			Statement stmnt = connection.createStatement();
			String query = "SELECT HOTEL_ID FROM USERS WHERE USERNAME = " + "'" + username + "'";
			ResultSet res = stmnt.executeQuery(query);
			if (res.next()){
				hotelIDdCorrUsername = res.getInt(1);
			}			
			stmnt.close();
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		System.out.println("username = " +username);
		System.out.println("password = " +password);
		System.out.println("hotelLoc = " +hotelLoc);
		
		
		System.out.println("passwordCorrUsername = " +passwordCorrUsername);
		System.out.println("hotelIDCorrHotelLoc = " +hotelIDCorrHotelLoc);
		System.out.println("hotelIDdCorrUsername = " +hotelIDdCorrUsername);
		
		// if the password entered corresponds to the user's password 
		// AND
		// the hotel branch trying to be accessed is indeed the hotel branch managed by the user
		// then return TRUE
		if (password.equals(passwordCorrUsername) && hotelIDCorrHotelLoc == hotelIDdCorrUsername){
			return true;
		}
		else 
			return false;
			
		
		
	}

	@Override
	public ArrayList<String> getHotelLoc() {
		ArrayList<String> hotelLoc = new ArrayList<String>();
		try{
		Statement stmnt = connection.createStatement();
		String query = "SELECT CITY FROM HOTELS";
		
		ResultSet res = stmnt.executeQuery(query);
		
		while (res.next()){
			hotelLoc.add(res.getString(1));			
		}
		
		stmnt.close();
 		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		
		return hotelLoc;
	}
	
	

}
