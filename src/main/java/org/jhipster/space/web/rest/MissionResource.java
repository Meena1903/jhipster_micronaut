package org.jhipster.space.web.rest;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.jhipster.space.domain.Mission;
import org.jhipster.space.repository.MissionRepository;
import org.jhipster.space.util.HeaderUtil;
import org.jhipster.space.util.PaginationUtil;
import org.jhipster.space.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link org.jhipster.space.domain.Mission}.
 */
@Controller("/api")
public class MissionResource {

    private final Logger log = LoggerFactory.getLogger(MissionResource.class);

    private static final String ENTITY_NAME = "mission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MissionRepository missionRepository;

    public MissionResource(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    /**
     * {@code POST  /missions} : Create a new mission.
     *
     * @param mission the mission to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new mission, or with status {@code 400 (Bad Request)} if the mission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/missions")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Mission> createMission(@Body Mission mission) throws URISyntaxException {
        log.debug("REST request to save Mission : {}", mission);
        if (mission.getId() != null) {
            throw new BadRequestAlertException("A new mission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mission result = missionRepository.save(mission);
        URI location = new URI("/api/missions/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /missions/:id} : Updates an existing mission.
     *
     * @param mission the mission to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated mission,
     * or with status {@code 400 (Bad Request)} if the mission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/missions/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Mission> updateMission(@Body Mission mission, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Mission : {}", mission);
        if (mission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Mission result = missionRepository.update(mission);
        return HttpResponse.ok(result).headers(
            headers -> HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, mission.getId().toString())
        );
    }

    /**
     * {@code GET  /missions} : get all the missions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of missions in body.
     */
    @Get("/missions{?eagerload}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<Mission>> getAllMissions(
        HttpRequest request,
        Pageable pageable,
        @Nullable String filter,
        @Nullable Boolean eagerload
    ) {
        if ("spaceevent-is-null".equals(filter)) {
            log.debug("REST request to get all Missions where spaceEvent is null");
            return HttpResponse.ok(
                StreamSupport.stream(missionRepository.findAll().spliterator(), false)
                    .filter(mission -> mission.getSpaceEvent() == null)
                    .collect(Collectors.toList())
            );
        }
        log.debug("REST request to get a page of Missions");
        Page<Mission> page;
        if (eagerload != null && eagerload) {
            page = missionRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = missionRepository.findAll(pageable);
        }
        return HttpResponse.ok(page.getContent()).headers(
            headers -> PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page)
        );
    }

    /**
     * {@code GET  /missions/:id} : get the "id" mission.
     *
     * @param id the id of the mission to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the mission, or with status {@code 404 (Not Found)}.
     */
    @Get("/missions/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Mission> getMission(@PathVariable Long id) {
        log.debug("REST request to get Mission : {}", id);

        return missionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * {@code DELETE  /missions/:id} : delete the "id" mission.
     *
     * @param id the id of the mission to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/missions/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteMission(@PathVariable Long id) {
        log.debug("REST request to delete Mission : {}", id);
        missionRepository.deleteById(id);
        return HttpResponse.noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
