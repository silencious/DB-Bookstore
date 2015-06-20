<%
	String userid = (String)session.getAttribute("userid");
	if (userid != null) {
		response.sendRedirect("me.jsp");
	}
%>
<html>
<head>
<title>Login</title>
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
</head>
<body>

	<div>
		<a href="register.jsp">Register</a> <a>/ Login</a>
	</div>
	<br>
	<div>
		<form id="log-fm" method="post" action="UserSession?method=login" novalidate>
			Name:<input type="text" name="name" required="true"> <br>
			<br> Password:<input type="password" name="password"
				required="true"> <br> <br> <input type="submit"
				name="login" value="Login">
		</form>
	</div>

</body>
</html>