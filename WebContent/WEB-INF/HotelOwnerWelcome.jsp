<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotel Occupancy</title>
</head>
<body>

	<c:set var="count" value="0" scope="page" />

		<div style="width:1000px">
		
		<form action='HotelOwnerController' method='POST'>  
		
			<c:forEach begin="1" end="${noHotels}" varStatus="loop">		
				
					<h1 style="text-align:center">${hotelLoc[loop.index - 1]} </h1>                     
					<table style="width:100%">
						<tr style="text-align:center">
							<th>Room Type</th>
							<th>Occupied</th>
							<th>Available</th>
							<th>Add Discount?</th>						
						</tr>
							
						<c:forEach var="item" items="${hotelOccupancy}">					
							 <c:if test="${item.hotelID == loop.index}">   								
								<tr style="text-align:center">
								  <td>${item.roomType}</td>
								  <td>${item.occupied}</td>
								  <td>${item.available}</td>
								  <td><input type="checkbox" name="checked${count}"></td>
								   <td><input type="hidden" name="id${count}"></td>								  
								   <td><input type="hidden" name="hotelLocation${count}" value="${item.hotelLocation}"></td>	
								   <td><input type="hidden" name="roomType${count}" value="${item.roomType}"></td>
								   <td><input type="hidden" name="hotelID${count}" value="${item.hotelID}"></td>	
								   <c:set var="count" value="${count + 1}" scope="page"/>		 	  
								</tr>
							</c:if>							
						</c:forEach>
						
					</table>
				
			</c:forEach>
			
			<div style="text-align:center">
				<br>
			 	<input  type="submit" name="action" value="Add Discount(s)"/>
			</div>
							
		</form>
		
		</div>

	
	
	
	
</body>
</html>