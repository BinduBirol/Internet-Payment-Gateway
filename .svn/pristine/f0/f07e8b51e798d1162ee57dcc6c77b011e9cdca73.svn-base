package com.iftiict.ipg;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

public class ExecuteCommand{
	
	public static String executeMyCommand(String command, String seperator) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line+seperator);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	public static String getConfigData()
	{
		String DBString="";
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("com/iftiict/ipg/config.data");
		try {
			DBString = IOUtils.toString(input, "UTF-8");
		} catch (IOException ee) {}
        
		return DBString;
	}
	public static String getDataFromBracBank(String data, String host, String port)
	{
		if(data.length()>0)
		{
			String encString="";
			
			Socket smtpSocket = null;  
			BufferedReader in = null;
			PrintWriter out = null;
			
			System.out.print(data);
			
			int iport;
	        iport=Integer.parseInt(port);
	        
			try{
				smtpSocket = new Socket(host, iport);
	            
				in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
			    out=new PrintWriter(smtpSocket.getOutputStream(), true);
			}catch (UnknownHostException e) {
	        	encString="<error_msg>Server refused connection;"+host+"</error_msg>";
	        } catch (IOException e) {
			      System.out.println("in or out failed");
			}
			
			try {
	        	smtpSocket.setKeepAlive(true);
	        	smtpSocket.setSoTimeout(10000);
				smtpSocket.setSoLinger(true, 10);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			out.println(data); 
			
			String responseLine;
	        try {
				while ((responseLine = in.readLine()) != null)
				{
					encString += responseLine;
				}
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	
	        out.close();
	        try {
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				smtpSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return encString;
		}
		else return "Processing failed.";
	}
	/*
	 *public static String getDataFromBracBank(String data, String host, String port)
	{
		String encString="";
		
		Socket smtpSocket = null;  
        DataOutputStream os = null;
        DataInputStream is = null;
        
        System.out.print(data);

        int iport;
        iport=Integer.parseInt(port);
        try {
            smtpSocket = new Socket(host, iport);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());
        } catch (UnknownHostException e) {
        	encString="<error_msg>Server refused connection;"+host+"</error_msg>";
        } catch (IOException e) {
        	encString="<error_msg>Server refused connection."+host+"</error_msg>";
        }
        
        try {
        	smtpSocket.setKeepAlive(true);
        	smtpSocket.setSoTimeout(1000);
			smtpSocket.setSoLinger(true, 10);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if (smtpSocket != null && os != null && is != null) {
        	try {
        		try{
					os.writeBytes(data);
					
					os.flush();
        		}
        		catch(Exception ee)
        		{
        			ee.printStackTrace();
        		}
        		
        		String responseLine;
                while ((responseLine = is.readLine()) != null)
                {
                	encString += responseLine;
                }
                os.close();
                is.close();
                smtpSocket.close();   
            } catch (UnknownHostException e) {
            	encString="<error_msg>Unknown host.</error_msg>";
            } catch (Exception e) {
            	encString="<error_msg>IOException."+e.getMessage()+"</error_msg>";
            }
        }
	
		return encString;
	}
	 */
	/*public static String getDataFromBracBank(String data, String host, String port)
	{
		System.out.println(data);
		
		String encString="";
		
		Socket smtpSocket = new Socket();  
		PrintWriter s_out = null;
		BufferedReader s_in = null;

        int iport;
        iport=Integer.parseInt(port);
        try {
			smtpSocket.connect(new InetSocketAddress(host , iport));
		} catch (IOException e) {
			//e.printStackTrace();
		}
		try {
			s_out = new PrintWriter(smtpSocket.getOutputStream(), true);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		try {
			s_in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
		} catch (IOException e) {
			//e.printStackTrace();
		}
        
        s_out.println(data);
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
        
        String responseLine;
        try {
			while ((responseLine = s_in.readLine()) != null)
			{
				encString += responseLine;
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
        
        s_out.close();
        try {
			s_in.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
        try {
			smtpSocket.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
        
        return encString;
	
	}*/
}
