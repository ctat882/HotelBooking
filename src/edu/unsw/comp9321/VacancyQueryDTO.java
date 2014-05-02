package edu.unsw.comp9321;

import java.sql.Date;

public class VacancyQueryDTO {

	private String city;
	private Double maxPrice;
	private Date checkIn;
	private Date checkOut;
	private int rooms;
	
	
	public VacancyQueryDTO () {
		this.city = "";
		this.checkIn = null;
		this.checkOut = null;
		this.rooms = 0;
		this.maxPrice = 0.0;
	}


	public VacancyQueryDTO(String city, Double maxPrice, Date checkIn,
			Date checkOut, int rooms) {
		super();
		this.city = city;
		this.maxPrice = maxPrice;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.rooms = rooms;
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


	public int getRooms() {
		return rooms;
	}


	public void setRooms(int rooms) {
		this.rooms = rooms;
	}
	
}