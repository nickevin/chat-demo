package com.demo.chat.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Description of the file purpose.
 *
 * @author  zhen.ni 2025-06-01 13:00 
 */
public class CodeUtilsTest {

    @Test
    public void cleanFile() throws IOException {
        CodeUtils.clean(new File("D:\\Workspace\\chat-demo\\src\\main\\resources\\static\\chatbot.html"));
    }

    @Test
    public void cleanCode() {
    }
}