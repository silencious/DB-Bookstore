<?php

$bookid = intval($_POST['bookid']);

include 'conn.php';

$sql = "DELETE FROM books WHERE bookid=$bookid";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>