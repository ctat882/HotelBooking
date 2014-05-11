<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="cart" class="edu.unsw.comp9321.logic.CartBean" scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Checkout</title>
</head>
<body>
<c:if test="${error eq 'true' }">
	<font color="red">
	<c:out value="${msg}"></c:out>
	</font>
</c:if>

<form action="Controller" method="post">
	<table>
		<c:set var="seen_single" value="false"/>
		<c:set var="seen_twin" value="false"/>
		<c:set var="seen_queen" value="false"/>
		<c:set var="seen_exec" value="false"/>
		<c:set var="seen_suite" value="false"/>
		<c:forEach items="${booking}" var="b" >
			
			<c:if test="${b.size eq 'Single' }">
				<c:if test="${seen_single eq 'false' }">
					<tr>
					<td>
						<c:out value="${SingleCount}"/>
					 	x Single Room = $
					 	<c:out value="${SingleTotal}"/>
				 	</td>
				 	</tr>
				 	<c:set var="seen_single" value="true"/>
				</c:if>
			</c:if>
			<c:if test="${b.size eq 'Twin' }">
				<c:if test="${seen_twin eq 'false' }">
					<tr>
					<td>
						<c:out value="${TwinCount}"/>
					 	x Twin Room = $
					 	<c:out value="${TwinTotal}"/>
				 	</td>
				 	</tr>
				 	<c:set var="seen_twin" value="true"/>
				</c:if>
			</c:if>
			<c:if test="${b.size eq 'Queen' }">
				<c:if test="${seen_queen eq 'false' }">
					<tr>
					<td>
						<c:out value="${QueenCount}"/>
					 	x Queen Room = $
					 	<c:out value="${QueenTotal}"/>
				 	</td>
				 	</tr>
				 	<c:set var="seen_queen" value="true"/>
				</c:if>
			</c:if>
			<c:if test="${b.size eq 'Executive' }">
				<c:if test="${seen_exec eq 'false' }">
					<tr>
					<td>
						<c:out value="${ExecutiveCount}"/>
					 	x Executive Room = $
					 	<c:out value="${ExecutiveTotal}"/>
				 	</td>
				 	</tr>
				 	<c:set var="seen_exec" value="true"/>
				</c:if>
			</c:if>
			<c:if test="${b.size eq 'Suite' }">
				<c:if test="${seen_suite eq 'false' }">
					<tr>
					<td>
						<c:out value="${SuiteCount}"/>
					 	x Suite Room = $
					 	<c:out value="${SuiteTotal}"/>
				 	</td>
				 	</tr>
				 	<c:set var="seen_suite" value="true"/>
				</c:if>
			</c:if>
		</c:forEach>
		<tr><td>TOTAL:<c:out value="${total}"></c:out></td></tr>
		<tr><td>Email:<input type="text" name="email"/></td></tr>
		<tr><td>Card#:<input type="text" name="cardnum"/></td></tr>
		<tr><td><input type="submit" value="Proceed"/></td></tr>
		<tr><td><input type="hidden" name="action" value="Proceed"/></td></tr>
	</table>
</form>
<form action="WelcomePage.jsp">
	<input type="submit" value="Cancel"/>
</form>

</body>
</html>