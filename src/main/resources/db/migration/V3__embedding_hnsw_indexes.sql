-- Таблица и расширение, если вдруг не применились ранее (идемпотентно)
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS embedding (
                                         question_id BIGINT PRIMARY KEY REFERENCES question(id) ON DELETE CASCADE,
    answer_embedding VECTOR(768) NOT NULL
    );

-- Три HNSW-индекса под разные метрики
CREATE INDEX IF NOT EXISTS idx_emb_hnsw_cos
    ON embedding USING hnsw (answer_embedding vector_cosine_ops);

CREATE INDEX IF NOT EXISTS idx_emb_hnsw_l2
    ON embedding USING hnsw (answer_embedding vector_l2_ops);

CREATE INDEX IF NOT EXISTS idx_emb_hnsw_ip
    ON embedding USING hnsw (answer_embedding vector_ip_ops);
