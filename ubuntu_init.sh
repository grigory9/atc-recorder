#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

cd $DIR

wget https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz
tar -xzf ./openjdk-11.0.2_linux-x64_bin.tar.gz
rm -f ./openjdk-11.0.2_linux-x64_bin.tar.gz

export JAVA_HOME=${DIR}/jdk-11.0.2/

wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" | sudo tee  /etc/apt/sources.list.d/pgdg.list
sudo apt update
sudo apt -y install postgresql-12 postgresql-client-12

sudo -u postgres psql -f init_database.sql
./gradlew clean bootRun
