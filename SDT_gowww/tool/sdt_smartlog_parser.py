#!/usr/bin/python
# coding:utf-8

"""
#Copyright (C) 2017 The TCL Mobile
Script Name: sdt_smart1_parser.py
Program: chaofei.wu.hz@tcl.com, 2017-09-22


Usage: sdt_smart1_parser.py <...>

"""
import os
import sys
import re
import time,datetime
import glob
import commands
from commands import *
from time import strftime, localtime
import json

def Readme():
    print('Usage: sdt_smart1_parser.py smartlog1.bin\n')

def test():
    print('[TCL*BIGDATA]')

if __name__ == "__main__":
    print(sys.argv[0])
    params = []
    TCL_PATH_KEY='[TCL*BIGDATA]'
    if len(sys.argv[1:]) == 0:
        Readme()
        sys.exit(0)
    else:
        filepathname=sys.argv[1]
        print(filepathname)        
        if not os.path.exists(filepathname):
            print("Error: not found file: "+filepathname)
            jout={}
            jout["result"]=0
            jout["comment"]="Error: not found file: "+filepathname
            print(json.dumps(jout, indent=4))
            print('[TCL*BIGDATA]')
            print(json.dumps(jout)) 
            sys.exit(0)
        else:
            cmdfile=sys.argv[0].replace('.py','')  
            out_data = commands.getoutput(cmdfile+" "+filepathname)
            out_data=""
            with open(filepathname+"/Smartlog.bin_@bd.json", 'r') as f:
                out_data+=f.read()
            f.closed
            out_data=out_data.replace('\n','')
            out_data=out_data.replace('	','')
            out_data=out_data.replace(',}','}')
            out_data=out_data.replace(',]',']')
            print('[TCL*BIGDATA]')
            if (len(out_data)>0):
                print("{\"data\":"+out_data+",\"result\":1}")
            else:
                print("{\"comment\":\"\",\"result\":0}")
    sys.exit(0)


        

