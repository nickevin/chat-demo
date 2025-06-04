package com.demo.chat.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Description of the file purpose.
 *
 * @author zhen.ni 2025-06-01 13:00
 */
public class CodeUtilsTest {

    @Test
    public void cleanFile() {
        try {
            CodeUtils.clean(new File("D:\\Workspace\\chat-demo\\src\\main\\resources\\static\\chat.html"));
            CodeUtils.clean(new File("D:\\Workspace\\chat-demo\\src\\main\\resources\\static\\style\\chat.css"));
        } catch (IOException e) {
        }
        try {
            CodeUtils.clean(new File("/Users/zhen/Documents/Workspace/java/chat-demo/src/main/resources/static/chat.html"));
            CodeUtils.clean(new File("/Users/zhen/Documents/Workspace/java/chat-demo/src/main/resources/static/style/chat.css"));
        } catch (IOException e) {
        }
    }

    @Test
    public void cleanCode() {
    }
}