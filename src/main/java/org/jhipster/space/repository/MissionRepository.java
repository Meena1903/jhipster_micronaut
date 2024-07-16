package org.jhipster.space.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
// TODO what is MN equivalent?
// import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.jhipster.space.domain.Mission;

/**
 * Micronaut Data  repository for the Mission entity.
 */
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    @Query(value = "select distinct mission_ from Mission mission_", countQuery = "select count(distinct mission_) from Mission mission_")
    public Page<Mission> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct mission from Mission mission")
    public List<Mission> findAllWithEagerRelationships();

    @Query("select mission from Mission mission where mission.id =:id")
    public Optional<Mission> findOneWithEagerRelationships(Long id);
}
