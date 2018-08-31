package models

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"fmt"
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"strings"
	"io/ioutil"
	"path"
	"os"
)

func Sql_data(query_type string,query_text string) string{
	fmt.Println(query_type+","+query_text)
	html_data:=""
	var dsn string
	db_type := beego.AppConfig.String("db_type")
	db_host := beego.AppConfig.String("db_host")
	db_port := beego.AppConfig.String("db_port")
	db_user := beego.AppConfig.String("db_user")
	db_pass := beego.AppConfig.String("db_pass")
	db_name := beego.AppConfig.String("db_name")
	switch db_type {
	case "mysql":
		orm.RegisterDriver("mysql", orm.DRMySQL)
		dsn = fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", db_user, db_pass, db_host, db_port, db_name)
		break
	default:
		beego.Critical("Database driver is not allowed:", db_type)
	}
	db, err := sql.Open(db_type, dsn)
	if err != nil {
		panic(err.Error())
	}
	str_where:=""
	if (strings.Index(query_type, "filename")== 0 && len(query_text)>0){
		str_where=fmt.Sprintf("where filename like '%%%s%%'",query_text)
		fmt.Println("1 "+str_where)
	}else if (strings.Index(query_type, "imei")== 0 && len(query_text)>0){
		str_where=fmt.Sprintf("where imei1 like '%%%s%%' or imei2 like '%%%s%%'",query_text,query_text)
		fmt.Println("2 "+str_where)
	}else if (strings.Index(query_type, "productname")== 0 && len(query_text)>0){
		str_where=fmt.Sprintf("where productname like '%%%s%%'",query_text)
		fmt.Println("3 "+str_where)
	}
	sqlstring := "select ids,filename,filetime,imei1,imei2,productname,idcard,perso_system,username,tool,loginfo,update_time from bd_data "+str_where+" order by update_time DESC limit 200" 
	//sqlstring := "select ids,filename,filetime from bd_data "
	fmt.Println(sqlstring)
	//r, err := db.Exec(sqlstring)
	//if err != nil {
//		log.Println(err)
//		log.Println(r)
	//} else {
//		log.Println("Database ", db_name, " created")
	//}
	rows, err := db.Query(sqlstring)
	if err != nil {
		fmt.Println("fetech data failed:", err.Error())
		return ""
	}
	defer rows.Close()
	defer db.Close()

	html_data=fmt.Sprintf("<thead><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></thead>", " "," filename","filetime","imei1","imei2","productname","idcard","perso_system","username","tool","loginfo","update_time")
	n:=0
	for rows.Next() {
		var ids int
		var filename,filetime,imei1,imei2,productname,idcard,perso_system,username,tool,loginfo,update_time string
		n++
		rows.Scan(&ids, &filename, &filetime,&imei1,&imei2,&productname,&idcard,&perso_system,&username,&tool,&loginfo,&update_time)
		//fmt.Println("ids:", ids, "filename:", filename, "filetime:", filetime)
		//html_data+=fmt.Sprintf("<tr onclick=\"select('%s')\"><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", filename,ids, filename,filetime,imei1,imei2,productname,idcard,perso_system,username,tool,loginfo,update_time)
		ids_ahref:=fmt.Sprintf("<a href=\"?p=2&&filename=%s\">%d</a>",filename,n)
		filename_ahref:=fmt.Sprintf("<a href=\"?p=1&&filename=%s\">%s</a>",filename,filename)
		html_data+=fmt.Sprintf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",ids_ahref, filename_ahref,filetime,imei1,imei2,productname,idcard,perso_system,username,tool,loginfo,update_time)
	}

	return html_data
}
func Sql_detail_filename(fname string) (int,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string) {
	fmt.Println(fname)
	var dsn string
	db_type := beego.AppConfig.String("db_type")
	db_host := beego.AppConfig.String("db_host")
	db_port := beego.AppConfig.String("db_port")
	db_user := beego.AppConfig.String("db_user")
	db_pass := beego.AppConfig.String("db_pass")
	db_name := beego.AppConfig.String("db_name")
	switch db_type {
	case "mysql":
		orm.RegisterDriver("mysql", orm.DRMySQL)
		dsn = fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", db_user, db_pass, db_host, db_port, db_name)
		break
	default:
		beego.Critical("Database driver is not allowed:", db_type)
	}
	db, err := sql.Open(db_type, dsn)
	if err != nil {
		panic(err.Error())
	}
	sqlstring := "select ids,filename,filetime,imei1,COALESCE(imei2, '') as imei2,productname,idcard,perso_system,bt_mac,wifi_mac,COALESCE(commercial_ref, '') as commercial_ref,COALESCE(fec_record, '') as fec_record, "
        sqlstring += " lifetime,COALESCE(productmodel, '') as productmodel,COALESCE(used_time, 0) as used_time,COALESCE(sim_time, 0) as sim_time,COALESCE(wifi_time, 0) as wifi_time,"
        sqlstring += " pcba_sn,mini_ver,bt_addr,wifi_addr,system_ver,username,tool,rooted,battery_v,update_time, "
	sqlstring += " COALESCE(launcher_time_lk, '') as launcher_time_lk,COALESCE(launcher_time_la, '') as launcher_time_la,COALESCE(boot_reason, '') as boot_reason, "	
	sqlstring += " COALESCE(parser_mail, '') as parser_mail,COALESCE(parser_text, '') as parser_text,COALESCE(parser_time, '') as parser_time,COALESCE(parser_history, '') as parser_history,COALESCE(parser_step, 0) as parser_step "
	sqlstring += " from bd_data where filename='"+fname+"' "
	//sqlstring := "select ids,filename,filetime from bd_data "

	fmt.Println(sqlstring)
	//var ids,lifetime int
	//var filename,filetime,imei1,imei2,productname,idcard,perso_system,bt_mac,wifi_mac,commercial_ref,fec_record string
        //var lifetime,productmodel,used_time,sim_time,wifi_time,pcba_sn,mini_ver,bt_addr,wifi_addr,system_ver,username,tool,rooted,battery_v,update_time,parser_mail string
        //var parser_text,parser_time,parser_history,parser_step string
	//var filename,filetime,imei1,imei2,productname,idcard,perso_system,bt_mac,wifi_mac,commercial_ref,fec_record string
        //var productmodel,used_time,sim_time,wifi_time,pcba_sn,mini_ver,bt_addr,wifi_addr,system_ver,username,tool,rooted,battery_v,update_time,parser_mail string
        //var parser_text,parser_time,parser_history,parser_step string
	ids:=0
	filename:=""
	filetime:=""
	imei1:=""
	imei2:=""
	productname:=""
	idcard:=""
	perso_system:=""
	bt_mac:=""
	wifi_mac:=""
	commercial_ref:=""
	fec_record:=""
	lifetime:=0
	productmodel:=""
	used_time:=""
	sim_time:=""
	wifi_time:=""
	pcba_sn:=""
	mini_ver:=""
	bt_addr:=""
	wifi_addr:=""
	system_ver:=""
	username:=""
	tool:=""
	rooted:=""
	battery_v:=""
	update_time:=""
	launcher_time_lk:=""
	launcher_time_la:=""
	boot_reason:=""
	parser_mail:=""
	parser_text:=""
	parser_time:=""
	parser_history:=""
	parser_step:=""
	defer db.Close()
	rows, err := db.Query(sqlstring)
	if err != nil {
	    fmt.Println("fetech data failed:", err.Error())
	}
	defer rows.Close()
	for rows.Next() {
	    rows.Scan(&ids,&filename,&filetime,&imei1,&imei2,&productname,&idcard,&perso_system,&bt_mac,&wifi_mac,&commercial_ref,&fec_record,
	    &lifetime,&productmodel,&used_time,&sim_time,&wifi_time,
	    &pcba_sn,&mini_ver,&bt_addr,&wifi_addr,&system_ver,&username,&tool,&rooted,&battery_v,&update_time,
	    &launcher_time_lk,&launcher_time_la,&boot_reason,
	    &parser_mail,&parser_text,&parser_time,&parser_history,&parser_step)
	    fmt.Println(filename+filetime+imei1+imei2+productname+idcard+perso_system+bt_mac+wifi_mac+commercial_ref+fec_record+productmodel+used_time+sim_time+wifi_time+pcba_sn+mini_ver+bt_addr+wifi_addr+system_ver+username+tool+rooted+battery_v+update_time+launcher_time_lk+launcher_time_la+parser_mail+parser_text+parser_time+parser_history+parser_step)
	}
	//lk:=`["2017-01-01 01:37:56", "2017-01-01 00:41:06", "2017-01-01 00:13:43", "2017-01-01 00:00:03", "2017-01-01 00:00:03", "2017-01-01 00:00:04", "2018-08-17 07:31:27", "2018-08-13 04:34:08", "2018-08-13 04:23:19", "2018-08-11 05:27:26", "2018-08-11 04:47:01", "2018-02-22 13:23:39", "2018-01-04 19:03:59", "", "", "", "", "", "", ""]`
	//la:=`["", "", "", "", "", "", "2018-08-17 07:37:05", "2018-08-13 04:39:50", "2018-08-13 04:28:58", "2018-08-11 05:33:04", "2018-08-11 05:08:05", "2018-02-22 14:12:37", "2018-01-04 19:12:15", "", "", "", "", "", "", ""]`
	lk:=launcher_time_lk
	la:=launcher_time_la
	fmt.Println(lk)
	fmt.Println(la)
	lk=strings.Replace(lk, "[", " ", -1)
	lk=strings.Replace(lk, "]", "", -1)
	la=strings.Replace(la, "[", " ", -1)
	la=strings.Replace(la, "]", "", -1)
        arr_lk := strings.Split(lk, ",")
	arr_la := strings.Split(la, ",")
        arr_lkla:=""
	for  i:=0; i<len(arr_lk);i++{
	    arr_lkla+=fmt.Sprintf("[%s -> %s]\n",arr_lk[i],arr_la[i])
	}
	fmt.Println(arr_lkla)
	//-----
	//boot_reason:=`[{"boot_mode": "fastboot", "boot_reason": "wdt"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "fastboot", "boot_reason": "wdt"}, {"boot_mode": "kernel_power_off_charging_boot", "boot_reason": "wdt"}, {"boot_mode": "fastboot", "boot_reason": "wdt"}, {"boot_mode": "normal_boot", "boot_reason": "wdt_by_pass_pwk"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "wdt_by_pass_pwk"}, {"boot_mode": "kernel_power_off_charging_boot", "boot_reason": "usb"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "wdt_by_pass_pwk"}, {"boot_mode": "kernel_power_off_charging_boot", "boot_reason": "usb"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "power_key"}, {"boot_mode": "normal_boot", "boot_reason": "wdt_by_pass_pwk"}, {"boot_mode": "unknown", "boot_reason": "null"}]`	
	fmt.Println(boot_reason)
	boot_reason=strings.Replace(boot_reason, "[", " ", -1)
	boot_reason=strings.Replace(boot_reason, "]", "", -1)
        arr_br := strings.Split(boot_reason, "},")
        arr_bootreason:=""
	fmt.Println(len(arr_br))
	for  i:=0; i<len(arr_br);i++{
	    arr_bootreason+=fmt.Sprintf("%s\n",arr_br[i])
	}
	fmt.Println(arr_bootreason)
	return lifetime,filename,filetime,imei1,imei2,productname,idcard,perso_system,bt_mac,wifi_mac,commercial_ref,fec_record,
		productmodel,used_time,sim_time,wifi_time,
		pcba_sn,mini_ver,bt_addr,wifi_addr,system_ver,username,tool,rooted,battery_v,update_time,
		parser_mail,parser_text,parser_time,parser_history,parser_step,
		arr_lkla,arr_bootreason
}
func Update_text(filename string,parser_text string,parser_mail string) bool{
	fmt.Println(filename)
	fmt.Println(parser_mail)
	var dsn string
	db_type := beego.AppConfig.String("db_type")
	db_host := beego.AppConfig.String("db_host")
	db_port := beego.AppConfig.String("db_port")
	db_user := beego.AppConfig.String("db_user")
	db_pass := beego.AppConfig.String("db_pass")
	db_name := beego.AppConfig.String("db_name")
	switch db_type {
	case "mysql":
		orm.RegisterDriver("mysql", orm.DRMySQL)
		dsn = fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", db_user, db_pass, db_host, db_port, db_name)
		break
	default:
		beego.Critical("Database driver is not allowed:", db_type)
	}
	db, err := sql.Open(db_type, dsn)
	if err != nil {
		panic(err.Error())
		return false
	}
	parser_text= strings.Replace(parser_text, "'", "\"", -1)
	parser_mail= strings.Replace(parser_mail, "'", "\"", -1)
	sqlstring:="update bd_data set parser_text='"+parser_text+"',parser_mail='"+parser_mail+"' where filename='"+filename+"'"
	fmt.Println(sqlstring)
	defer db.Close()
	_, err = db.Exec(sqlstring)
	if err != nil {
	    fmt.Println("fetech data failed:", err.Error())
	}
	fmt.Println("Succ")
	return true
}
var filelist=[]string{}
func ListAllFile(folder string){
	files, _ := ioutil.ReadDir(folder)
	for _, file := range files {
		if (file.IsDir()) {
			ListAllFile(folder + "/" +file.Name())
		}else{
			fmt.Println(folder + "/" + file.Name())
			filelist=append(filelist,folder + "/" + file.Name())
		}
	}

}
func getFileSize(filepathname string) int64{  
	fin, err := os.Open(filepathname)
	defer fin.Close()
	if err != nil {
	    return -1
	}
	buf_len, _:=fin.Seek(0, os.SEEK_END)
	fmt.Println(buf_len)
	return buf_len
}  
func fmtFileSize(size int64) string{
	if(size<1024){
		return fmt.Sprintf("%d bytes",size)
	}else if(size<1024*1024){
		kbsize := size/1024
		return fmt.Sprintf("%d KB",kbsize)
	}else if(size<1024*1024*1024){
		mbsize := size/1024/1024
		return fmt.Sprintf("%d MB",mbsize)
	}else if(size<1024*1024*1024*1024){
		gbsize := size/1024/1024/1024
		return fmt.Sprintf("%d GB",gbsize)
	}else{
		return "size: error"
	}
}
func List_file_from_filename(filename string) string{
	fmt.Println(filename)
	html_data:=""
	zipfilenameLen1 :=len("SDT1_12202127212120282126252622229");//demo format
	dirpath := beego.AppConfig.String("download_dir") //"X:/tclbigdata/data/out"
	dirname := strings.Replace(filename, ".zip", "", -1)
	dirname = strings.Replace(dirname, ".ZIP", "", -1)
	folder :=dirpath+"/"+dirname
	if (len(dirname)== zipfilenameLen1 && (strings.Index(dirname,"SDT1_")!=0)) {
		return "error filename"
	}
	fmt.Println(folder)
	filelist=[]string{}
	ListAllFile(folder)
	if (len(filelist)==0){
		html_data="not found files"
	}
	html_data+=path.Base(folder)
	for i := 0; i < len(filelist); i++ {
	     fpath:=filelist[i]
	     fpath1:= strings.Replace(fpath, dirpath, "", -1)
	     fpath2:= strings.Replace(fpath, folder+"/", "", -1)
	     //fmt.Println(fmtFileSize(getFileSize(fpath)))
	     filesize:=fmtFileSize(getFileSize(fpath))
	     html_data+=fmt.Sprintf("<div><a href='filedownload?f=%s'>%s</a>   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>%s</span></div>",fpath1,fpath2,filesize)
	}
	return html_data
}
func init() {
}

