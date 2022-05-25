package ir.ac.kntu.orm.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    public enum Type {
        OneToOne,
        OneToMany,
        ManyToOne,
        ManyToMany
    }
    Type type();
    String name();
    boolean lazy() default true;
}
