package org.dmaituganov.alfalab.test.task1.generator;

import org.dmaituganov.alfalab.test.task1.common.db.dao.DocumentDAO;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Document;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Person;
import org.dmaituganov.alfalab.test.task1.common.db.hibernate.HibernateDAOProvider;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class GenerateDocuments {
    private static final List<Person> PERSONS = new ArrayList<>();
    static {
        List<Document> docs = new ArrayList<>();
        var vlad = new Person()
            .setGender(Person.Gender.male)
            .setFirstName("Владислав")
            .setMiddleName("Николаевич")
            .setLastName("Скрипко")
            .setBirthDate(LocalDate.of(1989, 12, 3))
            .setDocuments(docs);
        docs.add(new Document()
            .setType(Document.DocType.passport)
            .setNumber("77798879")
            .setExpirationDate(vlad.getBirthDate().plus(20, ChronoUnit.YEARS)));
        docs.add(new Document()
            .setType(Document.DocType.passport)
            .setNumber("77798880")
            .setExpirationDate(vlad.getBirthDate().plus(45, ChronoUnit.YEARS)));
        docs.add(new Document()
            .setType(Document.DocType.INN)
            .setNumber("8078777789078")
            .setExpirationDate(DocumentDAO.NO_EXPIRATION_DATE));
        docs.add(new Document()
            .setType(Document.DocType.SNILS)
            .setNumber("870-123458")
            .setExpirationDate(LocalDate.of(2025, 1, 30)));

        docs = new ArrayList<>();
        var elena = new Person()
            .setGender(Person.Gender.female)
            .setFirstName("Елена")
            .setMiddleName("Викторовна")
            .setLastName("Бойко")
            .setBirthDate(LocalDate.of(1991, 3, 23))
            .setDocuments(docs);
        docs.add(new Document()
            .setType(Document.DocType.passport)
            .setNumber("778907098")
            .setExpirationDate(elena.getBirthDate().plus(20, ChronoUnit.YEARS)));
        docs.add(new Document()
            .setType(Document.DocType.passport)
            .setNumber("7789770909")
            .setExpirationDate(elena.getBirthDate().plus(45, ChronoUnit.YEARS)));

        docs = new ArrayList<>();
        Person valya = new Person()
            .setGender(Person.Gender.female)
            .setFirstName("Валентина")
            .setMiddleName("Юрьевна")
            .setLastName("Куклина")
            .setBirthDate(LocalDate.of(1996, 2, 2))
            .setDocuments(docs);
        docs.add(new Document()
            .setType(Document.DocType.OMI_policy)
            .setNumber("87810342777")
            .setExpirationDate(LocalDate.of(2045, 1, 12)));
        docs.add(new Document()
            .setType(Document.DocType.foreign_passport)
            .setNumber("0784677734346")
            .setExpirationDate(LocalDate.of(2026, 8, 19)));

        PERSONS.add(vlad);
        PERSONS.add(elena);
        PERSONS.add(valya);
    }

    public static void main(String[] args) {
        try (var hibernate = new HibernateDAOProvider()) {
            PERSONS.forEach(pd -> hibernate.persons().addPerson(pd));
        }
    }
}
