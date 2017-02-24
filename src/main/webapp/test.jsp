<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<title>测试</title>
</head>
<body>
<h2>File Upload</h2>
		<form id="fileuploadForm" action="user/upload/headImg.json?uid=2" method="POST" enctype="multipart/form-data">
		<!-- <input id="uid" name="uid" value="40" type="hidden" /> -->
			<div class="header">
		  		<h2>Form</h2>
			</div>
			<label for="file">File</label>
			<input id="file" type="file" name="file" />
			<p><button type="submit">Upload</button></p>		
		</form>
</body>
</html>