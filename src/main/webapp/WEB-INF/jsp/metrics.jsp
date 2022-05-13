<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>FFQ Metrics</title>
</head>
<body style="background-color:White;">
<!-- <img src="https://www.tutorialspoint.com/html/images/test.png" alt="Simply Easy Learning" width="200" height="80"> -->
	<h1 style="border:">
		<form:form action="addmetrics" method="POST" modelAttribute="metrics">
		<!-- <table align="center"> -->	
				<tr>
					<td align="center" colspan="2"><font face="Arial" size="2px"
						color="White"><h1 align="center"
								style="background-color:#251FA3;">FFQ
								Metrics</h1></font></td>
				</tr>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<tr>
					<td><font face="Arial" size="3px" color="Blue"><b>From Date/Time:</b></font></td>
					<td><form:input path="fromDateAndTime" placeholder="YYYY-MM-DD HH:MM:SS"/></td>
				</tr>
				<tr>
					<td><font face="Arial" size="3px" color="Blue"><b>To Date/Time:</b></font></td>
					<td><form:input path="toDateAndTime" placeholder="YYYY-MM-DD HH:MM:SS"/></td>
				</tr>

				<tr>
					<td><font face="Arial" size="3px" color="Blue"><b>States:</b></font></td>
					<td><form:select path="states" items="${stateList}" /></td>
				</tr><br><br>
	                
				<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<td align="center" colspan="2"><input type="submit"
						style="background-color: #FBEEE6;color: black; width: 150px; height: 40px;"
						value="METRICS"></td>
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;
					<input type="submit" name="altSubmit" formaction="addmetrics1" style="background-color: #FBEEE6;color: black; width: 150px; height: 40px;"
					    value="Download Excel"></p>
					</td>	
					
			</table>
		</form:form>
	</h1>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

	<table align="center" cellpadding="5" cellspacing="1" border="1">
		<tr bgcolor="#AED6F1">
			<td><b>LOB's</b>
			<td><b>Quotes Started</b></td>
			<td><b>Quotes Completed</b></td>
			<td><b>Knocked Out Quotes(IID)</b></td>
			<td><b>Knocked Out Quotes</b></td>
			<td><b>OnlineBind</b></td>
			<td><b>OfflineBind</b></td>
			<td><b>Verified Quotes</b></td>
			<td><b>Acxiom</b></td>
		</tr>

		<tr>
			<c:forEach items="${formersList}" var="formersObj">
				<td>${formersObj}</td>
			</c:forEach>
		</tr>
		<tr>
			<c:forEach items="${bristolList}" var="bristolObj">
				<td>${bristolObj}</td>
			</c:forEach>
		</tr>
		<tr>
			<c:forEach items="${rentersList}" var="rentersObj">
				<td>${rentersObj}</td>
			</c:forEach>
		</tr>

		<tr>
			<c:forEach items="${homeList}" var="homeObj">
				<td>${homeObj}</td>
			</c:forEach>
		</tr>
		<tr>
			<c:forEach items="${condoList}" var="condoObj">
				<td>${condoObj}</td>
			</c:forEach>
		</tr>
		<tr>
			<c:forEach items="${businessList}" var="businessObj">
				<td>${businessObj}</td>
			</c:forEach>
		</tr>

		<tr>
			<c:forEach items="${lifeList}" var="lifeObj">
				<td>${lifeObj}</td>
			</c:forEach>
		</tr>
		<tr>
			<c:forEach items="${hqmList}" var="hqmObj">
				<td>${hqmObj}</td>
			</c:forEach>
		</tr>


	</table>
</body>
</html>