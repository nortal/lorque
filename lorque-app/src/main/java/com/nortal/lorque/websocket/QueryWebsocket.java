package com.nortal.lorque.websocket;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Vassili Jakovlev
 */
@ServerEndpoint(value = "/ws/queries", encoders = {JsonEncoder.class})
public class QueryWebsocket {

  private static Session session;

  @OnOpen
  public void start(Session session) {
    if (QueryWebsocket.session == null || !QueryWebsocket.session.isOpen()) {
      QueryWebsocket.session = session;
    }
    jndiBind();
  }

  private void jndiBind() {
    Context initContext = null;
    try {
      initContext = new InitialContext();
      initContext.rebind("queryWebsocket", this);
    } catch (NamingException e) {
      e.printStackTrace();
    }

  }

  @OnClose
  public void close() {
    QueryWebsocket.session = null;
  }

  private static void closeSession(Session session) {
    try {
      session.close();
    } catch (IOException e) {
      // Ignore
    }
  }

  private static void sendMessage(Session session, Object message) {
    if (!session.isOpen()) {
      return;
    }
    try {
      session.getBasicRemote().sendObject(message);
    } catch (IOException | EncodeException e) {
      closeSession(session);
    }
  }

  public static void broadcast(Object msg) {
    if (session == null) {
      return;
    }
    session.getOpenSessions().stream().filter(Session::isOpen).forEach(s -> sendMessage(s, msg));
  }


}