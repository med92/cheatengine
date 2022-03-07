import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWord, Word } from '../word.model';

import { WordService } from './word.service';

describe('Word Service', () => {
  let service: WordService;
  let httpMock: HttpTestingController;
  let elemDefault: IWord;
  let expectedResult: IWord | IWord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      guessedWord: 'AAAAAAA',
      firstLetter: 'AAAAAAA',
      lenght: 0,
      forbidenLetters: 'AAAAAAA',
      discoveredLetters: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Word', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Word()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Word', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          guessedWord: 'BBBBBB',
          firstLetter: 'BBBBBB',
          lenght: 1,
          forbidenLetters: 'BBBBBB',
          discoveredLetters: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Word', () => {
      const patchObject = Object.assign(
        {
          guessedWord: 'BBBBBB',
          firstLetter: 'BBBBBB',
          lenght: 1,
          forbidenLetters: 'BBBBBB',
        },
        new Word()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Word', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          guessedWord: 'BBBBBB',
          firstLetter: 'BBBBBB',
          lenght: 1,
          forbidenLetters: 'BBBBBB',
          discoveredLetters: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Word', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWordToCollectionIfMissing', () => {
      it('should add a Word to an empty array', () => {
        const word: IWord = { id: 123 };
        expectedResult = service.addWordToCollectionIfMissing([], word);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(word);
      });

      it('should not add a Word to an array that contains it', () => {
        const word: IWord = { id: 123 };
        const wordCollection: IWord[] = [
          {
            ...word,
          },
          { id: 456 },
        ];
        expectedResult = service.addWordToCollectionIfMissing(wordCollection, word);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Word to an array that doesn't contain it", () => {
        const word: IWord = { id: 123 };
        const wordCollection: IWord[] = [{ id: 456 }];
        expectedResult = service.addWordToCollectionIfMissing(wordCollection, word);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(word);
      });

      it('should add only unique Word to an array', () => {
        const wordArray: IWord[] = [{ id: 123 }, { id: 456 }, { id: 73870 }];
        const wordCollection: IWord[] = [{ id: 123 }];
        expectedResult = service.addWordToCollectionIfMissing(wordCollection, ...wordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const word: IWord = { id: 123 };
        const word2: IWord = { id: 456 };
        expectedResult = service.addWordToCollectionIfMissing([], word, word2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(word);
        expect(expectedResult).toContain(word2);
      });

      it('should accept null and undefined values', () => {
        const word: IWord = { id: 123 };
        expectedResult = service.addWordToCollectionIfMissing([], null, word, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(word);
      });

      it('should return initial array if no Word is added', () => {
        const wordCollection: IWord[] = [{ id: 123 }];
        expectedResult = service.addWordToCollectionIfMissing(wordCollection, undefined, null);
        expect(expectedResult).toEqual(wordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
