package org.dmaituganov.alfalab.test.task1.common.db.hibernate.dao;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.dmaituganov.alfalab.test.task1.common.db.dao.PersonDAO;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Person;

@SuperBuilder
public class PersonDAOImpl extends HibernateDAO<Person> implements PersonDAO {
    @Override
    public Person addPerson(@NonNull Person entity) {
        return addEntity(entity);
    }
}
