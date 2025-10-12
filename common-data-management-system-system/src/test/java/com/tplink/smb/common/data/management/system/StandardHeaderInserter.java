package com.tplink.smb.common.data.management.system;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StandardHeaderInserter {

  // 标准版权头
  private static final String COPYRIGHT_HEADER =
      "/*\n" + " * Copyright (c) 2025, TP-Link. All rights reserved.\n" + " */";

  public static void main(String[] args) {
    // ✅ 在这里直接填写你要处理的路径（可以是多个）
    List<String> pathsToProcess =
        Arrays.asList(
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-common\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-generator\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-logging\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-system\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-tools\\src\\main\\java"

            // 添加更多路径...
            );

    Set<Path> processedFiles = new HashSet<>();

    for (String pathStr : pathsToProcess) {
      Path path = Paths.get(pathStr.trim());

      if (!Files.exists(path)) {
        System.err.println("❌ 路径不存在: " + path);
        continue;
      }

      try {
        if (Files.isRegularFile(path)) {
          // 是单个 Java 文件
          if (path.toString().endsWith(".java")) {
            processFile(path, processedFiles);
          } else {
            System.out.println("⚠️  跳过非 Java 文件: " + path);
          }
        } else if (Files.isDirectory(path)) {
          // 是目录，遍历所有 .java 文件
          Files.walk(path)
              .filter(p -> p.toString().endsWith(".java"))
              .forEach(p -> processFile(p, processedFiles));
        } else {
          System.err.println("❓ 不可识别的路径类型: " + path);
        }
      } catch (IOException e) {
        System.err.println("🚫 访问失败: " + path + " -> " + e.getMessage());
      }
    }

    System.out.println("✅ 共处理 " + processedFiles.size() + " 个 Java 文件。");
  }

  /** 处理单个 Java 文件：替换 package 前所有内容为标准头 */
  private static void processFile(Path filePath, Set<Path> processedFiles) {
    synchronized (processedFiles) {
      if (processedFiles.contains(filePath)) return;
    }

    try {
      String content = Files.readString(filePath);

      // 查找第一个 "package" 的位置
      int packageIndex = content.indexOf("package ");
      if (packageIndex == -1) {
        System.out.println("🟡 无 package 声明，跳过: " + filePath);
        return; // 没有 package，跳过
      }

      // 保留 package 及之后的内容
      String packageAndRest = content.substring(packageIndex);
      String newContent = COPYRIGHT_HEADER + "\n" + packageAndRest;

      // 写回文件
      Files.writeString(
          filePath, newContent, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

      System.out.println("🔄 已更新头部: " + filePath);
      synchronized (processedFiles) {
        processedFiles.add(filePath);
      }

    } catch (IOException e) {
      System.err.println("❌ 修改失败: " + filePath + " -> " + e.getMessage());
    }
  }
}
