Electronic Handover Installation Instructions
==

**These instructions apply only to those setting up the web server, not the end user. No specific installation is required for the end user.**

These instructions will show you how to install all dependencies and run the system on your local machine.

Our project is dependent on the following programs:
* Java 8 or later
* Maven 3.5.2 or later
* PostgreSQL 10.1 or later

**Throughout this instruction set we will assume you are using a Unix based OS and have Homebrew/Linuxbrew installed.** If you don't have brew installed you can find instructions at the following links:

Homebrew: https://brew.sh/

Linuxbrew: http://linuxbrew.sh/

1. Install the latest version of Java which can be found at the following address:

   http://www.oracle.com/technetwork/java/javase/downloads/index.html

   Follow the instructions there to download and install the relevant version for your system.

2. Install Maven using brew:
```bash
brew install maven
```

3. Install PostgreSQL using brew:
```bash
brew install postgres
```
4.  Run the PostgreSQL server:
```bash
postgres -D /usr/local/var/postgres
```
5. In a separate terminal window, launch the PostgreSQL shell and create a new user _quantech_ with permission to create databases:
```bash
psql
CREATE ROLE quantech WITH LOGIN PASSWORD 'quantech';
ALTER ROLE quantech CREATEDB;
```
You can quit psql using the `\q` command.
We recommend you change the password for _quantech_ once you have everything installed and running. You can do this in psql:
```bash
\password quantech
```
Then enter a new password. You'll also need to change line 13 of [application-dev.yml](src/main/resources/application-dev.yml) to your new password.

6. Using psql, create a new database and give _quantech_ all privileges:
```bash
CREATE DATABASE handoverdb;
GRANT ALL PRIVILEGES ON DATABASE handoverdb TO quantech;
```

7. In a terminal window, navigate to electronic-handover and run the Spring Boot Web Application:
```bash
cd electronic-handover
mvn spring-boot:run
```
8. In your web browser go to:
```bash
http://localhost:8080/
```
If all has been successful you should see the Electronic Handover login page.
