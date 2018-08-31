CREATE DEFINER=`chaofei_wu`@`%` PROCEDURE `bd_json_tr`(INflag int,INfilename varchar(50))
BEGIN
declare n int default 99;
declare k int default 99;
declare datetime1 datetime;
declare jtime datetime;
declare JMI json;
declare JSL json;
declare JBL json;
declare JPI json;

declare jtemp json;
declare filetime datetime;
declare filesave1 json;
#-JMI
declare username varchar(20);
declare imei1 varchar(16);
declare imei2 varchar(16);
declare productname varchar(30);
declare idcard varchar(30);
declare perso_system varchar(30);
declare bt_mac varchar(20);
declare wifi_mac varchar(20);
declare commercial_ref varchar(20);#
declare rooted varchar(20);
declare battery_v int default 0;
declare tool varchar(20);

#-JSL
declare jsl_version varchar(10);
declare launcher_time json;
declare launcher_time_lk json;
declare launcher_time_la json;
declare boot_reason json;
declare boot_reason1 json;

declare fec_record varchar(70);
#-JBL
declare lifetime int;
declare productmodel varchar(20);#
declare used_time varchar(20);#
declare sim_time varchar(20);#
declare wifi_time varchar(20);#

#-JPI
declare pcba_sn varchar(20);#
declare mini_ver varchar(20);#
declare bt_addr varchar(20);#
declare wifi_addr varchar(20);#
declare system_ver varchar(20);#
declare test varchar(20);#

declare loginfo varchar(200) default '';#
declare str_temp varchar(200) default '';#
#-
select count(1) into n from bd_json where filename=INfilename and type="mobileinfo" limit 1;
if (n>0) then
select JSON_EXTRACT(json, '$.data'),time,filesave into JMI,filetime,filesave1 from bd_json where filename=INfilename and type="mobileinfo"  limit 1;
set loginfo=concat(loginfo,"mobileinfo");
else
select JSON_EXTRACT(json, '$.data'),time,filesave into JPI,filetime,filesave1 from bd_json where filename=INfilename and type="proinfo" limit 1;
end if;

select count(1) into n from bd_json where filename=INfilename and type="proinfo" limit 1;
if (n>0) then
select JSON_EXTRACT(json, '$.data'),time,filesave into JPI,filetime,filesave1  from bd_json where filename=INfilename and type="proinfo"  limit 1;
set loginfo=concat(loginfo,"/proinfo");
end if;

select count(1) into n from bd_json where filename=INfilename and type="smartlog" limit 1;
if (n>0) then
select JSON_EXTRACT(json, '$.data'),time,filesave into JSL,filetime,filesave1  from bd_json where filename=INfilename and type="smartlog" limit 1;
set loginfo=concat(loginfo,"/smartlog");
end if;

select count(1) into n from bd_json where filename=INfilename and type="biglog" limit 1;
if (n>0) then
select JSON_EXTRACT(json, '$.data'),time,filesave into JBL,filetime,filesave1 from bd_json where filename=INfilename and type="biglog"  limit 1;
set loginfo=concat(loginfo,"/biglog");
end if;

#select JMI,JSL,JBL,JPI;
#-JMI
#--
select REPLACE(JSON_EXTRACT(JMI,'$.username'),'"','') into username;
select REPLACE(JSON_EXTRACT(JMI,'$.imei1'),'"','') into imei1;
select REPLACE(JSON_EXTRACT(JMI,'$.imei2'),'"','') into imei2;
select REPLACE(JSON_EXTRACT(JMI,'$.project_name'),'"','') into productname;
select REPLACE(JSON_EXTRACT(JMI,'$.product_name'),'"','') into idcard;
select REPLACE(JSON_EXTRACT(JMI,'$.perso_system'),'"','') into perso_system;
select REPLACE(JSON_EXTRACT(JMI,'$.bt_mac'),'"','') into bt_mac;
select REPLACE(JSON_EXTRACT(JMI,'$.wifi_mac'),'"','') into wifi_mac;
select REPLACE(JSON_EXTRACT(JMI,'$.commercial_ref'),'"','') into commercial_ref;
select REPLACE(JSON_EXTRACT(JMI,'$.rooted'),'"','') into rooted;
select REPLACE(JSON_EXTRACT(JMI,'$.battery_v'),'"','') into battery_v;
select REPLACE(JSON_EXTRACT(JMI,'$.tool'),'"','') into tool;
#-JSL
#-jsl_version
select JSON_EXTRACT(JSL, '$.version') into str_temp; #jsl_version
select concat(JSON_EXTRACT(str_temp, '$.version1'),".",JSON_EXTRACT(str_temp, '$.version2'),".",JSON_EXTRACT(str_temp, '$.version3')) into jsl_version;
SET loginfo=REPLACE(loginfo, 'smartlog', concat('smartlog:',COALESCE(jsl_version, ''))); 
#-
select JSON_EXTRACT(JSON_EXTRACT(JSL, '$.life_time_info'),'$.emmc_life_time') into lifetime;

