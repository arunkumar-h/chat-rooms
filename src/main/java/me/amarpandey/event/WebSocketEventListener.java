package me.amarpandey.event;

import me.amarpandey.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	public SimpMessageSendingOperations sendingOption;


	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection");
	}

	public void handleWebSocketDisconnectListener(SessionConnectedEvent event) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = headerAccessor.getSessionAttributes().get("username").toString();

		if (username != null) {
			logger.info("User Disconnected : " + username);

			Message message = new Message();

			message.setFrom(username);
			message.setMessageType(Message.Type.LEAVE);

			sendingOption.convertAndSend("/topic/public", message);
		}
	}
}
