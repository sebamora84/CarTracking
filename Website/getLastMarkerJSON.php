<?php
include 'Models/MarkersManager.php';
if(isset($_REQUEST['unit_id']))
{
    $unit_id = $_REQUEST['unit_id'];
}
else
{
	$unit_id = 0;
}
if(isset($_REQUEST['last_id']))
{
    $last_id = $_REQUEST['last_id'];
}
else
{
	$last_id = 0;
}
$markersManager = new MarkersManager();
$marker = $markersManager->getLastMarker($unit_id);
echo json_encode($marker);
?>