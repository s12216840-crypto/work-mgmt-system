import { Routes } from '@angular/router';

import { Home } from './features/home/home';
import { Users } from './features/users/users';
import { OrganizationsComponent } from './features/organizations/organizations.component';
import { TeamsComponent } from './features/teams/teams.component';
import { ProjectsComponent } from './features/projects/projects.component';
import { TasksComponent } from './features/tasks/tasks.component';
import { Login } from './features/login/login';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },

  {
    path: 'login',
    component: Login,
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

  {
    path: 'projects',
    component: ProjectsComponent,
  },

  {
    path: 'tasks',
    component: TasksComponent,
  },
];
