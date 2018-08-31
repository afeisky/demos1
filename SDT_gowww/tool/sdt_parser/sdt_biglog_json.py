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
    print('Usage: tct_biglog.py biglog.json\n')

def test():
    print('[TCL*BIGDATA]')

def addJson(key,value,json):
    keyn=key
    keyn=keyn.replace(" ","")
    json[keyn]=value
    return json

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
            sys.exit(1)
        jfrom=[]
        with open(filepathname) as json_file:
            jfrom = json.load(json_file)
        #{u'PRODUCT_MODEL': u'5049G', u'version': u'V1.0.1', u'LIFE_TIME': 1, u'USER_TIME': {u'wifi_time': 0, u'sim_time': 0, u'used_time': 56}}
        jdata={}
        if (False):#test
            if(isinstance(jfrom,dict)):
                for level0 in jfrom:
                    jfrom0=jfrom[level0]
                    if(isinstance(jfrom0,dict)):
                        print('%s:%s'%(level0,jfrom0))
                        for level1 in jfrom0:
                            jfrom1=jfrom0[level1]
                            print('    %s:%s'%(level1,jfrom1))
                            addJson(level0+"__"+level1,jfrom1,jdata) 
                    else:
                        print('%s:%s'%(level0,jfrom0))
                        addJson(level0,jfrom0,jdata) 

        print(json.dumps(jfrom, indent=4))
        print(json.dumps(jdata, indent=4))
        jout={}
        jout["result"]=1
        jout["data"]=jfrom
        print(json.dumps(jout, indent=4))
        print('[TCL*BIGDATA]')        
        print(json.dumps(jout))
    sys.exit(0)


        

