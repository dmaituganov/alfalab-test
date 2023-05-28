package org.dmaituganov.alfalab.test.task1.common.db.entities;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@SequenceGenerator(name = "person_seq", sequenceName = "PERSON_PERSON_ID_seq", allocationSize = 1)
@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    private Long personId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "person_gender")
    @Type(PostgreSQLEnumType.class)
    private Gender gender;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private List<Document> documents;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.documents.forEach(d -> d.setPerson(this));
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public enum Gender {
        male,
        female
        ;
    }
}
