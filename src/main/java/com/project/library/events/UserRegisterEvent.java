package com.project.library.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisterEvent {
    private String email;

//    public UserRegisterEvent(Object source) {
//        super(source);
//    }
}
