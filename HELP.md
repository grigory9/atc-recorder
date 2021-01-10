### TUTOR

docker run -d --name pg12 -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -v pg12_data_volume:/var/lib/postgresql/data postgres:12.2

docker exec -it pg12 psql -U postgres

-- commands from init_database.sql

./gradlew clean bootRun
