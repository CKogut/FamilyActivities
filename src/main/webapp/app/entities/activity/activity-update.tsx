import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActivity } from 'app/shared/model/activity.model';
import { getEntity, updateEntity, createEntity, reset } from './activity.reducer';

export const ActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activityEntity = useAppSelector(state => state.activity.entity);
  const loading = useAppSelector(state => state.activity.loading);
  const updating = useAppSelector(state => state.activity.updating);
  const updateSuccess = useAppSelector(state => state.activity.updateSuccess);

  const handleClose = () => {
    navigate('/activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.minParticipants !== undefined && typeof values.minParticipants !== 'number') {
      values.minParticipants = Number(values.minParticipants);
    }
    if (values.maxParticipants !== undefined && typeof values.maxParticipants !== 'number') {
      values.maxParticipants = Number(values.maxParticipants);
    }

    const entity = {
      ...activityEntity,
      ...values,
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
          ...activityEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="familyActivitiesApp.activity.home.createOrEditLabel" data-cy="ActivityCreateUpdateHeading">
            <Translate contentKey="familyActivitiesApp.activity.home.createOrEditLabel">Create or edit a Activity</Translate>
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
                  id="activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('familyActivitiesApp.activity.description')}
                id="activity-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('familyActivitiesApp.activity.cost')}
                id="activity-cost"
                name="cost"
                data-cy="cost"
                type="text"
              />
              <ValidatedField
                label={translate('familyActivitiesApp.activity.minParticipants')}
                id="activity-minParticipants"
                name="minParticipants"
                data-cy="minParticipants"
                type="text"
              />
              <ValidatedField
                label={translate('familyActivitiesApp.activity.maxParticipants')}
                id="activity-maxParticipants"
                name="maxParticipants"
                data-cy="maxParticipants"
                type="text"
              />
              <ValidatedField
                label={translate('familyActivitiesApp.activity.time')}
                id="activity-time"
                name="time"
                data-cy="time"
                type="text"
              />
              <ValidatedField
                label={translate('familyActivitiesApp.activity.location')}
                id="activity-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activity" replace color="info">
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

export default ActivityUpdate;
