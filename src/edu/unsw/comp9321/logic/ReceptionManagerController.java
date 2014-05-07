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

import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.UserDAOImpl;
import edu.unsw.comp9321.jdbc.UserDTO;

public class ReceptionManagerController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ReceptionManagerController.class.getName());
	
	private UserDAOImpl user;
	
	public ReceptionManagerController() throws ServletException{
		super();
		try{
			user = new UserDAOImpl();
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
		// Initial redirect
		if (action == null || action.equals("Back to Login")){
			//Get Hotel Locations for user to select in login form
			ArrayList<String> hotelLoc = user.getHotelLoc();
			// Set hotelLoc attribute
			request.setAttribute("hotelLoc", hotelLoc);
			// Move to Authorization Page
			forwardPage = "ReceptionManagerAuth.jsp";	
		}
		else if (action.equals("Login")){
			//Get Input from Login Form
			String username = request.getParameter("user");
			String password = request.getParameter("password");
			String hotelLoc = request.getParameter("hotelLoc");
			
			//Store this in UserDTO
			UserDTO userDTO = new UserDTO(username, password, hotelLoc);
			// Verify login details
			Boolean verified = user.verifyLogin(userDTO);
			
			if (verified){
				forwardPage = "ReceptionManagerWelcome.jsp";
			}
			else{
				forwardPage = "AuthenticationFailed.jsp";
			}
		}
		
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
		
		
	}
}
