package ir.ac.kntu.web;

import ir.ac.kntu.orm.mapping.annotations.Entity;
import ir.ac.kntu.orm.mapping.annotations.Key;
import ir.ac.kntu.orm.mapping.annotations.Property;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString @Entity
public class Person {
    @Key
    @Property
    private Integer id;
    @Property
    private String name;
    @Property
    private Integer age;
}
