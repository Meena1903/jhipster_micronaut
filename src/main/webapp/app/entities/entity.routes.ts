import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'space-event',
    data: { pageTitle: 'spaceApp.spaceEvent.home.title' },
    loadChildren: () => import('./space-event/space-event.routes'),
  },
  {
    path: 'mission',
    data: { pageTitle: 'spaceApp.mission.home.title' },
    loadChildren: () => import('./mission/mission.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
