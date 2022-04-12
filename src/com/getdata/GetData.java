package com.getdata;

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

@WebServlet("/GetData")
public class GetData extends HttpServlet {
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
				ResultSet rs = st.executeQuery("SELECT * from winter_internship where is_deleted = 0");

				ArrayList <Pojo> data = new ArrayList();

				while (rs.next()) {
					
					Pojo obj = new Pojo();
					
					obj.setSl_no(rs.getInt("sl_no"));
					obj.setBusiness_code(rs.getString("business_code"));
					obj.setCust_number(rs.getInt("cust_number"));
					obj.setClear_date(rs.getString("clear_date"));
					obj.setBuisness_year(rs.getString("buisness_year"));
					obj.setDoc_id(rs.getString("doc_id"));
					obj.setPosting_date(rs.getString("posting_date"));
					obj.setDocument_create_date(rs.getString("document_create_date"));
					obj.setDocument_create_date1(rs.getString("document_create_date1"));
					obj.setDue_in_date(rs.getString("due_in_date"));
					obj.setInvoice_currency(rs.getString("invoice_currency"));
					obj.setDocument_type(rs.getString("document_type"));
					obj.setPosting_id(rs.getInt("posting_id"));
					obj.setArea_business(rs.getString("area_business"));
					obj.setTotal_open_amount(rs.getDouble("total_open_amount"));
					obj.setBaseline_create_date(rs.getString("baseline_create_date"));
					obj.setCust_payment_terms(rs.getString("cust_payment_terms"));
					obj.setInvoice_id(rs.getInt("invoice_id"));
					obj.setIsOpen(rs.getShort("isOpen"));
					obj.setAging_bucket(rs.getString("aging_bucket"));
					obj.setIs_deleted(rs.getShort("is_deleted"));
					
					data.add(obj);

				} 
				out.println(gson.toJson(data));
				st.close();
				con.close();
			}

		} catch (SQLException ex) {
			out.println(gson.toJson(false));
			ex.printStackTrace();
		}
	}
}


