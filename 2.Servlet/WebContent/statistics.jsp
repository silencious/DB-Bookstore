<html>
<head>
<meta charset="UTF-8">
<title>Statistics</title>
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
		function getstats() {
			$.ajax({
				url : "StatServlet",
				type : "POST",
				dataType : "json",
				data : {
					username : $("#username").val(),
					from : $("#from").val(),
					to : $("#to").val(),
					category : $("#category").val()
				},
				complete : function(data) {
					var results = eval('(' + data.responseText + ')').data;
					$("#OrderCount").text(results.OrderCount);
					$("#BookCount").text(results.BookCount);
					$("#UserCount").text(results.UserCount);
					$("#TotalPrice").text(results.TotalPrice);
					$("#PricePerOrder").text(
							(results.TotalPrice / results.OrderCount)
									.toFixed(2));
					$("#PricePerBook")
							.text(
									(results.TotalPrice / results.BookCount)
											.toFixed(2));
					$("#PricePerUser")
							.text(
									(results.TotalPrice / results.UserCount)
											.toFixed(2));
				}
			});
		}
	</script>
	<div id="Options">
		<h3>Options:</h3>
		<form action="javascript:void(0)" id="optform" method="post"
			novalidate>
			<table>
				<tr>
					<td>Username:</td>
					<td><input name="username" id="username" type="text"></td>
				</tr>
				<tr>
					<td>From:</td>
					<td><input name="from" id="from" type="date"></td>
				</tr>
				<tr>
					<td>To:</td>
					<td><input name="to" id="to" type="date"></td>
				</tr>
				<tr>
					<td>Category:</td>
					<td><select name="category" id="category">
							<option value=""></option>
							<option value="Fiction">Fiction</option>
							<option value="History">History</option>
							<option value="Science">Science</option>
							<option value="Technology">Technology</option>
					</select>
					<td>
				</tr>
			</table>
		</form>
		<button onclick="getstats()">See stats</button>
	</div>
	<div id="Stats">
		<h3>Statistics:</h3>
		<table id="StatList">
			<tr>
				<td>Number of orders:</td>
				<td id="OrderCount"></td>
			</tr>
			<tr>
				<td>Number of books:</td>
				<td id="BookCount"></td>
			</tr>
			<tr>
				<td>Number of users:</td>
				<td id="UserCount"></td>
			</tr>
			<tr>
				<td>Total Price:</td>
				<td id="TotalPrice"></td>
			</tr>
			<tr>
				<td>Average price per order:</td>
				<td id="PricePerOrder"></td>
			</tr>
			<tr>
				<td>Average price per book:</td>
				<td id="PricePerBook"></td>
			</tr>
			<tr>
				<td>Average price per user:</td>
				<td id="PricePerUser"></td>
			</tr>
		</table>
	</div>
</body>
</html>