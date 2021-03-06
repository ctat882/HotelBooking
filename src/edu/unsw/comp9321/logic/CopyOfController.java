package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import edu.unsw.comp9321.logic.CartBean;
import edu.unsw.comp9321.mail.SmtpAuthenticator;
import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.*;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class CopyOfController extends HttpServlet {
	private static final long serialVersionUID = 1L;    
	private HotelDAO hotels;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CopyOfController() {
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
//		sendMail(request,response);
		String action = request.getParameter("action");
		String nextPage = "";
		if (action == null) {
			request.setAttribute("hotels", hotels.getCities());
			nextPage = "WelcomePage.jsp";
		}
		else if (action.equals("Search")) {
			nextPage = handleSearch(request);
		}
		else if (action.equals("Checkout")) {
			nextPage = handleConfirm(request);
		}
		else if (action.equals("Proceed")) {
			nextPage = handleCheckout(request,response);
		}
		//TODO add Command pattern
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/"+nextPage);
		dispatcher.forward(request, response);
	}
	
	private String handleCheckout(HttpServletRequest request, HttpServletResponse response) {
		String nextPage = "Thankyou.jsp";
		CartBean cart = (CartBean) request.getSession().getAttribute("cart");
		// Validate input
		String msg = validateCheckoutInput(request);
		if (msg.contentEquals("Valid")) {
	
			// TODO 
			// Email 
			
			String pin = generatePin();
			String url = generateUrl(cart.getSelection(),pin);
			//TODO must sort out extra beds
			if (hotels.makeBooking(cart.getSelection(), cart.getSearch().getCheckin(),
					cart.getSearch().getCheckout(), pin, url, 0)) {
				
				//TODO send email. Should create a thread for this
				try {
					System.out.println("TRYING TO EMAIL");
					sendMail(request,response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			request.setAttribute("error", true);
			request.setAttribute("msg", msg);
			nextPage = "Checkout.jsp";
		}
		//TODO unfinished
		return nextPage;
		
	}
	
	private void sendMail (HttpServletRequest request,
            HttpServletResponse response) throws IOException {
		 // Recipient's email ID needs to be mentioned.
	      String to = "ctattam@gmail.com";
	 
	      // Sender's email ID needs to be mentioned
	      String from = "corey_tattam@hotmail.com";
	 
	      // Assuming you are sending email from localhost
	      String host = "smtp.cse.unsw.edu.au";
	 
	      // Get system properties
	      Properties properties = System.getProperties();
	 
	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);
	      properties.setProperty("mail.smtp.user", "ctat882");
	      properties.setProperty("mail.user", "ctat882");
	      properties.setProperty("password", "loki2586");
	      properties.setProperty("mail.smtp.password", "loki2586");
	      properties.setProperty("mail.password", "loki2586");
	      properties.setProperty("mail.smtp.port", "587");
	      properties.setProperty("mail.smtp.auth","true");
	      properties.setProperty("mail.smtp.starttls.enable","true");
	      
	      SmtpAuthenticator authentication = new SmtpAuthenticator();
	      
	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties,authentication);
	      
		  // Set response content type
	      response.setContentType("text/html");
	      PrintWriter out = response.getWriter();

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);
	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));
	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(to));
	         // Set Subject: header field
	         message.setSubject("This is the Subject Line!");
	         // Now set the actual message
	         message.setText("This is actual message");
	         // Send message
	         System.out.println("Just before Transport.send");
	         Transport.send(message);
	         System.out.println("Just after Transport.send");
	         String title = "Send Email";
	         String res = "Sent message successfully....";
	         String docType =
	         "<!doctype html public \"-//w3c//dtd html 4.0 " +
	         "transitional//en\">\n";
	         out.println(docType +
	         "<html>\n" +
	         "<head><title>" + title + "</title></head>\n" +
	         "<body bgcolor=\"#f0f0f0\">\n" +
	         "<h1 align=\"center\">" + title + "</h1>\n" +
	         "<p align=\"center\">" + res + "</p>\n" +
	         "</body></html>");
	      }catch (Exception mex) {
	         mex.printStackTrace();
	      }
	}
	
	private String validateCheckoutInput(HttpServletRequest request) {
		String msg = "Valid";
		InputValidator vC = new InputValidator();
		if(! vC.isValidEmail(request.getParameter("email"))) {
			msg = "Invalid email address";
		}
		else if (! vC.isValidNumbers(request.getParameter("cardnum"))) {
			msg = "Invalid credit card number";
		}
		
		return msg;
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
		// This is for jmeter testing
		ArrayList<RoomDTO> booking;
		if(selection == 6) {
			booking  = new ArrayList<RoomDTO>();
			RoomDTO twin = new RoomDTO();
			RoomDTO queen = new RoomDTO();
			
			twin.setAvailability("Available");
			twin.setHotel(1);
			twin.setPrice(999.99);
			twin.setSize("Twin");
			twin.setRoom_num(new Random().nextInt(16));
			
			queen.setAvailability("Available");
			queen.setHotel(1);
			queen.setPrice(999.99);
			queen.setSize("Queen");
			queen.setRoom_num(new Random().nextInt(16));
			
			booking.add(twin);
			booking.add(queen);
			request.setAttribute("total", 9999);
			request.setAttribute("booking", booking);
			request.setAttribute("Twin Total",9999);
			request.setAttribute("Queen Total",9999);
			
		}
		else {
//			ArrayList<RoomDTO> booking = cart.getSearch().getResults().get(selection);
			booking = cart.getSearch().getResults().get(selection);
			double total = calculateBookingTotal(booking,cart,request);
			HashMap<String,Double> prices = cart.getSearch().getPrices();
			request.setAttribute("total", total);
			request.setAttribute("booking", booking);
			for(String key : prices.keySet()) {
				if (prices.get(key) > 0.0) request.setAttribute(key +"Total", prices.get(key));
				if (prices.get(key) > 0.0) request.setAttribute(key +"Count",getRoomCount(booking,key));
			}
			cart.setSelection(booking);
		}
		
//		
		
		
		
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
		System.out.println("Validating");
		String msg = validateSearchInput(request);
		System.out.println("Finished Validating");
		
		if (msg.contentEquals("Valid")) {
			VacancyQueryDTO query = new VacancyQueryDTO(request);
			// Added for Jmeter testing
			request.setAttribute("checkin", query.getCheck_in());
			request.setAttribute("checkout",query.getCheck_out());
			System.out.println("Room search");
			SearchResults sR = hotels.customerRoomSearch(query);
			System.out.println("Exit room search");
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
		System.out.println("Going to 2nd page");
		
		return nextPage;
		
	}

	private String validateSearchInput (HttpServletRequest request) {
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
