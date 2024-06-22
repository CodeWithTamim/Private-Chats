CREATE TABLE
  chats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    receiver_username VARCHAR(255),
    sender_username VARCHAR(255),
    message VARCHAR(255)
  );