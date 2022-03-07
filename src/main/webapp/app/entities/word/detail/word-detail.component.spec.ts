import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WordDetailComponent } from './word-detail.component';

describe('Word Management Detail Component', () => {
  let comp: WordDetailComponent;
  let fixture: ComponentFixture<WordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ word: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load word on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.word).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
