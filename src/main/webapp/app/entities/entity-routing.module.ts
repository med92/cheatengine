import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'word',
        data: { pageTitle: 'cheaEngineApp.word.home.title' },
        loadChildren: () => import('./word/word.module').then(m => m.WordModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
