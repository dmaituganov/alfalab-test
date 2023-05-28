package org.dmaituganov.alfalab.test.task1.common.db.entities;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@SequenceGenerator(name = "document_seq", sequenceName = "DOCUMENT_DOCUMENT_ID_seq", allocationSize = 1)
@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq")
             private Long documentId;
    @JoinColumn(name="personId")
    @ManyToOne(fetch = FetchType.EAGER)
             private Person person;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "document_type")
    @Type(PostgreSQLEnumType.class)
    @NonNull private DocType type;
    @NonNull private String number;
    @NonNull private LocalDate expirationDate;

             private Instant createdAt;
             private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public enum DocType {
        passport,
        SNILS,
        INN,
        OMI_policy,
        foreign_passport,
        ;
    }
}
