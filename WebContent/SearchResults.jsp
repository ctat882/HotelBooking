<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="cart" class="edu.unsw.comp9321.logic.CartBean" scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Rooms Available</title>
</head>
<body>
<form action="Controller" method="post">
<table>
<c:set var="counter" value="0"/>
<c:forEach items="${results}" var="i">	
	
	<tr>
		<c:set var="extra" value="false"/>
		<c:forEach var="j" items="${i}">
			<td><c:out value="${j.size}"/></td>
			<td><c:out value="${j.price}"/></td>
			<c:if test="${j.size ne 'Single' }">
				<c:set var="extra" value="true"></c:set>
			</c:if>			
		</c:forEach>	
		<c:if test="${extra eq 'true'}">
			<td><input type="checkbox" name="extra_bed" value="${counter}"/></td>
		</c:if>
		<c:if test="${extra eq 'false'}">
			<td><input type="checkbox" disabled/></td>
		</c:if>
		<td><input type="radio" name="choice" value="${counter }"/></td>
	</tr>
	<c:set var="counter" value="${counter + 1 }"/>
	<c:set var="extra" value="false"/>

</c:forEach>
<tr><td><input type="submit" value="Confirm"/></td></tr>
<tr><td><input type="hidden" name="action" value="Confirm"/></td></tr>
</table>
</form>
<form action="Welcome.jsp">
	<input type="submit" value="Back to Search"/>
</form>
</body>
</html>