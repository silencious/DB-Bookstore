<html>
<head>
<title>Register</title>
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
</head>
<body>

	<div>
		<a>Register /</a> <a href="login.jsp">Login</a>
	</div>
	<p>
		<span class="error">* Necessary</span>
	</p>
	<form id="reg-fm" method="post" action="UserServlet?method=insert&src=register.jsp" novalidate>
		Name:<input type="text" name="name"> <span class="error">*
		</span> <br> <br> Password:<input type="password" name="password">
		<span class="error">*</span> <br> <br> Email:<input
			type="text" name="email"> <span class="error">* </span> <br>
		<br> <input type="submit" name="submit" value="Submit">
	</form>

</body>
</html>