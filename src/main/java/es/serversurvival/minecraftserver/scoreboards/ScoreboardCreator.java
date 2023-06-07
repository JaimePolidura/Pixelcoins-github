package es.serversurvival.minecraftserver.scoreboards;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ScoreboardCreator {
}
