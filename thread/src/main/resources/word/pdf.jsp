<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.tri.user.model.*"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html public "-//w3c//dtd xhtml 1.0 transitional//en" "http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <title>在线预览</title>
    <script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>
    <script type="text/javascript">
	var path = "${path}";
	$(function() {
		var cmnPath = "<%=basePath%>";
		var frame = document.getElementById("pdfIframe");
		path = cmnPath + "office/openPdf?path=" + encodeURI(encodeURI(path));
		$("#pdfContent").append("<iframe style='height:640px;width:100%;' src='"+path+"'></iframe>");
		/* alert(path);
		frame.src = path;
		frame.contentWindow.location.reload(true); */
	});
</script>
    <style>
        html,body {
            height: 100%;
        }
    </style>
</head>
<body>
<span id="pdfContent">
		<!-- <iframe id="pdfIframe" style="height:640px;width:100%;">


		</iframe>	 -->
	</span>
</body>
</html>

