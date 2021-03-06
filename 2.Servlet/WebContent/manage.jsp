
<%
	String userid = (String)session.getAttribute("userid");
	if (userid == null || !userid.equals("1")) {
		%><script>
			alert("You must login as admin!");
			window.location.href = "login.jsp";
		</script>
<%
	}
%>
<html>
<head>
<meta charset="UTF-8">
<title>Bookstore Manager</title>
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
.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}

.fitem input {
	width: 160px;
}
</style>
</head>
<body>
	<h2>Bookstore Manager</h2>
	<a href="statistics.jsp">Statistics</a>
	<p>Click the buttons on datagrid toolbar to do crud actions.</p>
	<table id="user-dg" title="Users" class="easyui-datagrid"
		style="width: 700px; height: 250px" url="UserServlet?method=select"
		toolbar="#user-toolbar" pagination="true" rownumbers="false"
		fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="userid" width="20">Userid</th>
				<th field="name" width="50">Name</th>
				<th field="password" width="50">Password</th>
				<th field="email" width="50">Email</th>
			</tr>
		</thead>
	</table>
	<div id="user-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true" onclick="newUser()">New User</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-edit" plain="true" onclick="editUser()">Edit User</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true" onclick="removeUser()">Remove
			User</a>
	</div>

	<div id="user-dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#user-buttons">
		<div class="ftitle">User Information</div>
		<form id="user-fm" method="post" novalidate>
			<div class="fitem">
				<label>Name:</label> <input name="name" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Password:</label> <input name="password"
					class="easyui-textbox" type="password" required="true">
			</div>
			<div class="fitem">
				<label>Email:</label> <input name="email" class="easyui-textbox"
					validType="email" required="true">
			</div>
		</form>
	</div>
	<div id="user-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="saveUser()" style="width: 90px">Save</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#user-dlg').dialog('close')"
			style="width: 90px">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function newUser() {
			$('#user-dlg').dialog('open').dialog('setTitle', 'New User');
			$('#user-fm').form('clear');
			url = 'UserServlet?method=insert';
		}
		function editUser() {
			var row = $('#user-dg').datagrid('getSelected');
			if (row) {
				$('#user-dlg').dialog('open').dialog('setTitle', 'Edit User');
				$('#user-fm').form('load', row);
				url = 'UserServlet?method=update&userid=' + row.userid;
			}
		}
		function saveUser() {
			$('#user-fm').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#user-dlg').dialog('close'); // close the dialog
						$('#user-dg').datagrid('reload'); // reload the user data
					}
				}
			});
		}
		function removeUser() {
			var row = $('#user-dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('Confirm',
						'Are you sure you want to remove this user?', function(
								r) {
							if (r) {
								$.post('UserServlet?method=delete', {
									userid : row.userid
								}, function(result) {
									if (result.success) {
										$('#user-dg').datagrid('reload'); // reload the user data
									} else {
										$.messager.show({ // show error message
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
						});
			}
		}
	</script>

	<br>
	<br>

	<table id="book-dg" title="Books" class="easyui-datagrid"
		style="width: 700px; height: 250px" url="BookServlet?method=select"
		toolbar="#book-toolbar" pagination="true" rownumbers="false"
		fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="bookid" width="20">Bookid</th>
				<th field="title" width="50">Title</th>
				<th field="author" width="50">Author</th>
				<th field="category" width="50">Category</th>
				<th field="price" width="20">Price</th>
				<th field="stock" width="20">Stock</th>
			</tr>
		</thead>
	</table>
	<div id="book-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true" onclick="newBook()">New Book</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-edit" plain="true" onclick="editBook()">Edit Book</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true" onclick="removeBook()">Remove
			Book</a>
	</div>

	<div id="book-dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#book-buttons">
		<div class="ftitle">Book Information</div>
		<form id="book-fm" method="post" novalidate>
			<div class="fitem">
				<label>Title:</label> <input name="title" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Author:</label> <input name="author" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Category:</label> <input name="category"
					class="easyui-textbox" required="false">
			</div>
			<div class="fitem">
				<label>Price:</label> <input name="price" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Stock:</label> <input name="stock" class="easyui-textbox"
					required="true">
			</div>
		</form>
	</div>
	<div id="book-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="saveBook()" style="width: 90px">Save</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#book-dlg').dialog('close')"
			style="width: 90px">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function newBook() {
			$('#book-dlg').dialog('open').dialog('setTitle', 'New Book');
			$('#book-fm').form('clear');
			url = 'BookServlet?method=insert';
		}
		function editBook() {
			var row = $('#book-dg').datagrid('getSelected');
			if (row) {
				$('#book-dlg').dialog('open').dialog('setTitle', 'Edit Book');
				$('#book-fm').form('load', row);
				url = 'BookServlet?method=update&bookid=' + row.bookid;
			}
		}
		function saveBook() {
			$('#book-fm').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#book-dlg').dialog('close'); // close the dialog
						$('#book-dg').datagrid('reload'); // reload the book data
					}
				}
			});
		}
		function removeBook() {
			var row = $('#book-dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('Confirm',
						'Are you sure you want to remove this book?', function(
								r) {
							if (r) {
								$.post('BookServlet?method=delete', {
									bookid : row.bookid
								}, function(result) {
									if (result.success) {
										$('#book-dg').datagrid('reload'); // reload the book data
									} else {
										$.messager.show({ // show error message
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
						});
			}
		}
	</script>
	<br>
	<br>

	<table id="order-dg" title="Orders" class="easyui-datagrid"
		style="width: 700px; height: 250px" url="OrderServlet?method=select"
		toolbar="#order-toolbar" pagination="true" rownumbers="false"
		fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="orderid" width="20">Orderid</th>
				<th field="time" width="50">Time</th>
				<th field="userid" width="20">Userid</th>
				<th field="name" width="30">Name</th>
				<th field="bookid" width="20">Bookid</th>
				<th field="title" width="50">Title</th>
				<th field="price" width="20">Price</th>
				<th field="num" width="20">Num</th>
			</tr>
		</thead>
	</table>
	<div id="order-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true" onclick="newOrder()">New Order</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-edit" plain="true" onclick="editOrder()">Edit Order</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true" onclick="removeOrder()">Remove
			Order</a>
	</div>

	<div id="order-dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#order-buttons">
		<div class="ftitle">Order Information</div>
		<form id="order-fm" method="post" novalidate>
			<div class="fitem">
				<label>Userid:</label> <input name="userid" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Bookid:</label> <input name="bookid" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Num</label> <input name="num" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>Time</label> <input name="time" class="easyui-textbox"
					required="true">
			</div>
		</form>
	</div>
	<div id="order-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="saveOrder()" style="width: 90px">Save</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#order-dlg').dialog('close')"
			style="width: 90px">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function newOrder() {
			$('#order-dlg').dialog('open').dialog('setTitle', 'New Order');
			$('#order-fm').form('clear');
			url = 'OrderServlet?method=insert';
		}
		function editOrder() {
			var row = $('#order-dg').datagrid('getSelected');
			if (row) {
				$('#order-dlg').dialog('open').dialog('setTitle', 'Edit Order');
				$('#order-fm').form('load', row);
				url = 'OrderServlet?method=update&orderid=' + row.orderid;
			}
		}
		function saveOrder() {
			$('#order-fm').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#order-dlg').dialog('close'); // close the dialog
						$('#order-dg').datagrid('reload'); // reload the order data
					}
				}
			});
		}
		function removeOrder() {
			var row = $('#order-dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('Confirm',
						'Are you sure you want to remove this order?',
						function(r) {
							if (r) {
								$.post('OrderServlet?method=delete', {
									orderid : row.orderid
								}, function(result) {
									if (result.success) {
										$('#order-dg').datagrid('reload'); // reload the order data
									} else {
										$.messager.show({ // show error message
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
						});
			}
		}
	</script>
</body>
</html>