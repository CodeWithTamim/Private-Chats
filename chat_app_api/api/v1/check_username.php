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
if ($username == null) {
  echo json_encode(array("success" => false, "message" => "username cannot be null"));
  die();
}

//means not taken.
//now check if user already exists or not.
//if user exitsts already then just send his data.

$check_user = $conn->prepare("SELECT * FROM users WHERE username = ?");
$check_user->bind_param("s", $username);
$check_user->execute();
$checked_result = $check_user->get_result();
if ($checked_result->num_rows > 0) {
  echo json_encode(array("success" => true, "message" => "Receiver found !", "user" => null));
  die();
} else {
  echo json_encode(array("success" => false, "message" => "User doesn't exits.", "user" => null));
  die();
}
?>