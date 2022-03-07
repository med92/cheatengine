package com.cheaengine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cheaengine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Word.class);
        Word word1 = new Word();
        word1.setId(1L);
        Word word2 = new Word();
        word2.setId(word1.getId());
        assertThat(word1).isEqualTo(word2);
        word2.setId(2L);
        assertThat(word1).isNotEqualTo(word2);
        word1.setId(null);
        assertThat(word1).isNotEqualTo(word2);
    }
}
