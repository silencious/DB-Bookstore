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
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BookServlet() {
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
				ps = con.prepareStatement("SELECT COUNT(*) FROM books");
				rs = ps.executeQuery();
				rs.next();
				results.put("total", rs.getInt(1));
				ps = con.prepareStatement("SELECT * FROM books LIMIT ?, ?");
				ps.setLong(1, offset);
				ps.setLong(2, rows);
				rs = ps.executeQuery();
				JSONArray books = new JSONArray();
				while (rs.next()) {
					JSONObject result = new JSONObject();
					result.put("bookid", String.valueOf(rs.getInt("bookid")));
					result.put("title", rs.getString("title"));
					result.put("author", rs.getString("author"));
					String cat = rs.getString("category");
					if (cat != null)
						result.put("category", rs.getString("category"));
					result.put("price", String.valueOf(rs.getInt("price")));
					result.put("stock", String.valueOf(rs.getInt("stock")));
					books.add(result);
				}
				results.put("rows", books);
			} else if (method.equals("search")) {
				String[] keywords = ((String) request.getParameter("keyword"))
						.split(" ");
				StringBuilder sb = new StringBuilder();
				sb.append(String
						.format("SELECT * FROM books WHERE title LIKE '%%%s%%' OR author LIKE '%%%s%%'",
								keywords[0], keywords[0]));
				for (int i = 1; i < keywords.length; i++) {
					sb.append(String.format(
							"OR title LIKE '%%%s%%' OR author LIKE '%%%s%%'",
							keywords[i], keywords[i]));
				}
				con = ds.getConnection();
				ps = con.prepareStatement(sb.toString());
				rs = ps.executeQuery();
				ArrayList<HashMap<String, String>> books = new ArrayList<HashMap<String, String>>();
				while (rs.next()) {
					HashMap<String, String> result = new HashMap<String, String>();
					result.put("bookid", String.valueOf(rs.getInt("bookid")));
					result.put("title", rs.getString("title"));
					result.put("author", rs.getString("author"));
					String cat = rs.getString("category");
					if (cat != null)
						result.put("category", rs.getString("category"));
					result.put("price", String.valueOf(rs.getInt("price")));
					result.put("stock", String.valueOf(rs.getInt("stock")));
					books.add(result);
				}
				request.setAttribute("books", books);
				request.getRequestDispatcher("search.jsp").forward(request,
						response);
			} else if (method.equals("insert")) {
				String title = (String) request.getParameter("title");
				String author = (String) request.getParameter("author");
				String category = (String) request.getParameter("category");
				int price = (int) request.getAttribute("price");
				int stock = (int) request.getAttribute("stock");

				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM books WHERE title = ?");
				ps.setString(1, title);
				rs = ps.executeQuery();
				rs.last();
				int count = rs.getRow();

				if (count == 0) {
					ps = con.prepareStatement("INSERT INTO books(title,author,category,price,stock) VALUES(?, ?, ?, ?, ?)");
					ps.setString(1, title);
					ps.setString(2, author);
					ps.setString(3, category);
					ps.setLong(4, price);
					ps.setLong(5, stock);
					ps.executeUpdate();
					results.put("success", true);
				} else {
					results.put("errorMsg", "Some errors occured.");
				}
			} else if (method.equals("update")) {
				int bookid = (int) request.getAttribute("bookid");
				String title = (String) request.getParameter("title");
				String author = (String) request.getParameter("author");
				String category = (String) request.getParameter("category");
				int price = (int) request.getAttribute("price");
				int stock = (int) request.getAttribute("stock");

				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM books WHERE bookid = ?");
				ps.setLong(1, bookid);
				rs = ps.executeQuery();
				rs.last();
				int count = rs.getRow();

				if (count != 0) {
					ps = con.prepareStatement("UPDATE books SET title=?,author=?,category=?price=?,stock=? WHERE bookid=?");
					ps.setString(1, title);
					ps.setString(2, author);
					ps.setString(3, category);
					ps.setLong(4, price);
					ps.setLong(5, stock);
					ps.setLong(6, bookid);
					ps.executeUpdate();
					results.put("success", true);
				} else {
					results.put("errorMsg", "Some errors occured.");
				}
			} else if (method.equals("delete")) {
				int bookid = (int) request.getAttribute("bookid");

				con = ds.getConnection();
				ps = con.prepareStatement("SELECT * FROM books WHERE bookid = ?");
				ps.setLong(1, bookid);
				rs = ps.executeQuery();
				rs.last();
				int count = rs.getRow();

				if (count != 0) {
					ps = con.prepareStatement("DELETE FROM books WHERE bookid=?");
					ps.setLong(1, bookid);
					ps.executeUpdate();
					results.put("success", true);
				} else {
					results.put("errorMsg", "Some errors occured.");
				}
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
