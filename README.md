# Project Description

Microservice to interact with a MySQL database to fill out two tables: 1) information about banking transactions and 2)
information about credit card transactions correlated with these bank account transactions. Once these two tables are
populated, various operations are completed on them including joins on transactional relationships, averaging
transactional categories, and more.

### Project Setup

In Order to run the project locally, the environment variables located in `application.yml` need to be populated either
through environment variables, or through spring's supported method of overriding properties with spring profiles which
is the method I opt to use.

1) In `src/main/resources` create a file called `application-local.yml`.
    - By default, this will not be checked into git,
      so adding secrets here poses no risk
2) Fill in the following properties
    - `spring.datasource.url: <db url>`
    - `spring.datasource.username: <db username>`
    - `spring.datasource.password: <db password>`

After that, setup is completed. In `application.yml`, it defaults to the local profile which is populated by the file
created in step 1.

### Interacting with the project

1) Run `./gradlew clean build`
2) Few options depending on how you're editing the project
    - If running in Intellij or likely other IDEs, navigate to `FinanceTrackerApplication.java` and run the file which
      will spin up SpringBoot
    - Run `./gradlew bootRun` in any terminal within the projects directory
3) Navigate to the `.http` directory
    - If using IntelliJ Ultimate, it provides support to make http calls. Navigate to any of the .http files, click the
      dropdown next to `Run With` and select `Add Environment to Private File`. You will then fill out the necessary
      information, reference it in the .http files, and you can start calling the APIs
    - Otherwise, just use the stubs to fill out any other API testing tool
4) The API calls should be called in the following order:
    - `http/create/create-tables.http`
    - `http/insert/upload-csv.http`

After those two API calls are completed, the tables are created and filled out and can be interacted in any way.

### Running a local docker image

1) start the docker daemon
2) run `./gradlew clean build` to get the latest jar
3) run `docker build -t finance-tracker:latest .`
4) start the docker image `docker run -p 8080:8080 finance-tracker:latest`
5) try interaction with the endpoints in the .http directory
6) kill the local process and run `docker container prune` to clear any existing containers

### Pulling from docker hub

Images get published during any push to main via GitHub actions

1) kill the local process and run `docker container prune` to clear any existing containers
2) create a .env file and add the following secrets
   - SQL_DB_URL=<>
   - USERNAME=<>
   - PASSWORD=<>
3) run
   `docker run --platform linux/amd64 -it --name finance-tracker -p 8080:8080 --env-file .env dkosmin/finance-tracker:latest`

### Integration testing setup

A test MySQL db docker image is used to run integration tests

1) run `docker-compose up -d` to build the test MySQL DB
2) run `./gradlew clean test` to run the integration tests
3) run `docker-compose down` to tear down the test MySQL DB