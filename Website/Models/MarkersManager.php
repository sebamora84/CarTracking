<?php
include 'dbConnection.php';
include 'Marker.php';
class MarkersManager 
{	
	function getMarkers($unit_id, $last_id){
		// Opens a connection
		$connection=GetDbConnection();
		// Select all the rows in the markers table
		$statement = $connection->prepare("SELECT * FROM markers WHERE id>:last_id AND unit_id=:uid ORDER BY id ASC");
		$statement->bindParam(':last_id', $last_id);
		$statement->bindParam(':uid', $unit_id);
		$statement->execute();
		$list=array();
		// Iterate through the rows, adding nodes for each
		while ($row = $statement->fetch()){
		  $marker = new Marker();		  
		  $marker->id = $row['id'];
		  $marker->unit_id = $row['unit_id'];
		  $marker->lat = $row['lat'];
		  $marker->lng = $row['lng'];
		  $marker->type = $row['type'];
		  $marker->timestamp = $row['timestamp'];   
		  array_push($list,$marker);	  
		}
		return $list;
	}
	
	function getMarker($id){
		// Opens a connection
		$connection=GetDbConnection();
		// Select the row in the markers table
		$statement = $connection->prepare("SELECT * FROM markers WHERE id=:id");
		$statement->bindParam(':id', $id);
		$statement->execute();
		// Iterate through the rows, adding nodes for each
		if ($statement->rowCount()==0)
		{
			return null;
		}
		$marker = new Marker();	
		while ($row = $statement->fetch()){		  	  
		  $marker->id = $row['id'];
		  $marker->unit_id = $row['unit_id'];
		  $marker->lat = $row['lat'];
		  $marker->lng = $row['lng'];
		  $marker->type = $row['type'];
		  $marker->timestamp = $row['timestamp'];   
		  $marker;
		}
		return $marker;
	}
	
	function createMarker($id ,$unit_id ,$lat ,$lng ,$type){
		// Opens a connection
		$connection=GetDbConnection();
		// Select all the rows in the markers table
		$statement = $connection->prepare("INSERT INTO markers(id ,unit_id ,lat ,lng ,type) VALUES (:id, :unit_id, :lat ,:lng ,:type);");
		$statement->bindParam(':id', $id, PDO::PARAM_INT);
		$statement->bindParam(':unit_id', $unit_id, PDO::PARAM_INT);
		$statement->bindParam(':lat', $lat, PDO::PARAM_STR);
		$statement->bindParam(':lng', $lng, PDO::PARAM_STR);
		$statement->bindParam(':type', $type, PDO::PARAM_STR);
		$statement->execute();
	}
}

?>