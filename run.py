#!/usr/bin/python
from subprocess import Popen, PIPE
import sys

#for debug
#print( check_output(["ls", "-A"]))

tangibleAPI = Popen(["java", "-jar", "TangibleAPI/TangibleAPI.jar", "TangibleAPI/resources"], stdout=PIPE, stderr=PIPE)
print("TangibleAPI is running with the pid "+str(tangibleAPI.pid))
# now let's wait until tangibleAPI print one of the two expected lines
setup = False;
for line in iter(tangibleAPI.stdout.readline, ''):
    line = line.strip(' \r\n')
    #print("from TangibleAPI: --> "+line)
    if(line == "TANGIBLE_API_READY"):
        print("tangibleAPI is ready, let's start SiftDriver now")
        siftDriver = Popen(["mono", "SiftDriver/SiftDriver.exe"], stdout=PIPE, stderr=PIPE)
        setup = True
        print("started!")
        break
    #else :
    #    print("TANGIBLE_API_READY was not the same thing that: "+line)
    if (line == "TANGIBLE_API_FAILED"):
        print("impossible to run the TangibleAPI")
        break
    pass

if(setup):
    print ("press enter to turn off the while thing")
    sys.stdin.readline()
    siftDriver.terminate()
    tangibleAPI.terminate()
    print ("we are done, press enter to quit the script")
    sys.stdin.readline()
    pass
print("bye bye")