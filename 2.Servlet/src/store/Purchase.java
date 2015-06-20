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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/Purchase")
public class Purchase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Purchase() {
		super();
	}

	@Resource(name = "jdbc/bookstore")
	DataSource ds;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		HashMap<Integer, Integer> cartitems = (HashMap<Integer, Integer>) session
				.getAttribute("cartitems");
		String userid = (String) session.getAttribute("userid");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			Iterator iter = cartitems.entrySet().iterator();
			HashMap<Integer, Integer> poststock = new HashMap<Integer, Integer>();
			while (iter.hasNext()) {
				ps = con.prepareStatement("SELECT * FROM books WHERE bookid=?");
				HashMap.Entry<Integer, Integer> entry = (HashMap.Entry<Integer, Integer>) iter
						.next();
				int bookid = entry.getKey();
				int num = entry.getValue();
				ps.setLong(1, bookid);
				rs = ps.executeQuery();
				rs.last();
				int stock = (int) rs.getLong("stock");
				if (stock < num) {
					out.println("<script>alert(\"Not enough stock!\");window.location.href = \"me.jsp\";</script>");
					rs.close();
					ps.close();
					con.close();
					return;
				}
				poststock.put(bookid, stock - num);
			}
			iter = cartitems.entrySet().iterator();
			while (iter.hasNext()) {
				HashMap.Entry<Integer, Integer> entry = (HashMap.Entry<Integer, Integer>) iter
						.next();
				int bookid = entry.getKey();
				int num = entry.getValue();

				ps = con.prepareStatement("UPDATE books SET stock=? WHERE bookid=?");
				ps.setLong(1, poststock.get(bookid));
				ps.setLong(2, bookid);
				ps.executeUpdate();

				ps = con.prepareStatement("INSERT INTO orders(userid,bookid,num) VALUES(?,?,?)");
				ps.setString(1, userid);
				ps.setLong(2, bookid);
				ps.setLong(3, num);
				ps.executeUpdate();
				session.setAttribute("cartitems", null);
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
			JSONObject results = new JSONObject();
			results.put("success", true);
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
