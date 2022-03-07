package com.cheaengine.service;

import com.cheaengine.domain.Word;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Word}.
 */
public interface WordService {
    /**
     * Save a word.
     *
     * @param word the entity to save.
     * @return the persisted entity.
     */
    Word save(Word word);

    /**
     * Partially updates a word.
     *
     * @param word the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Word> partialUpdate(Word word);

    /**
     * Get all the words.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Word> findAll(Pageable pageable);

    /**
     * Get the "id" word.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Word> findOne(Long id);

    /**
     * Delete the "id" word.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
