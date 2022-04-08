package com.deleteData;

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

@WebServlet("/DeleteData")
public class DeleteData extends HttpServlet {
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

				Pojo obj = new Pojo();

				st.execute(obj.getDeleteStatment(req.getParameter("sl_no")));
				out.println(gson.toJson(true));
				st.close();
				con.close();
			}

		} catch (

		SQLException ex) {
			out.println(gson.toJson(false));
			ex.printStackTrace();
		}
	}
}
