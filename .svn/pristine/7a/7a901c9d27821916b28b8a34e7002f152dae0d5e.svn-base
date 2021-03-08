<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Payment through BracBank Gateway</title>
</head>

<body bgcolor="#F0F0F0" onLoad="document.send_form.submit();">
	 <h1 align="center">Please wait, Payment is processing</h1>
	 
     <form name="send_form" method="post" action="<s:property value="url"/>">
         <input type="hidden" value="<%=session.getAttribute("encryptedInvoce") %>" name="encryptedInvoicePay">
     </form>
 	
	<!-- Trans ID <s:property value="transID"/>
	<br> 
	Amount: <s:property value="amount"/>
	<br>
	Gateway <s:property value="gateway"/>-->
	
</body>

</html>