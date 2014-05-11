package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321.jdbc.DiscountDTOGiri;

public class DiscountServlet extends HttpServlet{	
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String action = request.getParameter("action");
		
		String nextPage = "";		
		
		// *********************      Add Hotel ID, Hotel Location and Room Type to discountInfo   ****************************************
		// *********************      Start/End Date and Discount Amount have not been added at this point *********************************
		if (action.equals("Add Discount(s)")){
			int counter = 0;
			
			// store the discount information into an array of DiscountDTO's
			ArrayList<DiscountDTOGiri> discountInfo = new ArrayList<DiscountDTOGiri>();	
			
			while (request.getParameterMap().containsKey("id" + counter)){
				
				String check = request.getParameter("checked" + counter);
				
				// Get the information from the ticked check boxes (hotels and rooms that a discount will be applied to)
				if (check != null){
					DiscountDTOGiri discDTO = new DiscountDTOGiri();
					int hotelID = Integer.parseInt(request.getParameter("hotelID" + counter));
					String hotelLoc = request.getParameter("hotelLocation" + counter);	
					String roomType = request.getParameter("roomType" + counter);			
									
					discDTO.setHotelID(hotelID);
					discDTO.setHotelLocation(hotelLoc);
					discDTO.setRoomType(roomType);
					
					discountInfo.add(discDTO);
					
				}
				
				++counter;
			}			
			// Set discountInfo Attribute
			HttpSession session = request.getSession();
			session.setAttribute("discountInfo", discountInfo);
			
			// Move to Discount.jsp
			nextPage = "Discount.jsp";
		}
		
		//  ********************************    Add Start/End Date and Discount amount to discountInfo  *********************************************************
		else if (action.equals("Update Discount(s)")){			

			InputValidator valid = new InputValidator();
			HttpSession session = request.getSession();
			
			session.setAttribute("discountError", "false");
			
			int counter = 0;			
			
			while (request.getParameterMap().containsKey("id" + counter)){
			
				String startDayString = request.getParameter("startDay" + counter);				
				int startDay = Integer.parseInt(startDayString);
				
				String startMonthString = request.getParameter("startMonth" + counter);
				int startMonth= Integer.parseInt(startMonthString);
				
				String startYearString = request.getParameter("startYear" + counter);
				int startYear= Integer.parseInt(startYearString);
				
				String endDayString = request.getParameter("endDay" + counter);
				int endDay = Integer.parseInt(endDayString);
				
				String endMonthString = request.getParameter("endMonth" + counter);
				int endMonth= Integer.parseInt(endMonthString);
				
				String endYearString = request.getParameter("endYear" + counter);
				int endYear= Integer.parseInt(endYearString);
				
				String discountString = request.getParameter("discountAmt" + counter);
				
				// If disallowed value is entered for discount, discount is set to 0;
				Double discount = 0.00;
				Boolean isDiscountValid = false;
				try{
					discount= Double.parseDouble(discountString);
					if (discount > 0 && discount <= 100)
						isDiscountValid = true;
				}catch(Exception e){					
					System.out.println("Caught Exception");					
				}
				
				
				// Is Discount Start Day equal to, or after today
				Boolean isDiscountStartValid = valid.dateIsAfterPresent(startDay, startMonth, startYear);
				// Is Discount End Day equal to, or after today
				Boolean isDiscountEndValid = valid.dateIsAfterPresent(endDay, endMonth, endYear);
				// Is "Discount Start" Before "Discount End"
				Boolean isDiscountStartBeforeEnd = valid.isDateABeforeDateB(startDay, startMonth, startYear, endDay, endMonth, endYear);
				// Is Discount Start Day a valid Date
				Boolean isStartDateValid = valid.isDateValid(startDay, startMonth, startYear);
				// Is Discount End Day a valid Date
				Boolean isEndDateValid = valid.isDateValid(endDay, endMonth, endYear);
				
		
				
				
				// Add the additional discount information into "discountInfo"
				ArrayList<DiscountDTOGiri> discountInfo = (ArrayList<DiscountDTOGiri>) session.getAttribute("discountInfo");
					
				// Set Discount Start Date
				discountInfo.get(counter).setStartDateDay(startDay);
				discountInfo.get(counter).setStartDateMonth(startMonth);
				discountInfo.get(counter).setStartDateYear(startYear);
				
				// Set Discount End Date
				discountInfo.get(counter).setEndDateDay(endDay);
				discountInfo.get(counter).setEndDateMonth(endMonth);
				discountInfo.get(counter).setEndDateYear(endYear);
				
				//Set Discount
				discountInfo.get(counter).setDiscount(discount);
				
				++counter;
	
				if (!isDiscountValid || !isDiscountStartValid || !isDiscountEndValid || !isDiscountStartBeforeEnd || !isStartDateValid || !isEndDateValid){
					session.setAttribute("discountError", "true");					
				}
			}
			nextPage = "HotelOwnerController";
		}
		else if (action.equals("Back to Welcome Screen")){			
			nextPage = "HotelOwnerController";
		}
		
		 RequestDispatcher rd = request.getRequestDispatcher("/"+nextPage);
		 rd.forward(request, response);
	
	}
	

}
