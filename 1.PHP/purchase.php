<?php
if (!isset($_COOKIE["userid"])) {
	header("Location: login.php");
}

$userid = $_COOKIE["userid"];
$bookid = $_GET["bookid"];
include 'conn.php';

$sql = "SELECT stock FROM books WHERE bookid=$bookid";
$result = mysql_query($sql);
$row = mysql_fetch_row($result);
if ($row[0] == 0) {
	die('Could not purchase: ' . mysql_error());
}

$sql = "INSERT INTO orders(userid,bookid) VALUES('$userid','$bookid')";
if (!mysql_query($sql)) {
	die('Could not purchase: ' . mysql_error());
}
$sql = "UPDATE books SET stock=($row[0]-1) WHERE bookid=$bookid";
if (!mysql_query($sql)) {
	die('Error: ' . mysql_error());
}
header("Location: me.php");
?>