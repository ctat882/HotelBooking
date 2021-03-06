package edu.unsw.comp9321.jdbc;

import java.util.Date;


import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

public class VacancyQueryDTO {

	private String city;
	private Double maxPrice;
	private String check_in;
	private String check_out;
	private Date checkIn;
	private Date checkOut;
	private int numRooms;
	
	
	public VacancyQueryDTO () {
		this.city = "";
		this.checkIn = null;
		this.checkOut = null;
		this.numRooms = 0;
		this.maxPrice = 0.0;
	}

	public VacancyQueryDTO (HttpServletRequest request) {
		this.city = request.getParameter("city");
				
		this.maxPrice = Double.parseDouble(request.getParameter("maxPrice"));
		this.numRooms = Integer.parseInt(request.getParameter("numRooms"));
		// Checkin
		int iDay = Integer.parseInt(request.getParameter("inDay"));
		int iMonth = Integer.parseInt(request.getParameter("inMonth"));
		int iYear = Integer.parseInt(request.getParameter("inYear"));
		Calendar cIn = Calendar.getInstance();
		cIn.setLenient(false);
		cIn.set(iYear, iMonth - 1, iDay);
		this.checkIn = cIn.getTime();
		// Checkout
		int oDay = Integer.parseInt(request.getParameter("outDay"));
		int oMonth = Integer.parseInt(request.getParameter("outMonth"));
		int oYear = Integer.parseInt(request.getParameter("outYear"));
		Calendar cOut = Calendar.getInstance();
		cOut.setLenient(false);
		cOut.set(oYear, oMonth - 1 , oDay);
		this.checkIn = cOut.getTime();		
		this.check_in = iYear + "-" + iMonth + "-" + iDay;
		this.check_out = oYear + "-" + oMonth + "-" + oDay;
		
	}

	public String getCheck_in() {
		return check_in;
	}

	public void setCheck_in(String check_in) {
		this.check_in = check_in;
	}

	public String getCheck_out() {
		return check_out;
	}

	public void setCheck_out(String check_out) {
		this.check_out = check_out;
	}

	public VacancyQueryDTO(String city, Double maxPrice, Date checkIn,
			Date checkOut, int rooms) {
		super();
		this.city = city;
		this.maxPrice = maxPrice;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.numRooms = rooms;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public Double getMaxPrice() {
		return maxPrice;
	}


	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}


	public Date getCheckIn() {
		return checkIn;
	}


	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}


	public Date getCheckOut() {
		return checkOut;
	}


	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}


	public int getNumRooms() {
		return numRooms;
	}


	public void setNumRooms(int rooms) {
		this.numRooms = rooms;
	}
	
//	public String convertCheckInToString () {
//		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
//		String d = "'" + df.format(this.checkIn) + "'";
//		return d;
//	}
//	
//	public String convertCheckOutToString () {
//		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
//		String d = "'" + df.format(this.checkOut) + "'";
//		return d;
//	}
	
	
}
