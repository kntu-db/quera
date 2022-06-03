package ir.ac.kntu.web;

import ir.ac.kntu.orm.mapping.annotations.Entity;
import ir.ac.kntu.orm.mapping.annotations.Key;
import ir.ac.kntu.orm.mapping.annotations.Property;
import ir.ac.kntu.orm.mapping.annotations.Relation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Book {
    @Key
    @Property
    private String name;

    @Relation(type= Relation.Type.ManyToOne, name = "personId")
    private Person person;
}
