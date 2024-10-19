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
      will sping up SpringBoot
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