<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="cart" class="edu.unsw.comp9321.logic.CartBean" scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body>

<c:if test="${error eq 'true' }">
	<font color="red">
	<c:out value="${msg}"></c:out>
	</font>
</c:if>
<div style="width:1000px">

	<form action='Controller' method='POST'> 

		<table style="border-collapse:collapse">
			<tr style="text-align:center">
				<th>City</th>
				<th colspan="3">Check In</th>
				<th colspan="3">Check Out</th>
				<th>Max Spend Per Night</th>
				<th>Number of Rooms</th>						
			</tr>
				<tr style="text-align:center">
					<td style="padding-left: 20px">
						<!--  <input type="text" name="city" /> -->
						<select name="city">
							<c:forEach var="hotel" items="${hotels}">
								<option value="${hotel}">${hotel}</option>
							</c:forEach>
						</select>
					</td>
					 <td style="padding-left: 20px"><select name="inDay">
					 
					 <c:forEach begin="1" end="31" var="val">						 
  							  <option value="${val}">${val}</option>
					</c:forEach>		
									
					 </select></td>
					 <td><select name="inMonth">
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
					
					 <td><select name="inYear">						 
					  <c:forEach begin="2014" end="2019" var="val">						 
  							  <option value="${val}">${val}</option>
					</c:forEach>													   
					</select></td>
					
					<td style="padding-left: 20px"><select name="outDay">
						   <c:forEach begin="1" end="31" var="val">						 
  							  	<option value="${val}">${val}</option>
						</c:forEach>
					 </select></td>
					 
					 <td><select name="outMonth">
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
					
					 <td><select name="outYear">
						  <c:forEach begin="2014" end="2019" var="val">						 
  							  	<option value="${val}">${val}</option>
						</c:forEach>							   
					</select></td>
					
					<td style="padding-left: 20px">\$<input type="text" name="maxPrice" value="0.00"/></td>
					<td style="padding-left: 20px"><input type="text" name="numRooms"/></td>		
				</tr>
	
		</table>
		
		<div style="text-align:center">
			<br>	
			<input  type="submit" name="submit" value="Search"/>
			<input type="hidden" name="action" value="Search"/>
		</div>
		
	</form>
</div>


</body>
</html>