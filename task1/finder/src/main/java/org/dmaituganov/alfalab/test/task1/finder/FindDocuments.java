package org.dmaituganov.alfalab.test.task1.finder;

import org.dmaituganov.alfalab.test.task1.common.db.entities.Document;
import org.dmaituganov.alfalab.test.task1.common.db.hibernate.HibernateDAOProvider;
import org.jetbrains.annotations.NotNull;

public class FindDocuments {
    private static String translate(@NotNull Document.DocType docType) {
        return switch (docType) {
            case passport -> "Паспорт";
            case SNILS -> "СНИЛС";
            case INN -> "ИНН";
            case OMI_policy -> "Полис ОМС";
            case foreign_passport -> "Загранпаспорт";
        };
    }
    public static void main(String[] args) {
        try (HibernateDAOProvider provider = new HibernateDAOProvider()) {
            provider.documents().findDocuments("777", d ->
                System.out.printf("%s %s %s, %s – %s\n",
                    d.getPerson().getLastName(), d.getPerson().getFirstName(), d.getPerson().getMiddleName(),
                    translate(d.getType()), d.getNumber()
                )
            );
        }
    }
}
