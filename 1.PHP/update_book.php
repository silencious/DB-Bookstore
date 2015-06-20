<?php

$bookid = intval($_REQUEST['bookid']);
$title = htmlspecialchars($_REQUEST['title']);
$author = htmlspecialchars($_REQUEST['author']);
$price = doubleval($_REQUEST['price']);
$stock = intval($_REQUEST['stock']);

include 'conn.php';

$sql = "UPDATE books SET title='$title',author='$author',price='$price',stock='$stock' WHERE bookid=$bookid";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array(
		'bookid' => $bookid,
		'title' => $title,
		'author' => $author,
		'price' => $price,
		'stock' => $stock
	));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>