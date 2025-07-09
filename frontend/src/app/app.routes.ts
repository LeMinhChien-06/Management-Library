import { Routes } from '@angular/router';
import { LoginComponent } from './features/authors/components/login/login.component';
import { CmsLayoutComponent } from './layouts/cms-layout/cms-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import {UserComponent} from './features/users/user/user.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'cms',
    component: CmsLayoutComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'user', component: UserComponent },
    ]
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
