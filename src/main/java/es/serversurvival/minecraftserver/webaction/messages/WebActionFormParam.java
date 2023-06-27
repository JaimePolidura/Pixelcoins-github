package es.serversurvival.minecraftserver.webaction.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WebActionFormParam {
    String desc() default "";
    int showPriory() default 0;
}
