package ir.ac.kntu.web.model;

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
public class Institute {
    @Key
    @Property
    private Integer id;

    @Property
    private String name;

    @Property
    private String type;

    @Relation(type = Relation.Type.ManyToOne)
    private City city;

    @Property
    private String website;

    @Relation(type = Relation.Type.ManyToOne)
    private User user;
}
