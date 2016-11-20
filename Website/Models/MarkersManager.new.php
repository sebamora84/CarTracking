<?php
require 'rb.php';
class MarkersManager 
{	
	function __construct(){
		$username="2244948_cartrk";
		$password="sebichin84";
		$dbname="2244948_cartrk";
		$dbhost="fdb15.biz.nf";
		R::setup('mysql:host='.$dbhost.';dbname='.$dbname, $username, $password);
	}
	
	function getMarkers($unit_id, $last_id){
		$markers = R::find( 'marker', ' id > ? AND unit_id = ? ORDER BY id', [ $last_id , $unit_id] );
		// Iterate through the rows, adding nodes for each
		if (count($markers)==0)
		{
			return null;
		}
		return $marker;		
	}
	
	function getMarker($id){
				
		$markers = R::findOne( 'marker', ' id = ?', [ $id ] );
		return $marker;
	}
	
	function createMarker($id, $device_id, $unit_id ,$lat ,$lng ,$accuracy ,$type, $timestamp){
		
		  $marker = R::dispense( 'marker' );
		  $marker->id = $id;
		  $marker->device_id = $device_id;
		  $marker->unit_id = $unit_id;
		  $marker->lat = $lat;
		  $marker->lng = $lng;
		  $marker->accuracy = $accuracy;
		  $marker->type = $type;		  
		  $marker->timestamp = $timestamp;
		  $internal_id = R::store( $marker );
	}
}

?>