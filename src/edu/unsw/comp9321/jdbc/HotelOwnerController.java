package edu.unsw.comp9321.jdbc;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.logging.Logger;

import edu.unsw.comp9321.jdbc.HotelOwnerDAO;
import edu.unsw.comp9321.jdbc.HotelOwnerDAOImpl;
import edu.unsw.comp9321.logic.Controller;
import edu.unsw.comp9321.common.ServiceLocatorException;


public class HotelOwnerController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(Controller.class.getName());
	
	private HotelOwnerDAO hotelOwner;	
	
	public HotelOwnerController() throws ServletException{	
		super();
		try{
			hotelOwner = new HotelOwnerDAOImpl();
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
		String forwardPage = "";
		// When moving to welcome page
		// Retrieve Hotel Occupancy Info 
		if (action == null || action == "welcome"){
			request.setAttribute("hotelOccupancy", hotelOwner.getRooms());
			request.setAttribute("noHotels", hotelOwner.getTotalHotels());
			request.setAttribute("hotelLoc", hotelOwner.getHotelLocList());
			forwardPage = "HotelOwnerWelcome.jsp";			
		}
		
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
	}
}
