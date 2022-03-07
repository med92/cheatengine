import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWord } from '../word.model';
import { WordService } from '../service/word.service';

@Component({
  templateUrl: './word-delete-dialog.component.html',
})
export class WordDeleteDialogComponent {
  word?: IWord;

  constructor(protected wordService: WordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
