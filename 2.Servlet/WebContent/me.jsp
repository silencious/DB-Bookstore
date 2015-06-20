
<%
	String userid = (String) session.getAttribute("userid");
	if (userid == null) {
		response.sendRedirect("login.jsp");
	}
%>
<html>
<head>
<meta charset="UTF-8">
<title>My Center</title>
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/color.css">
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
</head>
<body>
<script>
	function buy(){
		alert("I assume you have paid for the books!");
		$.post("Purchase", {
			success : function(){
				$.messager.show({
					title : "Success",
					msg : "Purchase successfully."
				})
				location.reload(true);
			}
		});
	}
</script>
	<a href="search.jsp">Home</a>
	<a href="UserSession?method=logout">Logout</a>
	<p>Hello, <%=session.getAttribute("username")%></p>
	<table id="cart-dg" title="Cart" class="easyui-datagrid"
		style="width: 700px; height: 300px" url="CartServlet?method=view"
		pagination="true" rownumbers="false" fitColumns="true"
		singleSelect="">
		<thead>
			<tr>
				<th field="title" width="50">Title</th>
				<th field="author" width="50">Author</th>
				<th field="price" width="50">Price</th>
				<th field="num" width="20">Num</th>
			</tr>
		</thead>
	</table>
	<br>
	<div>
	<button onclick="buy()">
	Buy All
	</button>
	</div>
	<br>
	<table id="order-dg" title="Orders" class="easyui-datagrid"
		style="width: 700px; height: 300px" url="OrderServlet?method=myorders"
		pagination="true" rownumbers="false" fitColumns="true"
		singleSelect="true">
		<thead>
			<tr>
				<th field="orderid" width="20">Order</th>
				<th field="time" width="50">Time</th>
				<th field="title" width="50">Title</th>
				<th field="price" width="50">Price</th>
				<th field="num" width="20">Num</th>
			</tr>
		</thead>
	</table>
</body>
</html>
