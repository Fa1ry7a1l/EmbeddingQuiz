package com.example.demo.repo;

import com.example.demo.model.EmbeddingRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmbeddingRepository extends Repository<EmbeddingRecord, Long> {


    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO embedding(question_id, answer_embedding)
            VALUES (:qid, CAST(:vec AS vector))
            ON CONFLICT (question_id) DO UPDATE
            SET answer_embedding = EXCLUDED.answer_embedding
            """, nativeQuery = true)
    void upsert(@Param("qid") long questionId,
                @Param("vec") String vectorLiteral);

    // COSINE
    @Query(value = """
            SELECT question_id
            FROM embedding
            WHERE question_id = :qid
              AND (answer_embedding <=> CAST(:vec AS vector)) <= :maxDist
            LIMIT 1
            """, nativeQuery = true)
    List<Long> matchByCosine(@Param("qid") long qid,
                                       @Param("vec") String vectorLiteral,
                                       @Param("maxDist") double maxDist);

    // L2
    @Query(value = """
            SELECT question_id
            FROM embedding
            WHERE question_id = :qid
              AND (answer_embedding <-> CAST(:vec AS vector)) <= :maxDist
            LIMIT 1
            """, nativeQuery = true)
    List<Long> matchByL2(@Param("qid") long qid,
                         @Param("vec") String vectorLiteral,
                         @Param("maxDist") double maxDist);

    // Inner Product (distance = -dot)
    @Query(value = """
            SELECT question_id
            FROM embedding
            WHERE question_id = :qid
              AND (answer_embedding <#> CAST(:vec AS vector)) <= :maxDist
            LIMIT 1
            """, nativeQuery = true)
    List<Long> matchByIP(@Param("qid") long qid,
                                   @Param("vec") String vectorLiteral,
                                   @Param("maxDist") double maxDist);


}
