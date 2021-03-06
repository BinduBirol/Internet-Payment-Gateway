package com.iftiict.ipg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection
{
	private Connection connection = null;
	Statement statement = null;
	
	public boolean setDBConnection()
	{
		String DBString=ExecuteCommand.getConfigData();
		
		String user="", pass="", host="", sid="", port="1521";
		
		user=DBString.substring(DBString.indexOf("<USER>")+6,DBString.indexOf("</USER>"));
		pass=DBString.substring(DBString.indexOf("<PASS>")+6,DBString.indexOf("</PASS>"));
		host=DBString.substring(DBString.indexOf("<HOST>")+6,DBString.indexOf("</HOST>"));
		sid=DBString.substring(DBString.indexOf("<SID>")+5,DBString.indexOf("</SID>"));
		port=DBString.substring(DBString.indexOf("<PORT>")+6,DBString.indexOf("</PORT>"));
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//System.out.println("Oracle JDBC Driver Registered!");
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			return false;
		}
		
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@"+host+":"+port+":"+sid+"",user,pass);
			//System.out.println("You made it, take control your database now!");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public int insertIPGData(String txnID, String ip, String ipg,String amount,String ipg_txn_id) throws SQLException
	{
		
		if(this.setDBConnection())
		{
			String sql = "INSERT INTO TBL_IPG_PAYMENT_LOG(TRANS_ID,REQUEST_FROM_IP,IPG_NAME,TRANS_AMOUNT,IPG_TXN_ID)";
			sql += " VALUES('"+txnID+"','"+ip+"','"+ipg+"','"+amount+"','"+ipg_txn_id+"')";

			//System.out.println(sql);
			
			try {
				statement = connection.createStatement();
				statement.executeUpdate(sql);
				System.out.println("(3)From insertIPGData-->" + sql);
			} catch (SQLException e)
			{
				System.out.println("Bindu & Nasir"+e.getMessage());
			}
			try {
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
			
			return 1;
		}
		return 0;
	}
	public int updateIPGData(String query, String where) throws SQLException
	{
		if(this.setDBConnection())
		{
			String sql = "UPDATE TBL_IPG_PAYMENT_LOG SET "+query+" WHERE "+where;

			//System.out.println(sql);
			
			try {
				statement = connection.createStatement();
				statement.executeUpdate(sql);
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
			try {
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
			
			return 1;
		}
		return 0;
	}
	public String getDBBLResponse(String code) throws SQLException
	{
		String msg=code;
		
		if(this.setDBConnection())
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT RESULT_DESC FROM TBL_IPG_RESULT_CODE WHERE IPG='DBBL' AND RESULT_CODE='"+code+"'");
			while (rs.next())
			{
				msg = rs.getString("RESULT_DESC");
			}
			try {
				if(rs!=null)
					rs.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}
		try {
			if(statement!=null)
				statement.close();
			if(connection!=null)
				connection.close();
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	
		return msg;
		
	}
	public String getCustomerInfo(String transId) throws SQLException
	{
		String customerId="";
		String subTotal="";
		String response = "";
		
		System.out.println("------------From IPG -------- getCustomerInfo()--------------");
		
		if(this.setDBConnection())
		{
			statement = connection.createStatement();
			//String sql = "SELECT CUSTOMER_ID FROM IPG_DTL WHERE TRANSACTION_ID = '"+transId+"'";
			
			String sql = "SELECT CUSTOMER_ID, TOTAL_AMOUNT FROM IPG_MST WHERE TRANSACTION_ID = '"+transId+"'";
			
			System.out.println("Final SQL:"+sql);
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next())
			{
				//System.out.println("Hi! Query esecuted and returned result set");
				customerId = rs.getString("CUSTOMER_ID");
				subTotal = rs.getString("TOTAL_AMOUNT");
			}
			response = customerId + "," + subTotal;
			try {
				if(rs!=null)
					rs.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}
		try {
			if(statement!=null)
				statement.close();
			if(connection!=null)
				connection.close();
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	
		//return customerId;
		
		return response;
		
	}
	
public String[] getDBBL_transId() {
		
		
		String sql = " select * from TBL_IPG_PAYMENT_LOG where INSERT_TIME in (  select max(INSERT_TIME) from TBL_IPG_PAYMENT_LOG ) ";
		String[] trans_ids = new String[2];
		if(this.setDBConnection()){
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()){				
				trans_ids[0]= rs.getString("TRANS_ID");
				trans_ids[1]= rs.getString("IPG_TXN_ID");
			}
			
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		}
		
		return trans_ids;
	}

}
