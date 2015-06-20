<html>
<head>
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/color.css">
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
<style type="text/css">
#schbox input {
	width: 40%;
	font-size: 24px;
}

#schbtn input {
	font-size: 24px;
}

#myctr {
	position: absolute;
	right: 10px;
	top: 10px;
}
</style>
</head>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%
	String keyword = (String) request.getParameter("keyword");
%>
<body>
	<script>
	function search(){
		$.post("CartServlet"), {
			method : "search"
			
		}
		$("results").datalist("reload");
	}
	function addtocart(bookid){
		var id = bookid.toString();
		$.post("CartServlet?method=add", {
			bookid : id,
			success : function(){
				$.messager.show({
					title : "Success",
					msg : "Add successfully."
				})
			}
		});
	}
	</script>
	<form action="BookServlet?method=search" id="schfm" method="post" novalidate>
		<div>
			<span id="schbox"> <input type="text" name="keyword"
				value="<%=(keyword == null) ? "" : keyword%>">
			</span> <span id="schbtn"> <input type="submit" value="Search">
			</span>
		</div>
	</form>
	<%
		String userid = (String) session.getAttribute("userid");
		String url1, text1, url2, text2;
		if (userid != null) {
			url1 = "me.jsp";
			text1 = "My Center";
			url2 = "UserSession?method=logout";
			text2 = "Logout";
		} else {
			url1 = "login.jsp";
			text1 = "Login";
			url2 = "register.jsp";
			text2 = "Register";
		}
	%>
	<div id="myctr">
		<a href=<%=url1%>><%=text1%></a> <a href=<%=url2%>><%=text2%></a>
	</div>

	<div>
		<ul class="easyui-datalist" id="results" border="0">
			<%
				String pagestr = (String) request.getParameter("page");
				int pagenum = (pagestr == null) ? 1 : Integer.parseInt(pagestr);
				if (keyword != null) {
					String[] keywords = keyword.split(" ");
					ArrayList<HashMap<String, String>> books = (ArrayList<HashMap<String, String>>) request
							.getAttribute("books");
					int booknum = books.size();
					int pagesize = 10;
					int maxpage = (int) Math.ceil(((double) booknum)
							/ ((double) pagesize));
					int num = Math.min(pagenum * pagesize, booknum);
					int i;
					for (i = (pagenum - 1) * pagesize; i < num; i++) {
			%>
			<li><table>
					<tr>
						<td>Title:</td>
						<td><%=books.get(i).get("title")%></td>
					</tr>
					<tr>
						<td>Author:</td>
						<td><%=books.get(i).get("author")%></td>
					</tr>
					<tr>
						<td>Price:</td>
						<td><%=books.get(i).get("price")%></td>
					</tr>
					<tr>
						<td>Stock:</td>
						<td><%=books.get(i).get("stock")%></td>
					</tr>
				</table>
				<div class="additem">
					<button onclick="addtocart(<%=books.get(i).get("bookid")%>)">Add</button>
				</div></li>
			<%
				}
			%>
		</ul>
		<br> Page:
		<%
			if (pagenum != 1) {
		%>
		<a
			href="BookServlet?method=search&keyword=<%=keyword%>&page=<%=pagenum - 1%>"><<</a>
		<%
			}
				for (i = 1; i <= maxpage; i++) {
					if (i == pagenum) {
						out.println(i);
					} else {
		%>
		<a href="BookServlet?method=search&keyword=<%=keyword%>&page=<%=i%>">
			<%=i%>
		</a>
		<%
			}
				}
				if (pagenum < maxpage) {
		%>
		<a
			href="BookServlet?method=search&keyword=<%=keyword%>&page=<%=pagenum + 1%>">>></a>
		<%
			}
			}
		%>
	</div>
</body>
</html>