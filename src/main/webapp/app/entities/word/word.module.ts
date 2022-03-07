import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WordComponent } from './list/word.component';
import { WordDetailComponent } from './detail/word-detail.component';
import { WordUpdateComponent } from './update/word-update.component';
import { WordDeleteDialogComponent } from './delete/word-delete-dialog.component';
import { WordRoutingModule } from './route/word-routing.module';

@NgModule({
  imports: [SharedModule, WordRoutingModule],
  declarations: [WordComponent, WordDetailComponent, WordUpdateComponent, WordDeleteDialogComponent],
  entryComponents: [WordDeleteDialogComponent],
})
export class WordModule {}
