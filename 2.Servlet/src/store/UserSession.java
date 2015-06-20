package store;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/UserSession")
public class UserSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/bookstore")
	DataSource ds;

	public UserSession() {
		super();
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String method = (String) request.getParameter("method");
			if (method.equals("login")) {
				String name = (String) request.getParameter("name");
				String password = (String) request.getParameter("password");

				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM users WHERE name = ?");
				ps.setString(1, name);
				rs = ps.executeQuery();
				rs.last();
				if (rs.getRow() != 0
						&& rs.getString("password").equals(password)) {
					int userid = rs.getInt("userid");
					session.setAttribute("userid", String.valueOf(userid));
					session.setAttribute("username", name);

					ps = con.prepareStatement("SELECT * FROM cartitems WHERE userid=?");
					ps.setLong(1, userid);
					out.println("here");
					rs = ps.executeQuery();
					HashMap<Integer, Integer> cartitems = (HashMap<Integer, Integer>) session
							.getAttribute("cartitems");
					if (cartitems == null) {
						cartitems = new HashMap<Integer, Integer>();
						session.setAttribute("cartitems", cartitems);
					}
					while (rs.next()) {
						int bookid = rs.getInt("bookid");
						if(cartitems.containsKey(bookid)) {
							cartitems.put(bookid, cartitems.get(bookid) + rs.getInt("num"));
						} else {
							cartitems.put(bookid, rs.getInt("num"));
						}
					}
					if (name.equals("admin")) {
						response.sendRedirect("manage.jsp");
					} else {
						response.sendRedirect("me.jsp");
					}
				} else {
					out.println("<script>alert(\"Incorrect username or password!\");window.location.href = \"login.jsp\";</script>");
				}
			} else if (method.equals("logout")) {
				String userid = (String) session.getAttribute("userid");
				HashMap<Integer, Integer> cartitems = (HashMap<Integer, Integer>) session
						.getAttribute("cartitems");
				if (cartitems != null && userid != null) {
					con = ds.getConnection();
					Iterator iter = cartitems.entrySet().iterator();
					while (iter.hasNext()) {
						HashMap.Entry<Integer, Integer> entry = (HashMap.Entry<Integer, Integer>) iter
								.next();
						int bookid = entry.getKey();
						int num = entry.getValue();
						ps = con.prepareStatement("SELECT * FROM cartitems WHERE userid=? and bookid=?");
						ps.setString(1, userid);
						ps.setLong(2, bookid);
						rs = ps.executeQuery();
						rs.last();
						if (rs.getRow() == 0) {
							ps = con.prepareStatement("INSERT INTO cartitems(userid, bookid, num) VALUES(?,?,?)");
							ps.setString(1, userid);
							ps.setLong(2, bookid);
							ps.setLong(3, num);
							ps.executeUpdate();
						} else {
							num = rs.getInt("num");
							ps = con.prepareStatement("UPDATE cartitems SET num=? WHERE userid=? AND bookid=?");
							ps.setLong(1, num);
							ps.setString(2, userid);
							ps.setLong(3, bookid);
							ps.executeUpdate();
						}
					}
				}
				session.invalidate();
				response.sendRedirect("login.jsp");
			}
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
