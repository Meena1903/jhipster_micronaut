package org.jhipster.space.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.util.List;
import org.jhipster.space.domain.Mission;
import org.jhipster.space.repository.MissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Integration tests for the {@Link MissionResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MissionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private MissionRepository missionRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Mission mission;

    @BeforeEach
    public void initTest() {
        mission = createEntity(transactionManager, em);
    }

    @AfterEach
    public void cleanUpTest() {
        deleteAll(transactionManager, em);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mission createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Mission mission = new Mission().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return mission;
    }

    /**
     * Delete all mission entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Mission.class);
    }

    @Test
    public void createMission() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // Create the Mission
        HttpResponse<Mission> response = client.exchange(HttpRequest.POST("/api/missions", mission), Mission.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeCreate + 1);
        Mission testMission = missionList.get(missionList.size() - 1);

        assertThat(testMission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void createMissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // Create the Mission with an existing ID
        mission.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<Mission> response = client
            .exchange(HttpRequest.POST("/api/missions", mission), Mission.class)
            .onErrorReturn(t -> (HttpResponse<Mission>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = missionRepository.findAll().size();
        // set the field null
        mission.setName(null);

        // Create the Mission, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Mission> response = client
            .exchange(HttpRequest.POST("/api/missions", mission), Mission.class)
            .onErrorReturn(t -> (HttpResponse<Mission>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMissions() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get the missionList w/ all the missions
        List<Mission> missions = client
            .retrieve(HttpRequest.GET("/api/missions?eagerload=true"), Argument.listOf(Mission.class))
            .blockingFirst();
        Mission testMission = missions.get(0);

        assertThat(testMission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get the mission
        Mission testMission = client.retrieve(HttpRequest.GET("/api/missions/" + mission.getId()), Mission.class).blockingFirst();

        assertThat(testMission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void getNonExistingMission() throws Exception {
        // Get the mission
        @SuppressWarnings("unchecked")
        HttpResponse<Mission> response = client
            .exchange(HttpRequest.GET("/api/missions/" + Long.MAX_VALUE), Mission.class)
            .onErrorReturn(t -> (HttpResponse<Mission>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission
        Mission updatedMission = missionRepository.findById(mission.getId()).get();

        updatedMission.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        @SuppressWarnings("unchecked")
        HttpResponse<Mission> response = client
            .exchange(HttpRequest.PUT("/api/missions/" + mission.getId(), updatedMission), Mission.class)
            .onErrorReturn(t -> (HttpResponse<Mission>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missionList.get(missionList.size() - 1);

        assertThat(testMission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    public void updateNonExistingMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Create the Mission

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<Mission> response = client
            .exchange(HttpRequest.PUT("/api/missions/" + mission.getId(), mission), Mission.class)
            .onErrorReturn(t -> (HttpResponse<Mission>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteMission() throws Exception {
        // Initialize the database with one entity
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeDelete = missionRepository.findAll().size();

        // Delete the mission
        @SuppressWarnings("unchecked")
        HttpResponse<Mission> response = client
            .exchange(HttpRequest.DELETE("/api/missions/" + mission.getId()), Mission.class)
            .onErrorReturn(t -> (HttpResponse<Mission>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mission.class);
        Mission mission1 = new Mission();
        mission1.setId(1L);
        Mission mission2 = new Mission();
        mission2.setId(mission1.getId());
        assertThat(mission1).isEqualTo(mission2);
        mission2.setId(2L);
        assertThat(mission1).isNotEqualTo(mission2);
        mission1.setId(null);
        assertThat(mission1).isNotEqualTo(mission2);
    }
}
