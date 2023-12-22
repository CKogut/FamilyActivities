import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './relationship.reducer';

export const RelationshipDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const relationshipEntity = useAppSelector(state => state.relationship.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="relationshipDetailsHeading">
          <Translate contentKey="familyActivitiesApp.relationship.detail.title">Relationship</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{relationshipEntity.id}</dd>
          <dt>
            <span id="liked">
              <Translate contentKey="familyActivitiesApp.relationship.liked">Liked</Translate>
            </span>
          </dt>
          <dd>{relationshipEntity.liked ? 'true' : 'false'}</dd>
          <dt>
            <span id="shared">
              <Translate contentKey="familyActivitiesApp.relationship.shared">Shared</Translate>
            </span>
          </dt>
          <dd>{relationshipEntity.shared ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="familyActivitiesApp.relationship.user">User</Translate>
          </dt>
          <dd>{relationshipEntity.user ? relationshipEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="familyActivitiesApp.relationship.description">Description</Translate>
          </dt>
          <dd>{relationshipEntity.description ? relationshipEntity.description.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/relationship" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/relationship/${relationshipEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RelationshipDetail;
