CREATE TYPE person_gender AS ENUM ('male', 'female');
CREATE TABLE "PERSON" (
    "PERSON_ID" bigserial PRIMARY KEY NOT NULL,

    "GENDER"      person_gender NOT NULL,
    "FIRST_NAME"  text NOT NULL,
    "MIDDLE_NAME" text,
    "LAST_NAME"   text NOT NULL,
    "BIRTH_DATE"  date NOT NULL,

    "CREATED_AT" timestamptz NOT NULL,
    "UPDATED_AT" timestamptz NOT NULL
);

CREATE TYPE document_type AS ENUM ('passport', 'SNILS', 'INN', 'OMI_policy', 'foreign_passport');

CREATE TABLE "DOCUMENT" (
    "DOCUMENT_ID" bigserial PRIMARY KEY NOT NULL,
    "PERSON_ID"   bigint NOT NULL,

    "TYPE"            document_type NOT NULL,
    "NUMBER"          text NOT NULL,
    "EXPIRATION_DATE" date NOT NULL ,

    "CREATED_AT" timestamptz NOT NULL,
    "UPDATED_AT" timestamptz NOT NULL
);
CREATE UNIQUE INDEX document_unique_person_id_type_number_uidx ON "DOCUMENT" ("PERSON_ID", "TYPE", "NUMBER");