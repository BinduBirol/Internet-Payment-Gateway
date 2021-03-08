package com.iftiict.ipg;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class MakePayment extends ActionSupport 
{
	public String url="";
	private String transId, amount="", gateway="",ipAddress="";
	private String error_msg="",certLoocation = "/root/000599991160000/";
	private Double dbl_amount=0d;
	private int card_type=1;
	
	public void insertData(String in_amount,String msg)
	{
		DBConnection db=new DBConnection();
		try {
			db.insertIPGData(transId, ipAddress, gateway, in_amount, msg);
		} catch (SQLException e)
		{
			System.out.println("INSERT error: "+e.getMessage());
		}
	}
	
	public String process() throws Exception
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		ipAddress = request.getHeader("X-FORWARD-FOR");
		if(ipAddress==null)
			ipAddress = request.getRemoteAddr();
		
		HttpSession session = request.getSession(true);
		
		System.out.println("(1)From Make Payment--> amount:" + amount);
		
		if(this.amount.length()==0)
		{
			error_msg="Provided amount is not correct.";
			session.setAttribute("error_msg", error_msg);
			return "error";
		}
		else
		{
			try{
				dbl_amount=Double.parseDouble(this.amount);
			}
			catch(Exception ee){
				error_msg="Provided amount is not correct*.";
				session.setAttribute("error_msg", error_msg);
				return "error";
			}
		}
			
		if(this.transId.length()==0)
		{
			error_msg="Transaction ID is not correct.";
			session.setAttribute("error_msg", error_msg);
			return "error";
		}
		
		if(this.gateway!="")
		{	
			/**
			 * Embedding the amount and other required field within transaction id for DBBL
			 */
			if(this.gateway.indexOf("DBBL")>=0)
			{
				
				System.out.println("*****From IPG_JGTDSL*****");
				
				amount = (Math.round(dbl_amount*100)) + "";
				
				if(this.gateway.indexOf("VISA")>0)
					card_type=4;
				else if(this.gateway.indexOf("MASTER")>0)
					card_type=5;
				else if(this.gateway.indexOf("MOBILE")>0)
					card_type=6;
				
				String DBString=ExecuteCommand.getConfigData();
				url=DBString.substring(DBString.indexOf("<DBBL>")+6,DBString.indexOf("</DBBL>"));
				certLoocation=DBString.substring(DBString.indexOf("<CLOC>")+6,DBString.indexOf("</CLOC>"));
				
				/**
				 * Embedding amount and other important required field to transaction id using command
				 */
				String command = "java -jar "+this.certLoocation+"ecomm_merchant.jar "+this.certLoocation+"merchant.properties -v "+amount+" 050 "+ipAddress+" "+card_type+"UtilityBillPayment";
				String output = ExecuteCommand.executeMyCommand(command,"");
				
				System.out.println("1. "+command);
				//gateway="DBBL | IP: "+ipAddress+", Output: ("+output+")"+command;
				//gateway +="CARD: "+card_type;
				
				System.out.println("____________________________________________________________");
				System.out.println("command output before sending to gateway: " + output);
				System.out.println("____________________________________________________________");
				
				if(output.indexOf("TRANSACTION_ID:")>=0)
				{
					//gateway +="Trx ID: "+output.substring(16);
					System.out.println("----------session block------");
					String dbbl_trans_id=output.substring(16);

		            session.setAttribute("JGTDSL_transId", transId);
		            session.setAttribute("DBBL_transId", dbbl_trans_id);
		            this.insertData(dbl_amount+"",dbbl_trans_id);
					dbbl_trans_id=URLEncoder.encode(dbbl_trans_id, "UTF-8");
					
					url +="card_type="+card_type+"&trans_id="+dbbl_trans_id;
					
					//gateway +=url;
					
					//this.insertData(dbl_amount+"",dbbl_trans_id);
					
					//////////////session print
					Enumeration<String>  e = session.getAttributeNames();

			        while ( e.hasMoreElements())
			        {
			            String i= e.nextElement();
			            System.out.println(i+ ": "+session.getValue(i));

			        }					
					System.out.println("---------session block ends------------");
					return "redirect_dbbl";
				}
				else
				{
					error_msg="Trx ID: "+transId+", DBBL Trx ID Generation Failed:"+output;
					//CreateLog.WriteToFile("Trx ID: "+transId+", DBBL Trx ID Gen Failed:"+output);
					session.setAttribute("error_msg", error_msg);
					return "error";
				}
				//return "success";
			}
			else if(this.gateway.indexOf("BRACBANK")>=0)
			{
				String DBString=ExecuteCommand.getConfigData();
				url=DBString.substring(DBString.indexOf("<BRAC>")+6,DBString.indexOf("</BRAC>"));
				certLoocation=DBString.substring(DBString.indexOf("<RURL>")+6,DBString.indexOf("</RURL>"));//reutrn URL
				
	            amount = String.format("%.2f", dbl_amount);
	            
	            String invoice="",host="", port="";
	            
	            host=DBString.substring(DBString.indexOf("<BB_SERVER>")+11,DBString.indexOf("</BB_SERVER>"));
	    		port=DBString.substring(DBString.indexOf("<BB_PORT>")+9,DBString.indexOf("</BB_PORT>"));	            
	            
	            invoice += "<req><mer_id>JGTDC</mer_id><mer_txn_id>"+System.currentTimeMillis()+"</mer_txn_id><action>SaleTxn</action>";
	            invoice += "<txn_amt>"+amount+"</txn_amt><cur>BDT</cur><lang>en</lang>";
	            invoice += "<ret_url>"+certLoocation+"</ret_url>";
	            //invoice += "<mer_var1></mer_var1><mer_var2></mer_var2><mer_var3></mer_var3><mer_var4></mer_var4>";
	            invoice += "</req>";
	            
	            //System.out.println(invoice);
	            //System.out.println(host);
	            //System.out.println(port);
	            
	            String encryptedInvoce="";
	            encryptedInvoce=ExecuteCommand.getDataFromBracBank(invoice, host, port);
	            
	            session.setAttribute("JGTDSL_transId", transId);
	            session.setAttribute("encryptedInvoce", encryptedInvoce);
	            
	            if(encryptedInvoce.contains("<error_msg>"))
	            {
					if(encryptedInvoce.contains("<error_code>"))
					{
						error_msg=encryptedInvoce.substring(encryptedInvoce.indexOf("<error_code>")+12,encryptedInvoce.indexOf("</error_code>"))+",";
					}
	            	error_msg +=encryptedInvoce.substring(encryptedInvoce.indexOf("<error_msg>")+11,encryptedInvoce.indexOf("</error_msg>"));
	            	
	            	session.setAttribute("error_msg", error_msg);
					return "error";
	            }
				//gateway=invoice;
	            
	            this.insertData(amount,"");
	            
				return "submit_brac_bank";
			}
			else
			{
				error_msg="Provided payment method is not available.";
				session.setAttribute("error_msg", error_msg);
				return "error";
			}
		}
		else{ error_msg="Payment method empty."; session.setAttribute("error_msg", error_msg);return "error";}
		
		//return "success";
	}
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String msg) {
		this.error_msg = msg;
	}
}
