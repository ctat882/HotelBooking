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

	<c:forEach begin="1" end="${noHotels}" varStatus="loop">		

		<h1 align="center">${hotelLoc[loop.index - 1]}</h1>
		
		
		<form action='cartController' method='POST'>                      <%-- ADJUST METHOD!!!!!!!!!!!!!!!!!!!!!!!  --%>     
				<table style="width:1000px" style="text-align:center">
					<tr style="text-align:center">
						<th>Room Type</th>
						<th>Occupied</th>
						<th>Available</th>						
					</tr>
					
					<c:forEach var="item" items="${hotelOccupancy}">					
						 <c:if test="${item.hotelID == loop.index}">   								
							<tr style="text-align:center">
							  <td>${item.roomType}</td>
							  <td>${item.occupied}</td>
							  <td>${item.available}</td>	  
							</tr>
						</c:if>
					</c:forEach>
				</table>				
		</form>
		
	</c:forEach>
	

	
</body>
</html>
