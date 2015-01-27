/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.nortal.lorque.websocket;

import java.io.IOException;
import java.util.Random;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/queries", encoders = {JsonEncoder.class})
public class QueryWebsocket {

  private static Session session;

  @OnOpen
  public void start(Session session) {
    if (QueryWebsocket.session == null || !QueryWebsocket.session.isOpen()) {
      QueryWebsocket.session = session;
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
    int i = new Random().nextInt(100);
    session.getOpenSessions().stream().filter(Session::isOpen).forEach(s -> sendMessage(s, msg));
  }


}