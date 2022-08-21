package com.example.astudy.entities;

import com.example.astudy.enums.CourseContentStatus;
import com.example.astudy.enums.CourseContentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * @Define Polymorphic Queries - querying a base class will retrieve all the subclass entities as well
 * Native SQL queries which query for entities that are mapped as part of an inheritance
 * must include all properties for the baseclass and all its subclasses.
 *
 * @annotation MappedSuperclass
 * This class no longer has an @Entity annotation (no longer has a supper class table).
 * Inheritance is only evident in the class but not the entity model.
 * It won't be persisted in the database by itself.
 * In this database, this will correspond to one Subclass table with columns for the declared
 * and inherited fields of the subclass.
 * If we're using this strategy, ancestors cannot contain associations with other entities.
 * The associations will be put into subclass.
 *
 * @anotaion Inheritance(strategy=InheritanceType.SINGLE_TABLE)
 * No longer have subclass tables. Default strategy if you don't specify one explicitly.
 * Create one table for each class hierarchy.
 * Supper class will have the declared properties along with all the properties of the Subclass.
 * Subclass associations will be placed in the Supper class
 * Note:
 * Since the records for all subclass entities will be in the same table, Hibernate needs a way to differentiate between them.
 * By default, this is done through a discriminator column called DTYPE that has the name of the entity as a value.
 * To customize the discriminator column, we can use the @DiscriminatorColumn and @DiscriminatorFormula annotation.
 * This strategy has the ADVANTAGE of polymorphic query performance since only one table needs to be accessed when querying parent entities.
 * On the other hand, this also means that we can no longer use NOT NULL constraints on subclass entity properties
 *
 * @annotation Inheritance(strategy = InheritanceType.JOINED)
 * Each class in the hierarchy is mapped to its table (have Supper class table along with Subclass table).
 * The only column that repeatedly appears in all the tables is the identifier, which will be used for joining them when needed.
 * The primary key of the Subclass entity also has a foreign key constraint to the primary key of its parent entity.
 * That mean supper and subclass must be having same ID name. To customize this column, we can add the @PrimaryKeyJoinColumn annotation.
 * Note:
 * The DISADVANTAGE of this inheritance mapping method is that retrieving entities requires joins between tables,
 * which can result in lower performance for large numbers of records.
 * The number of joins is higher when querying the parent class because it will join with every single related child
 * so performance is more likely to be affected the higher up the hierarchy we want to retrieve records.
 *
 * @annotation Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
 * This strategy maps each entity to its table, which contains all the properties of entity, including the ones inherited.
 * The resulting schema is similar to the one using @MappedSuperclass.
 * There are associations of the subclass to the class associated with the parent class.
 * But Table per Class will indeed define entities for parent classes, allowing associations and polymorphic queries as a result.
 * Id field should only be defined in the root class.
 *
 * Note:
 * GeneratedValue annotation of parent class must be GenerationType.TABLE
 * when querying the base class, which will return all the subclass records as well by using a UNION statement in the background.
 * The use of UNION can also lead to inferior performance when choosing this strategy.
 * Another issue is that we can no longer use identity key generation
 *
 *
 * @author vohuong108
 * @version 1.0
 * @since 1.0
 */

//@MappedSuperclass
@Entity(name = "week_content")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="rel_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public class WeekContent {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private int contentOrder;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(name = "content_status", columnDefinition = "varchar(255) default 'PRIVATE'")
    @Enumerated(EnumType.STRING)
    private CourseContentStatus contentStatus = CourseContentStatus.PRIVATE;

    @Column(name = "content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseContentType contentType;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date releaseDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN default 1")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

}
