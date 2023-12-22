import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IActivity } from 'app/shared/model/activity.model';
import { getEntities as getActivities } from 'app/entities/activity/activity.reducer';
import { IRelationship } from 'app/shared/model/relationship.model';
import { getEntity, updateEntity, createEntity, reset } from './relationship.reducer';

export const RelationshipUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const activities = useAppSelector(state => state.activity.entities);
  const relationshipEntity = useAppSelector(state => state.relationship.entity);
  const loading = useAppSelector(state => state.relationship.loading);
  const updating = useAppSelector(state => state.relationship.updating);
  const updateSuccess = useAppSelector(state => state.relationship.updateSuccess);

  const handleClose = () => {
    navigate('/relationship');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getActivities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...relationshipEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      description: activities.find(it => it.id.toString() === values.description.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...relationshipEntity,
          user: relationshipEntity?.user?.id,
          description: relationshipEntity?.description?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="familyActivitiesApp.relationship.home.createOrEditLabel" data-cy="RelationshipCreateUpdateHeading">
            <Translate contentKey="familyActivitiesApp.relationship.home.createOrEditLabel">Create or edit a Relationship</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="relationship-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('familyActivitiesApp.relationship.liked')}
                id="relationship-liked"
                name="liked"
                data-cy="liked"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('familyActivitiesApp.relationship.shared')}
                id="relationship-shared"
                name="shared"
                data-cy="shared"
                check
                type="checkbox"
              />
              <ValidatedField
                id="relationship-user"
                name="user"
                data-cy="user"
                label={translate('familyActivitiesApp.relationship.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="relationship-description"
                name="description"
                data-cy="description"
                label={translate('familyActivitiesApp.relationship.description')}
                type="select"
              >
                <option value="" key="0" />
                {activities
                  ? activities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/relationship" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RelationshipUpdate;
