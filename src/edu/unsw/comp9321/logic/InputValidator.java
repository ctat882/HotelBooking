package edu.unsw.comp9321.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class InputValidator {

	public InputValidator(){};
	
	/**
	 * Check if a date is correct and in a sql format
	 * @param date
	 * @return True if the date is valid, false otherwise
	 */
	public boolean isValidDate (int day, int month, int year) {
		boolean valid = true;
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		try {
			cal.set(year, month, day);
		} catch (Exception e) {
			valid = false;
		}		
		
		//TODO Must check that the check in date and check out date are not the same
		return valid;
	}
	
	/**
	 * Check if a string contains only alpha-numeric characters.
	 * TODO this is UNTESTED! 
	 * @param string
	 * @return True if the string is valid, false otherwise.
	 */
	public boolean isValidText (String string) {
		boolean valid = false;
		if(string.matches("^[a-zA-Z0-9]+$")) valid = true;
		return valid;		
	}
	
	/**
	 * Check if the string is a valid email string, and does not contain
	 * any forbidden characters.
	 * @param email
	 * @return True if the email is valid, false otherwise.
	 */
	public boolean isValidEmail (String email) {
		boolean valid = false;
		//TODO
		return valid;
	}
	
	/**
	 * Check if the string is a valid url string.
	 * @param url
	 * @return True if the url is valid, false otherwise.
	 */
	public boolean isValidURL (String url) {
		boolean valid = false;
		//TODO
		return valid;
	}
	
	public boolean isValidCurrency (String money) {
		boolean valid = false;
		if (money.matches("^[0-9]+[.]?[0-9]*$")) valid = true;
		return valid;
	}
	
	public boolean isValidQuantity (String num) {
		boolean valid = false;
		if (num.matches("^[0-9]+$")) valid = true;
		return valid;
	}
	
	
	public boolean isCheckOutAfterIn (int iDay,int iMonth, int iYear,int oDay,int oMonth, int oYear) {
		Calendar in = Calendar.getInstance();
		in.setLenient(false);
		Calendar out = Calendar.getInstance();
		out.setLenient(false);		
		in.set(iYear, iMonth, iDay);
		out.set(oYear, oMonth, oDay);
		boolean isAfter = false;
		Date cIn = in.getTime();
		Date cOut = out.getTime();
		if (cOut.after(cIn)) isAfter = true;
		return isAfter;
	}
	
	public boolean isCheckInDateInFuture (int day, int month, int year) {
		boolean isFuture = false;
		Calendar now = Calendar.getInstance();
		Calendar in = Calendar.getInstance();
		in.setLenient(false);
		in.set(year, month , day);		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("in = " +df.format(in.getTime()));
		System.out.println("now = " +df.format(now.getTime()));
		
		if(in.after(now)) isFuture = true;		
		return isFuture;
	}
	
	public boolean dateIsAfterPresent (int day, int month, int year){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, day);
		Date bookingStartDate = calendar.getTime();
		
		// Get today's date
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		Date todayDate = c.getTime();
				
		if (bookingStartDate.after(todayDate) || bookingStartDate.equals(todayDate)){
			return true;
		}
		else
			return false;
		
	}
	public boolean isDateABeforeDateB (int dayA, int monthA, int yearA, int dayB, int monthB, int yearB){
		Calendar calendarA = Calendar.getInstance();
		calendarA.set(yearA, monthA-1, dayA);
		Date dateA = calendarA.getTime();
		
		
		Calendar calendarB = Calendar.getInstance();
		calendarB.set(yearB, monthB-1, dayB);
		Date dateB = calendarB.getTime();
		
		if (dateA.before(dateB) || dateA.equals(dateB)){
			return true;
		}
		else
			return false;
	}
	
	
	// Is the input Date Valid?
	// E.g 31/06/2014 is not a valid date
	public boolean isDateValid (int day, int month, int year){
		Calendar calendar = Calendar.getInstance();		
		calendar.setLenient(false);
		calendar.set(year, month-1, day);
		
		try{
			Date date = calendar.getTime();			
			return true;
		}catch(Exception e){
			return false;
		}
	}
}


