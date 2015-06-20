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

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CartServlet() {
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
		if (cartitems == null) {
			cartitems = new HashMap<Integer, Integer>();
			session.setAttribute("cartitems", cartitems);
		}
		String method = (String) request.getParameter("method");
		JSONObject results = new JSONObject();
		if (method.equals("add")) {
			Integer id = Integer.parseInt((String) request
					.getParameter("bookid"));
			if (cartitems.containsKey(id)) {
				cartitems.put(id, cartitems.get(id) + 1);
			} else {
				cartitems.put(id, 1);
			}
			results.put("success", true);
		} else if (method.equals("view")) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				con = ds.getConnection();
				JSONArray cart = new JSONArray();
				Iterator iter = cartitems.entrySet().iterator();
				while (iter.hasNext()) {
					ps = con.prepareStatement("SELECT * FROM books WHERE bookid=?");
					HashMap.Entry<Integer, Integer> entry = (HashMap.Entry<Integer, Integer>) iter
							.next();
					int id = entry.getKey();
					int num = entry.getValue();
					ps.setLong(1, id);
					rs = ps.executeQuery();
					rs.last();
					JSONObject result = new JSONObject();
					result.put("bookid", String.valueOf(rs.getInt("bookid")));
					result.put("title", rs.getString("title"));
					result.put("author", rs.getString("author"));
					result.put("price", String.valueOf(rs.getInt("price")));
					result.put("num", String.valueOf(num));
					cart.add(result);
				}
				results.put("total", cart.size());
				results.put("rows", cart);
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
				out.close();
			}
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
