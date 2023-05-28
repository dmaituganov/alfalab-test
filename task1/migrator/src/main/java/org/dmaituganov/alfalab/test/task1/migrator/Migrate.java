package org.dmaituganov.alfalab.test.task1.migrator;

import org.dmaituganov.alfalab.test.task1.common.config.SysPropsConfig;
import org.flywaydb.core.Flyway;

public class Migrate {
    public static void main(String[] args) {
        String user = SysPropsConfig.Postgres.USER.get();
        String password = SysPropsConfig.Postgres.PASSWORD.get();
        String jdbc = SysPropsConfig.Postgres.buildJdbcUrl();
        String schema = SysPropsConfig.Postgres.SCHEMA.get();

        Flyway flyway = Flyway.configure().dataSource(jdbc, user, password)
            .schemas(schema)
            .baselineVersion("0")
            .baselineOnMigrate(true)
            .load();
        flyway.migrate();

        System.out.println("Migration done.");
    }
}
