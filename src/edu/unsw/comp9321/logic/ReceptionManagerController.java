package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.BookingDAOImpl;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.HotelOccupancyDAOImpl;
import edu.unsw.comp9321.jdbc.UserDAOImpl;
import edu.unsw.comp9321.jdbc.UserDTO;

public class ReceptionManagerController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ReceptionManagerController.class.getName());
	
	private UserDAOImpl user;
	private BookingDAOImpl booking;
	private HotelOccupancyDAOImpl occupancy;
	
	public ReceptionManagerController() throws ServletException{
		super();
		try{
			user = new UserDAOImpl();
			booking = new BookingDAOImpl();
			occupancy = new HotelOccupancyDAOImpl();
		}catch(ServiceLocatorException e){
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		} catch (SQLException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		}	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		processRequest(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String forwardPage = "";
		String action = request.getParameter("action");
		System.out.println("action= " + action);
		
		// Go to Login Page
		if (action == null || action.equals("Logout") || action.equals("Back to Login")){
			//Get Hotel Locations for user to select in login form
			ArrayList<String> hotelLoc = occupancy.getHotelLocList();
			// Set hotelLoc attribute
			request.setAttribute("hotelLoc", hotelLoc);
			// Move to Authorization Page
			forwardPage = "/WEB-INF/ReceptionManagerAuth.jsp";	
		}
		else if (action.equals("Login")){
			
			HttpSession session = request.getSession();
			
			// Get Input from Login Form
			String username = request.getParameter("user");
			String password = request.getParameter("password");
			String hotelLoc = request.getParameter("hotelLoc");
			
			// Store this in UserDTO
			UserDTO userDTO = new UserDTO(username, password, hotelLoc);
			// Verify login details
			Boolean verified = user.verifyLogin(userDTO);
			
			// Store User Info as session attributes
			 session.setAttribute("username", username);
			 session.setAttribute("password", password);
			 session.setAttribute("hotelLoc", hotelLoc);
			 
			if (verified){
			
				// Get unassigned Bookings
				ArrayList<BookingDTO> unassignedBookings = booking.getBookings(false, userDTO);
				 // Set unassigned bookings
				 session.setAttribute("unassignedBookings", unassignedBookings);
				 
				 // Get assignedBookings
				 ArrayList<BookingDTO> assignedBookings = booking.getBookings(true, userDTO);
				 // Set assigned bookings
				 session.setAttribute("assignedBookings", assignedBookings);				
				 
				 // Set the hotel location string
				 // Set the hotelID that the user has access to
				 session.setAttribute("hotelLocation", hotelLoc);				 
				 session.setAttribute("userHotelId", user.getUserHotelId(username));
				 
				 forwardPage = "/WEB-INF/ReceptionManagerWelcome.jsp";
			} 
			else{
				 forwardPage = "/WEB-INF/AuthenticationFailed.jsp";
			}
		}
		else if (action.equals("Assign Booking")){
		
			// Get the checked radio Button
			String check = request.getParameter("checked");	
			
			
			int bookingID = Integer.parseInt(request.getParameter("bookingID" + check));
			String checkInDate = request.getParameter("checkIn" + check);
			String checkOutDate = request.getParameter("checkOut" + check);
			String roomType = request.getParameter("type" + check);
					
			HttpSession session = request.getSession();
			int userHotelID = (Integer) session.getAttribute("userHotelId");
						
			ArrayList<Integer> availableRoomsList = occupancy.getAvailableRoomsForRoomType(checkInDate, checkOutDate, roomType, userHotelID);
				
			// Set values to be used in "AssignBooking.jsp"
			session.setAttribute("availableRooms", availableRoomsList);
			session.setAttribute("bookingID", bookingID);
			session.setAttribute("checkInDate", checkInDate);
			session.setAttribute("checkOutDate", checkOutDate);
			session.setAttribute("roomType", roomType);
			
			forwardPage = "/WEB-INF/AssignBooking.jsp"; 
			
		}
		else if (action.equals("Confirm Booking") || action.equals("Unassign Booking")){
			
			HttpSession session = request.getSession();
			int hotelID = (Integer) session.getAttribute("userHotelId");
	
			
			if (action.equals("Confirm Booking")){
				
				//String roomType = (String) session.getAttribute("roomType");				
				int bookingID = (Integer) session.getAttribute("bookingID");			
				int assignedRoomNo = Integer.parseInt(request.getParameter("assignedRoomNo"));
				// Update BOOKING AND ROOMS Table
				booking.assignRoom(bookingID, assignedRoomNo, hotelID);
			}
			else if (action.equals("Unassign Booking")){
				// Get the checked radio Button
				String check = request.getParameter("checked");	
				int bookingID = Integer.parseInt(request.getParameter("bookingID" + check));
				int assignedRoomNo = Integer.parseInt(request.getParameter("assignedRoom" + check));
				// Unassign Bookings - Update BOOKING and ROOMS table
				booking.unassignRoom(bookingID, assignedRoomNo, hotelID);
			}
			
			
		
			String username = (String) session.getAttribute("username");
			String password = (String) session.getAttribute("password");
			String hotelLoc = (String) session.getAttribute("hotelLoc");
			
			UserDTO userDTO = new UserDTO(username, password, hotelLoc);
			userDTO.setUserHotelId(hotelID);
			
			
					// *** Re-gather Updated Unassigned Bookings List
			// Get Unassigned Bookings
			ArrayList<BookingDTO> unassignedBookings = booking.getBookings(false, userDTO);
			// Set unassigned bookings
			session.setAttribute("unassignedBookings", unassignedBookings);
			
			
					// *** Re-gather Updated Assigned Bookings List
			// Get assignedBookings
			 ArrayList<BookingDTO> assignedBookings = booking.getBookings(true, userDTO);
			 // Set assigned bookings
			 session.setAttribute("assignedBookings", assignedBookings);	
			
			
			
			forwardPage = "ReceptionManagerWelcome.jsp"; 
		}
		else if(action.equals("Back to Welcome")){			
			HttpSession session = request.getSession();
			String username = (String) session.getAttribute("username");
			String password = (String) session.getAttribute("password");
			String hotelLoc = (String) session.getAttribute("hotelLoc");
			int userHotelID =  (Integer) session.getAttribute("userHotelId");
			
			// Set all the information relating to the user
			UserDTO userDTO = new UserDTO(username, password, hotelLoc);
			userDTO.setUserHotelId(userHotelID);
			
			// Get Bookings (possibly updated)
			ArrayList<BookingDTO> unassignedBookings = booking.getBookings(false, userDTO);
			
			// Set unassigned bookings
		    session.setAttribute("unassignedBookings", unassignedBookings);
			 					
		    forwardPage = "ReceptionManagerWelcome.jsp"; 		
		}
			
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
		
		
	}
}
