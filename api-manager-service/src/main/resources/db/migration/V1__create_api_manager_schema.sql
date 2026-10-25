CREATE TABLE ORGANIZATIONS (
                               ID BIGINT NOT NULL AUTO_INCREMENT,
                               NAME VARCHAR(150) NOT NULL,
                               SLUG VARCHAR(100) NOT NULL,
                               STATUS VARCHAR(20) NOT NULL,
                               CREATED_AT DATETIME(6) NOT NULL,
                               UPDATED_AT DATETIME(6) NOT NULL,
                               DELETED_AT DATETIME(6),
                               CONSTRAINT PK_ORGANIZATIONS PRIMARY KEY (ID),
                               CONSTRAINT UK_ORGANIZATIONS_NAME UNIQUE (NAME),
                               CONSTRAINT UK_ORGANIZATIONS_SLUG UNIQUE (SLUG)
);

CREATE TABLE ROLES (
                       ID BIGINT NOT NULL AUTO_INCREMENT,
                       NAME VARCHAR(50) NOT NULL,
                       CONSTRAINT PK_ROLES PRIMARY KEY (ID),
                       CONSTRAINT UK_ROLES_NAME UNIQUE (NAME)
);

CREATE TABLE MANAGEMENT_USERS (
                                  ID BIGINT NOT NULL AUTO_INCREMENT,
                                  ORGANIZATION_ID BIGINT,
                                  ROLE_ID BIGINT NOT NULL,
                                  USERNAME VARCHAR(100) NOT NULL,
                                  PASSWORD_HASH VARCHAR(255) NOT NULL,
                                  ENABLED BOOLEAN NOT NULL DEFAULT TRUE,
                                  CREATED_AT DATETIME(6) NOT NULL,
                                  UPDATED_AT DATETIME(6) NOT NULL,
                                  CONSTRAINT PK_MANAGEMENT_USERS PRIMARY KEY (ID),
                                  CONSTRAINT UK_MANAGEMENT_USERS_USERNAME UNIQUE (USERNAME),
                                  CONSTRAINT UK_MANAGEMENT_USERS_ORGANIZATION UNIQUE (ORGANIZATION_ID),
                                  CONSTRAINT FK_MANAGEMENT_USERS_ORGANIZATION FOREIGN KEY (ORGANIZATION_ID) REFERENCES ORGANIZATIONS (ID),
                                  CONSTRAINT FK_MANAGEMENT_USERS_ROLE FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ID)
);

CREATE TABLE API_ROUTES (
                            ID BIGINT NOT NULL AUTO_INCREMENT,
                            ORGANIZATION_ID BIGINT NOT NULL,
                            ROUTE_ID VARCHAR(100) NOT NULL,
                            NAME VARCHAR(200) NOT NULL,
                            PATH_PATTERN VARCHAR(500) NOT NULL,
                            TARGET_BASE_URL VARCHAR(1000) NOT NULL,
                            TARGET_PATH_PREFIX VARCHAR(500) NOT NULL,
                            REQUESTS_PER_MINUTE INTEGER,
                            RESPONSE_TIMEOUT_MS INTEGER NOT NULL,
                            IDEMPOTENCY_ENABLED BOOLEAN NOT NULL DEFAULT FALSE,
                            ACTIVE BOOLEAN NOT NULL DEFAULT FALSE,
                            CONFIGURATION_VERSION BIGINT NOT NULL,
                            CREATED_AT DATETIME(6) NOT NULL,
                            UPDATED_AT DATETIME(6) NOT NULL,
                            CONSTRAINT PK_API_ROUTES PRIMARY KEY (ID),
                            CONSTRAINT UK_API_ROUTES_TENANT_ROUTE UNIQUE (ORGANIZATION_ID, ROUTE_ID),
                            CONSTRAINT FK_API_ROUTES_ORGANIZATION FOREIGN KEY (ORGANIZATION_ID) REFERENCES ORGANIZATIONS (ID)
);

CREATE TABLE ROUTE_ALLOWED_METHODS (
                                       ROUTE_PK BIGINT NOT NULL,
                                       HTTP_METHOD VARCHAR(10) NOT NULL,
                                       CONSTRAINT PK_ROUTE_ALLOWED_METHODS PRIMARY KEY (ROUTE_PK, HTTP_METHOD),
                                       CONSTRAINT FK_ROUTE_ALLOWED_METHODS_ROUTE FOREIGN KEY (ROUTE_PK) REFERENCES API_ROUTES (ID) ON DELETE CASCADE
);

CREATE TABLE ROUTE_HEADER_RULES (
                                    ID BIGINT NOT NULL AUTO_INCREMENT,
                                    ROUTE_PK BIGINT NOT NULL,
                                    DIRECTION VARCHAR(20) NOT NULL,
                                    ACTION VARCHAR(20) NOT NULL,
                                    HEADER_NAME VARCHAR(200) NOT NULL,
                                    HEADER_VALUE VARCHAR(2000),
                                    CREATED_AT DATETIME(6) NOT NULL,
                                    UPDATED_AT DATETIME(6) NOT NULL,
                                    CONSTRAINT PK_ROUTE_HEADER_RULES PRIMARY KEY (ID),
                                    CONSTRAINT FK_ROUTE_HEADER_RULES_ROUTE FOREIGN KEY (ROUTE_PK) REFERENCES API_ROUTES (ID) ON DELETE CASCADE
);

CREATE TABLE ROUTE_CONFIGURATION_VERSIONS (
                                              ID BIGINT NOT NULL AUTO_INCREMENT,
                                              ORGANIZATION_ID BIGINT NOT NULL,
                                              ROUTE_ID VARCHAR(100) NOT NULL,
                                              VERSION_NUMBER BIGINT NOT NULL,
                                              CHANGE_TYPE VARCHAR(30) NOT NULL,
                                              CONFIGURATION_SNAPSHOT LONGTEXT NOT NULL,
                                              CREATED_AT DATETIME(6) NOT NULL,
                                              CONSTRAINT PK_ROUTE_CONFIGURATION_VERSIONS PRIMARY KEY (ID),
                                              CONSTRAINT UK_ROUTE_VERSIONS_TENANT_VERSION UNIQUE (ORGANIZATION_ID, ROUTE_ID, VERSION_NUMBER),
                                              CONSTRAINT FK_ROUTE_VERSIONS_ORGANIZATION FOREIGN KEY (ORGANIZATION_ID) REFERENCES ORGANIZATIONS (ID)
);

CREATE TABLE CONTROL_PLANE_AUDIT_RECORDS (
                                             ID BIGINT NOT NULL AUTO_INCREMENT,
                                             ORGANIZATION_ID BIGINT,
                                             ACTOR VARCHAR(100) NOT NULL,
                                             ACTION VARCHAR(100) NOT NULL,
                                             RESOURCE_TYPE VARCHAR(100) NOT NULL,
                                             RESOURCE_ID VARCHAR(100) NOT NULL,
                                             CORRELATION_ID VARCHAR(100),
                                             DURATION_MS BIGINT NOT NULL,
                                             RESULT VARCHAR(20) NOT NULL,
                                             DETAILS VARCHAR(2000),
                                             CREATED_AT DATETIME(6) NOT NULL,
                                             CONSTRAINT PK_CONTROL_PLANE_AUDIT_RECORDS PRIMARY KEY (ID),
                                             CONSTRAINT FK_AUDIT_ORGANIZATION FOREIGN KEY (ORGANIZATION_ID) REFERENCES ORGANIZATIONS (ID)
);