/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.google.common.collect.Maps;
import com.tplink.smb.common.data.management.system.utils.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class ScpClientUtil {

  private static final Map<String, ScpClientUtil> instance = Maps.newHashMap();
  private final String ip;
  private final int port;
  private final String username;
  private final String password;

  public ScpClientUtil(String ip, int port, String username, String password) {
    this.ip = ip;
    this.port = port;
    this.username = username;
    this.password = password;
  }

  public static synchronized ScpClientUtil getInstance(
      String ip, int port, String username, String password) {
    instance.computeIfAbsent(ip, i -> new ScpClientUtil(i, port, username, password));
    return instance.get(ip);
  }

  public void getFile(String remoteFile, String localTargetDirectory) {
    Connection conn = new Connection(ip, port);
    try {
      conn.connect();
      boolean isAuthenticated = conn.authenticateWithPassword(username, password);
      if (!isAuthenticated) {
        System.err.println("authentication failed");
      }
      SCPClient client = new SCPClient(conn);
      client.get(remoteFile, localTargetDirectory);
    } catch (IOException ex) {
      Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      conn.close();
    }
  }

  public void putFile(String localFile, String remoteTargetDirectory) {
    putFile(localFile, null, remoteTargetDirectory);
  }

  public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory) {
    putFile(localFile, remoteFileName, remoteTargetDirectory, null);
  }

  public void putFile(
      String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
    Connection conn = new Connection(ip, port);
    try {
      conn.connect();
      boolean isAuthenticated = conn.authenticateWithPassword(username, password);
      if (!isAuthenticated) {
        System.err.println("authentication failed");
      }
      SCPClient client = new SCPClient(conn);
      if (StringUtils.isBlank(mode)) {
        mode = "0600";
      }
      if (remoteFileName == null) {
        client.put(localFile, remoteTargetDirectory);
      } else {
        client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
      }
    } catch (IOException ex) {
      Logger.getLogger(ScpClientUtil.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      conn.close();
    }
  }
}
