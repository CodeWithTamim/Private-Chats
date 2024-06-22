<?php
include 'db.php';
header("Content-Type: application/json");
$valid_key = "Hacker";
$posted_key = $_GET['api_key'] ?? null;

if($posted_key !== $valid_key){
  echo json_encode(array("success"=>false,"message"=>"Unauthorized"));
  die();
}

?>