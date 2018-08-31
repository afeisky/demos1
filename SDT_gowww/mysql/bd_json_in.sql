CREATE DEFINER=`chaofei_wu`@`%` PROCEDURE `bd_json_in`(INfilename varchar(50),INtype varchar(10),INimei varchar(16), INtime datetime, INjson varchar(6000),INfilesave varchar(200))
BEGIN
declare jimei varchar(50) default "AFEI00";
declare n int default 99;
declare k int default 99;
declare datetime1 datetime;
declare jtime datetime;
declare jsondata json;
select date_format(now(),'%Y-%m-%d %H:%i:%s') into datetime1;
select count(1) into n from bd_json where filename=INfilename and type=INtype; 
select json,imei,time into jsondata,jimei,jtime from bd_json where filename=INfilename and type=INtype limit 1; 
IF n>0 then
    IF (jsondata=INjson and jimei=INimei and jtime=INtime) then
        select 0,concat_ws(",","exist!",INfilename,INtype,INimei,INtime,datetime1) as comment;
    else
        UPDATE bd_json SET json=INjson,update_time=datetime1,imei=INimei,time=INtime,filesave=INfilesave where filename=INfilename and type=INtype;
        select 1,concat_ws(",","update success",INfilename,INtype,INimei,INtime,datetime1) as comment;
    END IF;
else
    INSERT INTO bd_json (json,filename,type,imei,time,filesave,update_time) VALUES(INjson,INfilename,INtype,INimei,INtime,INfilesave,datetime1);
    select 1,concat_ws(",","insert success",INfilename,INtype,INimei,INtime,datetime1) as comment;
END IF;
###
END