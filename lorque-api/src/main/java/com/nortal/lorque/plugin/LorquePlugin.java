package com.nortal.lorque.plugin;

import com.nortal.lorque.Query;

/**
 * @author Vassili Jakovlev
 */
public interface LorquePlugin {

  String getName();

  void execute(Query query, PluginCall pluginCall);

}
