@echo off
cd wrap
@echo on
@wrapper.exe -p winwrapper.conf
@wrapper.exe -r winwrapper.conf
@echo off
cd ..
@echo on