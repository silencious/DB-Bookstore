<?php

$userid = intval($_POST['userid']);

include 'conn.php';

$sql = "DELETE FROM users WHERE userid=$userid";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>