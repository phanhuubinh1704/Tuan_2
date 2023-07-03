package ChatApp;

import redis.clients.jedis.Jedis;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatApp {
	private static final String Chat = "chat_messages";
	public static void main(String[] args) {
		JFrame frame = new JFrame("Chat App 1");
		final JTextArea chatArea = new JTextArea();
		chatArea.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);

		final JTextField messageField = new JTextField();
		messageField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		messageField.setBounds(0, 330, 429, 48);

		JButton sendButton = new JButton("Gửi");
		sendButton.setBackground(Color.RED);
		sendButton.setBounds(439, 330, 61, 49);

		final Jedis jedis = new Jedis(
				"redis://default:tAoljg4N4LvV7gxFTiEWsR053wKw1epJ@redis-10751.c299.asia-northeast1-1.gce.cloud.redislabs.com:10751");
		String chatMessages = jedis.get(Chat);
		if (chatMessages != null) {
			chatArea.setText(chatMessages);
		}

		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = messageField.getText();
				if (!message.isEmpty()) {
					String formattedMessage = message + "\n";
					chatArea.append(formattedMessage);
					jedis.append(Chat, formattedMessage);
					messageField.setText("");
				}
			}
		});
		frame.getContentPane().setLayout(null);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setBounds(0, 0, 500, 300);
		frame.getContentPane().add(scrollPane);
		frame.getContentPane().add(messageField);
		frame.getContentPane().add(sendButton);
		frame.setSize(520, 437);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				jedis.close();
			}
		});
	}
}
