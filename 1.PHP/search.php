<html>
<head>
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.4.2/themes/color.css">
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
	<style type="text/css">
		#schbox input{
			width:40%;
			font-size:24px;
		}
		#schbtn input{
			font-size:24px;
		}
		#myctr{
			position:absolute;
			right:10px;
			top:10px;
		}
		.purchase{
			height:24px;
			font-size:24px;
		}
	</style>
</head>
<body>
	<form action="<?php $_SERVER["PHP_SELF"]?>" id="schfm" method="post" novalidate>
		<div>
			<span id="schbox">
				<input type="text" name="keywords" value="<?php echo $_POST["keywords"]?>">
			</span>
			<span id="schbtn">
				<input type="submit" value="Search">
			</span>
		</div>
	</form>
	<?php 
		if (isset($_COOKIE['userid'])) {
			$url1 = "me.php";
			$text1 = "My Center";
			$url2 = "me.php?logout=true";
			$text2 = "Logout";
		} else {
			$url1 = "login.php";
			$text1 = "Login";
			$url2 = "register.html";
			$text2 = "Register";
		}
	?>
	<div id="myctr">
		<a href=<?php echo $url1?>><?php echo $text1?></a>
		<a href=<?php echo $url2?>><?php echo $text2?></a>
	</div>
	
	<div>
		<ul class="easyui-datalist"  id="results" border="0">
<?php
	$books = array();
	if ($_SERVER["REQUEST_METHOD"] == "POST") {
		$keywords = explode(" ",htmlspecialchars($_POST["keywords"]));
		$wordnum = count($keywords);
		if ($wordnum != 0) {
			include "conn.php";
			$sql = "SELECT * FROM books WHERE (title REGEXP '$keywords[0]' OR author REGEXP '$keywords[0]'";
			for ($i=1; $i<$wordnum; $i++) {
				$sql = $sql."OR title REGEXP '".$keywords[$i]."' OR author REGEXP '".$keywords[$i]."'";
			}
			$sql = $sql.")";
			$result = mysql_query($sql);
			while ($row = mysql_fetch_array($result,MYSQL_ASSOC)) {
				array_push($books,$row);
			}
		} else {
			return;
		}
		$booknum = count($books);
		$pagesize = 10;
		$page = $_POST['page'];
		if (!$page || $page < 1) {
			$page = 1;
		}
		$maxpage = ceil($booknum/$pagesize);
		$num = min($page*$pagesize,$booknum);
		for ($i=($page-1)*$pagesize; $i<$num; $i++) {
	?>
			<li><table>
				<tr>
					<td>Title:</td>
					<td><?php echo $books[$i][title]?></td>
				</tr>
				<tr>
					<td>Author:</td>
					<td><?php echo $books[$i][author]?></td>
				</tr>
				<tr>
					<td>Price:</td>
					<td><?php echo $books[$i][price]?></td>
				</tr>
				<tr>
					<td>Stock:</td>
					<td><?php echo $books[$i][stock]?></td>
				</tr>
			</table>
			<div class="purchase"><a href="purchase.php?bookid=<?php echo $books[$i][bookid]?>">Buy</a></div>
			</li>
	<?php 
		}
	?>	
		</ul>
		Page:
	<?php 
		if ($page!=1) {
			?>
				<a href="javascript:load(<?php echo ($page-1)?>)"><<</a>
			<?php 	
		}
		for ($i=1; $i<=$maxpage; $i++) {
			if ($i == $page) {
				echo $i;
			} else {
			?>
				<a href="javascript:load(<?php echo $i?>)"><?php echo $i?></a>
			<?php
			}
		}
		if ($page < $maxpage) {
			?>
				<a href="javascript:load(<?php echo ($page+1)?>)">>></a>
			<?php 	
		}
	}
	?>
	</div>
	<script>
		function load(num){
			var temp_form=document.createElement("form");
			temp_form .action="search";
			temp_form .method="post";
			var opt1=document.createElement("textarea");
			opt1.name="keywords";
			opt1.value="<?php echo $_POST["keywords"]?>";
			temp_form .appendChild(opt1);
			var opt2=document.createElement("textarea");
			opt2.name="page";
			opt2.value=num;
			temp_form .appendChild(opt2);
			temp_form .submit();
		}
	</script>
</body>
</html>