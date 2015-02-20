package com.nortal.lorque.plugin.impl;

import com.nortal.lorque.plugin.LorquePlugin;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import java.util.*;

/**
 * @author Vassili Jakovlev
 */
@Component(name = "PluginService")
@Instantiate
@Provides
public class PluginServiceImpl implements PluginService {

  @Requires(optional = false, specification = LorquePlugin.class)
  private Collection<LorquePlugin> plugins;

  @Override
  public Collection<LorquePlugin> getPlugins() {
    return Collections.unmodifiableCollection(plugins);
  }

}