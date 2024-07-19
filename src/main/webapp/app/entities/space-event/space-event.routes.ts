import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SpaceEventComponent } from './list/space-event.component';
import { SpaceEventDetailComponent } from './detail/space-event-detail.component';
import { SpaceEventUpdateComponent } from './update/space-event-update.component';
import SpaceEventResolve from './route/space-event-routing-resolve.service';

const spaceEventRoute: Routes = [
  {
    path: '',
    component: SpaceEventComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpaceEventDetailComponent,
    resolve: {
      spaceEvent: SpaceEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpaceEventUpdateComponent,
    resolve: {
      spaceEvent: SpaceEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpaceEventUpdateComponent,
    resolve: {
      spaceEvent: SpaceEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default spaceEventRoute;
