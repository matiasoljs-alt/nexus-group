@echo off
setlocal

rem Verifica se JAVA_HOME esta definido
if "%JAVA_HOME%"=="" (
    if exist "C:\Users\Luana\AppData\Local\Programs\IntelliJ IDEA 2026.2.2\jbr" (
        set "JAVA_HOME=C:\Users\Luana\AppData\Local\Programs\IntelliJ IDEA 2026.2.2\jbr"
    )
)

if not "%JAVA_HOME%"=="" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

rem Procura maven instalado ou bundlado no IntelliJ
set "MVN_CMD=mvn"
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    if exist "C:\Users\Luana\AppData\Local\Programs\IntelliJ IDEA 2026.2.2\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" (
        set "MVN_CMD=C:\Users\Luana\AppData\Local\Programs\IntelliJ IDEA 2026.2.2\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"
    )
)

call "%MVN_CMD%" %*
endlocal
