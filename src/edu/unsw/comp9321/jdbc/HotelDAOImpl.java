package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import edu.unsw.comp9321.logic.DateCalculator;





import edu.unsw.comp9321.common.ServiceLocatorException;

public class HotelDAOImpl implements HotelDAO{
	static Logger logger = Logger.getLogger(HotelDAOImpl.class.getName());
	private Connection connection;
	
	public HotelDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
	}
	@Override
	public ArrayList<VacancyQueryDTO> findVacantRooms(VacancyQueryDTO query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeBooking(Object booking) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchResults customerRoomSearch(VacancyQueryDTO query) {
		
		/** What to get from the database:
			-> List of Rooms from hotel that are free
			-> Get list of bookings for appropriate hotel, subtract from the total rooms
				available
			-> Get any discounts that may be active during this time
			-> Determine on or off peak
			-> Compute the discounts
		*/
		
		ArrayList<RoomDTO> rooms = getAllRooms(query);
		ArrayList<BookingDTO> bookings = getHotelBookings(query);
		ArrayList<DiscountDTO> discounts =getHotelDiscounts(query);
					
		// TODO some function here that determines how many rooms
		// are actually available... possibly clear the amount
		// of rooms needed by first getting the discounts
		// and then removing all rooms that don't fit the financial
		// criteria of the query
		
		eliminateRoomsOnBookings(rooms,bookings);
		// Determine peak days
		DateCalculator dC = new DateCalculator();
		
		int daysOnPeak = dC.numDaysOnPeak(query.getCheckIn(), query.getCheckOut());
		
		
		// create two arrays, one for the days on peak, and the other for days with discounts.
		// This way, can iterate over them side by side.
		
		Boolean[] on_peak = new Boolean[365];
		dC.fillDaysOnPeak(on_peak, query.getCheckIn(), query.getCheckOut());
		int[] singles_discount = new int[365];
		int[] twin_discount = new int[365];
		int[] queen_discount = new int[365];
		int[] exec_discount = new int[365];
		int[] suite_discount = new int[365];
		
		 
		for (DiscountDTO disc : discounts) {
			if (disc.getRoom_type().contentEquals("Single")) {
				
			}
			else if (disc.getRoom_type().contentEquals("Twin")) {
				
			}
			else if (disc.getRoom_type().contentEquals("Queen")) {
				
			}
			else if (disc.getRoom_type().contentEquals("Executive")) {
				
			}
			else if (disc.getRoom_type().contentEquals("Suite")) {
				
			}			
		}
		
		return null;
	}
	
	/**
	 * Assumes that the booking is less than 365 days
	 * @param query
	 * @return
	 */
	private int numDaysOnPeak (VacancyQueryDTO query) {
		/*Peak periods are 
		 * Dec. 15th - Feb 15th, 
		 * March 25th - April 14th, 
		 * July 1st - July 20th
		 * Sept. 20th - Oct 10th.		
		*/
		
		/** DAYS OF THE YEAR */
		final int jan01 = 1;
		final int feb15 = 46;		
		final int mar25 = 84;
		final int apr14 = 105;		
		final int jul01 = 183;
		final int jul20 = 202;		
		final int sep20 = 264;
		final int oct10 = 283;		
		final int dec15 = 349;
		final int dec31 = 365;
		
		// Get Query dates
		Calendar check_in = Calendar.getInstance();
		check_in.setTime(query.getCheckIn());		
		Calendar check_out = Calendar.getInstance();
		check_out.setTime(query.getCheckOut());
		// Check if leap year
		GregorianCalendar greg = new GregorianCalendar();
		// If leap year subtract one day from Day Of Year
		int checkInDOY = check_in.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(check_in.get(Calendar.YEAR))) checkInDOY--;		
		int checkOutDOY = check_out.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(check_out.get(Calendar.YEAR))) checkOutDOY--;		
		int daysInPeak = 0;		
		// Now determine if check in and check out fall within range of peak periods
		// TODO probably have to add +1 here		
		if ( checkInDOY >= jan01 && checkInDOY <= feb15  ) {
			if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += (feb15 - checkInDOY) + (checkOutDOY -mar25);
			}
			else if (checkOutDOY >= jul01 && checkOutDOY <= jul20) {
				daysInPeak += (feb15 - checkInDOY) + (apr14 - mar25) 
						+ (checkOutDOY - jul01);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += (feb15 - checkInDOY) + (apr14 - mar25) + (jul20 - jul01) 
						+ (checkOutDOY - sep20);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += (feb15 - checkInDOY) + (apr14 - mar25) + (jul20 - jul01) 
						+ (oct10 - sep20) + (checkOutDOY - dec15);
			}
		}
		else if (checkInDOY >= mar25 && checkInDOY <= apr14 ) {
			if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= jul01 && checkOutDOY <= jul20) {
				daysInPeak += ( apr14 - checkInDOY) + (checkOutDOY - jul01);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += ( apr14 - checkInDOY) + (jul20 - jul01) 
						+ (checkOutDOY - sep20);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += ( apr14 - checkInDOY) + (jul20 - jul01)
						+ (oct10 - sep20) + (checkOutDOY - dec15);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( apr14 - checkInDOY) + (jul20 - jul01) + (oct10 - sep20) 
						+ (dec31 - dec15) + (checkOutDOY - jan01);
			}
			
		}
		else if (checkInDOY >= jul01 && checkInDOY <= jul20) {
			if (checkOutDOY >= jul01 && checkInDOY <= jul20) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += ( jul20 - checkInDOY) + (checkOutDOY - sep20);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += ( jul20 - checkInDOY) + (oct10 - sep20) + (checkOutDOY - dec15);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( jul20 - checkInDOY) + (oct10 - sep20) 
						+ (dec31 - dec15) + (checkOutDOY - jan01);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += ( jul20 - checkInDOY) + (oct10 - sep20) 
						+ (dec31 - dec15) + (feb15 - jan01) + (checkOutDOY - mar25);
			}
			
		}
		else if (checkInDOY >= sep20 && checkInDOY <= oct10) {			
			if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += ( oct10 - checkInDOY) + (checkOutDOY - dec15);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( oct10 - checkInDOY)	+ (dec31 - dec15) + (checkOutDOY - jan01);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += ( oct10 - checkInDOY)	+ (dec31 - dec15) 
						+ (feb15 - jan01) + (checkOutDOY - mar25);
			}
			else if (checkOutDOY >= jul01 && checkInDOY <= jul20) {
				daysInPeak += ( oct10 - checkInDOY)	+ (dec31 - dec15) 
						+ (feb15 - jan01) + (apr14 - mar25) + (checkOutDOY - jul01);
			}
			
		}
		else if (checkInDOY >= dec15 && checkInDOY <= dec31) {
			if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( dec31 - checkInDOY) + (checkOutDOY - jan01);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += ( dec31 - checkInDOY)	+ (feb15 - jan01) + (checkOutDOY - mar25);
			}
			else if (checkOutDOY >= jul01 && checkInDOY <= jul20) {
				daysInPeak += ( dec31 - checkInDOY)	+ (feb15 - jan01) 
						+ (apr14 - mar25) + (checkOutDOY - jul01);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += ( dec31 - checkInDOY)	+ (feb15 - jan01) 
						+ (apr14 - mar25) + (jul20 - jul01) + (checkOutDOY - sep20);
			}
		}		
		return daysInPeak;		
	}
	
	private void eliminateRoomsOnBookings (ArrayList<RoomDTO> rooms, ArrayList<BookingDTO> bookings) {
		boolean allDeleted = false;
		int numRooms = 0;
		for(int i = 0; i < bookings.size(); i++) {
			numRooms = bookings.get(i).getQuantity();
			int deleted = 0;
			int j = rooms.size() - 1;
			while(! allDeleted) {				
				if(rooms.get(j).getSize().contentEquals(bookings.get(j).getSize())) {
					rooms.remove(j);
					deleted++;
					if (deleted == numRooms) {
						allDeleted = true;
					}
				}
				else {
					j--;
				}
			}
			allDeleted = false;
		}
	}
	
	/**
	 * Get all the rooms from a Hotel.
	 * @param query
	 * @return
	 */
	public ArrayList<RoomDTO> getAllRooms (VacancyQueryDTO query) {
		ArrayList<RoomDTO> rooms = new ArrayList<RoomDTO>();
		String getRoomsQuery = "SELECT * FROM Rooms r "
				+ "JOIN Hotels h on r.hotel = h.id "
				+ "WHERE h.city = ?"; 
		try {
			PreparedStatement roomQuery = connection.prepareStatement(getRoomsQuery);
			roomQuery.setString(1, query.getCity());
			ResultSet allRoomRes = roomQuery.executeQuery();	
			while(allRoomRes.next()) {
				RoomDTO r = new RoomDTO();
				r.setAvailability(allRoomRes.getString("availability"));
				r.setPrice(allRoomRes.getDouble("price"));
				r.setRoom_num(allRoomRes.getInt("room_num"));
				r.setSize(allRoomRes.getString("size"));
				rooms.add(r);
			}
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return rooms;
	}
	
	/**
	 * Get all the discounts for a Hotel within the given dates.
	 * @param query
	 * @return
	 */
	public ArrayList<DiscountDTO> getHotelDiscounts (VacancyQueryDTO query) {
		ArrayList<DiscountDTO> discounts = new ArrayList<DiscountDTO>();
		try {
			String sqlQuery = "SELECT * FROM Discounts d "		
								+ "JOIN Hotels h ON d.hotel = h.id "
								+ "WHERE h.city = ? "
									+ "AND d.start_date >= ? "
									+ "AND d.end_date < ?";			
			PreparedStatement discQuery = connection.prepareStatement(sqlQuery);
			discQuery.setString(1, query.getCity());
			discQuery.setDate(2, query.getCheckIn());
			discQuery.setDate(3, query.getCheckOut());
			ResultSet discRes = discQuery.executeQuery();
			while(discRes.next()) {
				DiscountDTO d = new DiscountDTO();
				d.setStart_date(discRes.getDate("start_date"));
				d.setEnd_date(discRes.getDate("end_date"));
				d.setHotel(discRes.getInt("hotel"));
				d.setId(discRes.getInt("discount_id"));				
				d.setRoom_type(discRes.getString("size"));
				d.setDiscount(discRes.getInt("discount"));				
				discounts.add(d);				
			}
		} catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return discounts;
	}
	
	/**
	 * Get all the bookings from a Hotel within the given dates.
	 * @param query
	 * @return
	 */
	public ArrayList<BookingDTO> getHotelBookings (VacancyQueryDTO query) {
		ArrayList<BookingDTO> bookings = new ArrayList<BookingDTO>();
		
		try {
			String sqlQuery = "SELECT * FROM Bookings b "
								+ "JOIN Hotels h ON b.hotel = h.id "
								+ "WHERE h.city = ? "
									+ "AND b.check_in >= ? "
									+ "AND b.check_out < ?";
			PreparedStatement bookingQuery = connection.prepareStatement(sqlQuery);
			bookingQuery.setString(1, query.getCity());
			bookingQuery.setDate(2, query.getCheckIn());
			bookingQuery.setDate(3, query.getCheckOut());
			ResultSet bookingRes = bookingQuery.executeQuery();
			while(bookingRes.next()) {
				BookingDTO b = new BookingDTO();
				b.setCheck_in(bookingRes.getDate("check_in"));
				b.setCheck_out(bookingRes.getDate("check_out"));
				b.setHotel(bookingRes.getInt("hotel"));
				b.setId(bookingRes.getInt("booking_id"));
				b.setPin(bookingRes.getInt("pin"));
				b.setUrl(bookingRes.getString("url"));
				b.setExtra_bed(bookingRes.getBoolean("extra_bed"));
				b.setQuantity(bookingRes.getInt("quantity"));
				b.setSize(bookingRes.getString("size"));
				bookings.add(b);				
			}
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		return bookings;
	}
	

}
