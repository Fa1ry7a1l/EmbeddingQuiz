package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "embedding")
public class EmbeddingRecord {
    @Id
    private Long questionId; // = Question.id


    // Храним как массив; маппим вручную SQL-ом (insert/update через nativeQuery)
    @Transient
    private float[] answerEmbedding;
}
