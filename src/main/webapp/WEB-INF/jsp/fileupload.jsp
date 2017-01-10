<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>fileupload | mvc-showcase</title>
	<%-- <link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script>	 --%>
</head>
<body>
	<div id="fileuploadContent">
		<h2>File Upload</h2>
		<!--
		    File Uploads must include CSRF in the URL.
		    See http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/#csrf-multipart
		-->
		<form id="fileuploadForm" action="bds/upload/avatar" method="POST" enctype="multipart/form-data" class="cleanform">
			<div class="header">
		  		<h2>Form</h2>
			</div>
			<label for="file">File</label>
			<input id="file" type="file" name="file" />
			<p><button type="submit">Upload</button></p>		
		</form>
		<script type="text/javascript">
			/* $(document).ready(function() {
				$('<input type="hidden" name="ajaxUpload" value="true" />').insertAfter($("#file"));
				$("#fileuploadForm").ajaxForm({ success: function(html) {
						$("#fileuploadContent").replaceWith(html);
					}
				});
			}); */
		</script>	
	</div>
</body>
</html>
