package ir.ac.kntu.web.utils;

public class NameConverter {

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static String fieldToSetter(String fieldName) {
        return "set" + capitalize(fieldName);
    }

    public static String fieldToGetter(String fieldName) {
        return "get" + capitalize(fieldName);
    }

    public static String fieldToColumn(String fieldName) {
        return fieldName.toLowerCase();
    }

    public static String getterToField(String getterName) {
        return getterName.substring(3, 4).toLowerCase() + getterName.substring(4);
    }

    public static String setterToField(String setterName) {
        return setterName.substring(3, 4).toLowerCase() + setterName.substring(4);
    }

    public static String getterToSetter(String getterName) {
        return "set" + capitalize(getterName.substring(3));
    }

    public static String setterToGetter(String setterName) {
        return "get" + capitalize(setterName.substring(3));
    }
}
