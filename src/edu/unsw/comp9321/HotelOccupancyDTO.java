package edu.unsw.comp9321.jdbc;

public class HotelOccupancyDTO {
	
	private int hotelID;
	private String hotelLocation;
	private String roomType;
	// number of rooms occupied
	private int occupied;
	// number of rooms available
	private int available;
	
	public HotelOccupancyDTO(int hotelID, String hotelLocation, String roomType, int occupied, int available){
		this.hotelID = hotelID;
		this.hotelLocation = hotelLocation;
		this.roomType = roomType;
		this.occupied = occupied;
		this.available = available;			
	}
	
	/* Set Methods  */
	public void sethotelID(int hotelID){
		this.hotelID = hotelID;
	}
	
	public void setHotelLocation(String hotelLocation){
		this.hotelLocation = hotelLocation;
	}
	
	public void setRoomType(String roomType){
		this.roomType = roomType;
	}
	
	public void setOccupied(int occupied){
		this.occupied = occupied;
	}
	
	public void setAvailable(int available){
		this.available = available;
	}
	
	
	/* Get Methods  */
	public int gethotelID(){
		return hotelID;
	}
	
	public String getHotelLocation(){
		return hotelLocation;
	}
	
	public String getRoomType(){
		return roomType;
	}
	
	public int getOccupied(){
		return occupied;
	}
	
	public int getAvailable(){
		return available;
	}
	
}














