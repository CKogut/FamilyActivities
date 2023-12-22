package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Relationship;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Relationship entity.
 */
@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    @Query("select relationship from Relationship relationship where relationship.user.login = ?#{authentication.name}")
    List<Relationship> findByUserIsCurrentUser();

    default Optional<Relationship> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Relationship> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Relationship> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select relationship from Relationship relationship left join fetch relationship.user",
        countQuery = "select count(relationship) from Relationship relationship"
    )
    Page<Relationship> findAllWithToOneRelationships(Pageable pageable);

    @Query("select relationship from Relationship relationship left join fetch relationship.user")
    List<Relationship> findAllWithToOneRelationships();

    @Query("select relationship from Relationship relationship left join fetch relationship.user where relationship.id =:id")
    Optional<Relationship> findOneWithToOneRelationships(@Param("id") Long id);
}
