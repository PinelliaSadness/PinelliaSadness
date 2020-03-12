<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.uqiansoft.web.util.StringUtils"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    pageContext.setAttribute("ctx", path);
%>
<!DOCTYPE html public "-//w3c//dtd xhtml 1.0 transitional//en" "http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <title>在线预览</title>
    <%@include file="../common.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>js/magnify/jquery.magnify.min.css" media="screen" />

    <script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>
    <%-- <script type="text/javascript" src="<%=basePath%>js/jQueryRotate.js"></script>
     --%>
    <script type="text/javascript" src="<%=basePath%>js/magnify/jquery.magnify.js"></script>
    <script type="text/javascript">
		var cmnPath = "<%=basePath%>";
		var path = "${path}";
		var type = "${type}";
		$(function(){
			var frame = document.getElementById("mutationImage");
			if (type=="image") {
				$("#image_div").show();
	       	 	frame.src = cmnPath+"office/openImage?path="+encodeURI(encodeURI(path));
			} else {
				$("#txtContent").show();
				$("#txtContent").empty();
				url = cmnPath+"office/openOtherTxt?path="+encodeURI(encodeURI(path));
				 $("#txtContent").append('<iframe style="height:600px;width:100%;" src="'+url+'"></iframe>');
			}
		});


		function showImage() {
			var img_url = cmnPath+"office/openImage?path="+encodeURI(encodeURI(path));
       	 	document.getElementById("data_href").href = img_url;
		}
 </script>
</head>
<body>
<span style="display: none;" width="100%;" height="100%;" id="txtContent">

	</span>


<div class="image-set" style="margin-top: 80px;margin: 80px;display: none;" align="center" id="image_div" >
    <a data-magnify="gallery" id="data_href" href="" onclick="showImage()" data-caption="图片预览" >
        <img width="500;" style="text-align: center;" id="mutationImage"  src="" alt="">
    </a>
</div>
</body>
</html>

