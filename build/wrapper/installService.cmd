@echo off
cd wrap
@echo on
@wrapper.exe -i winwrapper.conf
@wrapper.exe -t winwrapper.conf
@echo off
cd ..
@echo on