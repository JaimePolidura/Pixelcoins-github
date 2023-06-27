package es.serversurvival.minecraftserver.webaction.messages;

import es.serversurvival.minecraftserver.webaction.WebActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class WebActionGetDataResponse {
    @Getter private final List<FormParam> params;
    @Getter private final String name;
    
    public static WebActionGetDataResponse fromWebAction(WebActionType webActionType) {
        return new WebActionGetDataResponse(
                Arrays.stream(webActionType.getRequestBodyClass().getDeclaredFields())
                        .map(FormParam::fromJavaClassField)
                        .collect(Collectors.toList()),
                webActionType.getNombre()
        );
    }

    @AllArgsConstructor
    public static class FormParam {
        @Getter private final String name;
        @Getter private final FormFieldType type;
        @Getter private final String desc;
        @Getter private final int showPriority;

        public static FormParam fromJavaClassField(Field field) {
            WebActionFormParam annotation = field.getAnnotation(WebActionFormParam.class);
            int showPriority = annotation != null ? annotation.showPriory() : 0;
            String desc = annotation != null ? annotation.desc() : "";

            return new FormParam(field.getName(), FormFieldType.fromClass(field.getType()), desc, showPriority);
        }
    }

    public enum FormFieldType {
        NUMBER, TEXT;

        public static FormFieldType fromClass(Class<?> clazz) {
            return clazz == int.class || clazz == short.class || clazz == long.class || clazz == double.class ?
                    FormFieldType.NUMBER :
                    FormFieldType.TEXT;
        }
    }
}
