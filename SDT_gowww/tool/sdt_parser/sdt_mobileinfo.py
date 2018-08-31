#!/usr/bin/python
# coding:utf-8

'''
#Copyright (C) 2017 The TCL Mobile
Script Name: sdt_mobileinfo.py
Program: chaofei.wu.hz@tcl.com, 2017-09-22


Usage: sdt_mobileinfo.py <...>

'''

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
    print('Usage: ./sdt_mobileinfo.py MobileInfo.txt')

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
    if len(sys.argv[1:]) == 0:
        Readme()
        sys.exit(1)
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
            sys.exit(1)
        jdata={}
        file = open(filepathname)
        while True:
            line = file.readline()
            if line:
                line=line.strip()
                print(line)
                key = "Username:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["username"]=one

                key = "Project Name:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["project_name"]=one
                
                key = "Product Name:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["product_name"]=one
                
                key = "Commercial Ref:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["commercial_ref"]=one
                
                key = "IMEI1:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["imei1"]=one
                
                key = "IMEI2:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["imei2"]=one
                
                key = "WIFI:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["wifi_mac"]=one
                
                key = "BT:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["bt_mac"]=one
                
                key = "ANDROID:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["perso_system"]=one

                key = "Rooted:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["rooted"]=one

                key = "Battery voltage:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["battery_v"]=one

                key = "Tool:";
                if (line.find(key) == 0) :
                    one = line[len(key):len(line)].strip()
                    jdata["tool"]=one

            else:
                break
        file.close()
        jout={}
        jout["result"]=1
        jout["data"]=jdata
        print(jdata) 
        print(json.dumps(jout, indent=4))
        print('[TCL*BIGDATA]')        
        print(json.dumps(jout))        
    sys.exit(0)
