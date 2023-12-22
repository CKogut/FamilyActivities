package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Relationship;
import com.mycompany.myapp.repository.RelationshipRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RelationshipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RelationshipResourceIT {

    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    private static final Boolean DEFAULT_SHARED = false;
    private static final Boolean UPDATED_SHARED = true;

    private static final String ENTITY_API_URL = "/api/relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Mock
    private RelationshipRepository relationshipRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelationshipMockMvc;

    private Relationship relationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relationship createEntity(EntityManager em) {
        Relationship relationship = new Relationship().liked(DEFAULT_LIKED).shared(DEFAULT_SHARED);
        return relationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relationship createUpdatedEntity(EntityManager em) {
        Relationship relationship = new Relationship().liked(UPDATED_LIKED).shared(UPDATED_SHARED);
        return relationship;
    }

    @BeforeEach
    public void initTest() {
        relationship = createEntity(em);
    }

    @Test
    @Transactional
    void createRelationship() throws Exception {
        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();
        // Create the Relationship
        restRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isCreated());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate + 1);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getLiked()).isEqualTo(DEFAULT_LIKED);
        assertThat(testRelationship.getShared()).isEqualTo(DEFAULT_SHARED);
    }

    @Test
    @Transactional
    void createRelationshipWithExistingId() throws Exception {
        // Create the Relationship with an existing ID
        relationship.setId(1L);

        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRelationships() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())))
            .andExpect(jsonPath("$.[*].shared").value(hasItem(DEFAULT_SHARED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRelationshipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(relationshipRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRelationshipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(relationshipRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRelationshipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(relationshipRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRelationshipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(relationshipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get the relationship
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, relationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relationship.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()))
            .andExpect(jsonPath("$.shared").value(DEFAULT_SHARED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRelationship() throws Exception {
        // Get the relationship
        restRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship
        Relationship updatedRelationship = relationshipRepository.findById(relationship.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRelationship are not directly saved in db
        em.detach(updatedRelationship);
        updatedRelationship.liked(UPDATED_LIKED).shared(UPDATED_SHARED);

        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getLiked()).isEqualTo(UPDATED_LIKED);
        assertThat(testRelationship.getShared()).isEqualTo(UPDATED_SHARED);
    }

    @Test
    @Transactional
    void putNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelationshipWithPatch() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship using partial update
        Relationship partialUpdatedRelationship = new Relationship();
        partialUpdatedRelationship.setId(relationship.getId());

        partialUpdatedRelationship.liked(UPDATED_LIKED);

        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getLiked()).isEqualTo(UPDATED_LIKED);
        assertThat(testRelationship.getShared()).isEqualTo(DEFAULT_SHARED);
    }

    @Test
    @Transactional
    void fullUpdateRelationshipWithPatch() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship using partial update
        Relationship partialUpdatedRelationship = new Relationship();
        partialUpdatedRelationship.setId(relationship.getId());

        partialUpdatedRelationship.liked(UPDATED_LIKED).shared(UPDATED_SHARED);

        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getLiked()).isEqualTo(UPDATED_LIKED);
        assertThat(testRelationship.getShared()).isEqualTo(UPDATED_SHARED);
    }

    @Test
    @Transactional
    void patchNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeDelete = relationshipRepository.findAll().size();

        // Delete the relationship
        restRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, relationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
