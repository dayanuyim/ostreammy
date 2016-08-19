<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Repo Info</title>

<style>
	label{
		float: left;
		width: 5em;
	}
</style>

</head>
<body>
	<h2>Reposiotry Infomation</h2>

	<div><label>repo</label>${repo.absolutePath}</div>

	<div><label>preload</label>${preload.absolutePath}</div>
</body>
</html>