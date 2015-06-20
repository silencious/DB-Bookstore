<?php

$title = htmlspecialchars($_REQUEST['title']);
$author = htmlspecialchars($_REQUEST['author']);
$price = doubleval($_REQUEST['price']);
$stock = intval($_REQUEST['stock']);

include 'conn.php';

$sql = "INSERT INTO books(title,author,price,stock) VALUES('$title','$author','$price','$stock')";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>