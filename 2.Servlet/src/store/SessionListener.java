package store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;

public class SessionListener implements HttpSessionListener {

	@Resource(name = "jdbc/bookstore")
	DataSource ds;

	public void sessionCreated(HttpSessionEvent event) {

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();

		HashMap<Integer, Integer> cartitems = (HashMap<Integer, Integer>) session
				.getAttribute("cartitems");
		String userid = (String) session.getAttribute("userid");
		if (cartitems == null || userid == null) {
			return;
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
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
					num += rs.getLong("num");
					ps = con.prepareStatement("UPDATE cartitems SET num=? WHERE userid=? AND bookid=?");
					ps.setLong(1, num);
					ps.setString(2, userid);
					ps.setLong(3, bookid);
					ps.executeUpdate();
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
		}
	}

}
