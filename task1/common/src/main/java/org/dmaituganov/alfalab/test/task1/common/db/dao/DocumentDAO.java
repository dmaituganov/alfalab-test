package org.dmaituganov.alfalab.test.task1.common.db.dao;

import lombok.NonNull;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Document;

import java.time.LocalDate;
import java.util.function.Consumer;

public interface DocumentDAO {
    LocalDate NO_EXPIRATION_DATE = LocalDate.of(2200, 12, 31);
    Document addDocument(@NonNull Document entity);

    void findDocuments(@NonNull String partOfDocumentNumber, Consumer<Document> consumer);
}
