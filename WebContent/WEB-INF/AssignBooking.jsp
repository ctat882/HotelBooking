<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Assign Booking</title>
</head>
<body>
		
	
	<div style="width:1000px" style="text-align:center">
		<h1 style="text-align:center">Assign Booking</h1>
		<h2 style="text-align:center">${hotelLocation}</h2>
				
		<form action='ReceptionManagerController' method='POST'>
			<table style="width:100%">
				<tr style="text-align:center">
						 <th>Booking ID</th>
						 <th>Check-In</th>
						 <th>Check-Out</th>
						 <th>Type</th>
						 <th>Available Rooms</th>
				</tr>
				
					<tr style="text-align:center">
						<td>${bookingID}</td>
						<td>${checkInDate}</td>
						<td>${checkOutDate}</td>
						<td>${roomType}</td>
						<td>
							<select name="assignedRoomNo">
								<c:forEach var="item" items="${availableRooms}">		
									<option value="${item}">${item}</option>									
								</c:forEach>
							</select>
						</td>
						
					</tr>	 					
				
			</table>
			<div style="text-align:center">
				<input  type="submit" name="action" value="Confirm Booking"/>
				<input  type="submit" name="action" value="Back to Welcome"/>
				<input  type="submit" name="action" value="Logout"/>
			</div>
		</form>
		
	</div>
</body>
</html>