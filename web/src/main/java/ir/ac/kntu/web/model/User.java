package ir.ac.kntu.web.model;

import ir.ac.kntu.orm.mapping.annotations.Entity;
import ir.ac.kntu.orm.mapping.annotations.Key;
import ir.ac.kntu.orm.mapping.annotations.Property;
import ir.ac.kntu.orm.mapping.annotations.Relation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class User {
    @Key
    @Property
    private Integer id;

    @Property
    private String firstname;

    @Property
    private String lastname;

    @Property
    private String mail;

    @Property
    private String password;

    @Property
    private String phone;

    @Property
    private String status;

    @Property
    private String type;

    @Relation(type = Relation.Type.ManyToOne)
    private Institute institute;

    @Relation(type = Relation.Type.ManyToOne)
    private City city;

    @Property
    private Boolean isPublic;

    @Property
    private Date joinedAt;

    @Property
    private Date birthDate;

}
