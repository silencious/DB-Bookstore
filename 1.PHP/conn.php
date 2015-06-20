<?php

$conn = @mysql_connect('127.0.0.1','root','aptx4869');
if (!$conn) {
	die('Could not connect: ' . mysql_error());
}
if (!mysql_select_db('bookstore', $conn)) {
	if (!mysql_query("CREATE DATABASE bookstore",$conn)) {
		die('Could not create database: ' . mysql_error());
	}
	mysql_select_db('bookstore', $conn);
	$sql = "CREATE TABLE users
	(
	userid int NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(userid),
	name varchar(16) NOT NULL,
	password varchar(16) NOT NULL,
	email varchar(32) NOT NULL
	)";
	mysql_query($sql);
	mysql_query("INSERT INTO users(name,password,email) VALUES('admin','admin','admin@ad.min'");
	$sql = "CREATE TABLE books
	(
	bookid int NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(bookid),
	title varchar(32) NOT NULL,
	author varchar(32) NOT NULL,
	price double(11,2) NOT NULL,
	stock int NOT NULL
	)";
	mysql_query($sql);
	$sql = "CREATE TABLE orders
	(
	orderid int NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(orderid),
	userid int NOT NULL,
	bookid int NOT NULL,
	time datetime NOT NULL
	)";
	mysql_query($sql);
}

?>