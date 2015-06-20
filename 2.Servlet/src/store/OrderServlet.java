package store;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderServlet() {
		super();
	}

	@Resource(name = "jdbc/bookstore")
	DataSource ds;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject results = new JSONObject();
		String method = (String) request.getParameter("method");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (method.equals("select")) {
				String pagestr = (String) request.getParameter("page");
				int page = (pagestr == null) ? 1 : Integer.parseInt(pagestr);
				int rows = 10;
				int offset = (page - 1) * rows;
				con = ds.getConnection();
				ps = con.prepareStatement("SELECT COUNT(*) FROM orders");
				rs = ps.executeQuery();
				rs.next();
				results.put("total", rs.getInt(1));
				ps = con.prepareStatement("SELECT * FROM orders NATURAL JOIN users NATURAL JOIN books LIMIT ?, ?");
				ps.setLong(1, offset);
				ps.setLong(2, rows);
				rs = ps.executeQuery();
				JSONArray orders = new JSONArray();
				while (rs.next()) {
					JSONObject result = new JSONObject();
					result.put("orderid", String.valueOf(rs.getInt("orderid")));
					result.put("time", rs.getString("time"));
					result.put("userid", String.valueOf(rs.getInt("userid")));
					result.put("name", rs.getString("name"));
					result.put("bookid", String.valueOf(rs.getInt("bookid")));
					result.put("title", rs.getString("title"));
					result.put("author", rs.getString("author"));
					result.put("price", String.valueOf(rs.getInt("price")));
					result.put("num", String.valueOf(rs.getInt("num")));
					orders.add(result);
				}
				results.put("rows", orders);
			} else if (method.equals("myorders")) {
				HttpSession session = request.getSession();
				String userid = (String) session.getAttribute("userid");
				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM orders NATURAL JOIN books WHERE userid=?");
				ps.setString(1, userid);
				rs = ps.executeQuery();
				JSONArray orders = new JSONArray();
				while (rs.next()) {
					JSONObject result = new JSONObject();
					result.put("orderid", String.valueOf(rs.getInt("orderid")));
					result.put("time", rs.getString("time"));
					result.put("title", rs.getString("title"));
					result.put("author", rs.getString("author"));
					result.put("price", String.valueOf(rs.getInt("price")));
					result.put("num", String.valueOf(rs.getInt("num")));
					orders.add(result);
				}
				results.put("total", orders.size());
				results.put("rows", orders);
			} else if (method.equals("insert")) {
				int userid = (int) request.getAttribute("userid");
				int bookid = (int) request.getAttribute("bookid");
				int num = (int) request.getAttribute("num");

				con = ds.getConnection();
				ps = con.prepareStatement("INSERT INTO orders(userid, bookid, num) VALUES(?, ?, ?)");
				ps.setLong(1, userid);
				ps.setLong(2, bookid);
				ps.setLong(3, num);
				ps.executeUpdate();
				results.put("success", true);
			} else if (method.equals("update")) {
				int userid = (int) request.getAttribute("userid");
				int bookid = (int) request.getAttribute("bookid");
				int num = (int) request.getAttribute("num");
				con = ds.getConnection();
				ps = con.prepareStatement("UPDATE orders SET num=? WHERE userid=? AND bookid=?");
				ps.setLong(1, num);
				ps.setLong(2, userid);
				ps.setLong(3, bookid);
				ps.executeUpdate();
				results.put("success", true);
			} else if (method.equals("delete")) {
				int userid = (int) request.getAttribute("userid");
				int bookid = (int) request.getAttribute("bookid");

				con = ds.getConnection();
				ps = con.prepareStatement("DELETE FROM orders WHERE userid=? AND bookid=?");
				ps.setLong(1, userid);
				ps.setLong(2, bookid);
				ps.executeUpdate();
				results.put("success", true);
			}
		} catch (SQLException e) {
			results.put("errorMsg", "Some errors occured.");
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
			out.close();
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
