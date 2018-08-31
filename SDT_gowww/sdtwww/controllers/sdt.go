package controllers

import (
	"github.com/astaxie/beego"
	"github.com/beego/wuchaofei/sdtwww/models"
	"fmt"
	"strings"
)

type Sdt1Controller struct {
	beego.Controller
}

func (this *Sdt1Controller) Get() {
	path := this.GetString("p")
	fname := this.GetString("filename")
	if (strings.Index(path, "1")== 0 && len(path)>0 && len(fname)>0){
		r := this.GetString("r")
		lifetime,filename,filetime,imei1,imei2,productname,idcard,perso_system,bt_mac,wifi_mac,commercial_ref,fec_record,productmodel,used_time,sim_time,wifi_time,pcba_sn,mini_ver,bt_addr,wifi_addr,system_ver,username,tool,rooted,battery_v,update_time,parser_mail,parser_text,parser_time,parser_history,parser_step,arr_lkla,arr_bootreason:=models.Sql_detail_filename(fname)
		//fmt.Println("-->"+filename+filetime+imei1+imei2+productname+idcard+perso_system+bt_mac+wifi_mac+commercial_ref+fec_record+productmodel+used_time+sim_time+wifi_time+pcba_sn+mini_ver+bt_addr+wifi_addr+system_ver+username+tool+rooted+battery_v+update_time+parser_mail+parser_text+parser_time+parser_history+parser_step)
		//fmt.Println("-->"+fec_record+productmodel+used_time+sim_time+wifi_time+pcba_sn+mini_ver+bt_addr+wifi_addr+system_ver+username+tool+rooted+battery_v+update_time+parser_mail+parser_text+parser_time+parser_history+parser_step)
		fmt.Println("-->"+filename)
		//fmt.Println("-->"+bt_addr)
		this.Data["filename"]=filename
		this.Data["filetime"]=filetime
		this.Data["imei1"]=imei1
		this.Data["imei2"]=imei2
		this.Data["productname"]=productname
		this.Data["idcard"]=idcard
		this.Data["perso_system"]=perso_system
		this.Data["bt_mac"]=bt_mac
		this.Data["wifi_mac"]=wifi_mac
		this.Data["commercial_ref"]=commercial_ref
		this.Data["fec_record"]=fec_record
		this.Data["lifetime"]=lifetime
		this.Data["productmodel"]=productmodel
		this.Data["used_time"]=used_time
		this.Data["sim_time"]=sim_time
		this.Data["wifi_time"]=wifi_time
		this.Data["pcba_sn"]=pcba_sn
		this.Data["mini_ver"]=mini_ver
		this.Data["bt_addr"]=bt_addr
		this.Data["wifi_addr"]=wifi_addr
		this.Data["system_ver"]=system_ver
		this.Data["username"]=username
		this.Data["tool"]=tool
		this.Data["rooted"]=rooted
		this.Data["battery_v"]=battery_v
		this.Data["update_time"]=update_time
		this.Data["parser_mail"]=parser_mail
		this.Data["parser_text"]=parser_text
		this.Data["parser_time"]=parser_time
		this.Data["parser_history"]=parser_history
		this.Data["parser_step"]=parser_step
		this.Data["arr_lkla"]=arr_lkla
		this.Data["arr_bootreason"]=arr_bootreason
		this.Data["success_text"]=r
		this.TplName = "sdt_detail.tpl"
		return
	}else if (strings.Index(path, "2")== 0 && len(path)>0 && len(fname)>0){
		str:=models.List_file_from_filename(fname)
		this.Data["Hdata"] = str
		//fmt.Print(str)
		this.TplName = "sdt_dir.tpl"
		return
	}
	str:=models.Sql_data("","")
	this.Data["Hdata"] = str
	//fmt.Print(str)
	this.TplName = "sdt.tpl"
}
func (this *Sdt1Controller) Post() {
	query_type := this.GetString("query_type")
	query_text := this.GetString("query_text")
	str:=models.Sql_data(query_type,query_text)
	this.Data["Hdata"] = str
	//fmt.Print(str)
	this.TplName = "sdt.tpl"
}

func (this *Sdt1Controller) Download() {
	filename := this.GetString("f")
	fmt.Println(filename)
	if(len(filename)>0){
	   //this.Ctx.Output.Download("download"+filename)
	   this.Ctx.Output.Download("static/download/"+filename)
	}
        //this.Ctx.Output.Download("static/images/a.jpg","aaa.jpg")

}


func (this *Sdt1Controller) ParserText() {
	p := this.GetString("p")
	filename := this.GetString("filename")
	parser_text := this.GetString("text")
	parser_mail := this.GetString("mail")
	fmt.Print(p+","+filename+","+parser_text+"===")
	models.Update_text(filename,parser_text,parser_mail)
	url:="/?p=1&filename="+filename+"&r=sucess"
	fmt.Print(url)
	this.Ctx.Redirect(302,url)
}
