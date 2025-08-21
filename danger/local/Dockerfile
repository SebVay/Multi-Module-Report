FROM ghcr.io/danger/danger-kotlin:1.3.3

WORKDIR /code
COPY . /code

# Ensure `master` exists for local Danger evaluation (from origin/main)
RUN git branch master origin/main

WORKDIR /code/danger/local

RUN danger-kotlin local
