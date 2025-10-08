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

    @Query(value = """
      SELECT q.id
      FROM question q
      JOIN embedding e ON q.id=e.question_id
      WHERE q.id = :qid AND (e.answer_embedding <=> CAST(:vec AS vector)) <= :maxDist
      """, nativeQuery = true)
    List<Long> matchByCosine(@Param("qid") long qid,
                             @Param("vec") String vectorLiteral,
                             @Param("maxDist") double maxDist);
}
