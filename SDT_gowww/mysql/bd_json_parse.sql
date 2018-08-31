CREATE DEFINER=`chaofei_wu`@`%` PROCEDURE `bd_json_parse`(INfilename varchar(50))
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
declare imei1 varchar(16);
declare imei2 varchar(16);
declare productname varchar(30);
declare idcard varchar(30);
declare perso_system varchar(30);
declare bt_mac varchar(20);
declare wifi_mac varchar(20);
declare commercial_ref varchar(20);#
#-JSL
declare launcher_time json;
declare launcher_time_lk json;
declare launcher_time_la json;
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

DECLARE done INT DEFAULT FALSE;
DECLARE rs CURSOR for select filename from bd_json group by filename order by filename;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=TRUE;

#---

 DROP TABLE IF EXISTS bd_data_temp;
 CREATE TEMPORARY TABLE bd_data_temp( 
 filename varchar(50),
 filetime datetime,
 filesave json,
 imei1 varchar(16),
 imei2 varchar(16),
 productname varchar(30),
 idcard varchar(30),
 perso_system varchar(30),
 bt_mac varchar(20),
 wifi_mac varchar(20),
 commercial_ref varchar(20),
#-JSL
 launcher_time_lk json,
 launcher_time_la json,
 fec_record varchar(70),
#-JBL
 lifetime int,
 productmodel varchar(20),
 used_time varchar(20),
 sim_time varchar(20),
 wifi_time varchar(20),

#-JPI
 pcba_sn varchar(20),
 mini_ver varchar(20),
 bt_addr varchar(20),
 wifi_addr varchar(20),
 system_ver varchar(20),
 PRIMARY KEY (`filename`)
 ) ENGINE=MYISAM DEFAULT charset=utf8;
#select date_format(now(),'%Y-%m-%d %H:%m:%s') into datetime1;

OPEN rs;
REPEAT
FETCH rs into INfilename ;
IF NOT done THEN
#-
select JSON_EXTRACT(json, '$.data'),time,filesave into JMI,filetime,filesave1 from bd_json where filename=INfilename and type="mobileinfo";
select JSON_EXTRACT(json, '$.data') into JSL from bd_json where filename=INfilename and type="smartlog";
select JSON_EXTRACT(json, '$.data') into JBL from bd_json where filename=INfilename and type="biglog";
select JSON_EXTRACT(json, '$.data') into JPI from bd_json where filename=INfilename and type="proinfo";

#select JMI,JSL,JBL,JPI;
#-JMI
#--
select REPLACE(JSON_EXTRACT(JMI,'$.imei1'),'"','') into imei1;
select REPLACE(JSON_EXTRACT(JMI,'$.imei2'),'"','') into imei2;
select REPLACE(JSON_EXTRACT(JMI,'$.project_name'),'"','') into productname;
select REPLACE(JSON_EXTRACT(JMI,'$.product_name'),'"','') into idcard;
select REPLACE(JSON_EXTRACT(JMI,'$.perso_system'),'"','') into perso_system;
select REPLACE(JSON_EXTRACT(JMI,'$.bt_mac'),'"','') into bt_mac;
select REPLACE(JSON_EXTRACT(JMI,'$.wifi_mac'),'"','') into wifi_mac;
select REPLACE(JSON_EXTRACT(JMI,'$.commercial_ref'),'"','') into commercial_ref;
#-JSL
select JSON_EXTRACT(JSON_EXTRACT(JSL, '$.life_time_info'),'$.emmc_life_time') into lifetime;

select JSON_EXTRACT(JSL, '$.lk_launcher_time') into launcher_time;
#select launcher_time;
select launcher_time->'$[*].lk_start_time' into launcher_time_lk;
select launcher_time->'$[*].launcher_start_time' into launcher_time_la;

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
if (commercial_ref is NULL) THEN
select REPLACE(JSON_EXTRACT(JPI,'$.commercial_ref'),'"','') into commercial_ref;
END IF;

#if (imei1='358105080095362') THEN
  #select imei1;    
#END if;

#-----

select count(1) into n from bd_data_temp where filename=INfilename ; 
IF n=0 then
INSERT INTO bd_data_temp (filename,filetime,filesave,
 fec_record,launcher_time_lk,launcher_time_la,imei1, imei2, productname,productmodel, idcard , perso_system ,
 bt_mac , wifi_mac , lifetime,  used_time,sim_time,wifi_time,
 system_ver,pcba_sn,mini_ver,commercial_ref ,bt_addr, wifi_addr)
 VALUES(INfilename,filetime,filesave1,
 fec_record,launcher_time_lk,launcher_time_la,imei1, imei2, productname,productmodel, idcard , perso_system ,
 bt_mac , wifi_mac , lifetime,  used_time,sim_time,wifi_time,
 system_ver,pcba_sn,mini_ver,commercial_ref ,bt_addr, wifi_addr);
ELSE
 UPDATE bd_data_temp SET filetime=filetime,filesave=filesave1,fec_record=fec_record,launcher_time_lk=launcher_time_lk,launcher_time_la=launcher_time_la,
 imei1=imei1, imei2=imei2, productname=productname,
 productmodel=productmodel,idcard=idcard,perso_system=perso_system, bt_mac=bt_mac,
 wifi_mac=wifi_mac,lifetime=lifetime,system_ver=system_ver,pcba_sn=pcba_sn,
 commercial_ref=commercial_ref,bt_addr=bt_addr,wifi_addr=wifi_addr,mini_ver=mini_ver
 where filename=INfilename; 
END IF;
#-----
END IF;
UNTIL done 
END REPEAT;
CLOSE rs;
SET done=FALSE;
select * from bd_data_temp;
DROP TABLE IF EXISTS bd_data_temp;
END