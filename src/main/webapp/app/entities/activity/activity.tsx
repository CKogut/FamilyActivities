import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { DurationFormat } from 'app/shared/DurationFormat';

import { getEntities } from './activity.reducer';

export const Activity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const activityList = useAppSelector(state => state.activity.entities);
  const loading = useAppSelector(state => state.activity.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="activity-heading" data-cy="ActivityHeading">
        <Translate contentKey="familyActivitiesApp.activity.home.title">Activities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="familyActivitiesApp.activity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/activity/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="familyActivitiesApp.activity.home.createLabel">Create new Activity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {activityList && activityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="familyActivitiesApp.activity.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="familyActivitiesApp.activity.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('cost')}>
                  <Translate contentKey="familyActivitiesApp.activity.cost">Cost</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cost')} />
                </th>
                <th className="hand" onClick={sort('minParticipants')}>
                  <Translate contentKey="familyActivitiesApp.activity.minParticipants">Min Participants</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('minParticipants')} />
                </th>
                <th className="hand" onClick={sort('maxParticipants')}>
                  <Translate contentKey="familyActivitiesApp.activity.maxParticipants">Max Participants</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maxParticipants')} />
                </th>
                <th className="hand" onClick={sort('time')}>
                  <Translate contentKey="familyActivitiesApp.activity.time">Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('time')} />
                </th>
                <th className="hand" onClick={sort('location')}>
                  <Translate contentKey="familyActivitiesApp.activity.location">Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('location')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {activityList.map((activity, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                      {activity.id}
                    </Button>
                  </td>
                  <td>{activity.description}</td>
                  <td>{activity.cost}</td>
                  <td>{activity.minParticipants}</td>
                  <td>{activity.maxParticipants}</td>
                  <td>{activity.time ? <DurationFormat value={activity.time} /> : null}</td>
                  <td>{activity.location}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/activity/${activity.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/activity/${activity.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/activity/${activity.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="familyActivitiesApp.activity.home.notFound">No Activities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Activity;
