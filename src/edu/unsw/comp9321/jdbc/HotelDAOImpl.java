package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
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
			-> Compute the price hikes and discounts
			-> Determine results based on query (max spend)
		*/
		SearchResults results = null;
		ArrayList<ArrayList<RoomDTO>> options = null;
		ArrayList<RoomDTO> rooms = getAllRooms(query);
		ArrayList<BookingDTO> bookings = getHotelBookings(query);
		ArrayList<DiscountDTO> discounts =getHotelDiscounts(query);
					
		// TODO some function here that determines how many rooms
		// are actually available... possibly clear the amount
		// of rooms needed by first getting the discounts
		// and then removing all rooms that don't fit the financial
		// criteria of the query
		
		eliminateRoomsOnBookings(rooms,bookings);
		if (rooms.size() >= query.getNumRooms()) {				
			
			// Determine peak days
			DateCalculator dC = new DateCalculator();
			// create two sets of arrays, one for the days on peak, and the other for days with discounts.
			// This way, can iterate over them side by side.
			
			boolean[] on_peak = new boolean[365];
			dC.fillDaysOnPeak(on_peak, query.getCheck_in(), query.getCheck_out());
			if (on_peak[2]) {
				System.out.println("Jan03 is on peak");
			}
			
			int[] single_discount = new int[365];
			int[] twin_discount = new int[365];
			int[] queen_discount = new int[365];
			int[] exec_discount = new int[365];
			int[] suite_discount = new int[365];		
			 
			for (DiscountDTO disc : discounts) {
				if (disc.getRoom_type().contentEquals("Single")) {
					dC.fillDiscountDays(single_discount, disc);
				}
				else if (disc.getRoom_type().contentEquals("Twin")) {
					dC.fillDiscountDays(twin_discount, disc);
				}
				else if (disc.getRoom_type().contentEquals("Queen")) {
					dC.fillDiscountDays(queen_discount, disc);
				}
				else if (disc.getRoom_type().contentEquals("Executive")) {
					dC.fillDiscountDays(exec_discount, disc);
				}
				else if (disc.getRoom_type().contentEquals("Suite")) {
					dC.fillDiscountDays(suite_discount, disc);
				}			
			}
			
			double[] sTotal = new double[365];
			Arrays.fill(sTotal, 70.00);
			calculateTotals(on_peak,single_discount,sTotal);
			
			
			
			
			double[] tTotal = new double[365];
			Arrays.fill(tTotal, 120.00);
			calculateTotals(on_peak,twin_discount,tTotal);
			
			System.out.println("tTotal for Jul03 = $" + tTotal[184]);
			System.out.println("tTotal for Jul02 = $" + tTotal[183]);
			System.out.println("tTotal for Jul01 = $" + tTotal[182]);
			System.out.println("tDiscount for Jul03 = $" + twin_discount[184]);
			System.out.println("tDiscount for Jul02 = $" + twin_discount[183]);
			System.out.println("tDiscount for Jul01 = $" + twin_discount[182]);
			
			double[] qTotal = new double[365];
			Arrays.fill(qTotal, 120.00);
			calculateTotals(on_peak,queen_discount,qTotal);
			
			double[] eTotal = new double[365];
			Arrays.fill(eTotal, 180.00);
			calculateTotals(on_peak,exec_discount,eTotal);
			
			double[] suiteTotal = new double[365];
			Arrays.fill(suiteTotal, 300.00);
			calculateTotals(on_peak,suite_discount,suiteTotal);
			
			HashMap<String,double[]> totals = new HashMap<String,double[]>(8);
			totals.put("Single", sTotal);
			totals.put("Twin", tTotal);
			totals.put("Queen",qTotal);
			totals.put("Executive",eTotal);
			totals.put("Suite",suiteTotal);
			
			// Now to determine what rooms meet the query criteria
			
			// Map number of rooms available of each type
			HashMap<String,Integer> roomsAvail = new HashMap<String,Integer>(8);
			roomsAvail.put("Single", 0);
			roomsAvail.put("Twin", 0);
			roomsAvail.put("Queen", 0);
			roomsAvail.put("Executive", 0);
			roomsAvail.put("Suite", 0);
			for(RoomDTO r : rooms) {
				roomsAvail.put(r.getSize(), roomsAvail.get(r.getSize()) + 1);
			}
			
			HashMap<String,Boolean> meetCriteria = new HashMap<String,Boolean>(8);
			meetCriteria.put("Single", false);
			meetCriteria.put("Twin", false);
			meetCriteria.put("Queen", false);
			meetCriteria.put("Executive", false);
			meetCriteria.put("Suite", false);
			
			HashMap<String,Double> highestPrice = new HashMap<String,Double>(8);
			highestPrice.put("Single", 0.0);
			highestPrice.put("Twin", 0.0);
			highestPrice.put("Queen", 0.0);
			highestPrice.put("Executive", 0.0);
			highestPrice.put("Suite", 0.0);
			
			// check against financial constraint
			for (String key : roomsAvail.keySet()) {
				if(roomsAvail.get(key) > 0) {
					double highest = dC.findHighestPrice(totals.get(key), query.getCheck_in(), query.getCheck_out());
					System.out.println("Highest price for " + key + " is: " + highest);
					System.out.println("Rooms available as " + key + " is: " + roomsAvail.get(key));
					highestPrice.put(key,highest);
					if (highest <= query.getMaxPrice()) {
						meetCriteria.put(key, true);
						System.out.println("Room " + key + " is a match" );
					}
				}
			}
			
			eliminateRoomsOnPrice(rooms,meetCriteria);
			eliminateRoomsOnQuantity(rooms,query.getNumRooms());
			for(RoomDTO r : rooms) {
				r.setPrice(highestPrice.get(r.getSize()));
			}
			System.out.println("Number of rooms for search = " + rooms.size());
			
			options = new ArrayList<ArrayList<RoomDTO>>();
			//TODO THIS NEEDS WORK!!! It will not give all combinations
			for(int i = 0; i <= rooms.size() - query.getNumRooms(); i++) {
				ArrayList<RoomDTO> combo = new ArrayList<RoomDTO>();
				for(int j = 0; j < query.getNumRooms(); j++) {
					combo.add(rooms.get(i + j));
				}
				options.add(combo);
			}
			// now to check the combinations against the quantity of rooms requested
			// PERMUTATIONS WITHOUT REPETITION, should be n! results (rooms.size() factorial).
			

			results = new SearchResults();
			results.setResults(options);
			results.setPrices(highestPrice);
//			options.setResults(comb(rooms));
			for(int i = 0; i < options.size(); i++) {
				System.out.println("Option " + i);
				for(int j = 0; j < options.get(i).size();j++) {
					RoomDTO r = options.get(i).get(j);
					System.out.println(r.getSize() + " ");
				}
			}
			
		}
		
		return results;
	}
	
	private void eliminateRoomsOnQuantity(ArrayList<RoomDTO> rooms, int quantity) {
		int single = 0,twin = 0,queen = 0,exec = 0,suite = 0;
		
		for(int i = rooms.size() - 1; i >= 0; i--) {
			if (rooms.get(i).getSize().contentEquals("Single")) {
				single++;
				if (single > quantity) rooms.remove(i);				
			}
			else if (rooms.get(i).getSize().contentEquals("Twin")) {
				twin++;
				if (twin > quantity) rooms.remove(i);				
			}
			else if (rooms.get(i).getSize().contentEquals("Queen")) {
				queen++;
				if (queen > quantity) rooms.remove(i);				
			}
			else if (rooms.get(i).getSize().contentEquals("Executive")) {
				exec++;
				if (exec > quantity) rooms.remove(i);				
			}
			else if (rooms.get(i).getSize().contentEquals("Suite")) {
				suite++;
				if (suite > quantity) rooms.remove(i);				
			}
		}
	}
	
	/**
	 * Eliminate rooms that do not meet the max price per night constraint
	 * @param rooms
	 * @param meetCriteria
	 */
	private void eliminateRoomsOnPrice (ArrayList<RoomDTO> rooms, HashMap<String,Boolean> meetCriteria) {
		int i = rooms.size() - 1;
		while (i >= 0) {
			if (! meetCriteria.get(rooms.get(i).getSize())) {
				rooms.remove(i);
			}
//			else {
				i--;
//			}
		}		
	}
	
	

     
   
	
	
	/**
	 * Get the total price for a room over 365 days (starting from query check in date).
	 * Apply any price hikes and discounts.
	 * @param basePrice
	 * @param peak
	 * @param discount
	 * @param total
	 */
	private void calculateTotals (boolean[] peak, int[] discount, double[] total) {
		for(int i = 0; i < total.length;i++) {
			// add the peak hike of 40% 
			if (peak[i]) {
				total[i] += (total[i] *  0.4);
//				System.out.print("Peak price of $" + total[i]);
			}
			// then apply discount
			if (discount[i] > 0) {
				total[i] = total[i] * (((double)discount[i] )/ 100);
				System.out.print("Discount applied, price is now $" + total[i]);
			}
		}
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
				
				j--;
				
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
			allRoomRes.close();
			roomQuery.close();
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
//			String sqlQuery = "SELECT * FROM Discounts d "		
//								+ "JOIN Hotels h ON d.hotel = h.id "
//								+ "WHERE h.city = ? "
//									+ "AND (d.start_date BETWEEN DATE(?) AND DATE(?) "
//										+ "OR d.end_date BETWEEN DATE(?) AND DATE(?))";		
			
			String sqlQuery = "SELECT * FROM Discounts d "		
					+ "JOIN Hotels h ON d.hotel = h.id "
					+ "WHERE h.city = '" + query.getCity() + "' "
						+ "AND (d.start_date BETWEEN DATE('" + query.getCheck_in()+"') AND DATE('"+ query.getCheck_out() +"') "
							+ "OR d.end_date BETWEEN DATE('" + query.getCheck_in()+"') AND DATE('"+ query.getCheck_out() +"'))";
			Statement discQuery = connection.createStatement();
//			PreparedStatement discQuery = connection.prepareStatement(sqlQuery);
//			discQuery.setString(1, query.getCity());
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(df.parse(query.getCheck_in()));
//			// d.start_date BETWEEN (start) AND (end)
//			java.sql.Date ci = new java.sql.Date(cal.getTime().getTime());
//			discQuery.setDate(2,ci);
//			
//			cal.clear();
//			cal.setTime(df.parse(query.getCheck_out()));
////			cal.add(Calendar.MONTH, -1);
//			java.sql.Date co = new java.sql.Date(cal.getTime().getTime());
//			discQuery.setDate(3,co);
//			// d.end_date BETWEEN (start) AND (end)
//			discQuery.setDate(4,ci);
//			discQuery.setDate(5,co);
//			ResultSet discRes = discQuery.executeQuery();
			ResultSet discRes = discQuery.executeQuery(sqlQuery);
			while(discRes.next()) {
				DiscountDTO d = new DiscountDTO();
//				d.setStart_date(discRes.getDate("start_date"));
				d.setStart(discRes.getString("start_date"));
				System.out.print("discount start = " + d.getStart());
//				d.setEnd_date(discRes.getDate("end_date"));
				d.setEnd(discRes.getString("end_date"));
				System.out.print("discount end = " + d.getEnd());
				d.setHotel(discRes.getInt("hotel"));
				d.setId(discRes.getInt("discount_id"));				
				d.setRoom_type(discRes.getString("room_type"));
				d.setDiscount(discRes.getInt("discount"));
				System.out.print("discount amount = " + d.getDiscount());
				discounts.add(d);				
			}
			discRes.close();
			discQuery.close();
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
								+ "WHERE h.city = '" + query.getCity() + "' "
								+ "AND (d.start_date BETWEEN DATE('" + query.getCheck_in()+"') AND DATE('"+ query.getCheck_out() +"') "
									+ "OR d.end_date BETWEEN DATE('" + query.getCheck_in()+"') AND DATE('"+ query.getCheck_out() +"'))";
//			System.out.println(query.getCheck_in());
//			System.out.println(query.getCheck_out());
			Statement bookingQuery = connection.createStatement();
			
			ResultSet bookingRes = bookingQuery.executeQuery(sqlQuery);
			while(bookingRes.next()) {
				BookingDTO b = new BookingDTO();
				b.setCheckin(bookingRes.getString("check_in"));
				b.setCheckout(bookingRes.getString("check_out"));
				b.setHotel(bookingRes.getInt("hotel"));
				b.setId(bookingRes.getInt("booking_id"));
				b.setPin(bookingRes.getInt("pin"));
				b.setUrl(bookingRes.getString("url"));
				b.setExtra_bed(bookingRes.getBoolean("extra_bed"));
				b.setQuantity(bookingRes.getInt("quantity"));
				b.setSize(bookingRes.getString("size"));
				bookings.add(b);				
			}
			bookingRes.close();
			bookingQuery.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		return bookings;
	}
	
	@Override
	public ArrayList<String> getCities() {
		ArrayList<String> cities = new ArrayList<String>();
		try {
			String query = "SELECT CITY FROM HOTELS";
			PreparedStatement s = connection.prepareStatement(query);
			ResultSet r = s.executeQuery();
			while(r.next()) {
				cities.add(r.getString("city"));
			}
			s.close();
			r.close();
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return cities;
	}
	

}
