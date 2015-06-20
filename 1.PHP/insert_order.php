<?php

$userid = htmlspecialchars($_REQUEST['userid']);
$bookid = htmlspecialchars($_REQUEST['bookid']);
$time = htmlspecialchars($_REQUEST['time']);

include 'conn.php';

$sql = "INSERT INTO orders(userid,bookid,time) VALUES('$userid','$bookid','$time')";
if (mysql_query($sql)) {
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>