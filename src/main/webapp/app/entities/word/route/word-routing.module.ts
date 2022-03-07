import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WordComponent } from '../list/word.component';
import { WordDetailComponent } from '../detail/word-detail.component';
import { WordUpdateComponent } from '../update/word-update.component';
import { WordRoutingResolveService } from './word-routing-resolve.service';

const wordRoute: Routes = [
  {
    path: '',
    component: WordComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WordDetailComponent,
    resolve: {
      word: WordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WordUpdateComponent,
    resolve: {
      word: WordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WordUpdateComponent,
    resolve: {
      word: WordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wordRoute)],
  exports: [RouterModule],
})
export class WordRoutingModule {}
