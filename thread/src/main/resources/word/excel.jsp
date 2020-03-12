<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.uqiansoft.web.util.StringUtils" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String declareno = StringUtils.convertNulltoString(request.getAttribute("declareno"));
    String selectId = StringUtils.convertNulltoString(request.getAttribute("selectId"));
%>
<!DOCTYPE html public "-//w3c//dtd xhtml 1.0 transitional//en" "http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <title>打开文档</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>style/reset.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>style/tab.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>style/receive/content.css">
    <%@include file="../common.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>style/attachement.css" media="screen" />
    <script type="text/javascript" >
	    function openDocument(url){
	    	var netOfficeEdit = document.getElementById("riseOffice");
	    	netOfficeEdit.OpenFromURL(url,"","Word.Document");
	    	//写入书签
	    	/* var unit = document.getElementById("danwei").value;
	    	var item = document.getElementById("shixiang").value;
	    	netOfficeEdit.SetbookmarkValue('danwei',unit);

	    	netOfficeEdit.SetbookmarkValue('shixiang',item);
	    	 */

		}
	    function save(){
	    	var netOfficeEdit = document.getElementById("riseOffice");
   	        if (netOfficeEdit === null || netOfficeEdit === "undefined") { return false; }
   	        try {
   	             netOfficeEdit.SaveToURL("<%=basePath%>word/insertform?declareno=<%=StringUtils.escapeHTML(declareno, true, true) %>","editfile","","<%=basePath%>word/fawen.doc","form1");
   	         }
   	         catch (err) {
   	            alert("err:" + err.number + ":" + err.description);
   	         } finally { }
	    }

		$(function(){
			var bodyheight = document.documentElement.clientHeight||document.body.clientHeight;
			$("#riseOffice").height(bodyheight);
			if(<%=StringUtils.escapeHTML(selectId, true, true) %> == 1){
				openDocument("<%=basePath%>word/fawen.doc");
			}else if (<%=StringUtils.escapeHTML(selectId, true, true) %> == 2){
				openDocument("<%=basePath%>word/fawenb.doc");
			}else if (<%=StringUtils.escapeHTML(selectId, true, true) %> == 3){
				openDocument("<%=basePath%>word/fawenN.doc");
			}
			var netOfficeEdit = document.getElementById("riseOffice");
			netOfficeEdit.FileNew = false;
			netOfficeEdit.FileOpen = false;
			netOfficeEdit.attachEvent("OnFileCommand",function(cmd,canceled){
				if(cmd == 3){
					save();
				}
				document.getElementById("riseOffice").CancelLastCommand = true;
    		});
		});
    	  </script>
</head>

<body style="overflow: hidden;">
<object id="riseOffice" classid="clsid:A39F1330-3322-4a1d-9BF0-0BA2BB90E970" codeBase="<%=basePath%>ocx/OfficeControl.cab#version=5,0,1,8" width="100%">
    <param name="BorderStyle" value="0">
    <param name="BorderColor" value="14402205">
    <param name="TitlebarColor" value="53668">
    <param name="TitlebarTextColor" value="0">
    <param name="MenubarColor" value="13160660">
    <param name="Caption" value="欢迎使用!">
    <param name="Titlebar" value="0">
    <param name="MaxUploadSize" value="10000000">
    <param name="CustomMenuCaption" value="辅助选项">
    <param name="ProductCaption" value="北京市经济信息中心">
    <param name="ProductKey" value="3B4C6C82E57D04EF01F5B34A84EFC65FEBC645CA">
    <SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>
</object>
</body>
</html>

