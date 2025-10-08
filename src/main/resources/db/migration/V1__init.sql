CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS quiz (
                                    id BIGSERIAL PRIMARY KEY,
                                    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS question (
                                        id BIGSERIAL PRIMARY KEY,
                                        quiz_id BIGINT NOT NULL REFERENCES quiz(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    answer_text TEXT NOT NULL
    );

-- размерность под вашу модель эмбеддингов
CREATE TABLE IF NOT EXISTS embedding (
                                         question_id BIGINT PRIMARY KEY REFERENCES question(id) ON DELETE CASCADE,
    answer_embedding VECTOR(768) NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_embedding_cosine
    ON embedding USING hnsw (answer_embedding vector_cosine_ops);
