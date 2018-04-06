#!/bin/bash
# run this first
# chmod u+x resetdb.sh
# to use type ./resetdb.sh
psql <<END_OF_SQL
DROP DATABASE handoverdb;
CREATE DATABASE handoverdb;
GRANT ALL PRIVILEGES ON DATABASE handoverdb TO quantech;
