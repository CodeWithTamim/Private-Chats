<?php
include 'api_validate.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
  echo json_encode(array("success" => false, "message" => "invalid request method."));
  die();
}


//get data from json body.

$json_data = file_get_contents('php://input');

//now decode the json and conver to an array
$data = json_decode($json_data, true);
//parse the data from array
$username = $data['username'] ?? null;
$device_id = $data['device_id'] ?? null;

if ($username == null) {
  echo json_encode(array("success" => false, "message" => "username cannot be null"));
  die();
}

if ($device_id == null) {
  echo json_encode(array("success" => false, "message" => "device_id cannot be null"));
  die();
}




//means not taken.
//now check if user already exists or not.
//if user exitsts already then just send his data.

$check_user = $conn->prepare("SELECT * FROM users WHERE username = ? AND device_id = ?");
$check_user->bind_param("ss", $username, $device_id);
$check_user->execute();
$checked_result = $check_user->get_result();
$checked_user = $checked_result->fetch_assoc();
if ($checked_result->num_rows > 0) {
  echo json_encode(array("success" => true, "message" => "Welcome again.", "user" => $checked_user));
  die();
}


//check if username is already taken or not.
$check_username = $conn->prepare("SELECT * FROM users WHERE username = ?");
$check_username->bind_param("s", $username);
$check_username->execute();
$result = $check_username->get_result();

if ($result->num_rows > 0) {
  echo json_encode(array("success" => false, "message" => "username already is taken"));
  die();
}



//now just register the user if not exists already in the database

$register_user = $conn->prepare("INSERT INTO users (username,device_id) VALUES(?,?)");
$register_user->bind_param("ss", $username, $device_id);
$register_result = $register_user->execute();

//now get the new user
$get_new_user = $conn->prepare("SELECT * FROM users WHERE username = ? AND device_id = ?");
$get_new_user->bind_param("ss", $username, $device_id);
$get_new_user->execute();
$get_new_result = $get_new_user->get_result();
$new_user = $get_new_result->fetch_assoc();


if ($register_result) {
  echo json_encode(array("success" => true, "message" => "registered successfully.", "user" => $new_user));
  die();
} else {
  echo json_encode(array("success" => false, "messag" => "failed to register user", "user" => $new_user));
  die();
}

?>