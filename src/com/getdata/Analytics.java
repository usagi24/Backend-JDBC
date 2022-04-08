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
import com.pojo.AnalyticsPojo;
import com.pojo.Pojo;

@WebServlet("/Analytics")
public class Analytics extends HttpServlet {
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
				ResultSet rs = st.executeQuery("SELECT business_code, COUNT(DISTINCT cust_number) AS cust_count, "
						+ "SUM(total_open_amount)/1000 AS amount " + "FROM winter_internship "
						+ "WHERE due_in_date BETWEEN '" + req.getParameter("startDueDate") + "' AND '"
						+ req.getParameter("endDueDate") + "' " + "AND clear_date BETWEEN '"
						+ req.getParameter("startClearDate") + "' AND '" + req.getParameter("endClearDate") + "' "
						+ "AND baseline_create_date BETWEEN '" + req.getParameter("startBaselineCreateDate") + "' AND '"
						+ req.getParameter("endBaselineCreateDate") + "' " + "AND invoice_currency LIKE '%"
						+ req.getParameter("invoiceCurrency") + "%' " + "GROUP BY business_code "
						+ "ORDER BY business_code");

				AnalyticsPojo obj = new AnalyticsPojo();
				while (rs.next()) {

					obj.business_code.add(rs.getString("business_code"));
					obj.cust_count.add(rs.getInt("cust_count"));
					obj.amount.add(rs.getDouble("amount"));
				}
				out.println(gson.toJson(obj));
				st.close();
				con.close();
			}

		} catch (SQLException ex) {
			out.println(gson.toJson(false));
			ex.printStackTrace();
		}
	}
}