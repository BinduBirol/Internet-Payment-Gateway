package com.iftiict.ipg;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class PaymentProcess extends ActionSupport {
	private String txnStatus = "FAILED";
	private String ipAddress, error_msg = "", certLoocation = "/root/000599991160000/";
	private String card_no = "", card_name = "";
	private String transID = "", ipgTrxID = "", approval_code = "",
			trxAmount = "", rrn_code = "", transDate = "";
	private String submitted_host = ""; // , brac_host="",brac_port="";
	private String encryptedReceiptPay = "";

	// for brac bank
	private String txn_status = "", txn_amount = "", transaction_id = "",
			merchant_name = "", masked_acc_no = "", customer_name = "";
	private String bank_ref_id = "", fail_reason = "", currency_code = "";
	private String mer_ref_id = "", mer_var_1 = "", mer_var_2 = "",
			mer_var_3 = "", mer_var_4 = "", image_source = "";

	// DBBL
	private String Ucaf_Cardholder_Confirm = "", card_type = "", trans_id = "";

	// Added by Nasir, for customer Info
	private String customerId = "";
	private String subTotal = "";
	private String trxDate = "";

	// private String TotalAmount = "";

	private void getConfigData() {
		String DBString = ExecuteCommand.getConfigData();
		certLoocation = DBString.substring(DBString.indexOf("<CLOC>") + 6,
				DBString.indexOf("</CLOC>"));
		submitted_host = DBString.substring(DBString.indexOf("<INVOICE>") + 9,
				DBString.indexOf("</INVOICE>"));

		// brac_host=DBString.substring(DBString.indexOf("<BB_SERVER>")+11,DBString.indexOf("</BB_SERVER>"));
		// brac_port=DBString.substring(DBString.indexOf("<BB_PORT>")+9,DBString.indexOf("</BB_PORT>"));
	}

	public void updateData(String query, String where) {
		DBConnection db = new DBConnection();
		try {
			db.updateIPGData(query, where);
		} catch (SQLException e) {
			System.out.println("UPDATE: " + e.getMessage());
		}
	}

	public String success() throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			
			/////print request
			System.out.println("001. REQUEST type: "+request.getMethod().toString());
			Enumeration<String> headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()) {
			  String headerName = headerNames.nextElement();			  
			  System.out.println("002. REQUEST header: Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
			}
			
			Enumeration<String> params = request.getParameterNames(); 
			while(params.hasMoreElements()){
			 String paramName = params.nextElement();
			 System.out.println("003. REQUEST params: Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
			}
			/////ends print req
			
			ipAddress = request.getHeader("X-FORWARD-FOR");
			
			
			
			if (ipAddress == null)
				ipAddress = request.getRemoteAddr();
			System.out.println("(4)--> ipAddress: " + ipAddress);

			this.getConfigData();

			String DBBL_transId = "";

			try {
				HttpSession session = request.getSession(true);
				
				////////////session after visiting dbbl
				Enumeration<String>  e = session.getAttributeNames();
				System.out.println("--------Session after visiting DBBL server------");
		        while ( e.hasMoreElements())
		        {
		            String i= e.nextElement();
		            System.out.println(i+ ": "+session.getValue(i));

		        }	
				
				//transID = session.getAttribute("JGTDSL_transId").toString();
				
				//System.out.println("(5)--> transID: " + transID);
				
				//DBBL_transId = session.getAttribute("DBBL_transId").toString();
				
				
				//if(DBBL_transId.isEmpty()||transID.isEmpty()){
					DBConnection dbc= new DBConnection();
					String [] trans_ids;
					trans_ids= dbc.getDBBL_transId();
					transID= trans_ids[0];
					DBBL_transId= trans_ids[1];
				//}
				System.out.println("check: "+transID+"\t"+DBBL_transId);
				ipgTrxID = DBBL_transId;
			} catch (Exception ee) {
				ee.printStackTrace();
			}

			String command = "java -jar " + this.certLoocation
					+ "ecomm_merchant.jar " + this.certLoocation
					+ "merchant.properties -c " + DBBL_transId + " "
					+ ipAddress;
			String output = ExecuteCommand.executeMyCommand(command, "|");

			// txnStatus="dbbl_success"+output+"<br>"+output.length();

			
			txnStatus = "FAILED";
			
			String customerInfo = "";
			
			String result_code = "001";
			
			if (transID !="") {
				//System.out.println("### From IP ### Before printing customer id: "+ "<--------------------------------->");
				customerInfo = this.getCustomerInfo(transID);
				//customerId = this.getCustomerCode(transID);
				
				String[] customerInfoAr = customerInfo.split(",");
				customerId = customerInfoAr[0];
				subTotal = customerInfoAr[1];
				
				System.out.println("Customer Id: " + customerId);
				System.out.println("Sub Total: " + subTotal);
			} 			
			System.out.println("command: "+command);
			System.out.println("*****###output: " + output + "###*****");
			
			if (output.length() > 0) {
				String[] outputs = { "", "", "", "", "", "", "", "", "", "" };
				int index = 0;

				for (int i = 0; i < output.length(); i++) {
					if (output.charAt(i) == '|')
						index++;
					else
						outputs[index] += output.charAt(i) + "";
				}

				String[] results = outputs[1].split(":");
				result_code = results[1].toString();

				String[] rrn_dtls = outputs[2].split(":");
				rrn_code = rrn_dtls[1];

				// ipgtrxid -- [3]

				String[] apprv_code = outputs[4].split(":");
				approval_code = apprv_code[1];

				String[] card_dlts = outputs[5].split(":");
				card_no = card_dlts[1].trim();

				String[] card_dlts1 = outputs[6].split(":");
				trxAmount = card_dlts1[1];

				String[] card_dtls3 = outputs[8].split(":");
				transDate = card_dtls3[1];
				
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				System.out.println("Transaction Date from response: " + transDate);
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				

				String[] card_dlts2 = outputs[9].split(":");
				card_name = card_dlts2[1].trim();

				if (result_code.contains("000")) {
					txnStatus = "SUCCESS";
					error_msg = "Approved";
				} else {
					result_code = result_code.trim();
					error_msg = this.getMessage(result_code);

					// System.out.println("TRIM:|"+result_code+"|");
				}
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date  = new Date();
			trxDate = dateFormat.format(date).toString(); 
			
			
			System.out.println("*************************************************");
			System.out.println("trxDate: " + trxDate);
			System.out.println("_________________________________________________");

			String query = "TRANS_STATUS='" + txnStatus
					+ "',RETURN_TIME=SYSDATE,CARD_NO='" + card_no
					+ "',CARD_NAME='" + card_name + "',";
			query += "BANK_REF_ID='" + rrn_code + "',MER_VER1='"
					+ approval_code + "',MER_VER2='" + transDate + "',REASON='"
					+ error_msg + "',";
			query += "TRANS_AMT='" + trxAmount + "'";
			this.updateData(query, "TRANS_ID='" + transID + "'");

			System.out.println("query: "+query);
			
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}

	public String failed() throws Exception {
		try {
			txnStatus = "FAILED";
			error_msg = "Transaction cancelled.";
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}

	public String bracbank() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			HttpSession session = request.getSession(true);
			transID = session.getAttribute("JGTDSL_transId").toString();
		} catch (Exception ee) {
		}

		try {
			this.getConfigData();

			// encryptedReceiptPay="52a513931aa283a7ca3bce3ce2c83f246485f9b3e7ad97ca5405c55554e3ba570cd7ecf3e9a95c5cd1a54ba9ef2eaacb6d6c89ae2a9780265e3f29f5ae9cb8a716f15bb750d18f150ee86a1b67f5242fa246982842a5d4d74436e52e11db691d2fe3ab506d2c99c026ef07daa07bb7263fd057f1797599c8acf159eabd6d4b3ae7f1be02f147686cc2bbd5d117b50a3b7d0204a2c7c498366b87714afd87b9779fc2cd50417021ffdc9de40d208cddf049a4700c3ff332ea477ac6f3eed80129470bff28268f9fb94b8a863e5c56cdd9f4f7aca04b132e1992afb118ea02cf2b8e9e322502dd72771d66885516df5870f0b33d426a7cb2409add0b6277d08608516b3155e82bb8a87fec1344a08f3eff72efc7eb2a5e3793238cf27fb145112bc35eb477ba6288a32adca43bee2329263f7988544c2f98893d1a4f8740159e01ef145608286818dd4d5d2900d3da7070b2edbdd32cc6ad69a8a6be5b8031fe2b474510e13dbc7a7546ffd82eaacb651aa4e7054a2178202f404d646eda5342f2fbca8307b255d94521d2ddca8b55092240821dcd77532716e16e83790df98d4e97e4589f7d0c2de8714ea9811bd3dc7894e6514562a182980ab9cfa48b48737eb224ce1ce29ea08dda1bc36c4d8da2461a286e4f03d4d00c25ca92fa72cdb131d29ad61a7480896598b35614f67ca96a46d0efb0e229149d1cece9fd17f3323697f88120f5acc1dcf9523f913fedef4bd24fbcf4a6a1b6b86d37a358c2364f3384a53308ce104a3907a5efbddb1c127368a2620c9e1853cf707dac2e8e97f8af2b51af178f75221cbf893ecdf4b2f19296636e8d187000d2352bf06c29ddeccf0d7a4677383a07a0";

			if (encryptedReceiptPay.length() < 1) {
				error_msg = "Bank responses is not correct.";
				return "error";
			} else {
				/*
				 * String decryptedInvoce="";
				 * decryptedInvoce=ExecuteCommand.getDataFromBracBank
				 * (encryptedReceiptPay, brac_host, brac_port);
				 * 
				 * System.out.println("Receive: "+decryptedInvoce);
				 * 
				 * if(decryptedInvoce.contains("<error_msg>")) {
				 * if(decryptedInvoce.contains("<error_code>")) {
				 * error_msg=decryptedInvoce
				 * .substring(decryptedInvoce.indexOf("<error_code>"
				 * )+12,decryptedInvoce.indexOf("</error_code>"))+","; }
				 * error_msg +=
				 * decryptedInvoce.substring(decryptedInvoce.indexOf
				 * ("<error_msg>")+11,decryptedInvoce.indexOf("</error_msg>"));
				 * return "error"; }
				 */

				error_msg = "";
				// transID=mer_ref_id;
				ipgTrxID = transaction_id;

				rrn_code = bank_ref_id;

				card_no = masked_acc_no;
				card_name = customer_name;

				txnStatus = txn_status;

				trxAmount = txn_amount;
				// approval_code=decryptedInvoce.substring(decryptedInvoce.indexOf("<bank_ref_id>")+13,decryptedInvoce.indexOf("</bank_ref_id>"));
				error_msg = fail_reason;

				if (txnStatus.contains("ACCEPTED")) {
					txnStatus = "SUCCESS";
					error_msg = "";
				} else {
					txnStatus = "FAILED";
				}
				
				
				
				String query = "TRANS_STATUS='" + txnStatus
						+ "',RETURN_TIME=SYSDATE,CARD_NO='" + card_no
						+ "',CARD_NAME='" + card_name + "',";
				query += "BANK_REF_ID='" + rrn_code + "',IPG_TXN_ID='"
						+ ipgTrxID + "',TRANS_AMT='" + trxAmount + "'";
				if (error_msg != "")
					query += ",REASON='" + error_msg + "'";

				this.updateData(query, "TRANS_ID='" + transID + "'");
			}

			return "success";
		} catch (Exception e) {
			return "error";
		}
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String status) {
		this.txnStatus = status;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public String getMessage(String code) {
		String msg = code.toString().trim();

		DBConnection db = new DBConnection();
		try {
			msg = db.getDBBLResponse(code);
		} catch (SQLException e) {
			System.out.println("SELECT: " + e.getMessage());
		}
		return msg;
	}
	
	public String getCustomerInfo(String transId) {
		String response = "";
		String traxId = transId.toString().trim();

		DBConnection db = new DBConnection();
		try {
			response = db.getCustomerInfo(traxId);
		} catch (Exception ex) {
			System.out.println("SELECT: " + ex.toString());
		}
		return response;

	}
	

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getIpgTrxID() {
		return ipgTrxID;
	}

	public void setIpgTrxID(String ipgTrxID) {
		this.ipgTrxID = ipgTrxID;
	}

	public String getApproval_code() {
		return approval_code;
	}

	public void setApproval_code(String approval_code) {
		this.approval_code = approval_code;
	}

	public String getTrxAmount() {
		return trxAmount;
	}

	public void setTrxAmount(String trxAmount) {
		this.trxAmount = trxAmount;
	}

	public String getTransID() {
		return transID;
	}

	public void setTransID(String transID) {
		this.transID = transID;
	}

	public String getSubmitted_host() {
		return submitted_host;
	}

	public void setSubmitted_host(String submitted_host) {
		this.submitted_host = submitted_host;
	}

	public String getEncryptedReceiptPay() {
		return encryptedReceiptPay;
	}

	public void setEncryptedReceiptPay(String encryptedReceiptPay) {
		this.encryptedReceiptPay = encryptedReceiptPay;
	}

	public String getTxn_status() {
		return txn_status;
	}

	public void setTxn_status(String txn_status) {
		this.txn_status = txn_status;
	}

	public String getTxn_amount() {
		return txn_amount;
	}

	public void setTxn_amount(String txn_amount) {
		this.txn_amount = txn_amount;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getMer_var_4() {
		return mer_var_4;
	}

	public void setMer_var_4(String mer_var_4) {
		this.mer_var_4 = mer_var_4;
	}

	public String getMer_var_1() {
		return mer_var_1;
	}

	public void setMer_var_1(String mer_var_1) {
		this.mer_var_1 = mer_var_1;
	}

	public String getMer_var_2() {
		return mer_var_2;
	}

	public void setMer_var_2(String mer_var_2) {
		this.mer_var_2 = mer_var_2;
	}

	public String getMer_var_3() {
		return mer_var_3;
	}

	public void setMer_var_3(String mer_var_3) {
		this.mer_var_3 = mer_var_3;
	}

	public String getMer_ref_id() {
		return mer_ref_id;
	}

	public void setMer_ref_id(String mer_ref_id) {
		this.mer_ref_id = mer_ref_id;
	}

	public String getMasked_acc_no() {
		return masked_acc_no;
	}

	public void setMasked_acc_no(String masked_acc_no) {
		this.masked_acc_no = masked_acc_no;
	}

	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String fail_reason) {
		this.fail_reason = fail_reason;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getBank_ref_id() {
		return bank_ref_id;
	}

	public void setBank_ref_id(String bank_ref_id) {
		this.bank_ref_id = bank_ref_id;
	}

	public String getImage_source() {
		return image_source;
	}

	public void setImage_source(String image_source) {
		this.image_source = image_source;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getUcaf_Cardholder_Confirm() {
		return Ucaf_Cardholder_Confirm;
	}

	public void setUcaf_Cardholder_Confirm(String ucaf_Cardholder_Confirm) {
		Ucaf_Cardholder_Confirm = ucaf_Cardholder_Confirm;
	}

	public String getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}

	

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * @return the subTotal
	 */
	public String getSubTotal() {
		return subTotal;
	}

	/**
	 * @param subTotal the subTotal to set
	 */
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * @return the trxDate
	 */
	public String getTrxDate() {
		return trxDate;
	}

	/**
	 * @param trxDate the trxDate to set
	 */
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	
	
	
}
