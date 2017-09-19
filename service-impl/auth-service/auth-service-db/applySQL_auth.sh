#!/bin/bash
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8

DEFAULT_URL="jdbc:postgresql://127.0.0.1:5432"

log()
{
    echo "[$(date +%F\ %T)] $@"
}

logn()
{
    echo -n "[$(date +%F\ %T)] $@"
}

log "+-----------------+"
log "|      AUTH   |"
log "|    UPDATESQL    |"
log "+-----------------+"

# fill variables. begin
DEFAULT_URL="jdbc:postgresql://127.0.0.1:5432"
DEFAULT_USR="postgres"

if [ _$1 == "_" ]; then # url not specified
    logn "Enter url for database (default is $DEFAULT_URL):"
    read URL
    if [ _$URL == "_" ]; then
        URL=$DEFAULT_URL;
    fi
else
    URL=$1;
fi

if [ _$2 == "_" ]; then # password not specified
    logn "Enter password for database superuser:"
    read -s EDB_PWD
else
    EDB_PWD=$2;
fi

if [ _$3 == "_" ]; then # user not specified
    logn "Enter superuser name (default is $DEFAULT_USR):"
    read EDB_USR
    if [ _$EDB_USR == "_" ]; then
        EDB_USR=$DEFAULT_USR;
    fi
else
    EDB_USR=$3;
fi
# fill variables. end

log "Running UPDATESQL using url: $URL"

./liquibase --driver=org.postgresql.Driver --username=$EDB_USR --liquibaseSchemaName=public --changeLogFile=initial_auth.xml --url=$URL/postgres?tcpKeepAlive=true --password=$EDB_PWD --logLevel=info updateSQL
if [ $? -ne 0 ]
then
exit 1
fi

./liquibase --driver=org.postgresql.Driver --username=$EDB_USR --liquibaseSchemaName=public --changeLogFile=prerun_auth.xml --url=$URL/authdb?tcpKeepAlive=true --password=$EDB_PWD --logLevel=info updateSQL
if [ $? -ne 0 ]
then
exit 1
fi

./liquibase --driver=org.postgresql.Driver --logLevel=info --username=$EDB_USR --liquibaseSchemaName=public --changeLogFile=master_auth.xml --url=$URL/authdb?tcpKeepAlive=true --password=$EDB_PWD updateSQL
if [ $? -ne 0 ]
then
exit 1
fi

./liquibase --driver=org.postgresql.Driver --username=$EDB_USR --liquibaseSchemaName=public --changeLogFile=postrun_auth.xml --url=$URL/authdb?tcpKeepAlive=true --password=$EDB_PWD --logLevel=info update
if [ $? -ne 0 ]
then
exit 1
fi
