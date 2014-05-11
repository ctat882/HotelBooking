<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reception Manager Welcome</title>
</head>
<body>

<%--
Display all bookings that have not yet been assigned
 --%>

	<c:set var="count" value="0" scope="page" />
		
	<div style="width:1000px" >
		
		<h1 style="text-align:center">${hotelLocation}</h1>
		<h2 style="text-align:center">Unassigned Bookings</h2>
		
		<form action='ReceptionManagerController' method='POST'>
			<table style="width:100%">
				<tr style="text-align:center">
						 <th>Booking ID</th>
						 <th>Check-In</th>
						 <th>Check-Out</th>
						 <th>Type</th>
						 <th>Assign Room?</th>
				</tr>
				<c:forEach var="item" items="${unassignedBookings}">
					<tr style="text-align:center">
						<td>${item.id}</td>
						<td>${item.checkIn}</td>
						<td>${item.checkOut}</td>
						<td>${item.size}</td>
						
						<c:choose>
							<c:when test="${count == 0}">
	   							<td><input type="radio" name="checked" value="${count}" checked='checked'></td>
							</c:when>
							<c:otherwise>
	   							<td><input type="radio" name="checked" value="${count}"></td>
							</c:otherwise>
						</c:choose>
						
						<td><input type="hidden" name="bookingID${count}" value="${item.id}"></td>		
						<td><input type="hidden" name="checkIn${count}" value="${item.checkIn}"></td>	
						<td><input type="hidden" name="checkOut${count}" value="${item.checkOut}"></td>		
						<td><input type="hidden" name="type${count}" value="${item.size}"></td>	
						<c:set var="count" value="${count + 1}" scope="page"/>	
					</tr>	 					
				</c:forEach>
				
				
			</table>
			
			<div style="text-align:center">
				<%-- Only display the 'Assign Booking' button if there are unassigned bookings --%>
				<c:if test="${count != 0}">
					<input  type="submit" name="action" value="Assign Booking"/>
				</c:if>
							
				<input  type="submit" name="action" value="Logout"/>
			</div>
		</form>
		
		<%--
		Display table with assigned Bookings
		--%>
		
		 <h2 style="text-align:center">Assigned Bookings</h2>
		
		<c:set var="count" value="0" scope="page"/>	
		
		<form action='ReceptionManagerController' method='POST'>
			<table style="width:100%">
				<tr style="text-align:center">
						 <th>Booking ID</th>
						 <th>Check-In</th>
						 <th>Check-Out</th>
						 <th>Type</th>
						 <th>Room Number</th>
						 <th>Unassign Room?</th>
				</tr>
				<c:forEach var="item" items="${assignedBookings}">
					<tr style="text-align:center">
						<td>${item.id}</td>
						<td>${item.checkIn}</td>
						<td>${item.checkOut}</td>
						<td>${item.size}</td>
						<td>${item.assignedRoom}</td>
						<c:choose>
							<c:when test="${count == 0}">
	   							<td><input type="radio" name="checked" value="${count}" checked='checked'></td>
							</c:when>
							<c:otherwise>
	   							<td><input type="radio" name="checked" value="${count}"></td>
							</c:otherwise>
						</c:choose>
						
						<td><input type="hidden" name="bookingID${count}" value="${item.id}"></td>		
						<td><input type="hidden" name="checkIn${count}" value="${item.checkIn}"></td>	
						<td><input type="hidden" name="checkOut${count}" value="${item.checkOut}"></td>		
						<td><input type="hidden" name="type${count}" value="${item.size}"></td>	
						<td><input type="hidden" name="assignedRoom${count}" value="${item.assignedRoom}"></td>	
						<c:set var="count" value="${count + 1}" scope="page"/>	
					</tr>	 					
				</c:forEach>
				
				
			</table>
			<div style="text-align:center">
			<%-- Only display the 'Assign Booking' button if there are unassigned bookings --%>
				<c:if test="${count != 0}">
					<input  type="submit" name="action" value="Unassign Booking"/>
				</c:if>
							
				<input  type="submit" name="action" value="Logout"/>
			</div>
		</form>
	
	</div>


</body>
</html>