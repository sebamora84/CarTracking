<?php
include 'Models/MarkersManager.php';
if(isset($_REQUEST['id']))
{
    $id = $_REQUEST['id'];
}
if(isset($_REQUEST['device_id']))
{
    $device_id = $_REQUEST['device_id'];
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
if(isset($_REQUEST['accuracy']))
{
    $accuracy = $_REQUEST['accuracy'];
}
if(isset($_REQUEST['type']))
{
    $type = $_REQUEST['type'];
}
if(isset($_REQUEST['timestamp']))
{
    $timestamp = $_REQUEST['timestamp'];
}

$markersManager = new MarkersManager();
$marker = $markersManager->getMarker($id);
if ($marker->id){
	//return duplicated id
	echo "Marker already added";
	return;
}

$markersManager->createMarker($id ,$device_id, $unit_id ,$lat ,$lng, $accuracy ,$type, $timestamp);
$marker = $markersManager->getMarker($id);
echo json_encode($marker);
exit();
?>