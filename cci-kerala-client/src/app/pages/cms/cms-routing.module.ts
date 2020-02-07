import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NewsUpdateComponent } from './news-update/news-update.component';
import { RoleGuard } from '../guards/role-guard.service';

const routes: Routes = [
  {path: 'news-update', pathMatch: 'full', component: NewsUpdateComponent,     
   canActivate: [RoleGuard],
  data: { title: 'News Update', expectedRoles: ['news_update']},}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CmsRoutingModule { }
