package ir.ac.kntu.orm.mapping.annotations;

public @interface Entity {
    String tableName() default "";
}
