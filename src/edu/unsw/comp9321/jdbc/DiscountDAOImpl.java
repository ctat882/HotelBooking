package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	public Boolean setDiscount(ArrayList<DiscountDTOGiri> discountInfoFull) {
		
		ArrayList<DiscountDTOGiri> discount = discountInfoFull;
		
		
		
		// *******   Check whether the discount overlaps with any other discount for the same Room type in the database  *******
		
		// 0) Cycle through all discounts to be added
		
		for (int i = 0; i < discount.size(); ++i){
			int hotelID = discount.get(i).getHotelID();			
			String roomType = discount.get(i).getRoomType();
			
			int startYear = discount.get(i).getStartDateYear();
			int startMonth = discount.get(i).getStartDateMonth();
			int startDay = discount.get(i).getStartDateDay();
					
			int endYear = discount.get(i).getEndDateYear();
			int endMonth = discount.get(i).getEndDateMonth();
			int endDay = discount.get(i).getEndDateDay();
			
			
			// TODO: FINISH
			
			// 1) Get all discounts for that room type and hotel already in the database			
			try{
				Statement stmnt = connection.createStatement();
				String query = "SELECT * FROM DISCOUNTS WHERE ROOM_TYPE = " + "'" + roomType + "'" + " AND HOTEL = " + hotelID;
				ResultSet res = stmnt.executeQuery(query);
				
				while (res.next()){
					// Store the Start/End Dates of the bookings in the database				
					Date dbBookingStartDate = res.getDate(4);
					Date dbBookingEndDate = res.getDate(5);
					int dbBookingHotelID = res.getInt(2);
					String dbBookingRoomType = res.getString(3);
					
					
					// CHANGED to getDateNew
					
					// Convert date of booking to be inserted
					Date bookingStartDate = getDate(startDay,startMonth,startYear);
					Date bookingEndDate = getDate(endDay, endMonth, endYear);  
					
					// 2) Compare with current booking
					System.out.println("dbBookingHotelID = " + dbBookingHotelID);
					System.out.println("hotelID = " + hotelID);
					
					
					System.out.println("roomType = " + roomType);
					System.out.println("dbBookingRoomType = " + dbBookingRoomType);
					
					// If Hotel ID's are identical
					if (hotelID == dbBookingHotelID){
						// If Room Types are identical
						if (roomType.equals(dbBookingRoomType)){
							
							System.out.println("bookingStartDate = " + bookingStartDate);
							System.out.println("bookingEndDate = " + bookingEndDate);
							System.out.println("dbBookingStartDate = " + dbBookingStartDate);
							System.out.println("dbBookingEndDate = " + dbBookingEndDate);
							
							SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
							if (f.format(bookingStartDate).equals(f.format(dbBookingStartDate))) {
								System.out.println("1");
								return true;
							}
							else if(f.format(bookingStartDate).equals(f.format(dbBookingEndDate))){
								System.out.println("2");
								return true;
							}
							else if(f.format(bookingEndDate).equals(f.format(dbBookingStartDate))){
								System.out.println("3");
								return true;
							}
							else if(f.format(bookingEndDate).equals(f.format(dbBookingEndDate))){
								System.out.println("4");
								return true;
							}							
							// If a booking for the same RoomType in the same Hotel collide, set Error Boolean
							else if ((bookingStartDate.after(dbBookingStartDate) && bookingStartDate.before(dbBookingEndDate))){
								System.out.println("5");
								return true;
							}
							else if (bookingEndDate.after(dbBookingStartDate) && bookingEndDate.before(dbBookingEndDate)){
								System.out.println("6");
								return true;
							}
						}
					}
				}
				
				
				
			}catch(Exception e){					
				System.out.println("Caught Exception");
				e.printStackTrace();
							
			}
		}
		
		
		
		// As all discount have passed the above tests, we can add them to the Discount table in the database		
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
							
			}
		}	
		
		return false;
	}
	
	
	// Change Integer to Date
	private Date getDate (int day, int month, int year){
		Calendar calendar = Calendar.getInstance();
		calendar.setLenient(false);
		calendar.set(year, month-1, day);	
		Date date = calendar.getTime();
		return date;
	}
	

}


