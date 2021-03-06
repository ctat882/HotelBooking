package edu.unsw.comp9321.jdbc;

import java.sql.Date;

public class BookingDTO {
	private int id;
	private int hotel;
	String hotelLoc;	
	private Date checkIn;
	private Date checkOut;
	private String size;
	private int quantity;
	private int pin;
	private String url;
	private boolean extra_bed;
	// Whether room has been assigned - 'Yes' or 'No'
	private String assigned;
	// Assigned Room Number
	private int assignedRoom;
	private String check_in;
	private String check_out;
	
	public BookingDTO(int id, int hotel, String hotelLoc, Date check_in, Date check_out, String size, int quantity, int pin, String url, boolean extra_bed, String assigned, int assignedRoom){
		this.id = id;
		this.hotel = hotel;
		this.hotelLoc = hotelLoc;
		this.checkIn = check_in;
		this.checkOut = check_out;
		this.size = size;
		this.quantity = quantity;
		this.pin = pin;
		this.url = url;
		this.extra_bed = extra_bed;
		this.assigned = assigned;
		this.assignedRoom = assignedRoom;
	}

	
	
	public BookingDTO() {
		this.id = 0;
		this.hotel = 0;
		this.hotelLoc = "";
		this.checkIn = null;
		this.checkOut = null;
		this.size = null;
		this.quantity = 0;
		this.pin = 0;
		this.url = null;
		this.extra_bed = false;	
		this.assigned = "";
		this.assignedRoom = 0;
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



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHotel() {
		return hotel;
	}
	public void setHotel(int hotel) {
		this.hotel = hotel;
	}
	
	//public String getHotelLoc() {
	//	return hotelLoc;
	//}

	//public void setHotelLoc(String hotelLoc) {
	//	this.hotelLoc = hotelLoc;
	//}
	
	public Date getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(Date check_in) {
		this.checkIn = check_in;
	}
	public Date getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(Date check_out) {
		this.checkOut = check_out;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isExtra_bed() {
		return extra_bed;
	}
	public void setExtra_bed(boolean extra_bed) {
		this.extra_bed = extra_bed;
	}
	
	
	public String getAssigned() {
		return assigned;
	}
	
	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public int getAssignedRoom() {
		return assignedRoom;
	}

	public void setAssignedRoom(int assignedRoom) {
		this.assignedRoom = assignedRoom;
	}
	
}
