<?php

$userid = intval($_REQUEST['userid']);
$name = htmlspecialchars($_REQUEST['name']);
$password = htmlspecialchars($_REQUEST['password']);
$email = htmlspecialchars($_REQUEST['email']);

include 'conn.php';

$sql = "UPDATE users SET name='$name',password='$password',email='$email' WHERE userid=$userid";
$result = @mysql_query($sql);
if ($result){
	echo json_encode(array(
		'userid' => $userid,
		'name' => $name,
		'password' => $password,
		'email' => $email
	));
} else {
	echo json_encode(array('errorMsg'=>'Some errors occured.'));
}
?>