package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

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
		
		
		return null;
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
