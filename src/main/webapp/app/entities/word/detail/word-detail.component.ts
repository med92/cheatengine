import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWord } from '../word.model';

@Component({
  selector: 'jhi-word-detail',
  templateUrl: './word-detail.component.html',
})
export class WordDetailComponent implements OnInit {
  word: IWord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ word }) => {
      this.word = word;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
