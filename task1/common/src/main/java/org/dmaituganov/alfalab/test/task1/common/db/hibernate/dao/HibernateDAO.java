package org.dmaituganov.alfalab.test.task1.common.db.hibernate.dao;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;

@SuperBuilder
public class HibernateDAO<E> {
    @NonNull protected final Session session;

    public E addEntity(@NonNull E entity) {
        Transaction tran = this.session.beginTransaction();
        this.session.persist(entity);
        tran.commit();
        return entity;
    }
}
