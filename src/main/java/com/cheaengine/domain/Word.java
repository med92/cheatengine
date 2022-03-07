package com.cheaengine.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Word.
 */
@Entity
@Table(name = "word")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.")
    @Column(name = "guessed_word")
    private String guessedWord;

    @Column(name = "first_letter")
    private String firstLetter;

    @Column(name = "lenght")
    private Integer lenght;

    @Column(name = "forbiden_letters")
    private String forbidenLetters;

    @Column(name = "discovered_letters")
    private String discoveredLetters;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Word id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuessedWord() {
        return this.guessedWord;
    }

    public Word guessedWord(String guessedWord) {
        this.setGuessedWord(guessedWord);
        return this;
    }

    public void setGuessedWord(String guessedWord) {
        this.guessedWord = guessedWord;
    }

    public String getFirstLetter() {
        return this.firstLetter;
    }

    public Word firstLetter(String firstLetter) {
        this.setFirstLetter(firstLetter);
        return this;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Integer getLenght() {
        return this.lenght;
    }

    public Word lenght(Integer lenght) {
        this.setLenght(lenght);
        return this;
    }

    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }

    public String getForbidenLetters() {
        return this.forbidenLetters;
    }

    public Word forbidenLetters(String forbidenLetters) {
        this.setForbidenLetters(forbidenLetters);
        return this;
    }

    public void setForbidenLetters(String forbidenLetters) {
        this.forbidenLetters = forbidenLetters;
    }

    public String getDiscoveredLetters() {
        return this.discoveredLetters;
    }

    public Word discoveredLetters(String discoveredLetters) {
        this.setDiscoveredLetters(discoveredLetters);
        return this;
    }

    public void setDiscoveredLetters(String discoveredLetters) {
        this.discoveredLetters = discoveredLetters;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Word)) {
            return false;
        }
        return id != null && id.equals(((Word) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Word{" +
            "id=" + getId() +
            ", guessedWord='" + getGuessedWord() + "'" +
            ", firstLetter='" + getFirstLetter() + "'" +
            ", lenght=" + getLenght() +
            ", forbidenLetters='" + getForbidenLetters() + "'" +
            ", discoveredLetters='" + getDiscoveredLetters() + "'" +
            "}";
    }
}
