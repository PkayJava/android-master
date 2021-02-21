package com.angkorteam.android.master;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

import java.io.IOException;

@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BootApplication.class, args);
    }

    @Bean
    public PromptProvider createPromptProvider() {
        return () -> new AttributedString("blueprint:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

}
