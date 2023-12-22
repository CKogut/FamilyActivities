package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActivityTestSamples.*;
import static com.mycompany.myapp.domain.RelationshipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RelationshipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relationship.class);
        Relationship relationship1 = getRelationshipSample1();
        Relationship relationship2 = new Relationship();
        assertThat(relationship1).isNotEqualTo(relationship2);

        relationship2.setId(relationship1.getId());
        assertThat(relationship1).isEqualTo(relationship2);

        relationship2 = getRelationshipSample2();
        assertThat(relationship1).isNotEqualTo(relationship2);
    }

    @Test
    void descriptionTest() throws Exception {
        Relationship relationship = getRelationshipRandomSampleGenerator();
        Activity activityBack = getActivityRandomSampleGenerator();

        relationship.setDescription(activityBack);
        assertThat(relationship.getDescription()).isEqualTo(activityBack);

        relationship.description(null);
        assertThat(relationship.getDescription()).isNull();
    }
}
