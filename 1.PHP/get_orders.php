<?php
$page = isset($_POST['page']) ? intval($_POST['page']) : 1;
$rows = isset($_POST['rows']) ? intval($_POST['rows']) : 10;
$offset = ($page-1)*$rows;
$result = array();

include 'conn.php';
if (!isset($_COOKIE['userid'])) {
	header("login.php");
}
$userid = $_COOKIE['userid'];
if ($userid==1) {
	$rs = mysql_query("SELECT COUNT(*) FROM orders");
	$row = mysql_fetch_row($rs);
	$result["total"] = $row[0];
	$rs = mysql_query("SELECT * FROM orders NATURAL JOIN users NATURAL JOIN books LIMIT $offset,$rows");

	$items = array();
	while($row = mysql_fetch_object($rs)){
		array_push($items, $row);
	}
	$result["rows"] = $items;

	echo json_encode($result);
} else {
	$rs = mysql_query("SELECT COUNT(*) FROM orders WHERE userid = $userid");
	$row = mysql_fetch_row($rs);
	$result["total"] = $row[0];
	$rs = mysql_query("SELECT orderid,time,bookid,title,author,price FROM orders NATURAL JOIN books WHERE userid = $userid LIMIT $offset,$rows");

	$items = array();
	while($row = mysql_fetch_object($rs)){
		array_push($items, $row);
	}
	$result["rows"] = $items;

	echo json_encode($result);
	
}
?>