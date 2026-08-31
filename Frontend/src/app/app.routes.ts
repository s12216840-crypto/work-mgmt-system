import { Routes } from '@angular/router';

import { Home } from './features/home/home';
import { Users } from './features/users/users';
import { OrganizationsComponent } from './features/organizations/organizations.component';
import { TeamsComponent } from './features/teams/teams.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: Home,
  },
  {
    path: 'users',
    component: Users,
  },
  {
    path: 'organizations',
    component: OrganizationsComponent,
  },
  {
    path: 'teams',
    component: TeamsComponent,
  },
];
