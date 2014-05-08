package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	public ArrayList<ArrayList<RoomDTO>> customerRoomSearch(VacancyQueryDTO query) {
		
		/** What to get from the database:
			-> List of Rooms from hotel that are free
			-> Get list of bookings for appropriate hotel, subtract from the total rooms
				available
			-> Get any discounts that may be active during this time
			-> Determine on or off peak
			-> Compute the price hikes and discounts
			-> Determine results based on query (max spend)
		*/
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
			dC.fillDaysOnPeak(on_peak, query.getCheckIn(), query.getCheckOut());
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
			
			// check against financial constraint
			for (String key : roomsAvail.keySet()) {
				if(roomsAvail.get(key) > 0) {
					double highest = dC.findHighestPrice(totals.get(key), query.getCheckIn(), query.getCheckOut());
					if (highest <= query.getMaxPrice()) {
						meetCriteria.put(key, true);
					}
				}
			}
			
			eliminateRoomsOnPrice(rooms,meetCriteria);
			
			// now to check the combinations against the quantity of rooms requested
			// PERMUTATIONS WITHOUT REPETITION, should be n! results (rooms.size() factorial).
			
			options = comb(rooms);
//			SearchResults options = new SearchResults();
//			options.setResults(comb(rooms));
			for(int i = 0; i < options.size(); i++) {
				System.out.println("Option " + i);
				for(int j = 0; j < options.get(i).size();j++) {
					RoomDTO r = options.get(i).get(j);
					System.out.println(r.getSize() + " ");
				}
			}
			
		}
		
		return options;
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
			else {
				i--;
			}
		}		
	}
	
	
	public static ArrayList<ArrayList<RoomDTO>> comb(ArrayList<RoomDTO> in)
    {
        ArrayList<ArrayList<RoomDTO>> out = new ArrayList<ArrayList<RoomDTO>>();
         
        for (int i = 0; i < Math.pow(2, in.size()); i ++)
        {
            ArrayList<RoomDTO> thing = new ArrayList<RoomDTO>();
             
            String t = Integer.toBinaryString(i);
             
            String zeroes = new String();
            for (int o = 0; o < in.size(); o ++)
            {
                zeroes = zeroes + "0";
            }
             
            DecimalFormat df = new DecimalFormat(zeroes);
             
            String s = df.format(Integer.parseInt(t));
             
            for (int j = 0; j < s.length(); j ++)
            {
                if (s.charAt(j) == new String("1").charAt(0))
                {
                    thing.add(in.get(j));                    
                }
                else
                {
                     
                }
            }
             
            out.add(thing);
        }
         
//        for (int i = 0; i < out.size(); i ++)
//        {
//            ArrayList<ArrayList<RoomDTO>> permsOfOne = permutations(out.get(i));
//             
//            for (int j = 0; j <permsOfOne.size(); j ++)
//            {
//                for (int d = 0; d <permsOfOne.get(j).size(); d ++)
//                {
//                    System.out.print(permsOfOne.get(j).get(d).getName() + ", ");
//                }
//                 
//                Strategy coolStrat = new Strategy(permsOfOne.get(j));
//                System.out.print("Sensitivity is:" + coolStrat.getSens() + " Specificity is:" + coolStrat.getSpec() + " Cost is: " + coolStrat.getCost());
//                 
//                System.out.println("END");
//            }
//        }
         
        return out;
    }
     
    public static ArrayList<ArrayList<RoomDTO>> permutations(ArrayList<RoomDTO> list)
    {
        return permutations(null, list, null);
    }
     
    public static ArrayList<ArrayList<RoomDTO>> permutations(ArrayList<RoomDTO> prefix, ArrayList<RoomDTO> suffix, ArrayList<ArrayList<RoomDTO>> output)
    {
        if(prefix == null)
            prefix = new ArrayList<RoomDTO>();
        if(output == null)
            output = new ArrayList<ArrayList<RoomDTO>>();
         
        if(suffix.size() == 1)
        {
            ArrayList<RoomDTO> newElement = new ArrayList<RoomDTO>(prefix);
            newElement.addAll(suffix);
            output.add(newElement);
            return output;
        }
         
        for(int i = 0; i < suffix.size(); i++)
        {
            ArrayList<RoomDTO> newPrefix = new ArrayList<RoomDTO>(prefix);
            newPrefix.add(suffix.get(i));
            ArrayList<RoomDTO> newSuffix = new ArrayList<RoomDTO>(suffix);
            newSuffix.remove(i);
            permutations(newPrefix,newSuffix,output);
        }
         
        return output;
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
			}
			// then apply discount
			if (discount[i] > 0) {
				total[i] = total[i] * (discount[i] / 100);
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
			String sqlQuery = "SELECT * FROM Discounts d "		
								+ "JOIN Hotels h ON d.hotel = h.id "
								+ "WHERE h.city = ? "
									+ "AND d.start_date >= ? "
									+ "AND d.end_date < ?";			
			PreparedStatement discQuery = connection.prepareStatement(sqlQuery);
			discQuery.setString(1, query.getCity());
			discQuery.setString(2, query.convertCheckInToString());
			discQuery.setString(3, query.convertCheckOutToString());
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
								+ "WHERE h.city = ? "
									+ "AND b.check_in >= ? "
									+ "AND b.check_out < ?";
			PreparedStatement bookingQuery = connection.prepareStatement(sqlQuery);
			bookingQuery.setString(1, query.getCity());
			bookingQuery.setString(2, query.convertCheckInToString());
			bookingQuery.setString(3, query.convertCheckOutToString());
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
