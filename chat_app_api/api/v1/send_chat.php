<?php
include 'api_validate.php';

header('Content-Type: application/json');

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(array("success" => false, "message" => "Invalid request method."));
    die();
}

// Get data from JSON body
$json_data = file_get_contents('php://input');
$data = json_decode($json_data, true);

// Parse the data from array
$receiver_username = $data['receiver_username'] ?? null;
$sender_username = $data['sender_username'] ?? null;
$message = $data['message'] ?? null;

// Validate input
if ($message === null) {
    echo json_encode(array("success" => false, "message" => "Message cannot be null"));
    die();
}

if ($sender_username === null) {
    echo json_encode(array("success" => false, "message" => "Sender username cannot be null"));
    die();
}

if ($receiver_username === null) {
    echo json_encode(array("success" => false, "message" => "Receiver username cannot be null"));
    die();
}

// Insert chat message into the database
$insertChat = $conn->prepare("INSERT INTO chats (receiver_username, sender_username, message) VALUES (?, ?, ?)");
$insertChat->bind_param("sss", $receiver_username, $sender_username, $message);
$insert_result = $insertChat->execute();

// Check if message was successfully inserted
if ($insert_result) {
    echo json_encode(array("success" => true, "message" => "Message sent successfully."));
} else {
    echo json_encode(array("success" => false, "message" => "Failed to send message", "error" => $conn->error));
}

// Close prepared statements and database connection
$insertChat->close();
$conn->close();
?>
