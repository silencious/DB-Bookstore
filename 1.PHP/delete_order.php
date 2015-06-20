<?php

$orderid = intval($_POST['orderid']);

include 'conn.php';

$sql = "DELETE FROM orders WHERE orderid=$orderid";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>