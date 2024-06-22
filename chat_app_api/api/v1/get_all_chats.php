<?php
include 'api_validate.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(array("success" => false, "message" => "Invalid request method."));
    die();
}

// Get data from JSON body
$json_data = file_get_contents('php://input');

// Decode the JSON and convert to an array
$data = json_decode($json_data, true);

// Parse the data from array
$sender_username = $data['sender_username'] ?? null;
$receiver_username = $data['receiver_username'] ?? null;

if ($sender_username === null || $receiver_username === null) {
    echo json_encode(array("success" => false, "message" => "Usernames cannot be null"));
    die();
}

// Prepare and execute the SQL statement
$get_all_chats = $conn->prepare("SELECT * FROM chats WHERE (sender_username = ? AND receiver_username = ?) OR (sender_username = ? AND receiver_username = ?)");
$get_all_chats->bind_param("ssss", $sender_username, $receiver_username, $receiver_username, $sender_username);
$get_all_chats->execute();
$result = $get_all_chats->get_result();

$chats = array();
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $chats[] = $row;
    }
    echo json_encode(array("success" => true, "message" => "Got all chats", "chats" => $chats));
} else {
    echo json_encode(array("success" => false, "message" => "No chats found between the specified users.", "chats" => $chats));
}

$get_all_chats->close();
$conn->close();
?>
