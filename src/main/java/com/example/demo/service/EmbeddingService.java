package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.repo.EmbeddingRepository;
import com.example.demo.util.Distance;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final OllamaClient ollama;
    private final EmbeddingRepository embeddings;


    @Transactional
    public void computeAndStoreForQuestion(Question q) {
        float[] v = ollama.embed(q.getAnswerText());
        embeddings.upsert(q.getId(), Distance.toVectorLiteral(v));
    }
}
