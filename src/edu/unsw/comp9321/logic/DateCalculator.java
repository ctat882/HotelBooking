package edu.unsw.comp9321.logic;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.unsw.comp9321.jdbc.DiscountDTO;

public class DateCalculator {
	// DAYS OF THE YEAR
	static final int jan01 = 1;
	static final int feb15 = 46;		
	static final int mar25 = 84;
	static final int apr14 = 105;		
	static final int jul01 = 183;
	static final int jul20 = 202;		
	static final int sep20 = 264;
	static final int oct10 = 283;		
	static final int dec15 = 349;
	static final int dec31 = 365;
	
	public DateCalculator(){};
	
	public void fillDiscountDays (int[] discounts, DiscountDTO discount) {
		Calendar start = Calendar.getInstance();
		start.setTime(discount.getStart_date());		
		Calendar end = Calendar.getInstance();
		end.setTime(discount.getEnd_date());
		GregorianCalendar greg = new GregorianCalendar();
		// If leap year subtract one day from Day Of Year
		int startDOY = start.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(start.get(Calendar.YEAR))) startDOY--;		
		int endDOY = end.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(end.get(Calendar.YEAR))) endDOY--;
		
		
	}
	
	
	/**
	 * Map the days on peak to a boolean array of the days of the year.
	 * @param onPeak
	 * @param checkin
	 * @param checkout
	 */
	public void fillDaysOnPeak (Boolean[] onPeak, Date checkin, Date checkout) {
		
		Calendar check_in = Calendar.getInstance();
		check_in.setTime(checkin);		
		Calendar check_out = Calendar.getInstance();
		check_out.setTime(checkout);
		GregorianCalendar greg = new GregorianCalendar();
		// If leap year subtract one day from Day Of Year
		int checkInDOY = check_in.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(check_in.get(Calendar.YEAR))) checkInDOY--;		
		int checkOutDOY = check_out.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(check_out.get(Calendar.YEAR))) checkOutDOY--;		
		
		//int numNightsStay;
		if (checkInDOY < checkOutDOY) {		// Then standard calculation
			//numNightsStay = checkOutDOY - checkInDOY;
			for (int i = checkInDOY - 1; i < checkOutDOY; i++) {
				if (i >= jan01 && i <= feb15) onPeak[i] = true;
				else if (i >= mar25 && i <= apr14) onPeak[i] = true;
				else if (i >= jul01 && i <= jul20) onPeak[i] = true;
				else if (i >= sep20 && i <= oct10) onPeak[i] = true;
				else if (i >= dec15 && i <= dec31) onPeak[i] = true;
			}
		}
		else {	// Wrap around
			// = (dec31 - checkInDOY) + checkOutDOY;	//TODO probably -1 from checkout date
			for (int i = jan01 - 1; i < checkOutDOY; i++) {
				if (i >= jan01 && i <= feb15) onPeak[i] = true;
				else if (i >= mar25 && i <= apr14) onPeak[i] = true;
				else if (i >= jul01 && i <= jul20) onPeak[i] = true;
				else if (i >= sep20 && i <= oct10) onPeak[i] = true;
				else if (i >= dec15 && i <= dec31) onPeak[i] = true;
			}
			for (int i = checkOutDOY - 1; i < dec31; i++) {
				if (i >= jan01 && i <= feb15) onPeak[i] = true;
				else if (i >= mar25 && i <= apr14) onPeak[i] = true;
				else if (i >= jul01 && i <= jul20) onPeak[i] = true;
				else if (i >= sep20 && i <= oct10) onPeak[i] = true;
				else if (i >= dec15 && i <= dec31) onPeak[i] = true;
			}
			
		}		
		
		
	}
	
	/**
	 * Calculate the number of On Peak days between two dates.
	 * Assumes that the booking is less than 365 days.
	 * @param query
	 * @return
	 */
 	public int numDaysOnPeak (Date start, Date end) {
		
		Calendar check_in = Calendar.getInstance();
		check_in.setTime(start);		
		Calendar check_out = Calendar.getInstance();
		check_out.setTime(end);
		// Check if leap year
		GregorianCalendar greg = new GregorianCalendar();
		// If leap year subtract one day from Day Of Year
		int checkInDOY = check_in.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(check_in.get(Calendar.YEAR))) checkInDOY--;		
		int checkOutDOY = check_out.get(Calendar.DAY_OF_YEAR);
		if (greg.isLeapYear(check_out.get(Calendar.YEAR))) checkOutDOY--;		
		int daysInPeak = 0;		
		// Now determine if check in and check out fall within range of peak periods
		// TODO probably have to add +1 here		
		if ( checkInDOY >= jan01 && checkInDOY <= feb15  ) {
			if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += (feb15 - checkInDOY) + (checkOutDOY -mar25);
			}
			else if (checkOutDOY >= jul01 && checkOutDOY <= jul20) {
				daysInPeak += (feb15 - checkInDOY) + (apr14 - mar25) 
						+ (checkOutDOY - jul01);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += (feb15 - checkInDOY) + (apr14 - mar25) + (jul20 - jul01) 
						+ (checkOutDOY - sep20);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += (feb15 - checkInDOY) + (apr14 - mar25) + (jul20 - jul01) 
						+ (oct10 - sep20) + (checkOutDOY - dec15);
			}
		}
		else if (checkInDOY >= mar25 && checkInDOY <= apr14 ) {
			if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= jul01 && checkOutDOY <= jul20) {
				daysInPeak += ( apr14 - checkInDOY) + (checkOutDOY - jul01);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += ( apr14 - checkInDOY) + (jul20 - jul01) 
						+ (checkOutDOY - sep20);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += ( apr14 - checkInDOY) + (jul20 - jul01)
						+ (oct10 - sep20) + (checkOutDOY - dec15);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( apr14 - checkInDOY) + (jul20 - jul01) + (oct10 - sep20) 
						+ (dec31 - dec15) + (checkOutDOY - jan01);
			}
			
		}
		else if (checkInDOY >= jul01 && checkInDOY <= jul20) {
			if (checkOutDOY >= jul01 && checkInDOY <= jul20) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += ( jul20 - checkInDOY) + (checkOutDOY - sep20);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += ( jul20 - checkInDOY) + (oct10 - sep20) + (checkOutDOY - dec15);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( jul20 - checkInDOY) + (oct10 - sep20) 
						+ (dec31 - dec15) + (checkOutDOY - jan01);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += ( jul20 - checkInDOY) + (oct10 - sep20) 
						+ (dec31 - dec15) + (feb15 - jan01) + (checkOutDOY - mar25);
			}
			
		}
		else if (checkInDOY >= sep20 && checkInDOY <= oct10) {			
			if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += ( oct10 - checkInDOY) + (checkOutDOY - dec15);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( oct10 - checkInDOY)	+ (dec31 - dec15) + (checkOutDOY - jan01);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += ( oct10 - checkInDOY)	+ (dec31 - dec15) 
						+ (feb15 - jan01) + (checkOutDOY - mar25);
			}
			else if (checkOutDOY >= jul01 && checkInDOY <= jul20) {
				daysInPeak += ( oct10 - checkInDOY)	+ (dec31 - dec15) 
						+ (feb15 - jan01) + (apr14 - mar25) + (checkOutDOY - jul01);
			}
			
		}
		else if (checkInDOY >= dec15 && checkInDOY <= dec31) {
			if (checkOutDOY >= dec15 && checkOutDOY <= dec31) {
				daysInPeak += (checkOutDOY - checkInDOY);
			}
			else if (checkOutDOY >= jan01 && checkOutDOY <= feb15) {
				daysInPeak += ( dec31 - checkInDOY) + (checkOutDOY - jan01);
			}
			else if (checkOutDOY >= mar25 && checkOutDOY <= apr14) {
				daysInPeak += ( dec31 - checkInDOY)	+ (feb15 - jan01) + (checkOutDOY - mar25);
			}
			else if (checkOutDOY >= jul01 && checkInDOY <= jul20) {
				daysInPeak += ( dec31 - checkInDOY)	+ (feb15 - jan01) 
						+ (apr14 - mar25) + (checkOutDOY - jul01);
			}
			else if (checkOutDOY >= sep20 && checkOutDOY <= oct10) {
				daysInPeak += ( dec31 - checkInDOY)	+ (feb15 - jan01) 
						+ (apr14 - mar25) + (jul20 - jul01) + (checkOutDOY - sep20);
			}
		}		
		return daysInPeak;
	}
}
