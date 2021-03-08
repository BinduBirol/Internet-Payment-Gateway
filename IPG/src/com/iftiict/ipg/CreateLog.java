package com.iftiict.ipg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

public class CreateLog {

	public static void WriteToFile(String data)
	{
		 File f = new File("log/"+getSysDate()+".log");
		    if(!f.getParentFile().exists()){
		        f.getParentFile().mkdirs();
		    }
		    if(!f.exists()){
		        try {
		            f.createNewFile();
		        } catch (Exception e) {
		        }
		    }
		    try {
		        //dir will change directory and specifies file name for writer
		        File dir = new File(f.getParentFile(), f.getName());
		        PrintWriter writer = new PrintWriter(dir);
		        writer.print(data);
		        writer.close();
		    } catch (FileNotFoundException e) {
		       
		    } 		
	}
	public static String getSysDate()
	{
		Calendar cal = Calendar.getInstance();
		
		return cal.DATE+""+cal.MONTH+""+cal.YEAR;
	}
}
