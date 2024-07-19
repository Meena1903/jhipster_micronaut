import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MissionComponent } from './list/mission.component';
import { MissionDetailComponent } from './detail/mission-detail.component';
import { MissionUpdateComponent } from './update/mission-update.component';
import MissionResolve from './route/mission-routing-resolve.service';

const missionRoute: Routes = [
  {
    path: '',
    component: MissionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MissionDetailComponent,
    resolve: {
      mission: MissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MissionUpdateComponent,
    resolve: {
      mission: MissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MissionUpdateComponent,
    resolve: {
      mission: MissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default missionRoute;
