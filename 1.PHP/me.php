<?php 
if ($_GET['logout']==true) {
	setcookie('userid',$_COOKIE['userid'],time()-3600);
	header("Location: search.php");
}
?>
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
	<a href="search.php">Back</a>
	<a href="<?php echo $_SERVER["PHP_SELF"]?>?logout=true">Logout</a>
	<table id="order-dg" title="Orders" class="easyui-datagrid"
		style="width: 700px; height: 500px" url="get_orders.php"
		pagination="true" rownumbers="false" fitColumns="true"
		singleSelect="true">
		<thead>
			<tr>
				<th field="orderid" width="15">Order</th>
				<th field="title" width="50">Title</th>
				<th field="price" width="50">Price</th>
			</tr>
		</thead>
	</table>
</body>
</html>
