package org.dmaituganov.alfalab.test.task1.common.db.dao;

import lombok.NonNull;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Person;

public interface PersonDAO {
    Person addPerson(@NonNull Person entity);
}
