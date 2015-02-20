package com.nortal.lorque.plugin.impl;

import com.nortal.lorque.plugin.LorquePlugin;

import java.util.Collection;

/**
 * Created by vassili on 19/02/15.
 */
public interface PluginService {
  Collection<LorquePlugin> getPlugins();
}
