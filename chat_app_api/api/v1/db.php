<?php
$host = "localhost";
$user = "root";
$pass =  "";
$db = "chat_app";

$conn = new mysqli($host,$user,$pass,$db);

if($conn->connect_error){
  echo json_encode(array("success"=>false,"message"=>"Failed to connect to database. ".$conn->connect_error));
  die();
}

?>