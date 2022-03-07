package com.cheaengine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cheaengine.IntegrationTest;
import com.cheaengine.domain.Word;
import com.cheaengine.repository.WordRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WordResourceIT {

    private static final String DEFAULT_GUESSED_WORD = "AAAAAAAAAA";
    private static final String UPDATED_GUESSED_WORD = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_LETTER = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_LETTER = "BBBBBBBBBB";

    private static final Integer DEFAULT_LENGHT = 1;
    private static final Integer UPDATED_LENGHT = 2;

    private static final String DEFAULT_FORBIDEN_LETTERS = "AAAAAAAAAA";
    private static final String UPDATED_FORBIDEN_LETTERS = "BBBBBBBBBB";

    private static final String DEFAULT_DISCOVERED_LETTERS = "AAAAAAAAAA";
    private static final String UPDATED_DISCOVERED_LETTERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/words";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWordMockMvc;

    private Word word;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Word createEntity(EntityManager em) {
        Word word = new Word()
            .guessedWord(DEFAULT_GUESSED_WORD)
            .firstLetter(DEFAULT_FIRST_LETTER)
            .lenght(DEFAULT_LENGHT)
            .forbidenLetters(DEFAULT_FORBIDEN_LETTERS)
            .discoveredLetters(DEFAULT_DISCOVERED_LETTERS);
        return word;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Word createUpdatedEntity(EntityManager em) {
        Word word = new Word()
            .guessedWord(UPDATED_GUESSED_WORD)
            .firstLetter(UPDATED_FIRST_LETTER)
            .lenght(UPDATED_LENGHT)
            .forbidenLetters(UPDATED_FORBIDEN_LETTERS)
            .discoveredLetters(UPDATED_DISCOVERED_LETTERS);
        return word;
    }

    @BeforeEach
    public void initTest() {
        word = createEntity(em);
    }

    @Test
    @Transactional
    void createWord() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();
        // Create the Word
        restWordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isCreated());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate + 1);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getGuessedWord()).isEqualTo(DEFAULT_GUESSED_WORD);
        assertThat(testWord.getFirstLetter()).isEqualTo(DEFAULT_FIRST_LETTER);
        assertThat(testWord.getLenght()).isEqualTo(DEFAULT_LENGHT);
        assertThat(testWord.getForbidenLetters()).isEqualTo(DEFAULT_FORBIDEN_LETTERS);
        assertThat(testWord.getDiscoveredLetters()).isEqualTo(DEFAULT_DISCOVERED_LETTERS);
    }

    @Test
    @Transactional
    void createWordWithExistingId() throws Exception {
        // Create the Word with an existing ID
        word.setId(1L);

        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWords() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList
        restWordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].guessedWord").value(hasItem(DEFAULT_GUESSED_WORD)))
            .andExpect(jsonPath("$.[*].firstLetter").value(hasItem(DEFAULT_FIRST_LETTER)))
            .andExpect(jsonPath("$.[*].lenght").value(hasItem(DEFAULT_LENGHT)))
            .andExpect(jsonPath("$.[*].forbidenLetters").value(hasItem(DEFAULT_FORBIDEN_LETTERS)))
            .andExpect(jsonPath("$.[*].discoveredLetters").value(hasItem(DEFAULT_DISCOVERED_LETTERS)));
    }

    @Test
    @Transactional
    void getWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get the word
        restWordMockMvc
            .perform(get(ENTITY_API_URL_ID, word.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(word.getId().intValue()))
            .andExpect(jsonPath("$.guessedWord").value(DEFAULT_GUESSED_WORD))
            .andExpect(jsonPath("$.firstLetter").value(DEFAULT_FIRST_LETTER))
            .andExpect(jsonPath("$.lenght").value(DEFAULT_LENGHT))
            .andExpect(jsonPath("$.forbidenLetters").value(DEFAULT_FORBIDEN_LETTERS))
            .andExpect(jsonPath("$.discoveredLetters").value(DEFAULT_DISCOVERED_LETTERS));
    }

    @Test
    @Transactional
    void getNonExistingWord() throws Exception {
        // Get the word
        restWordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word
        Word updatedWord = wordRepository.findById(word.getId()).get();
        // Disconnect from session so that the updates on updatedWord are not directly saved in db
        em.detach(updatedWord);
        updatedWord
            .guessedWord(UPDATED_GUESSED_WORD)
            .firstLetter(UPDATED_FIRST_LETTER)
            .lenght(UPDATED_LENGHT)
            .forbidenLetters(UPDATED_FORBIDEN_LETTERS)
            .discoveredLetters(UPDATED_DISCOVERED_LETTERS);

        restWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWord))
            )
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getGuessedWord()).isEqualTo(UPDATED_GUESSED_WORD);
        assertThat(testWord.getFirstLetter()).isEqualTo(UPDATED_FIRST_LETTER);
        assertThat(testWord.getLenght()).isEqualTo(UPDATED_LENGHT);
        assertThat(testWord.getForbidenLetters()).isEqualTo(UPDATED_FORBIDEN_LETTERS);
        assertThat(testWord.getDiscoveredLetters()).isEqualTo(UPDATED_DISCOVERED_LETTERS);
    }

    @Test
    @Transactional
    void putNonExistingWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, word.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWordWithPatch() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word using partial update
        Word partialUpdatedWord = new Word();
        partialUpdatedWord.setId(word.getId());

        partialUpdatedWord.guessedWord(UPDATED_GUESSED_WORD).lenght(UPDATED_LENGHT);

        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWord))
            )
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getGuessedWord()).isEqualTo(UPDATED_GUESSED_WORD);
        assertThat(testWord.getFirstLetter()).isEqualTo(DEFAULT_FIRST_LETTER);
        assertThat(testWord.getLenght()).isEqualTo(UPDATED_LENGHT);
        assertThat(testWord.getForbidenLetters()).isEqualTo(DEFAULT_FORBIDEN_LETTERS);
        assertThat(testWord.getDiscoveredLetters()).isEqualTo(DEFAULT_DISCOVERED_LETTERS);
    }

    @Test
    @Transactional
    void fullUpdateWordWithPatch() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word using partial update
        Word partialUpdatedWord = new Word();
        partialUpdatedWord.setId(word.getId());

        partialUpdatedWord
            .guessedWord(UPDATED_GUESSED_WORD)
            .firstLetter(UPDATED_FIRST_LETTER)
            .lenght(UPDATED_LENGHT)
            .forbidenLetters(UPDATED_FORBIDEN_LETTERS)
            .discoveredLetters(UPDATED_DISCOVERED_LETTERS);

        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWord))
            )
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getGuessedWord()).isEqualTo(UPDATED_GUESSED_WORD);
        assertThat(testWord.getFirstLetter()).isEqualTo(UPDATED_FIRST_LETTER);
        assertThat(testWord.getLenght()).isEqualTo(UPDATED_LENGHT);
        assertThat(testWord.getForbidenLetters()).isEqualTo(UPDATED_FORBIDEN_LETTERS);
        assertThat(testWord.getDiscoveredLetters()).isEqualTo(UPDATED_DISCOVERED_LETTERS);
    }

    @Test
    @Transactional
    void patchNonExistingWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, word.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeDelete = wordRepository.findAll().size();

        // Delete the word
        restWordMockMvc
            .perform(delete(ENTITY_API_URL_ID, word.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
