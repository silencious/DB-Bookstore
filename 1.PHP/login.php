<?php
if (isset($_COOKIE["userid"])) {
	header("Location: me.php");
}
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	$name = htmlspecialchars($_POST["name"]);
	$password = htmlspecialchars($_POST["password"]);
	include 'conn.php';

	$sql = "SELECT userid FROM users WHERE (name = '$name' AND password = '$password')";
	$result = @mysql_query($sql);
	if (!$result){
		echo "<script>alert('Some errors occured.')</script>";
	} else {
		if (mysql_num_rows($result) == 1) {
			$row = mysql_fetch_row($result);
			setcookie("userid",$row[0],time()+3600);
			if ($name == "admin") {
				header("Location: manager.html");
			} else {
				header("Location: search.php");
			}
		} else {
			echo "<script>alert('Invalid username or password')</script>";
		}
	}
}
?>

<html>
<head>
<meta charset="UTF-8">
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
</head>
<body>
	<div>
		<a href="register.html">Register</a> <a>/ Login</a>
	</div>
	<br>
	<div>
		<form id="log-fm" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>" novalidate>
			Name:<input type="text" name="name" required="true" value=<?php $_POST["name"]?>>
			<br><br>
			Password:<input type="password" name="password" required="true">
			<br><br>
			<input type="submit" name="login" value="Login">
		</form>
	</div>

</body>

</html>
