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
$device_id = $data['device_id'] ?? null;
if ($device_id == null) {
  echo json_encode(array("success" => false, "message" => "device_id cannot be null"));
  die();
}

//means not taken.
//now check if user already exists or not.
//if user exitsts already then just send his data.

$check_user = $conn->prepare("SELECT * FROM users WHERE device_id = ?");
$check_user->bind_param("s", $device_id);
$check_user->execute();
$checked_result = $check_user->get_result();
$checked_user = $checked_result->fetch_assoc();
if ($checked_result->num_rows > 0) {
  echo json_encode(array("success" => true, "message" => "Welcome again.", "user" => $checked_user));
  die();
} else {
  echo json_encode(array("success" => false, "message" => "User doesn't exits.", "user" => $checked_user));
  die();
}
?>