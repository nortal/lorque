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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ProvisionActivator implements BundleActivator {
  private final ServletContext servletContext;

  public ProvisionActivator(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public void start(BundleContext context) throws Exception {
    servletContext.setAttribute(BundleContext.class.getName(), context);
    ArrayList<Bundle> installed = new ArrayList<>();
    for (URL url : findBundles()) {
      this.servletContext.log("Installing bundle [" + url + "]");
      Bundle bundle = context.installBundle(url.toExternalForm());
      installed.add(bundle);
    }
    for (Bundle bundle : installed) {
      bundle.start();
    }
  }

  public void stop(BundleContext context) throws Exception {
  }

  private List<URL> findBundles() throws Exception {
    ArrayList<URL> list = new ArrayList<>();
    list.addAll(loadPackagedBundles());
    list.addAll(loadUserBundles());
    return list;
  }

  private ArrayList<URL> loadPackagedBundles() throws MalformedURLException {
    ArrayList<URL> bundles = new ArrayList<>();
    for (Object o : this.servletContext.getResourcePaths("/WEB-INF/bundles/")) {
      String name = (String) o;
      if (name.endsWith(".jar")) {
        URL url = this.servletContext.getResource(name);
        if (url != null) {
          bundles.add(url);
        }
      }
    }
    return bundles;
  }

  private ArrayList<URL> loadUserBundles() throws MalformedURLException {
    ArrayList<URL> bundles = new ArrayList<>();
    String userDir = System.getProperty("user.home") + File.separator + ".lorque";
    File file = new File(userDir + File.separator + "plugins");
    if (!file.exists() || !file.isDirectory() || !file.getName().endsWith(".jar")) {
      return bundles;
    }
    File[] files = file.listFiles();
    if (files == null) {
      return bundles;
    }
    for (File plugin : files) {
      bundles.add(plugin.toURI().toURL());
    }
    return bundles;
  }
}