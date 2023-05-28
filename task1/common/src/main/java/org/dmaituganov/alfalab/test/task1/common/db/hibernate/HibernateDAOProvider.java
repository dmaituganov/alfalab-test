package org.dmaituganov.alfalab.test.task1.common.db.hibernate;

import org.dmaituganov.alfalab.test.task1.common.config.SysPropsConfig;
import org.dmaituganov.alfalab.test.task1.common.db.dao.DAOProvider;
import org.dmaituganov.alfalab.test.task1.common.db.dao.DocumentDAO;
import org.dmaituganov.alfalab.test.task1.common.db.dao.PersonDAO;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Document;
import org.dmaituganov.alfalab.test.task1.common.db.entities.Person;
import org.dmaituganov.alfalab.test.task1.common.db.hibernate.dao.DocumentDAOImpl;
import org.dmaituganov.alfalab.test.task1.common.db.hibernate.dao.PersonDAOImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.postgresql.Driver;

import java.util.Properties;

public class HibernateDAOProvider implements DAOProvider, AutoCloseable {
    private StandardServiceRegistry registry;
    private SessionFactory sessionFactory;

    private static Configuration prepareConfiguration() {
        Configuration conf = new Configuration();
        Properties props = new Properties();
        props.put(AvailableSettings.DRIVER, Driver.class.getName());
        props.put(AvailableSettings.URL, SysPropsConfig.Postgres.buildJdbcUrl());
        props.put(AvailableSettings.USER, SysPropsConfig.Postgres.USER.get());
        props.put(AvailableSettings.PASS, SysPropsConfig.Postgres.PASSWORD.get());

        props.put(AvailableSettings.POOL_SIZE, SysPropsConfig.Hibernate.CONNECTION_POOL_SIZE.get());
        props.put(AvailableSettings.SHOW_SQL, SysPropsConfig.Hibernate.SHOW_SQL.get());
        props.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, SysPropsConfig.Hibernate.CURRENT_SESSION_CONTEXT_CLASS.get());

        props.put(AvailableSettings.GLOBALLY_QUOTED_IDENTIFIERS, true);
        conf.addProperties(props);
        conf.setPhysicalNamingStrategy(new CustomPhysicalNamingStrategy());

        conf.addAnnotatedClass(Person.class);
        conf.addAnnotatedClass(Document.class);
        return conf;
    }
    public SessionFactory getSessionFactory() {
        if (this.sessionFactory == null) {
            try {
                Configuration configuration = prepareConfiguration();
                this.registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                this.sessionFactory = configuration.buildSessionFactory();
            } catch (RuntimeException e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                throw e;
            }
        }
        return sessionFactory;
    }

    public void shutdown() {
        if (this.registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    public void close() {
        shutdown();
    }

    @Override
    public PersonDAO persons() {
        return PersonDAOImpl.builder()
            .session(getSessionFactory().getCurrentSession())
            .build();
    }

    @Override
    public DocumentDAO documents() {
        return DocumentDAOImpl.builder()
            .session(getSessionFactory().getCurrentSession())
            .build();
    }
}
