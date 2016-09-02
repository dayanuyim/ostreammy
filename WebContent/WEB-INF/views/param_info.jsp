<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Repo Info</title>

<style>
	label{
		display: inline-block;
		min-width: 12em;
		font-weight: bold;
	}
	
	li{
		display: inline-block;
		list-style-position:inside;
		background: lightgray;
		border: 1px solid black;
		border-radius: 3px;
		margin: 3px;
		padding: 3px;
	}
</style>

</head>
<body>
	<h2>Param Infomation</h2>

	<c:forEach var="entry" items="${params}">
	  <div>
	  	<label><c:out value="${entry.key}" /></label>
	  	<ul>
			<c:forEach var="value" items="${entry.value}">
				<li>${value}</li>
			</c:forEach>
	  	</ul>
	  	${value}
	  </div>
	</c:forEach>
</body>
</html>