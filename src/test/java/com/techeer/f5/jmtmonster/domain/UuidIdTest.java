package com.techeer.f5.jmtmonster.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.techeer.f5.jmtmonster.global.config.QuerydslConfig;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Import({QuerydslConfig.class})
class UuidIdTest {

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("uuid2 generator로 ID 만들기")
    class CreateUuidIdUsingGenerator {

        @Test
        @Transactional
        @Commit
        @DisplayName("ID 없이 생성 - persist 전 null, 후 UUID")
        void createWithoutId_idGeneratedOnlyAfterPersist() {
            // given
            UuidIdEntity entity = new UuidIdEntity("Entity");
            UUID beforeId = entity.getId();  // null

            // when
            em.persist(entity);

            // then
            UUID afterId = entity.getId();
            assertThat(afterId).isNotEqualTo(beforeId);
        }
    }

    @Nested
    @DisplayName("@Builder.Default로 UUID.randomUUID() 디폴트 값으로 넣기")
    class CreateUuidIdFromAppTest {

        @Test
        @Transactional
        @DisplayName("Builder로 ID 주입 안함 - DB Insert 실패")
        void createUsingBuilderWithOutId_dbInsertFail() {
            UuidIdFromAppEntity entity = UuidIdFromAppEntity.builder()
                    .name("Entity")
                    .build();

            System.out.println("entity.id = " + entity.getId());

            assertThatThrownBy(() -> {
                em.persist(entity);
            })
                    .isInstanceOf(PersistenceException.class)
                    .hasMessageContaining("detached entity passed to persist");
        }

        @Test
        @Transactional
        @DisplayName("Builder로 ID 주입 - DB Insert 실패")
        void createUsingBuilderWithId_dbInsertFail() {
            UuidIdFromAppEntity entity = UuidIdFromAppEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Entity")
                    .build();

            System.out.println("entity.id = " + entity.getId());
            assertThat(entity.getId()).isNotNull();

            assertThatThrownBy(() -> {
                em.persist(entity);
            })
                    .isInstanceOf(PersistenceException.class)
                    .hasMessageContaining("detached entity passed to persist");
        }

        @Test
        @Transactional
        @DisplayName("생성자로 ID 주입 안함 - persist 전 null, 후 UUID")
        void createUsingConstructorWithOutId_differentIdBeforeAndAfterPersist() {
            // given
            UuidIdFromAppEntity entity = new UuidIdFromAppEntity("Entity");
            assertThat(entity.getId()).isNull();

            // when
            em.persist(entity);

            // then
            UUID persistedId = entity.getId();
            UuidIdFromAppEntity found = em.find(UuidIdFromAppEntity.class, persistedId);
            UUID foundId = entity.getId();
            assertThat(persistedId).isNotNull();
        }

        @Test
        @Transactional
        @DisplayName("생성자로 ID 주입 - DB Insert 실패")
        void createUsingConstructorWithId_dbInsertFail() {
            // given
            UuidIdFromAppEntity entity = new UuidIdFromAppEntity(UUID.randomUUID(), "Entity");

            System.out.println("entity.id = " + entity.getId());
            assertThat(entity.getId()).isNotNull();

            assertThatThrownBy(() -> {
                em.persist(entity);
            })
                    .isInstanceOf(PersistenceException.class)
                    .hasMessageContaining("detached entity passed to persist");
        }
    }

    @Nested
    @DisplayName("uuid2 generator 설정 없이 만들기")
    class CreateUuidFromAppWoGenEntityTest {

        @Test
        @Transactional
        @DisplayName("Builder로 ID 주입 - persist 전후 ID 일치")
        void createUsingBuilderWithId_sameIdBeforeAndAfterPersist() {
            // given
            UUID expectedId = UUID.randomUUID();
            UuidIdFromAppWoGenEntity entity = UuidIdFromAppWoGenEntity.builder()
                    .id(expectedId)
                    .name("Entity")
                    .build();

            // when
            em.persist(entity);

            // then
            UuidIdFromAppWoGenEntity found = em.find(
                    UuidIdFromAppWoGenEntity.class, entity.getId());
            UUID foundId = found.getId();
            assertThat(foundId).isEqualTo(expectedId);
        }

        @Test
        @Transactional
        @DisplayName("Builder로 ID 주입 안함 - persist 전후 ID 일치")
        void createUsingBuilderWithOutId_sameIdBeforeAndAfterPersist() {
            UuidIdFromAppWoGenEntity entity = UuidIdFromAppWoGenEntity.builder()
                    .name("Entity")
                    .build();
            UUID expectedId = entity.getId();
            assertThat(expectedId).isNotNull();

            em.persist(entity);

            UuidIdFromAppWoGenEntity found = em.find(
                    UuidIdFromAppWoGenEntity.class, entity.getId());
            UUID foundId = found.getId();

            assertThat(foundId).isEqualTo(expectedId);
        }

        @Test
        @Transactional
        @DisplayName("생성자로 ID 주입 - persist 전후 ID 일치")
        void createUsingConstructorWithId_sameIdBeforeAndAfterPersist() {
            // given
            UUID expectedId = UUID.randomUUID();
            UuidIdFromAppWoGenEntity entity = new UuidIdFromAppWoGenEntity(expectedId, "Entity");

            // when
            em.persist(entity);

            // then
            UuidIdFromAppWoGenEntity found = em.find(
                    UuidIdFromAppWoGenEntity.class, entity.getId());
            UUID foundId = found.getId();
            assertThat(foundId).isEqualTo(expectedId);
        }

        @Test
        @Transactional
        @DisplayName("생성자로 ID 주입 안함 - DB Insert 실패")
        void createUsingConstructorWithOutId_shouldFail() {
            // Builder
            UuidIdFromAppWoGenEntity entity = new UuidIdFromAppWoGenEntity("Entity");

            assertThat(entity.getId()).isNull();

            // ID가 없어서 Insert 실패
            assertThatThrownBy(() -> {
                em.persist(entity);
            })
                    .isInstanceOf(PersistenceException.class)
                    .hasMessageContaining(
                            "ids for this class must be manually assigned before calling save");
        }
    }
}


@Entity
@Getter
@NoArgsConstructor
@Profile("test")
class UuidIdEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    public UuidIdEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UuidIdEntity(String name) {
        this.name = name;
    }
}


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Profile("test")
class UuidIdFromAppEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String name;

    public UuidIdFromAppEntity(String name) {
        this.name = name;
    }
}

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Profile("test")
class UuidIdFromAppWoGenEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String name;

    public UuidIdFromAppWoGenEntity(String name) {
        this.name = name;
    }
}
