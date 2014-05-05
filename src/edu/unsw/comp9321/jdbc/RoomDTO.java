package edu.unsw.comp9321.jdbc;

public class RoomDTO {
	
	private String size;
	private String availability;
	private int room_num;
	private double price;
	private int hotel;
	
	public RoomDTO() {
		this.size = null;
		this.availability = null;
		this.price = 0.0;
		this.room_num = 0;
		this.hotel = 0;
		
	}
	
	public int getHotel() {
		return hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public int getRoom_num() {
		return room_num;
	}
	public void setRoom_num(int room_num) {
		this.room_num = room_num;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
