<?php
function GetDbConnection() {
	$username="b8_18936075";
	$password="sebichin84";
	$dbname="b8_18936075_cartracking";
	$dbhost="sql309.byethost8.com";
	
	$db = new PDO('mysql:host='.$dbhost.';dbname='.$dbname, $username, $password);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	return $db;
}
?>