How to run this app?

1. Create and open the Docker container with the database:

docker-compose up --build

2. Execute the following command to create the database:

docker-compose exec db psql -U postgres -c "CREATE DATABASE library;"

3. Run the Spring Boot application.

You can also check the openapi.yaml file for the API description.