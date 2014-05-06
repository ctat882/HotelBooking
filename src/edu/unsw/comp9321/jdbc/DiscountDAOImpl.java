package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;

public class DiscountDAOImpl implements DiscountDAO {
	
	static Logger logger = Logger.getLogger(DiscountDAOImpl.class.getName());
	private Connection connection;
	
	public DiscountDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");		
	}

	@Override
	public Boolean setDiscount(ArrayList<DiscountDTO> discountInfoFull) {
		
		ArrayList<DiscountDTO> discount = discountInfoFull;
		Boolean discountNotAdded = false;
		
		for (int i = 0; i < discount.size(); ++i){
			int hotelID = discount.get(i).getHotelID();
			String roomType = "'" + discount.get(i).getRoomType() + "'";
			
			int startYear = discount.get(i).getStartDateYear();
			int startMonth = discount.get(i).getStartDateMonth();
			int startDay = discount.get(i).getStartDateDay();
			String startDate = "'" + startYear + "-" + startMonth + "-" + startDay + "'";
			
			int endYear = discount.get(i).getEndDateYear();
			int endMonth = discount.get(i).getEndDateMonth();
			int endDay = discount.get(i).getEndDateDay();
			String endDate = "'" + endYear + "-" + endMonth + "-" + endDay + "'";
			
			Double discountPercent = discount.get(i).getDiscount();
			
			// Add discount info to the database
			try{
				Statement stmnt = connection.createStatement();
				String query = "INSERT INTO DISCOUNTS (hotel, room_type, start_date, end_date, discount) VALUES (" + hotelID +"," + 
														roomType + "," + startDate + "," + endDate + "," + discountPercent +")";
				stmnt.executeUpdate(query);
				
			}catch(Exception e){					
				System.out.println("Caught Exception");
				e.printStackTrace();
				discountNotAdded = true;
				
				
			}
		}
		
		// if at least one discount was not added, then return false
		if (discountNotAdded)
			return false;
		else
			return true;
		
		
		
		
	}
	
	

}
