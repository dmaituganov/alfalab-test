package org.dmaituganov.alfalab.test.task1.common.db.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    @Override
    public Identifier toPhysicalCatalogName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertToUpperSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertToUpperSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertToUpperSnakeCase(identifier);
    }

    private Identifier convertToUpperSnakeCase(final Identifier identifier) {
        if (identifier == null) {
            return null;
        }
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText()
            .replaceAll(regex, replacement)
            .toUpperCase();
        return Identifier.toIdentifier(newName, true);
    }
}