#select launcher_time;   *** NOTE: lk_laucher_time <>lk_launcher_time =lk_lauNcher_time ***
select JSON_EXTRACT(JSL, '$.lk_laucher_time') into launcher_time;
select launcher_time->'$[*].lk_start_time' into launcher_time_lk;
select launcher_time->'$[*].laucher_start_time' into launcher_time_la;
#
select JSON_EXTRACT(JSL, '$.boot_mode_and_reason') into boot_reason;
#
select JSON_EXTRACT(JSL, '$.fec_record') into jtemp;
select concat(JSON_EXTRACT(jtemp, '$.fec_decode_time'),',',JSON_EXTRACT(jtemp, '$.fec_decode_time'),',',JSON_EXTRACT(jtemp, '$.fec_decode_time')) into fec_record;


#select JSON_EXTRACT(JSON_EXTRACT(JSL, '$.launcher_start_time'),'$.launcher_start_time') into used_time;
#select JSON_EXTRACT(JSON_EXTRACT(JSL, '$.launcher_start_time'),'$.launcher_start_time') into used_time;

 
#-JBL
IF (lifetime is NULL) THEN
select JSON_EXTRACT(JBL,'$.LIFE_TIME') into lifetime;
END if;
select REPLACE(JSON_EXTRACT(JBL,'$.PRODUCT_MODEL'),'"','') into productmodel;
select JSON_EXTRACT(JSON_EXTRACT(JBL, '$.USER_TIME'),'$.used_time') into used_time;
select JSON_EXTRACT(JSON_EXTRACT(JBL, '$.USER_TIME'),'$.sim_time') into sim_time;
select JSON_EXTRACT(JSON_EXTRACT(JBL, '$.USER_TIME'),'$.wifi_time') into wifi_time;

#-JPI

select REPLACE(JSON_EXTRACT(JPI,'$.pcba_sn'),'"','') into pcba_sn;
select REPLACE(JSON_EXTRACT(JPI,'$.mini_ver'),'"','') into mini_ver;
select REPLACE(JSON_EXTRACT(JPI,'$.bt_addr'),'"','') into bt_addr;
select REPLACE(JSON_EXTRACT(JPI,'$.wifi_addr'),'"','') into wifi_addr;
select REPLACE(JSON_EXTRACT(JPI,'$.system_ver'),'"','') into system_ver;
#select commercial_ref;
IF (commercial_ref is NULL) THEN
select REPLACE(JSON_EXTRACT(JPI,'$.commercial_ref'),'"','') into commercial_ref;
END IF;

#if (imei1='358105080095362') THEN
  #select imei1;    
#END if;

#-----

select count(1) into n from bd_data where filename=INfilename ; 
IF n=0 then
#insert into bd_table1 (filename,json1,text) value(filename,launcher_time,"insert");
INSERT INTO bd_data (filename,filetime,filesave,
 fec_record,launcher_time_lk,launcher_time_la,boot_reason,imei1, imei2, productname,productmodel, idcard , perso_system ,
 bt_mac , wifi_mac , lifetime,  used_time,sim_time,wifi_time,
 system_ver,pcba_sn,mini_ver,commercial_ref ,bt_addr, wifi_addr,username,rooted,battery_v,tool,loginfo,update_time)
 VALUES(INfilename,filetime,filesave1,
 fec_record,launcher_time_lk,launcher_time_la,boot_reason,imei1, imei2, productname,productmodel, idcard , perso_system ,
 bt_mac , wifi_mac , lifetime,  used_time,sim_time,wifi_time,
 system_ver,pcba_sn,mini_ver,commercial_ref ,bt_addr, wifi_addr,username,rooted,battery_v,tool,loginfo,date_format(now(),'%Y-%m-%d %H:%i:%s'));
ELSE
IF (INflag=1) THEN # this is Tigger
 #insert into bd_table1 (filename,json1,text) value(filename,launcher_time,"update");
 UPDATE bd_data SET filetime=filetime,filesave=filesave1,fec_record=fec_record,launcher_time_lk=launcher_time_lk,boot_reason=boot_reason,launcher_time_la=launcher_time_la,
 imei1=imei1, imei2=imei2, productname=productname,
 productmodel=productmodel,idcard=idcard,perso_system=perso_system, bt_mac=bt_mac,
 wifi_mac=wifi_mac,lifetime=lifetime,system_ver=system_ver,pcba_sn=pcba_sn,
 commercial_ref=commercial_ref,bt_addr=bt_addr,wifi_addr=wifi_addr,mini_ver=mini_ver,username=username,rooted=rooted,battery_v=battery_v,tool=tool,
 loginfo=loginfo,update_time=date_format(now(),'%Y-%m-%d %H:%i:%s')
 where filename=INfilename; 
ELSE  # this is do: call bd_json_tr(0,"");
 #insert into bd_table1 (filename,json1,text) value(filename,launcher_time,"update");
 UPDATE bd_data SET filetime=filetime,filesave=filesave1,fec_record=fec_record,launcher_time_lk=launcher_time_lk,boot_reason=boot_reason,launcher_time_la=launcher_time_la,
 imei1=imei1, imei2=imei2, productname=productname,
 productmodel=productmodel,idcard=idcard,perso_system=perso_system, bt_mac=bt_mac,
 wifi_mac=wifi_mac,lifetime=lifetime,system_ver=system_ver,pcba_sn=pcba_sn,
 commercial_ref=commercial_ref,bt_addr=bt_addr,wifi_addr=wifi_addr,mini_ver=mini_ver,username=username,rooted=rooted,battery_v=battery_v,tool=tool,
 loginfo=loginfo
 where filename=INfilename; 
END IF;

END IF; 
#-----
END