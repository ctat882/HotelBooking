<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Discount(s) Added Successfully</title>
</head>
<body>

<c:choose>
    <c:when test="${sessionScope.discountAdded == 'true'}">
        <p align="center">  
            All Discount(s) added successfully!     
        </p>
    </c:when>
    <c:when test="${sessionScope.discountAdded == 'false'}">
        <p align="center">
            Error Found! No Discounts added!
        </p>
    </c:when>
</c:choose>





			
	<br>
	<form action='HotelOwnerController' method='POST' style="text-align:center">
		<input  type="submit" name="action" value="Back to Welcome Screen"/>
	</form>
	
</body>
</html>