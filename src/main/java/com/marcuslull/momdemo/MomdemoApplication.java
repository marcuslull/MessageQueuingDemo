package com.marcuslull.momdemo;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@Push(PushMode.MANUAL)
@SpringBootApplication
@PropertySource("classpath:secrets.properties")
public class MomdemoApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(MomdemoApplication.class, args);
    }

}
