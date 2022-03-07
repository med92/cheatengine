import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWord, getWordIdentifier } from '../word.model';

export type EntityResponseType = HttpResponse<IWord>;
export type EntityArrayResponseType = HttpResponse<IWord[]>;

@Injectable({ providedIn: 'root' })
export class WordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/words');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(word: IWord): Observable<EntityResponseType> {
    return this.http.post<IWord>(this.resourceUrl, word, { observe: 'response' });
  }

  update(word: IWord): Observable<EntityResponseType> {
    return this.http.put<IWord>(`${this.resourceUrl}/${getWordIdentifier(word) as number}`, word, { observe: 'response' });
  }

  partialUpdate(word: IWord): Observable<EntityResponseType> {
    return this.http.patch<IWord>(`${this.resourceUrl}/${getWordIdentifier(word) as number}`, word, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWordToCollectionIfMissing(wordCollection: IWord[], ...wordsToCheck: (IWord | null | undefined)[]): IWord[] {
    const words: IWord[] = wordsToCheck.filter(isPresent);
    if (words.length > 0) {
      const wordCollectionIdentifiers = wordCollection.map(wordItem => getWordIdentifier(wordItem)!);
      const wordsToAdd = words.filter(wordItem => {
        const wordIdentifier = getWordIdentifier(wordItem);
        if (wordIdentifier == null || wordCollectionIdentifiers.includes(wordIdentifier)) {
          return false;
        }
        wordCollectionIdentifiers.push(wordIdentifier);
        return true;
      });
      return [...wordsToAdd, ...wordCollection];
    }
    return wordCollection;
  }
}
