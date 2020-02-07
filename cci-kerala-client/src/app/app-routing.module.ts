import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { LoggedinGuard } from './pages/guards/loggedin-guard.service';
import { RoleGuard } from './pages/guards/role-guard.service';
import { RecourcesComponent } from './pages/recources/recources.component';

const routes: Routes = [

    {
        path: '',
        component: LoginComponent,
        canActivate: [LoggedinGuard],
        pathMatch: 'full',
        data: { title: 'CCMIS-Login' }
    },
    {
        path: 'ccihome', loadChildren: './pages/data-entry/data-entry.module#DataEntryModule',
        canActivate: [RoleGuard],
        data: { title: 'CCMIS-Home', expectedRoles: ['dataentry'] },
    },
    {
        path: 'dashboard',
        loadChildren: './pages/dashboard/dashboard.module#DashboardModule',
        canActivate: [RoleGuard],
        data: { title: 'Dashboard', expectedRoles: ['dashboard']},
    },
    {
        path: 'submission',
        loadChildren: './pages/submission/submission.module#SubmissionModule',
        canActivate: [RoleGuard],
        data: { title: 'Submission Management', expectedRoles: ['submissionManagement']},
    },
    {
        path: 'reports',
        loadChildren: './pages/reports/reports.module#ReportsModule',
        canActivate: [RoleGuard],
        data: { title: 'Reports', expectedRoles: ['report']},
    },
    {
        path: 'user',
        loadChildren: './pages/user-management/user-management.module#UserManagementModule',
        canActivate: [RoleGuard],
        data: { title: 'User Management', expectedRoles: ['USER_MGMT_ALL_API']},
    },
    {
        path: 'recources',
        component: RecourcesComponent,
        pathMatch: 'full',
        canActivate: [RoleGuard],
        data: { title: 'CCMIS-Resources',expectedRoles: ['report','dashboard','USER_MGMT_ALL_API','submissionManagement','dataentry'] }
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
