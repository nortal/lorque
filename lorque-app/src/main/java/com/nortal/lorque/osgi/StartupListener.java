/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nortal.lorque.osgi;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerContainer;
import java.util.Enumeration;

public final class StartupListener implements ServletContextListener {
  private FrameworkService service;

  public void contextInitialized(ServletContextEvent event) {
    ServletContext servletContext = event.getServletContext();
    this.service = new FrameworkService(servletContext);
    this.service.start();
    Enumeration<String> attributeNames = servletContext.getAttributeNames();
    while (attributeNames.hasMoreElements()) {
      System.out.println(attributeNames.nextElement());
    }

    Object sc = servletContext.getAttribute("javax.websocket.server.ServerContainer");
    try {
//      System.out.println(sc instanceof ServerContainer);
//      System.out.println(sc instanceof WebSocketContainer);
//      System.out.println(sc.getClass().getClassLoader());
//      System.out.println(this.getClass().getClassLoader());
      ServerContainer serverContainer = (ServerContainer) sc;
//      serverContainer.addEndpoint(QueryWebsocket.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
// Add endpoint manually to server container
//    try {
//    } catch (DeploymentException e) {
//      e.printStackTrace();
//    }

//    onStartup(event);
  }

//  public void onStartup(ServletContextEvent event) {
//    AnnotationConfigWebApplicationContext appContext =
//        new AnnotationConfigWebApplicationContext();
//    appContext.register(AppConfig.class);
//    addApacheCxfServlet(event.getServletContext());
//    ContextLoaderListener contextLoaderListener = new ContextLoaderListener(appContext);
//    contextLoaderListener.contextInitialized(event);
////    container.addListener(contextLoaderListener);
//  }
//
//  private void addApacheCxfServlet(ServletContext servletContext) {
//
//    CXFServlet cxfServlet = new CXFServlet();
//
//    ServletRegistration.Dynamic appServlet = servletContext.addServlet("CXFServlet", cxfServlet);
//    appServlet.setLoadOnStartup(1);
//
//    Set<String> mappingConflicts = appServlet.addMapping("/api/v1/*");
//  }

  public void contextDestroyed(ServletContextEvent event) {
    this.service.stop();
  }
}