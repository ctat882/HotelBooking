package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.logic.CartBean;
import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.*;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;    
	private HotelDAO hotels;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		/**
		 * TODO 
		 * -> Initialize the database connection
		 * -> 
		 */
		try {
//			ServletContext context = getServletContext();
			hotels = new HotelDAOImpl();
		} catch (ServiceLocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/**
		 * TODO Commands: 
		 * "Back to search"
		 * "Search"
		 * "Add"
		 * "Checkout"
		 * "Remove"
		 */
		String action = request.getParameter("action");
		String nextPage = "";
		if (action == null) {
			request.setAttribute("hotels", hotels.getCities());
			nextPage = "WelcomePage.jsp";
		}
		else if (action.equals("Search")) {
			nextPage = handleSearch(request);
		}
		else if (action.equals("Confirm")) {
			nextPage = handleConfirm(request);
		}
		//TODO add Command pattern
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/"+nextPage);
		dispatcher.forward(request, response);
	}
	private String handleConfirm(HttpServletRequest request) {
		String nextPage = "";
		CartBean cart = (CartBean) request.getSession().getAttribute("cart");
		
		
		return nextPage;
	}
	
	private String handleSearch(HttpServletRequest request) {
		String nextPage = "";
		
		String msg = validateInput(request);
		if (msg.contentEquals("Valid")) {
			VacancyQueryDTO query = new VacancyQueryDTO(request);
			SearchResults sR = hotels.customerRoomSearch(query);
			ArrayList<ArrayList<RoomDTO>> results = sR.getResults();
			if(results == null) {
				msg = "Unfortunately there are no rooms available matching your "
						+ "search criteria";
				request.setAttribute("error", true);
				request.setAttribute("msg", msg);
				request.setAttribute("hotels", hotels.getCities());
				nextPage = "WelcomePage.jsp";
			}
			else if (results.isEmpty()) {
				request.setAttribute("error", true);
				msg = "Unfortunately there are no rooms available matching your "
						+ "search criteria";
				request.setAttribute("msg", msg);
				request.setAttribute("hotels", hotels.getCities());
				nextPage = "WelcomePage.jsp";
			}
			else { // There are results
				CartBean cart = (CartBean) request.getSession().getAttribute("cart");
				if (cart == null) {
					cart = new CartBean();
				}
				cart.setSearch(sR);
				request.setAttribute("results",results);
				nextPage = "SearchResults.jsp";
			}
			
			
		}
		else {
			request.setAttribute("error", true);
			request.setAttribute("msg", msg);
			request.setAttribute("hotels", hotels.getCities());
			nextPage = "WelcomePage.jsp";
		}
		// Create dates
		
		
		return nextPage;
		
	}

	private String validateInput (HttpServletRequest request) {
		boolean isInputValid = true;
		double price = Double.parseDouble(request.getParameter("maxPrice"));
		int quantity = Integer.parseInt(request.getParameter("numRooms"));
		// Checkin
		int iDay = Integer.parseInt(request.getParameter("inDay"));
		int iMonth = Integer.parseInt(request.getParameter("inMonth"));
		int iYear = Integer.parseInt(request.getParameter("inYear"));
		// Checkout
		int oDay = Integer.parseInt(request.getParameter("outDay"));
		int oMonth = Integer.parseInt(request.getParameter("outMonth"));
		int oYear = Integer.parseInt(request.getParameter("outYear"));		
		
		// Input Validation		
		String error = "";
		InputValidator iV = new InputValidator();
		
		if (! iV.isValidDate(iDay, iMonth - 1, iYear)) {
			error = "Incorrect check in date";
			isInputValid = false;
		}
		else if (! iV.isValidDate(oDay, oMonth - 1, oYear)) {
			error = "Incorrect check out date";
			isInputValid = false;
		}
		else if ((iDay == oDay) && (iMonth == oMonth) && (iYear == oYear)) {
			error = "Cannot check in and out on the same day";
			isInputValid = false;
		}
		else if (! iV.isCheckInDateInFuture(iDay, iMonth - 1, iYear)) {
			error = "Check in must be no earlier than today";
			isInputValid = false;
		}
		else if (! iV.isCheckOutAfterIn(iDay, iMonth - 1, iYear, oDay, oMonth - 1, oYear)) {
			error = "Check in must be before check out";
			isInputValid = false;
		}
		else if (! iV.isValidCurrency(request.getParameter("maxPrice"))) {
			error = "Price per night must be in the form $#(.##)";
			isInputValid = false;
		}
		else if (price < 0) {
			error = "Price per night must be >= $0";
			isInputValid = false;
		}
		else if (! iV.isValidQuantity(request.getParameter("numRooms"))) {
			error = "Number of rooms must be an integer";
			isInputValid = false;
		}
		else if (quantity <= 0) {
			error = "Number of rooms must be >= 0";
			isInputValid = false;
		}
		if(isInputValid) error = "Valid";
		return error;
	}
}
