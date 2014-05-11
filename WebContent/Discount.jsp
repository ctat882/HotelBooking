<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<c:set var="count" value="0" scope="page" />
	
	
	<div style="width:1000px">
	
		<form action='DiscountServlet' method='POST'> 
	
			<table style="border-collapse:collapse">
				<tr style="text-align:center">
					<th>City</th>
					<th>Room Type</th>
					<th colspan="3">Start Date</th>
					<th colspan="3">End Date</th>
					<th>Discount %</th>						
				</tr>
				<c:forEach var="item" items="${discountInfo}">
					<tr style="text-align:center">
						<td style="padding-left: 20px">${item.hotelLocation}</td>
						 <td style="padding-left: 20px">${item.roomType}</td>
						 
						 <td style="padding-left: 20px"><select name="startDay${count}">						 
							 <c:forEach begin="1" end="31" var="val">						 
	   							  <option value="${val}">${val}</option>
							</c:forEach>												
						 </select></td>
						 
						 <td><select name="startMonth${count}">
							    <option value="01">January</option>
							    <option value="02">February</option>
							    <option value="03">March</option>
							    <option value="04">April</option>
							    <option value="05">May</option>
							    <option value="06">June</option>
							    <option value="07">July</option>
							    <option value="08">August</option>
							    <option value="09">September</option>
							    <option value="10">October</option>
							    <option value="11">November</option>
							    <option value="12">December</option>
						</select></td>
						
						 <td><select name="startYear${count}">						 
						  <c:forEach begin="2014" end="2019" var="val">						 
   							  <option value="${val}">${val}</option>
						</c:forEach>													   
						</select></td>
						
						<td style="padding-left: 20px"><select name="endDay${count}">
							   <c:forEach begin="1" end="31" var="val">						 
   							  	<option value="${val}">${val}</option>
							</c:forEach>
						 </select></td>
						 
						 <td><select name="endMonth${count}">
							    <option value="01">January</option>
							    <option value="02">February</option>
							    <option value="03">March</option>
							    <option value="04">April</option>
							    <option value="05">May</option>
							    <option value="06">June</option>
							    <option value="07">July</option>
							    <option value="08">August</option>
							    <option value="09">September</option>
							    <option value="10">October</option>
							    <option value="11">November</option>
							    <option value="12">December</option>
						</select></td>
						
						 <td><select name="endYear${count}">
							  <c:forEach begin="2014" end="2019" var="val">						 
   							  	<option value="${val}">${val}</option>
							</c:forEach>							   
						</select></td>
						
						<td style="padding-left: 20px"><input type="text" name="discountAmt${count}" value="0"></td>
						<td><input type="hidden" name="id${count}"></td>					
					</tr>
					<c:set var="count" value="${count + 1}" scope="page"/>	
				</c:forEach>			
			</table>
			
			<div style="text-align:center">
				<br>	
				<input  type="submit" name="action" value="Update Discount(s)"/>
				<input  type="submit" name="action" value="Back to Welcome Screen"/>
			</div>
			
		</form>
	</div>
</body>
</html>