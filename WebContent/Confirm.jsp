<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Confirm Discounts</title>
</head>
<body>

		<form action='HotelOwnerController' method='POST'>
		
					<h1 style="text-align:center">Confirm Discount(s)</h1>                     
					<table style="width:100%">
						<tr style="text-align:center">
							<th>City</th>
							<th>Room Type</th>
							<th>Start Date</th>
							<th>End Date</th>
							<th>Discount %</th>					
						</tr>
							
						<c:forEach var="item" items="${discountInfo}">
								<tr style="text-align:center">
								  <td>${item.hotelLocation}</td>
								  <td>${item.roomType}</td>
								  <td>${item.startDateDay}-${item.startDateMonth}-${item.startDateYear}</td>
								  <td>${item.endDateDay}-${item.endDateMonth}-${item.endDateYear}</td>		
								  <td>${item.discount}</td>						 	 	  
								</tr>				
						</c:forEach>
						
					</table>				
			
			<div style="text-align:center">
				<br>	
				<input  type="submit" name="action" value="Confirm Discount(s)"/>
				<input  type="submit" name="action" value="Back to Welcome Screen"/>
			</div>
							
		</form>



</body>

</html>