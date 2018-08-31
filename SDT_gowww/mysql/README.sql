CREATE DEFINER=`chaofei_admin`@`` PROCEDURE `README`()
BEGIN
# chaofei.wu.hz@tcl.com add for tclsysdb  2018-08-30
#
#  数据库用户名 chaofei_wu  密码 chaofei123 
#  bd_json,有2个触发器
#  bd_data,有2个触发器
#
#  bd_json_in存储过程
#  bd_json_tr存储过程


#SELECT filename,json FROM tclsysdb.bd_json where filename="SDT1_32504148505801971183149334352.zip" and type="smartlog"

#update bd_data set parser_step=0 where filename="SDT1_32504148505801971183149334352.zip"


#update bd_data set parser_history="" where filename="SDT1_32504148505801971183149334352.zip"

#update bd_data set parser_text="ddfdsf" where filename="SDT1_32504148505801971183149334352.zip"


#select date_format(now(),'%Y-%m-%d %H:%i:%s')
#select now()bd_databd_json_inbd_json_inbd_json_tr

#call test();

#call bd_json_parse1();

#call bd_json_tr1("SDT1_02000108000803010105040100050.zip");

#SELECT filename,json FROM bd_json where filename="SDT1_02000108000803010105040100050.zip";
#SELECT filename,boot_reason from bd_data where filename="SDT1_02000108000803010105040100050.zip";

#delete from bd_data where filename="SDT1_02000108000803010100030700050.zip";

#update bd_json set done=0 where ids=73;

#select * from  bd_table1;

#call bd_json_tr2("SDT1_32504148505801971183149334352.zip");	
#call bd_json_tr2("SDT1_32504148505701991102702654923.zip");	
#call bd_json_tr2("SDT1_32504128403800970009213713549.zip");

#SET SQL_SAFE_UPDATES = 0;  #Error Code: 1175. disable safe mode
#delete from bd_json where filename="SDT1_02000108000803010105040100050.zip";
END