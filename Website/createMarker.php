<?php
include 'Models/MarkersManager.php';
if(isset($_REQUEST['id']))
{
    $id = $_REQUEST['id'];
}
if(isset($_REQUEST['unit_id']))
{
    $unit_id = $_REQUEST['unit_id'];
}
if(isset($_REQUEST['lat']))
{
    $lat = $_REQUEST['lat'];
}
if(isset($_REQUEST['lng']))
{
    $lng = $_REQUEST['lng'];
}
if(isset($_REQUEST['type']))
{
    $type = $_REQUEST['type'];
}

$markersManager = new MarkersManager();
$marker = $markersManager->getMarker($id);
if ($marker->id){
	//return duplicated id errorcode
	echo 3005;
	return;
}

$markersManager->createMarker($id ,$unit_id ,$lat ,$lng ,$type);
$marker = $markersManager->getMarker($id);
echo json_encode($marker);

?>