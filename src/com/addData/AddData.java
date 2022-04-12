package com.addData;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.Pojo;

@WebServlet("/AddData")
public class AddData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String url = "jdbc:mysql://localhost/grey_goose";
		String user = "root";
		String password = "mysql123";
		PrintWriter out = res.getWriter();
		Gson gson = new Gson();

		String clientOrigin = req.getHeader("origin");

		res.setHeader("Access-Control-Allow-Origin", clientOrigin);
		res.setHeader("Access-Control-Allow-Methods", "GET");
		res.setHeader("Access-Control-Allow-Headers", "Content-Type");
		res.setHeader("Access-Control-Max-Age", "86400");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Connection con = DriverManager.getConnection(url, user, password);
			if (con != null) {
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(sl_no) as next_sl FROM winter_internship");
				
				Pojo obj = new Pojo();

				rs.next();
				obj.setSl_no(rs.getInt("next_sl") + 1);
				obj.setBusiness_code(req.getParameter("business_code"));
				obj.setCust_number(Integer.parseInt(req.getParameter("cust_number")));
				obj.setClear_date(req.getParameter("clear_date"));
				obj.setBuisness_year(req.getParameter("buisness_year"));
				obj.setDoc_id(req.getParameter("doc_id"));
				obj.setPosting_date(req.getParameter("posting_date"));
				obj.setDocument_create_date(req.getParameter("document_create_date"));
				obj.setDocument_create_date1(req.getParameter("document_create_date"));
				obj.setDue_in_date(req.getParameter("due_in_date"));
				obj.setInvoice_currency(req.getParameter("invoice_currency"));
				obj.setDocument_type(req.getParameter("document_type"));
				obj.setPosting_id(Integer.parseInt(req.getParameter("posting_id")));
				obj.setArea_business(req.getParameter("area_business"));
				obj.setTotal_open_amount(Double.parseDouble(req.getParameter("total_open_amount")));
				obj.setBaseline_create_date(req.getParameter("baseline_create_date"));
				obj.setCust_payment_terms(req.getParameter("cust_payment_terms"));
				obj.setInvoice_id(Integer.parseInt(req.getParameter("invoice_id")));
				obj.setIsOpen(Short.parseShort(req.getParameter("isOpen")));

				st.execute(obj.getInsertStatement());
				out.println(gson.toJson(true));
				st.close();
				con.close();
			}

		} catch (SQLException ex) {
			out.println(gson.toJson(false));
			ex.printStackTrace();
		}
	}
}
