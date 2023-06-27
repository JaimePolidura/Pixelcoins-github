package es.serversurvival.minecraftserver.webaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class WebActionGetDataResponse {
    @Getter private final List<FormField> parametros;
    @Getter private final String nombre;
    
    public static WebActionGetDataResponse fromWebAction(WebActionType webActionType) {
        return new WebActionGetDataResponse(
                Arrays.stream(webActionType.getRequestBodyClass().getDeclaredFields())
                        .map(WebActionGetDataResponse.FormField::fromJavaClassField)
                        .collect(Collectors.toList()),
                webActionType.getNombre()
        );
    }

    @AllArgsConstructor
    public static class FormField {
        @Getter private final String nombre;
        @Getter private final FormFieldType tipo;

        public static FormField fromJavaClassField(Field field) {
            return new FormField(field.getName(), FormFieldType.fromClass(field.getType()));
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
