<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Payment through DBBL Gateway</title>
</head>
<body bgcolor="#F0F0F0" onLoad="document.trans_form.submit();">
	<h1 align="center">Please wait, Payment is checking...</h1>
	
	<form name="trans_form" id="frm_transaction" action="<s:property value="submitted_host"/>" method="post">
		<input type="hidden" name="txnStatus" value="<s:property value="txnStatus"/>">
		<input type="hidden" name="transID" value="<s:property value="transID"/>">
		<input type="hidden" name="ipgTrxID" value="<s:property value="ipgTrxID"/>">
		<input type="hidden" name="error_msg" value="<s:property value="error_msg"/>">		
		<input type="hidden" name="card_no" value="<s:property value="card_no"/>">
		<input type="hidden" name="card_name" value="<s:property value="card_name"/>">
		
		<input type="hidden" name="trxAmount" value="<s:property value="trxAmount"/>">
		<input type="hidden" name="customerId" value="<s:property value="customerId"/>">
		<input type="hidden" name="subTotal" value="<s:property value="subTotal"/>">
		<input type="hidden" name="trxDate" value="<s:property value="trxDate"/>">
	</form>
	
	<!-- Trans ID: <s:property value="transID"/><br>	
	Status: <strong><s:property value="txnStatus"/></strong>
	<br>
	IPG TrxID: <s:property value="ipgTrxID"/>
	<br>
	Message: <s:property value="error_msg"/>
	<br>
	Card No: <s:property value="card_no"/>
	<br>
	Card Name: <s:property value="card_name"/>-->
</body>

<script>
//alert('Payment_Process');
</script>
</html>