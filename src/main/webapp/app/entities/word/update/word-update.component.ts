import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWord, Word } from '../word.model';
import { WordService } from '../service/word.service';

@Component({
  selector: 'jhi-word-update',
  templateUrl: './word-update.component.html',
})
export class WordUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    guessedWord: [],
    firstLetter: [],
    lenght: [],
    forbidenLetters: [],
    discoveredLetters: [],
  });

  constructor(protected wordService: WordService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ word }) => {
      this.updateForm(word);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const word = this.createFromForm();
    if (word.id !== undefined) {
      this.subscribeToSaveResponse(this.wordService.update(word));
    } else {
      this.subscribeToSaveResponse(this.wordService.create(word));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWord>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(word: IWord): void {
    this.editForm.patchValue({
      id: word.id,
      guessedWord: word.guessedWord,
      firstLetter: word.firstLetter,
      lenght: word.lenght,
      forbidenLetters: word.forbidenLetters,
      discoveredLetters: word.discoveredLetters,
    });
  }

  protected createFromForm(): IWord {
    return {
      ...new Word(),
      id: this.editForm.get(['id'])!.value,
      guessedWord: this.editForm.get(['guessedWord'])!.value,
      firstLetter: this.editForm.get(['firstLetter'])!.value,
      lenght: this.editForm.get(['lenght'])!.value,
      forbidenLetters: this.editForm.get(['forbidenLetters'])!.value,
      discoveredLetters: this.editForm.get(['discoveredLetters'])!.value,
    };
  }
}
