import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Relationship from './relationship';
import RelationshipDetail from './relationship-detail';
import RelationshipUpdate from './relationship-update';
import RelationshipDeleteDialog from './relationship-delete-dialog';

const RelationshipRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Relationship />} />
    <Route path="new" element={<RelationshipUpdate />} />
    <Route path=":id">
      <Route index element={<RelationshipDetail />} />
      <Route path="edit" element={<RelationshipUpdate />} />
      <Route path="delete" element={<RelationshipDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RelationshipRoutes;
