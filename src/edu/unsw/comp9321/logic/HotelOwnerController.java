package edu.unsw.comp9321.logic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.DiscountDAO;
import edu.unsw.comp9321.jdbc.DiscountDAOImpl;
import edu.unsw.comp9321.jdbc.DiscountDTO;
import edu.unsw.comp9321.jdbc.HotelOwnerDAO;
import edu.unsw.comp9321.jdbc.HotelOwnerDAOImpl;


public class HotelOwnerController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HotelOwnerController.class.getName());
	
	private HotelOwnerDAO hotelOwner;
	private DiscountDAO discountInfo;
	
	public HotelOwnerController() throws ServletException{	
		super();
		try{
			hotelOwner = new HotelOwnerDAOImpl();
			discountInfo = new DiscountDAOImpl();
		} catch (ServiceLocatorException e) {
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
		String action = request.getParameter("action");
		System.out.println("action = " + action);
		String forwardPage = "";
		// When moving to welcome page
		// Retrieve Hotel Occupancy Info 
		if (action == null || action.equals("Back to Welcome Screen")){
			request.setAttribute("hotelOccupancy", hotelOwner.getRooms());
			request.setAttribute("noHotels", hotelOwner.getTotalHotels());
			request.setAttribute("hotelLoc", hotelOwner.getHotelLocList());			
			forwardPage = "HotelOwnerWelcome.jsp";	
			
		}
		// Initially select which hotels/rooms to apply discount to
		else if (action.equals("Add Discount(s)")){			
			forwardPage = "DiscountServlet";				
		}
		// Add Start/End Date and Discount Amount information
		else if(action.equals("Update Discount(s)")){			
			forwardPage = "Confirm.jsp";
		}
		// Finalize discount
		else if(action.equals("Confirm Discount(s)")){
			// Save the discounts to the database
			HttpSession session = request.getSession();
			ArrayList<DiscountDTO> discountInfoFull = (ArrayList<DiscountDTO>) session.getAttribute("discountInfo");
			
			// Check to see whether there were any failures in adding Discounts to the database
			Boolean discountAdded = discountInfo.setDiscount(discountInfoFull);
			if (discountAdded){
				request.setAttribute("discountAdded", "true");
			}
			else{
				request.setAttribute("discountAdded", "false");
			}
			//Invalidate Session
			session.invalidate();			
			forwardPage = "DiscountEnd.jsp";
			
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
		
	}
}
