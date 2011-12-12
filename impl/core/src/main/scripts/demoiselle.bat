@echo off
@rem 
@rem running Demoiselle Nimble on ms-windows
@rem 

@rem Set local scope for the variables with windows NT shell

if "%OS%"=="Windows_NT" setlocal

:begin
@rem Determine what directory it is in.

set DIRNAME=%~dp0

if "%DIRNAME%" == "" set DIRNAME=.\


SET PARM=%1
SET COMP=""%PARM%
IF %COMP% GTR "" (
@rem line Command
"%DIRNAME%\startDemoiselle.bat" "%DIRNAME%" %*
) ELSE ( 
echo no parameters informed, default is graphical interface!
"%DIRNAME%\startDemoiselle.bat" "%DIRNAME%" -g

)

@rem End local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" endlocal
%COMSPEC% /C exit /B %ERRORLEVEL%