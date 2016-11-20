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
// Start XML file, create parent node
$dom = new DOMDocument("1.0");
$node = $dom->createElement("markers");
$parnode = $dom->appendChild($node);
header("Content-type: text/xml");
$markersManager = new MarkersManager();
$markers = $markersManager->getMarkers($unit_id,$last_id);
// Iterate through the rows, adding XML nodes for each
foreach ($markers as $marker){
  // ADD TO XML DOCUMENT NODE
  $node = $dom->createElement("marker");
  $newnode = $parnode->appendChild($node);
  $newnode->setAttribute("id", $marker->id);
  $newnode->setAttribute("unit_id", $marker->unit_id);
  $newnode->setAttribute("lat", $marker->lat);
  $newnode->setAttribute("lng", $marker->lng);
  $newnode->setAttribute("accuracy", $marker->accuracy);
  $newnode->setAttribute("type", $marker->type);
  $newnode->setAttribute("timestamp", $marker->timestamp);    
}
//close connection and return
echo $dom->saveXML();

?>