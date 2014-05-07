<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reception Manager Login</title>
</head>
<body>
	<h1 style="text-align:center">Reception Manager Login</h1>
	    

	<div style="text-align:center">
		<form action='ReceptionManagerController' method='POST'>
			User Name: <input type="text" name="user"><br>
		    Password: <input type="password" name="password"><br>
			
			<select name="hotelLoc">
				<c:forEach var="item" items="${hotelLoc}">
					<option value="${item}">${item}</option>					
				</c:forEach>
			</select>
			<br>
			<input  type="submit" name="action" value="Login"/>
		</form>
	</div>
</body>
</html>