package com.example.demo.service;

import com.example.demo.config.AppProperties;
import com.example.demo.repo.EmbeddingRepository;
import com.example.demo.util.Distance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimilarityService {
    private final OllamaClient ollama;
    private final EmbeddingRepository embeddings;
    private final AppProperties props;


    /**
     * Возвращает true/false по косинусному расстоянию в pgvector
     * Мы используем порог по similarity: sim >= threshold
     * В pgvector косинусное расстояние d ∈ [0,2], similarity = 1 - d/2
     */
    public boolean matches(long questionId, String userAnswer) {
        float[] v = ollama.embed(userAnswer);
// конвертация: similarity >= T => (1 - d/2) >= T => d <= 2*(1-T)
        double maxDist =  (1 - props.getSimilarity().getThreshold());
        var vec = Distance.toVectorLiteral(v);
        return !embeddings.matchByCosine(questionId, vec, maxDist).isEmpty();
    }
}