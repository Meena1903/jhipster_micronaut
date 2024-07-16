package org.jhipster.space.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MissionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMissionAllPropertiesEquals(Mission expected, Mission actual) {
        assertMissionAutoGeneratedPropertiesEquals(expected, actual);
        assertMissionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMissionAllUpdatablePropertiesEquals(Mission expected, Mission actual) {
        assertMissionUpdatableFieldsEquals(expected, actual);
        assertMissionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMissionAutoGeneratedPropertiesEquals(Mission expected, Mission actual) {
        assertThat(expected)
            .as("Verify Mission auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMissionUpdatableFieldsEquals(Mission expected, Mission actual) {
        assertThat(expected)
            .as("Verify Mission relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMissionUpdatableRelationshipsEquals(Mission expected, Mission actual) {}
}
