package com.cheaengine.web.rest;

import com.cheaengine.domain.Word;
import com.cheaengine.repository.WordRepository;
import com.cheaengine.service.WordService;
import com.cheaengine.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cheaengine.domain.Word}.
 */
@RestController
@RequestMapping("/api")
public class WordResource {

    private final Logger log = LoggerFactory.getLogger(WordResource.class);

    private static final String ENTITY_NAME = "word";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WordService wordService;

    private final WordRepository wordRepository;

    public WordResource(WordService wordService, WordRepository wordRepository) {
        this.wordService = wordService;
        this.wordRepository = wordRepository;
    }

    /**
     * {@code POST  /words} : Create a new word.
     *
     * @param word the word to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new word, or with status {@code 400 (Bad Request)} if the word has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/words")
    public ResponseEntity<Word> createWord(@RequestBody Word word) throws URISyntaxException {
        log.debug("REST request to save Word : {}", word);
        if (word.getId() != null) {
            throw new BadRequestAlertException("A new word cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Word result = wordService.save(word);
        return ResponseEntity
            .created(new URI("/api/words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /words/:id} : Updates an existing word.
     *
     * @param id the id of the word to save.
     * @param word the word to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated word,
     * or with status {@code 400 (Bad Request)} if the word is not valid,
     * or with status {@code 500 (Internal Server Error)} if the word couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/words/{id}")
    public ResponseEntity<Word> updateWord(@PathVariable(value = "id", required = false) final Long id, @RequestBody Word word)
        throws URISyntaxException {
        log.debug("REST request to update Word : {}, {}", id, word);
        if (word.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, word.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Word result = wordService.save(word);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, word.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /words/:id} : Partial updates given fields of an existing word, field will ignore if it is null
     *
     * @param id the id of the word to save.
     * @param word the word to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated word,
     * or with status {@code 400 (Bad Request)} if the word is not valid,
     * or with status {@code 404 (Not Found)} if the word is not found,
     * or with status {@code 500 (Internal Server Error)} if the word couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/words/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Word> partialUpdateWord(@PathVariable(value = "id", required = false) final Long id, @RequestBody Word word)
        throws URISyntaxException {
        log.debug("REST request to partial update Word partially : {}, {}", id, word);
        if (word.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, word.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Word> result = wordService.partialUpdate(word);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, word.getId().toString())
        );
    }

    /**
     * {@code GET  /words} : get all the words.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of words in body.
     */
    @GetMapping("/words")
    public ResponseEntity<List<Word>> getAllWords(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Words");
        Page<Word> page = wordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /words/:id} : get the "id" word.
     *
     * @param id the id of the word to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the word, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/words/{id}")
    public ResponseEntity<Word> getWord(@PathVariable Long id) {
        log.debug("REST request to get Word : {}", id);
        Optional<Word> word = wordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(word);
    }

    /**
     * {@code DELETE  /words/:id} : delete the "id" word.
     *
     * @param id the id of the word to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/words/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        log.debug("REST request to delete Word : {}", id);
        wordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
