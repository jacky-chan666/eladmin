/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */

package com.tplink.smb.common.data.management.system;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class JavadocStandardizer {

  // 标准化后的模板（支持动态插入 since 日期）
  private static final String STANDARD_JAVADOC =
      "\n/**\n" + " * @author Chen Jiayuan\n" + " * @version 1.0\n" + " * @since %s\n" + " */";

  // 匹配原始 Javadoc 的正则（从 /** 开始到 */ 结束，包含任意 @tags）
  private static final Pattern JAVADOC_PATTERN =
      Pattern.compile("^\\s*/\\*\\*(.*?)\\*/", Pattern.DOTALL | Pattern.MULTILINE);

  public static void main(String[] args) {
    // ✅ 在这里添加你要处理的所有路径（支持目录和单个文件）
    List<String> pathsToProcess =
        Arrays.asList(
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-common\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-generator\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-logging\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-system\\src\\main\\java",
            "E:\\User\\desktop\\tplink\\JavaProjects\\test\\eladmin\\common-data-management-system-tools\\src\\main\\java"
            // 可继续添加更多目录或具体 .java 文件
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

  /** 处理单个 Java 文件：标准化其第一个 Javadoc 注释块 */
  private static void processFile(Path filePath, Set<Path> processedFiles) {
    synchronized (processedFiles) {
      if (processedFiles.contains(filePath)) return; // 防止重复处理
    }

    try {
      String content = Files.readString(filePath);
      String newContent = standardizeJavadoc(content);
      Files.writeString(
          filePath, newContent, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
      System.out.println("🔄 已处理: " + filePath);
      synchronized (processedFiles) {
        processedFiles.add(filePath);
      }
    } catch (IOException e) {
      System.err.println("❌ 处理失败: " + filePath + " -> " + e.getMessage());
    }
  }

  /** 将源码中的第一个 Javadoc 块标准化 */
  public static String standardizeJavadoc(String sourceCode) {
    // 动态生成 since 日期（格式：yyyy/MM/dd）
    String today = java.time.LocalDate.now().toString().replace("-", "/");

    // 使用 matcher 找到第一个匹配的 Javadoc 并替换
    Matcher matcher = JAVADOC_PATTERN.matcher(sourceCode);
    if (matcher.find()) {
      // 替换第一个找到的 Javadoc，其他保持不变
      return matcher.replaceFirst(String.format(STANDARD_JAVADOC, "2025/9/30")); // 固定日期 or 改为 today
    }

    // 如果没有找到 Javadoc，则不修改
    return sourceCode;
  }
}
