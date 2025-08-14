CREATE TABLE categories (
                            id UUID PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE posts (
                       id UUID PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       status VARCHAR(255) NOT NULL,
                       reading_time INTEGER NOT NULL,
                       author_id UUID NOT NULL REFERENCES users(id),
                       category_id UUID NOT NULL REFERENCES categories(id),
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);

CREATE TABLE tags (
                      id UUID PRIMARY KEY,
                      name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE post_tags (
                           post_id UUID NOT NULL REFERENCES posts(id),
                           tag_id UUID NOT NULL REFERENCES tags(id),
                           PRIMARY KEY (post_id, tag_id)
);
