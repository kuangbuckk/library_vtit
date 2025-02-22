package com.project.library.listeners;

import com.project.library.events.UserRegisterEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterListener {

    @Async
    @EventListener
    public void sendWelcomeEmail(UserRegisterEvent userRegisterEvent) {
        System.out.println("Sending welcome email to " + userRegisterEvent.getEmail() + "...");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Sent welcome email to " + userRegisterEvent.getEmail());
    }
}
