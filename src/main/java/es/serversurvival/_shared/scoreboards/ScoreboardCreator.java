package es.serversurvival._shared.scoreboards;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ScoreboardCreator {
}
