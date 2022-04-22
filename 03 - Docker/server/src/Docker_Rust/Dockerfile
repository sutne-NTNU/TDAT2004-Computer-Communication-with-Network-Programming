FROM rust:1.31

WORKDIR /usr/src/oving3/server/src/Docker_Rust
COPY . .
COPY Cargo.toml Cargo.toml

RUN cargo install --path .

CMD ["main"]