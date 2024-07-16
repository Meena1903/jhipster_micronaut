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
import org.jhipster.space.domain.SpaceEvent;

/**
 * Micronaut Data  repository for the SpaceEvent entity.
 */
@Repository
public interface SpaceEventRepository extends JpaRepository<SpaceEvent, Long> {
    @Query(
        value = "select distinct spaceEvent_ from SpaceEvent spaceEvent_",
        countQuery = "select count(distinct spaceEvent_) from SpaceEvent spaceEvent_"
    )
    public Page<SpaceEvent> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct spaceEvent from SpaceEvent spaceEvent")
    public List<SpaceEvent> findAllWithEagerRelationships();

    @Query("select spaceEvent from SpaceEvent spaceEvent where spaceEvent.id =:id")
    public Optional<SpaceEvent> findOneWithEagerRelationships(Long id);
}
