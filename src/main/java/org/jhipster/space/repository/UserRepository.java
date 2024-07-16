package org.jhipster.space.repository;

import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import java.time.Instant;
import java.util.Optional;
import org.jhipster.space.domain.User;

/**
 * Micronaut Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public static String USERS_CACHE = "usersByLogin";

    public Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    public Optional<User> findOneById(String id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_CACHE)
    public Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_CACHE)
    public Optional<User> findOneByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_CACHE)
    public Optional<User> findOneByLoginIgnoreCaseOrEmail(String login, String email);

    public Page<User> findByLoginNotEqual(String login, Pageable pageable);

    public void update(@Id String id, Instant createdDate);
}
