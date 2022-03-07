export interface IWord {
  id?: number;
  guessedWord?: string | null;
  firstLetter?: string | null;
  lenght?: number | null;
  forbidenLetters?: string | null;
  discoveredLetters?: string | null;
}

export class Word implements IWord {
  constructor(
    public id?: number,
    public guessedWord?: string | null,
    public firstLetter?: string | null,
    public lenght?: number | null,
    public forbidenLetters?: string | null,
    public discoveredLetters?: string | null
  ) {}
}

export function getWordIdentifier(word: IWord): number | undefined {
  return word.id;
}
