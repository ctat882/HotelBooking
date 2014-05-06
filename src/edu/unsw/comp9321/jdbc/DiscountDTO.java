package edu.unsw.comp9321.jdbc;

import java.util.Date;

public class DiscountDTO {

	int hotelID;
	String roomType;
	String hotelLocation;
	
	int startDateDay;
	int startDateMonth;
	int startDateYear;
	
	int endDateDay;
	int endDateMonth;
	int endDateYear;
	
	Date endDate;
	Double discount;
	
	/************************* Set Methods ******************/
	
	// Set Hotel ID
	public void setHotelID(int hotelID){
		this.hotelID = hotelID;		
	}
	
	// Set Hotel Location
	public void setHotelLocation(String hotelLocation){
		this.hotelLocation = hotelLocation;		
	}
	
	// Set Room Type
	public void setRoomType(String roomType){
		this.roomType = roomType;	
	}
	
	// Set Start Date
	public void setStartDateDay(int startDateDay){
		this.startDateDay = startDateDay;	
	}
	
	public void setStartDateMonth(int startDateMonth){
		this.startDateMonth = startDateMonth;	
	}
	
	public void setStartDateYear(int startDateYear){
		this.startDateYear = startDateYear;	
	}
		
	
	
	// Set End Date
	public void setEndDateDay(int endDateDay){
		this.endDateDay = endDateDay;	
	}
	
	public void setEndDateMonth(int endDateMonth){
		this.endDateMonth= endDateMonth;	
	}
	
	public void setEndDateYear(int endDateYear){
		this.endDateYear= endDateYear;	
	}
	
	// Set Discount
	public void setDiscount(Double discount){
		this.discount = discount;	
	}
	
	
	/************************* Get Methods ******************/
	// Get Hotel ID
	public int getHotelID(){
		return hotelID;		
	}
	
	// Get Room Type
	public String getRoomType(){
		return roomType;		
	}
	
	// Get Start Date
	public int getStartDateDay(){
		return startDateDay;		
	}
	
	public int getStartDateMonth(){
		return startDateMonth;		
	}
	
	public int getStartDateYear(){
		return startDateYear;		
	}
	
	// Get End Date	
	public int getEndDateDay(){
		return endDateDay;		
	}
	
	public int getEndDateMonth(){
		return endDateMonth;		
	}
	
	public int getEndDateYear(){
		return endDateYear;		
	}
	
	// Get Discount
	public Double getDiscount(){
		return discount;		
	}
	
	// Get Hotel Location
	public String getHotelLocation(){
		return hotelLocation;		
	}
}
