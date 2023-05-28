package org.dmaituganov.alfalab.test.task1.common.db.hibernate.dao;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.dmaituganov.alfalab.test.task1.common.db.dao.DocumentDAO;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Document;
import org.hibernate.Transaction;

import java.util.function.Consumer;

@SuperBuilder
public class DocumentDAOImpl extends HibernateDAO<Document> implements DocumentDAO {
    @Override
    public Document addDocument(@NonNull Document entity) {
        return addEntity(entity);
    }

    @Override
    public void findDocuments(@NonNull String partOfDocumentNumber, Consumer<Document> consumer) {
        String queryStr = """
            SELECT d
            FROM Person AS p INNER JOIN p.documents AS d
            WHERE d.expirationDate > now() AND d.number LIKE concat('%',?1,'%') 
            """;
        Transaction tran = session.beginTransaction();
        session.createSelectionQuery(queryStr, Document.class)
            .setParameter(1, partOfDocumentNumber)
            .getResultStream()
            .forEach(consumer);
        tran.commit();
    }
}
