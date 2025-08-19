FROM ghcr.io/danger/danger-kotlin:1.3.3

WORKDIR /code
COPY . /code

# Ensure `master` exists for local Danger evaluation (from origin/main)
RUN git branch master origin/main

WORKDIR /code/integration-app/danger/

# Execute Danger and move the resulting JSON to a well-known output path
RUN danger-kotlin local
#RUN mkdir -p /output && mv danger_out.json /output/
