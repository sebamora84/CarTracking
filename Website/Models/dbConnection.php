<?php
function GetDbConnection() {
	$username="2244948_cartrk";
	$password="sebichin84";
	$dbname="2244948_cartrk";
	$dbhost="fdb15.biz.nf";
	$db = new PDO('mysql:host='.$dbhost.';dbname='.$dbname, $username, $password);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	return $db;
}
?>