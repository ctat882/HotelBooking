package edu.unsw.comp9321.jdbc;

import java.sql.Date;

public class BookingDTO {
	private int id;
	private int hotel;
	private Date check_in;
	private Date check_out;
	private String size;
	private int quantity;
	private int pin;
	private String url;
	private boolean extra_bed;
	// Whether room has been assigned - 'Yes' or 'No'
	private String assigned;
	// Assigned Room Number
	private int assignedRoom;
	
	public BookingDTO(int id, int hotel, Date check_in, Date check_out, String size, int quantity, int pin, String url, boolean extra_bed, String assigned, int assignedRoom){
		this.id = id;
		this.hotel = hotel;
		this.check_in = check_in;
		this.check_out = check_out;
		this.size = size;
		this.quantity = quantity;
		this.pin = pin;
		this.url = url;
		this.extra_bed = extra_bed;
		this.assigned = assigned;
		this.assignedRoom = assignedRoom;
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
	
	
	public BookingDTO() {
		this.id = 0;
		this.hotel = 0;
		this.check_in = null;
		this.check_out = null;
		this.size = null;
		this.quantity = 0;
		this.pin = 0;
		this.url = null;
		this.extra_bed = false;				
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
	public Date getCheck_in() {
		return check_in;
	}
	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}
	public Date getCheck_out() {
		return check_out;
	}
	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
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
	
}
