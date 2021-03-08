package com.iftiict.ipg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;

import org.apache.struts2.ServletActionContext;

public class ErrorsMsg{
	private String error_msg="";	
	public String process() throws Exception
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);
        setError_msg(session.getAttribute("error_msg").toString());
		return "success";
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
}
