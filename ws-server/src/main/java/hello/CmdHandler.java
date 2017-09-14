package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * Created by zhipeng.zuo on 2017/9/13.
 */
public class CmdHandler extends TextWebSocketHandler {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    TextMessage msg = new TextMessage ("back, " + message.getPayload() + "!");

    logger.info("message received: {}", message.getPayload());
    try {
      session.sendMessage(msg);
      logger.info("message  session.sendMessage: {}",msg);
    } catch (IOException e){
     logger.error(e.getMessage(), e);
  }
}
}
