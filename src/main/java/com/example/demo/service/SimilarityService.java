package com.example.demo.service;

import com.example.demo.config.AppProperties;
import com.example.demo.repo.EmbeddingRepository;
import com.example.demo.util.Distance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimilarityService {
    private final OllamaClient ollama;
    private final EmbeddingRepository embeddings;
    private final AppProperties props;


    public List<Boolean> matches(long questionId, String userAnswer) {
        float[] v = ollama.embed(userAnswer);

        double maxCosineDist = (1 - props.getSimilarity().getThreshold());
        double maxL2 = Math.sqrt(2.0 * (1 - props.getSimilarity().getThreshold()));
        double maxIpDist = -props.getSimilarity().getThreshold()* 400;
        var vec = Distance.toVectorLiteral(v);
        return List.of(
                !embeddings.matchByCosine(questionId, vec, maxCosineDist).isEmpty(),
                !embeddings.matchByL2(questionId, vec, maxL2).isEmpty(),
                !embeddings.matchByIP(questionId, vec, maxIpDist).isEmpty()
        );
    }
}