<?php

$name = htmlspecialchars($_REQUEST['name']);
$password = htmlspecialchars($_REQUEST['password']);
$email = htmlspecialchars($_REQUEST['email']);

include 'conn.php';

$sql = "SELECT * FROM users WHERE (name = '$name' OR email = '$email')";
$result = @mysql_query($sql);
if ($result && mysql_num_rows($result)!=0) {
	echo json_encode(array('success'=>false));
} else {
	$sql = "INSERT INTO users(name,password,email) VALUES('$name','$password','$email')";
	$result = @mysql_query($sql);
	if ($result){
		echo json_encode(array('success'=>true));
	} else {
		echo json_encode(array('errorMsg'=>'Some errors occured.'));
	}
}
?>