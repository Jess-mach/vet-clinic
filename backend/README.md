sudo docker run -d   --name postgres   -e POSTGRES_PASSWORD=morangos   -e POSTGRES_USER=jess   -e POSTGRES_DB=appdb   -p 5432:5432   ^Cstgres:16


lsof -ti:8080 | xargs kill -9