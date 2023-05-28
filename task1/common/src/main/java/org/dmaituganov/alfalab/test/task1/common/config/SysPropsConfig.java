package org.dmaituganov.alfalab.test.task1.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

public class SysPropsConfig {
    public interface EnumBasedSysProp {
        @NonNull
        String name();
        @Nullable
        String def();

        @Nullable
        default String get() {
            return System.getProperty(this.name().toLowerCase(), this.def());
        }
    }
    @AllArgsConstructor
    @Getter
    @Accessors(fluent = true)
    public enum Postgres implements EnumBasedSysProp {
        USER("task1"),
        PASSWORD("task1"),
        HOST("localhost"),
        PORT("5432"),
        DB("task1"),
        SCHEMA("public"),
        ;

        @Nullable private final String def;
        Postgres() {
            this(null);
        }

        public static String buildJdbcUrl() {
            return "jdbc:postgresql://%s:%s/%s".formatted(HOST.get(), PORT.get(), DB.get());
        }
    }

    @AllArgsConstructor
    @Getter
    @Accessors(fluent = true)
    public enum Hibernate implements EnumBasedSysProp {
        CONNECTION_POOL_SIZE("3"),
        SHOW_SQL("false"),
        CURRENT_SESSION_CONTEXT_CLASS("thread"),
        ;

        @Nullable private final String def;
        Hibernate() {
            this(null);
        }
    }
}
