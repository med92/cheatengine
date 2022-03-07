package com.cheaengine.service.impl;

import com.cheaengine.domain.Word;
import com.cheaengine.repository.WordRepository;
import com.cheaengine.service.WordService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Word}.
 */
@Service
@Transactional
public class WordServiceImpl implements WordService {

    private final Logger log = LoggerFactory.getLogger(WordServiceImpl.class);

    private final WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Word save(Word word) {
        log.debug("Request to save Word : {}", word);
        return wordRepository.save(word);
    }

    @Override
    public Optional<Word> partialUpdate(Word word) {
        log.debug("Request to partially update Word : {}", word);

        return wordRepository
            .findById(word.getId())
            .map(existingWord -> {
                if (word.getGuessedWord() != null) {
                    existingWord.setGuessedWord(word.getGuessedWord());
                }
                if (word.getFirstLetter() != null) {
                    existingWord.setFirstLetter(word.getFirstLetter());
                }
                if (word.getLenght() != null) {
                    existingWord.setLenght(word.getLenght());
                }
                if (word.getForbidenLetters() != null) {
                    existingWord.setForbidenLetters(word.getForbidenLetters());
                }
                if (word.getDiscoveredLetters() != null) {
                    existingWord.setDiscoveredLetters(word.getDiscoveredLetters());
                }

                return existingWord;
            })
            .map(wordRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Word> findAll(Pageable pageable) {
        log.debug("Request to get all Words");
        return wordRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Word> findOne(Long id) {
        log.debug("Request to get Word : {}", id);
        return wordRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Word : {}", id);
        wordRepository.deleteById(id);
    }
}
