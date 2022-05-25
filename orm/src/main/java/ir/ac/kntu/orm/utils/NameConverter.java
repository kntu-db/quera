package ir.ac.kntu.orm.utils;

public class NameConverter {
    public static String fieldToSetter(String fieldName) {
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    public static String fieldToGetter(String fieldName) {
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
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
        return "set" + Character.toUpperCase(getterName.charAt(3)) + getterName.substring(4);
    }

    public static String setterToGetter(String setterName) {
        return "get" + Character.toUpperCase(setterName.charAt(3)) + setterName.substring(4);
    }
}
