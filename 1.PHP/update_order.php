<?php

$orderid = intval($_REQUEST['orderid']);
$userid = intval($_REQUEST['userid']);
$bookid = intval($_REQUEST['bookid']);
$time = htmlspecialchars($_REQUEST['time']);

include 'conn.php';

$sql = "UPDATE orders SET userid=$userid,bookid=$bookid,time='$time' WHERE orderid=$orderid";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array(
		'orderid' =>orderid,
		'userid' => $userid,
		'bookid' => $bookid,
		'time' => $time
	));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>