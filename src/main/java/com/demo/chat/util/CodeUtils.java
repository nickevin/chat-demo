package com.demo.chat.util;

import java.io.File;
import java.util.List;

/**
 * Description of the file purpose.
 *
 * @author zhen.ni 2024-02-10 22:37
 */
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CodeUtils {

    public static void clean(File file) throws IOException {
        Path path = Paths.get(file.toURI());
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        String cleanedContent = clean(content);
        String bakFileName = generateBakFileName(path);
        Path bakPath = path.resolveSibling(bakFileName);
        Files.write(bakPath, cleanedContent.getBytes(StandardCharsets.UTF_8));
    }

    public static String clean (String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String withoutComments = removeComments(input);
        return removeEmptyLines(withoutComments);
    }

    private static String generateBakFileName(Path originalPath) {
        String fileName = originalPath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            String namePart = fileName.substring(0, dotIndex);
            String extPart = fileName.substring(dotIndex);
            return namePart + "_bak" + extPart;
        } else {
            return fileName + "_bak";
        }
    }

    private static String removeComments(String input) {
        final int NORMAL = 0;
        final int IN_SINGLE_QUOTE_STR = 1;
        final int IN_DOUBLE_QUOTE_STR = 2;
        final int IN_SINGLE_LINE_COMMENT = 3;
        final int IN_BLOCK_COMMENT = 4;
        final int IN_HTML_COMMENT = 5;
        int state = NORMAL;
        StringBuilder result = new StringBuilder();
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char current = input.charAt(i);
            switch (state) {
                case NORMAL:
                    if (current == '\'') {
                        state = IN_SINGLE_QUOTE_STR;
                        result.append(current);
                    }
                    else if (current == '"') {
                        state = IN_DOUBLE_QUOTE_STR;
                        result.append(current);
                    }
                    else if (current == '/' && i + 1 < len && input.charAt(i + 1) == '/') {
                        state = IN_SINGLE_LINE_COMMENT;
                        i++;
                    }
                    else if (current == '/' && i + 1 < len && input.charAt(i + 1) == '*') {
                        state = IN_BLOCK_COMMENT;
                        i++;
                    }
                    else if (current == '<' && i + 3 < len
                            && input.charAt(i + 1) == '!'
                            && input.charAt(i + 2) == '-'
                            && input.charAt(i + 3) == '-') {
                        state = IN_HTML_COMMENT;
                        i += 3;
                    }
                    else {
                        result.append(current);
                    }
                    break;
                case IN_SINGLE_QUOTE_STR:
                    if (current == '\\') {
                        result.append(current);
                        if (i + 1 < len) {
                            result.append(input.charAt(++i));
                        }
                    }
                    else if (current == '\'') {
                        state = NORMAL;
                        result.append(current);
                    }
                    else {
                        result.append(current);
                    }
                    break;
                case IN_DOUBLE_QUOTE_STR:
                    if (current == '\\') {
                        result.append(current);
                        if (i + 1 < len) {
                            result.append(input.charAt(++i));
                        }
                    } else if (current == '"') {
                        state = NORMAL;
                        result.append(current);
                    } else {
                        result.append(current);
                    }
                    break;
                case IN_SINGLE_LINE_COMMENT:
                    if (current == '\n') {
                        state = NORMAL;
                        result.append(current);
                    }
                    break;
                case IN_BLOCK_COMMENT:
                    if (current == '*' && i + 1 < len && input.charAt(i + 1) == '/') {
                        state = NORMAL;
                        i++;
                    }
                    break;
                case IN_HTML_COMMENT:
                    if (current == '-' && i + 2 < len
                            && input.charAt(i + 1) == '-'
                            && input.charAt(i + 2) == '>') {
                        state = NORMAL;
                        i += 2;
                    }
                    break;
            }
        }
        return result.toString();
    }

    private static String removeEmptyLines(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        String[] lines = s.split("\\r?\\n");
        List<String> nonEmptyLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmptyLines.add(line);
            }
        }
        return String.join("\n", nonEmptyLines);
    }
}