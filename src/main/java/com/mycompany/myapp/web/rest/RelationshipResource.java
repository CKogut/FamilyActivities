package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Relationship;
import com.mycompany.myapp.repository.RelationshipRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Relationship}.
 */
@RestController
@RequestMapping("/api/relationships")
@Transactional
public class RelationshipResource {

    private final Logger log = LoggerFactory.getLogger(RelationshipResource.class);

    private static final String ENTITY_NAME = "relationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelationshipRepository relationshipRepository;

    public RelationshipResource(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    /**
     * {@code POST  /relationships} : Create a new relationship.
     *
     * @param relationship the relationship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relationship, or with status {@code 400 (Bad Request)} if the relationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Relationship> createRelationship(@RequestBody Relationship relationship) throws URISyntaxException {
        log.debug("REST request to save Relationship : {}", relationship);
        if (relationship.getId() != null) {
            throw new BadRequestAlertException("A new relationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Relationship result = relationshipRepository.save(relationship);
        return ResponseEntity
            .created(new URI("/api/relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /relationships/:id} : Updates an existing relationship.
     *
     * @param id the id of the relationship to save.
     * @param relationship the relationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relationship,
     * or with status {@code 400 (Bad Request)} if the relationship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Relationship> updateRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Relationship relationship
    ) throws URISyntaxException {
        log.debug("REST request to update Relationship : {}, {}", id, relationship);
        if (relationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Relationship result = relationshipRepository.save(relationship);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relationship.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /relationships/:id} : Partial updates given fields of an existing relationship, field will ignore if it is null
     *
     * @param id the id of the relationship to save.
     * @param relationship the relationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relationship,
     * or with status {@code 400 (Bad Request)} if the relationship is not valid,
     * or with status {@code 404 (Not Found)} if the relationship is not found,
     * or with status {@code 500 (Internal Server Error)} if the relationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Relationship> partialUpdateRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Relationship relationship
    ) throws URISyntaxException {
        log.debug("REST request to partial update Relationship partially : {}, {}", id, relationship);
        if (relationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Relationship> result = relationshipRepository
            .findById(relationship.getId())
            .map(existingRelationship -> {
                if (relationship.getLiked() != null) {
                    existingRelationship.setLiked(relationship.getLiked());
                }
                if (relationship.getShared() != null) {
                    existingRelationship.setShared(relationship.getShared());
                }

                return existingRelationship;
            })
            .map(relationshipRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relationship.getId().toString())
        );
    }

    /**
     * {@code GET  /relationships} : get all the relationships.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relationships in body.
     */
    @GetMapping("")
    public List<Relationship> getAllRelationships(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Relationships");
        if (eagerload) {
            return relationshipRepository.findAllWithEagerRelationships();
        } else {
            return relationshipRepository.findAll();
        }
    }

    /**
     * {@code GET  /relationships/:id} : get the "id" relationship.
     *
     * @param id the id of the relationship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relationship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Relationship> getRelationship(@PathVariable("id") Long id) {
        log.debug("REST request to get Relationship : {}", id);
        Optional<Relationship> relationship = relationshipRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(relationship);
    }

    /**
     * {@code DELETE  /relationships/:id} : delete the "id" relationship.
     *
     * @param id the id of the relationship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable("id") Long id) {
        log.debug("REST request to delete Relationship : {}", id);
        relationshipRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
