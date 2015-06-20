package store;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
			String method = (String) request.getParameter("method");
			if (method.equals("select")) {
				String pagestr = (String) request.getParameter("page");
				int page = (pagestr == null) ? 1 : Integer.parseInt(pagestr);
				int rows = 10;
				int offset = (page - 1) * rows;
				con = ds.getConnection();
				ps = con.prepareStatement("SELECT COUNT(*) FROM users");
				rs = ps.executeQuery();
				rs.next();
				results.put("total", rs.getInt(1));
				ps = con.prepareStatement("SELECT * FROM users LIMIT ?, ?");
				ps.setLong(1, offset);
				ps.setLong(2, rows);
				rs = ps.executeQuery();
				JSONArray users = new JSONArray();
				while (rs.next()) {
					JSONObject result = new JSONObject();
					result.put("userid", String.valueOf(rs.getInt("userid")));
					result.put("name", rs.getString("name"));
					result.put("password", rs.getString("password"));
					result.put("email", rs.getString("email"));
					users.add(result);
				}
				results.put("rows", users);
			} else if (method.equals("insert")) {
				String name = (String) request.getParameter("name");
				String email = (String) request.getParameter("email");
				String password = (String) request.getParameter("password");

				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM users WHERE name = ?");
				ps.setString(1, name);
				rs = ps.executeQuery();
				rs.last();
				int count = rs.getRow();

				if (count == 0) {
					ps = con.prepareStatement("INSERT INTO users(name,password,email) VALUES(?, ?, ?)");
					ps.setString(1, name);
					ps.setString(2, password);
					ps.setString(3, email);
					ps.executeUpdate();
					String src = (String) request.getParameter("src");
					if (src.equals("register.jsp"))
						response.sendRedirect("login.jsp");
					else
						results.put("success", true);
				} else {
					results.put("errorMsg", "Some errors occured.");
				}
			} else if (method.equals("update")) {
				int userid = (int) request.getAttribute("userid");
				String name = (String) request.getParameter("name");
				String email = (String) request.getParameter("email");
				String password = (String) request.getParameter("password");
				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM users WHERE userid = ?");
				ps.setLong(1, userid);
				rs = ps.executeQuery();
				rs.last();
				int count = rs.getRow();

				if (count != 0) {
					ps = con.prepareStatement("UPDATE users SET name=?,password=?,email=? WHERE userid=?");
					ps.setString(1, name);
					ps.setString(2, password);
					ps.setString(3, email);
					ps.setLong(4, userid);
					ps.executeUpdate();
					results.put("success", true);
				} else {
					results.put("errorMsg", "Some errors occured.");
				}
			} else if (method.equals("delete")) {
				String name = (String) request.getParameter("name");

				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM users WHERE name = ?");
				ps.setString(1, name);
				rs = ps.executeQuery();
				rs.last();
				int count = rs.getRow();

				if (count != 0) {
					ps = con.prepareStatement("DELETE FROM users WHERE name=?");
					ps.setString(1, name);
					ps.executeUpdate();
					results.put("success", true);
				} else {
					results.put("errorMsg", "Some errors occured.");
				}
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
