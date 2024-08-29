package com.sahinokdem.researcher_metadata.annotation;

import com.sahinokdem.researcher_metadata.enums.State;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DFA {
    State value() default State.SEND_FORM;
}
