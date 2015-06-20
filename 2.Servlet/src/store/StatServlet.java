package store;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

@WebServlet("/StatServlet")
public class StatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StatServlet() {
		super();
	}

	@Resource(name = "jdbc/bookstore")
	DataSource ds;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject results = new JSONObject();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String name = (String)request.getParameter("username");
			String fromtime = (String)request.getParameter("from");
			String totime = (String)request.getParameter("to");
			String category = (String)request.getParameter("category");
			
			con = ds.getConnection();
			String sql = "SELECT COUNT(DISTINCT userid,time) AS OrderCount, COUNT(num) AS BookCount, COUNT(DISTINCT userid) AS UserCount, SUM(price*num) AS TotalPrice FROM orders NATURAL JOIN books NATURAL JOIN users";
			String cond1 = (name == null || name.isEmpty())? null : "name=\"" + name + "\"";
			String cond2 = (fromtime == null || fromtime.isEmpty())? null : "time>=\"" + fromtime + " 0:0:0\"";
			String cond3 = (totime == null || totime.isEmpty())? null : "time<=\"" + totime + " 23:59:59\"";
			String cond4 = (category == null || category.isEmpty())? null : "category=\"" + category + "\"";
			
			int cnt = 0; 
			if (cond1 != null) cnt++;
			if (cond2 != null) cnt++;
			if (cond3 != null) cnt++;
			if (cond4 != null) cnt++;
			if (cnt != 0){
				sql += " WHERE ";
				if (cond1 != null) {
					sql += cond1;
					cnt--;
					if (cnt > 0)
						sql += " AND ";
				}
				if (cond2 != null) {
					sql += cond2;
					cnt--;
					if (cnt > 0)
						sql += " AND ";
				}
				if (cond3 != null) {
					sql += cond3;
					cnt--;
					if (cnt > 0)
						sql += " AND ";
				}
				if (cond4 != null) {
					sql += cond4;
				}	
			}
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();		
			rs.last();
			JSONObject data = new JSONObject();
			data.put("OrderCount", rs.getLong("OrderCount"));
			data.put("BookCount", rs.getLong("BookCount"));
			data.put("UserCount", rs.getLong("UserCount"));
			data.put("TotalPrice", rs.getDouble("TotalPrice"));
			results.put("data", data);
			results.put("success", true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
			out.println(results.toString());
			//out.close();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
