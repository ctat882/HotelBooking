package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
		else if (action.equals("Checkout")) {
			nextPage = handleCheckout(request);
		}
		//TODO add Command pattern
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/"+nextPage);
		dispatcher.forward(request, response);
	}
	
	private String handleCheckout(HttpServletRequest request) {
		String nextPage = "Checkout.jsp";
		CartBean cart = (CartBean) request.getSession().getAttribute("cart");
		// TODO 
		// generate pin
		// generate url
		// generate 
		
		String pin = generatePin();
		String url = generateUrl(cart.getSelection(),pin);
		//TODO must sort out extra beds
		hotels.makeBooking(cart.getSelection(), cart.getSearch().getCheckin(),
				cart.getSearch().getCheckout(), pin, url, 0);
		//TODO unfinished
		return nextPage;
		
	}
	
	private String generateUrl (ArrayList<RoomDTO> booking, String pin) {
		String url = "http://";
		String server = "localhost:8080/Assign2/";
		String unique = pin.hashCode() + String.valueOf(booking.hashCode());
		
		try {
			MessageDigest mD = MessageDigest.getInstance("MD5");
			byte[] msgBytes = unique.getBytes();
			byte[] output = mD.digest(msgBytes);
			unique = output.toString();
			if (unique.length() > 50) {
				unique = unique.substring(0, 49);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		url += server + unique;
		if (url.length() > 80) {
			url = url.substring(0, 79);
		}			
		
		return url;
	}
	
	/**
	 * Generate a random four digit number
	 * @return
	 */
	private String generatePin() {
		String pin = null;
		InputValidator iV = new InputValidator();
		boolean valid = false;
		while (!valid) {
			int minimum = 1000;
			int maximum = 9999;
			int rand = minimum +  (int)(Math.random()* ((maximum - minimum) + 1));
			pin = String.valueOf(rand);
			if (iV.isValidPin(pin)) valid = true;
		}	
		
		return pin;
	}
	private String handleConfirm(HttpServletRequest request) {
		String nextPage = "Checkout.jsp";
		CartBean cart = (CartBean) request.getSession().getAttribute("cart");
		int selection = Integer.parseInt(request.getParameter("choice"));
		ArrayList<RoomDTO> booking = cart.getSearch().getResults().get(selection);
		double total = calculateBookingTotal(booking,cart,request);
		HashMap<String,Double> prices = cart.getSearch().getPrices();
		request.setAttribute("total", total);
		request.setAttribute("booking", booking);
		for(String key : prices.keySet()) {
			if (prices.get(key) > 0.0) request.setAttribute(key +"Total", prices.get(key));
			if (prices.get(key) > 0.0) request.setAttribute(key +"Count",getRoomCount(booking,key));
		}
		cart.setSelection(booking);
		return nextPage;
	}
	
	private int getRoomCount (ArrayList<RoomDTO> booking, String type) {
		int num = 0;
		for (RoomDTO r : booking) {
			if (r.getSize().contentEquals(type)) num++;
		}
		return num;
	}
	
	private double calculateBookingTotal(ArrayList<RoomDTO>booking,CartBean cart,HttpServletRequest request) {
		double total = 0.0;
		String checkin = cart.getSearch().getCheckin();
		String checkout = cart.getSearch().getCheckout();
		DateCalculator dC = new DateCalculator();
		HashMap<String,Double> prices = new HashMap<String,Double>(8); 
		prices.put("Single", 0.0);
		prices.put("Twin", 0.0);
		prices.put("Queen", 0.0);
		prices.put("Executive", 0.0);
		prices.put("Suite", 0.0);
		// Get the number of each type of room
		HashMap<String,Integer> numRoomTypes = new HashMap<String,Integer>(8);
		numRoomTypes.put("Single", 0);
		numRoomTypes.put("Twin", 0);
		numRoomTypes.put("Queen", 0);
		numRoomTypes.put("Executive", 0);
		numRoomTypes.put("Suite", 0);
		for(RoomDTO r : booking) {			
			numRoomTypes.put(r.getSize(), numRoomTypes.get(r.getSize()) + 1);			
		}
		
		if (numRoomTypes.get("Single") > 0) {
			prices.put("Single",dC.getTotalPrice(checkin,checkout,cart.getSearch().getSingle_totals()));
			prices.put("Single", prices.get("Single") * numRoomTypes.get("Single"));
		}
		if (numRoomTypes.get("Twin") > 0) {
			prices.put("Twin",dC.getTotalPrice(checkin,checkout,cart.getSearch().getTwin_totals()));
			prices.put("Twin", prices.get("Twin") * numRoomTypes.get("Twin"));
		}
		if (numRoomTypes.get("Queen") > 0) {
			prices.put("Queen",dC.getTotalPrice(checkin,checkout,cart.getSearch().getQueen_totals()));
			prices.put("Queen", prices.get("Queen") * numRoomTypes.get("Queen"));
		}
		if (numRoomTypes.get("Executive") > 0) {
			prices.put("Executive",dC.getTotalPrice(checkin,checkout,cart.getSearch().getExecutive_totals()));
			prices.put("Executive", prices.get("Executive") * numRoomTypes.get("Executive"));
		}
		if (numRoomTypes.get("Suite") > 0) {
			prices.put("Suite",dC.getTotalPrice(checkin,checkout,cart.getSearch().getSuite_totals()));
			prices.put("Suite", prices.get("Suite") * numRoomTypes.get("Suite"));
		}
		
		for(String key : prices.keySet()) {
			cart.getSearch().getPrices().put(key, prices.get(key));
			total += prices.get(key);
		}
		
		return total;
		
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
		double price = -1;
		int quantity = -1;
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
		if (request.getParameter("maxPrice") == null || request.getParameter("maxPrice").isEmpty()) {
			error = "The max price cannot be empty";
			isInputValid = false;
		}
		else {
			 price = Double.parseDouble(request.getParameter("maxPrice"));
		}
		
		if (request.getParameter("numRooms") == null || request.getParameter("numRooms").isEmpty()) {
			error = "The number of rooms cannot be empty";
			isInputValid = false;
		}
		else {
			 quantity = Integer.parseInt(request.getParameter("numRooms"));
		}
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
