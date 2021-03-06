@echo off
if not exist ..\log mkdir ..\log
set log_name=..\log\apply.log

setlocal enabledelayedexpansion
echo.>"%TEMP%\~.ddf"
makecab /D RptFileName="%TEMP%\~.rpt" /D InfFileName="%TEMP%\~.inf" -f "%TEMP%\~.ddf">nul
for /f "tokens=4-7" %%a in ('type "%TEMP%\~.rpt"') do (
if not defined cmd_month set "cmd_month=%%a"
if not defined cmd_day   set "cmd_day=%%b"
if not defined cmd_time  set "cmd_time=%%c"
if not defined cmd_year  set "cmd_year=%%d")
set Jan=01&set Feb=02&set Mar=03&set Apr=04&set May=05&set Jun=06&set Jul=07&set Aug=08&set Sep=09&set Oct=10&set Nov=11&set Dec=12
for %%i in (Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec) do if /i "%cmd_month%"=="%%i" set cmd_month=!%%i!
del /q "%TEMP%\~.*"
set sql_name=..\log\changesql_%cmd_year:~2,2%%cmd_month%%cmd_day%_%cmd_time:~0,2%%cmd_time:~3,2%%cmd_time:~6,2%
set sql_name=%sql_name: =0%.sql

echo ******************************* >> %log_name%
echo TEMP = %TEMP%
call :logger %DATE% %TIME% "+-----------------+"
call :logger %DATE% %TIME% "|      METAL      |"
call :logger %DATE% %TIME% "|    UPDATESQL    |"
call :logger %DATE% %TIME% "+-----------------+"
echo .

SETLOCAL
set NLS_LANG=AMERICAN_AMERICA.AL32UTF8
set default_url="jdbc:postgresql://192.168.56.103:5454"

if "%1"=="" (
	set /p url="Enter url for database (default is %default_url%):"
) else (
	set url=%1
)

if "%url%"=="" ( 
	set url=%default_url%
)

if "%2"=="" (
	set /p pass="Enter password for database superuser:"
) else (
	set pass=%2
)

if "%3"=="" (
	set user=postgres
) else (
	set user=%3
)

echo %DATE% %TIME% Running UPDATESQL using url: %url%
echo .

echo %DATE% %TIME% mtdb initialization changesets
echo %DATE% %TIME% mtdb initialization changesets >> %log_name%
call liquibase --driver=org.postgresql.Driver --username=%user% --liquibaseSchemaName=public --changeLogFile=initial_mt.xml --url=%url%/postgres?tcpKeepAlive:true --password=%pass% --logLevel=info updateSQL > %sql_name%
IF NOT %ERRORLEVEL%  == 0 (CALL :logger ***ERROR***
GOTO endscr)

echo %DATE% %TIME% mtdb prerun changesets
echo %DATE% %TIME% mtdb prerun changesets >> %log_name%
call liquibase --driver=org.postgresql.Driver --username=%user% --liquibaseSchemaName=public --changeLogFile=prerun_mt.xml --url=%url%/mtdb?tcpKeepAlive:true --password=%pass% --logLevel=info updateSQL >> %sql_name%
IF NOT %ERRORLEVEL%  == 0 (CALL :logger ***ERROR***
GOTO endscr)

echo %DATE% %TIME% mtdb master changesets
echo %DATE% %TIME% mtdb master changesets >> %log_name%
call liquibase --driver=org.postgresql.Driver --logLevel=info --username=%user% --liquibaseSchemaName=public --changeLogFile=master_mt.xml --url=%url%/mtdb?tcpKeepAlive:true --password=%pass% updateSQL >> %sql_name%
IF NOT %ERRORLEVEL%  == 0 (CALL :logger ***ERROR***
GOTO endscr)

echo %DATE% %TIME% mtdb postrun changesets
echo %DATE% %TIME% mtdb postrun changesets >> %log_name%
call liquibase --driver=org.postgresql.Driver --username=%user% --liquibaseSchemaName=public --changeLogFile=postrun_mt.xml --url=%url%/mtdb?tcpKeepAlive:true --password=%pass% --logLevel=info updateSQL  >> %sql_name%
IF NOT %ERRORLEVEL%  == 0 (CALL :logger ***ERROR***
GOTO endscr)

ENDLOCAL

:endscr
echo %DATE% %TIME% METAL mtdb UPDATESQL end
echo %DATE% %TIME% METAL mtdb UPDATESQL end >> %log_name%
echo %DATE% %TIME% METAL mtdb UPDATESQL end >> %log_name%
echo  >> %log_name%
goto end

:logger
	ECHO %*
	ECHO %* >> %log_name%
	exit /b
	
:end
