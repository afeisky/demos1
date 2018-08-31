#!/usr/bin/python
# coding:utf-8

"""
#Copyright (C) 2017 The TCL Mobile
Script Name: sdt_biglog.py
Program: chaofei.wu.hz@tcl.com, 2017-09-22


Usage: sdt_biglog.py <...>

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
    print('Usage: ./sdt_do_finish.py SDT1_12202127212121212120202020269.zip do do/SDT1_12202127212121212120202020269 success')

def test():
    print('[TCL*BIGDATA]')

def addJson(key,value,json):
    keyn="bl_"+key
    keyn=keyn.replace(" ","")
    json[keyn]=value
    return json

if __name__ == "__main__":
    print(sys.argv[0])
    params = []
    TCL_PATH_KEY='[TCL*BIGDATA]'
    if len(sys.argv[1:]) <4:
        Readme()
        sys.exit(1)
    else:
        print("param len:%d"%len(sys.argv[1:]))
        fromFile=sys.argv[1]
        toDir=sys.argv[2]
        doDir=sys.argv[3]
        flagStr=sys.argv[4] 
        print(fromFile)
        print(toDir)
        print(doDir)
        print(flagStr)
        


        

