package ir.ac.kntu.orm.repo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface Join {
    String alias();
    String path();
}
