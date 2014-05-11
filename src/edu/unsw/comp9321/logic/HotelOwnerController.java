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
import edu.unsw.comp9321.jdbc.DiscountDTOGiri;
import edu.unsw.comp9321.jdbc.HotelOccupancyDAO;
import edu.unsw.comp9321.jdbc.HotelOccupancyDAOImpl;


public class HotelOwnerController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HotelOwnerController.class.getName());
	
	private HotelOccupancyDAO hotelOwner;
	private DiscountDAO discountInfo;
	
	public HotelOwnerController() throws ServletException{	
		super();
		try{
			hotelOwner = new HotelOccupancyDAOImpl();
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
		
		
		HttpSession session = request.getSession();
		
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
					
			String discountError = (String) session.getAttribute("discountError");
			
			if (discountError.equals("true")){
				session.setAttribute("discountAdded", "false");
				forwardPage = "DiscountEnd.jsp";
				
			}
			else if (discountError.equals("false")){
				session.setAttribute("discountAdded", "true");
				forwardPage = "Confirm.jsp";				
			}
			
			
		}
		// Finalize discount
		else if(action.equals("Confirm Discount(s)")){
			// Save the discounts to the database
			
			ArrayList<DiscountDTOGiri> discountInfoFull = (ArrayList<DiscountDTOGiri>) session.getAttribute("discountInfo");
			
			// Set Discounts
			Boolean discountError = discountInfo.setDiscount(discountInfoFull);
			
			if (discountError){
				session.setAttribute("discountAdded", "false");
			}
			else{
				session.setAttribute("discountAdded", "true");
			}
				
			forwardPage = "DiscountEnd.jsp";
			
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
		
	}
}
