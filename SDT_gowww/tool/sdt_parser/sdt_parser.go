package main

import (
	"fmt"
	"os"
	"log"
	"strings"
	"bufio"
	"io"
	"os/exec"
	"io/ioutil"
	"bytes"
	"encoding/json"
	"runtime"
	"path"
	"time"
	"path/filepath"
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
)

const (
	VERSION= "v1.0.2"
	TCL_BIGDATA ="[TCL*BIGDATA]"
	//bigdata_user:Aa123456@tcp(10.92.33.85:3306)/ans_pms_db?charset=utf8
	/*
	MS101="big"
	MS102="data"
	MS103="_"
	MS104="user"
	MS201=":A"
	MS202="a"
	MS203="1234"
	MS204="56"
	MS205="@tcp("
	MS301="10.92.33."
	MS302="8"
	MS303="5"
	MS401=":330"
	MS402="6"
	MS403=")/"
	MS501="ans_"
	MS502="pms_d"
	MS503="b"
	MS504="?"
	MS6="charset=utf8"
*/
	MS101="ch"
	MS102="aofei"
	MS103="_"
	MS104="wu"
	MS201=":"
	MS202="ch"
	MS203="aofei"
	MS204="123"
	MS205="@tcp("
	MS301="10.92.34."
	MS302="5"
	MS303="7"
	MS401=":330"
	MS402="8"
	MS403=")/"
	MS501="tcl"
	MS502="sys"
	MS503="db"
	MS504="?"
	MS6="charset=utf8"
)

func MSSTRING() string{
	msstring:=""
	msstring+=MS101
	msstring+=MS102
	msstring+=MS103
	msstring+=MS104
	msstring+=MS201
	msstring+=MS202
	msstring+=MS203
	msstring+=MS204
	msstring+=MS205
	msstring+=MS301
	msstring+=MS302
	msstring+=MS303
	msstring+=MS401
	msstring+=MS402
	msstring+=MS403
	msstring+=MS501
	msstring+=MS502
	msstring+=MS503
	msstring+=MS504
	msstring+=MS6
	return msstring
}
var (
	needDebug  =false
	needSave2db  =false
	needForce	=false
	ishere	=false
	filelist                      = []string{}
	fileSucc                      = 0
	fileFail                      = 0
	run_pathfile  string
 	run_path      string
	work_path     string
	cmdlist       []string
	g_projectname string
	g_productname string
	g_username    string
	g_imei       string
	g_datetime    string
	g_zipkey  	string
)

//sshfs -o allow_other root@10.92.33.85:/simplex/SDT/ /automount/sdt_data
// ip: 10.92.33.85, port:22 , user:root , password:SWDadmin@123    # data ssave path: /local/simplex_library/SDT/
//"mysql:  bigdata_user:Aa123456@tcp(10.92.33.85:3306)/ans_pms_db?charset=utf8")

func readme(){
	logi("Usage:", "sdt_parser <save2db> <debug> <force>")
	logi("VERSION:", VERSION)
	logi("    sdt_parser  /disk2/tclbigdata/data/out")
	logi("    sdt_parser  /disk2/tclbigdata/data/out/SDT1_32504148504701972180515951757")
	logi("    param <force>:  force to parse dir data.")
}

func main() {

	/*
	whoami, ret := getCmdOutput("whoami")
	logw("whoami:", whoami)
	if (ret) {
		if (strings.Index(whoami[0:4], "root") != 0) {
			loge("Error: only root can run it!!!!")
			//return
		}
	}
	*/

	///////logw(MSSTRING())
	args := os.Args
	logi("-------------->")
	for i, a := range args {
		if (strings.Index(a, "debug") >= 0) {
			needDebug=true
			logi("debug=true")
			continue
		}
		if (strings.Index(a, "save2db") >= 0) {
			needSave2db=true
			logi("save2db=true")
			continue
		}
		if (strings.Index(a, "force") >= 0) {
			needForce=true
			logi("force=true")
			continue
		}
		if (strings.Index(a, "here") >= 0) {
			ishere=true
			logi("here=true")
			continue
		}
		logi("param ", i, a)
		if (i==1){
			work_path = args[1]
		}
	}
	//logd("param len", len(args))
	if len(args) <=1 || len(work_path)==0 {
		readme()
		return
	}
	pathfile,err := filepath.Abs(args[0])
	run_pathfile=pathfile
	logd("run_pathfile: ", run_pathfile)
	run_path = path.Dir(run_pathfile)
	logd("run_path: ", run_path)
	logd("work_path: ", work_path)
	file_settings_sys:= run_path +"/sdt_parser.ini"
	if (!fileIsExists(file_settings_sys)){
		loge("Not found settings file: "+file_settings_sys)
		return
	}
	logd("file_settings_sys=", file_settings_sys)

	if (true) {
		//return
		//os.Exit(0)
	}

	ReadLine(file_settings_sys, processSettingsiniLine)
	if !checkCmd(){
		loge("Error: some run files not found!")
		return
	}
	isOneDir := false
	dirnamelen:=len("SDT1_32504148504701972180515951757")
	dirname:=path.Base(work_path)
	loge("path_dir-->",dirname)
	if (len(dirname)==dirnamelen && strings.Index(dirname, "SDT1_")==0) {
		isOneDir = true
	}
	//exec.Command("chmod","777",filename)
	//----
	if (true) {
		//return
	}

	runtime_start := time.Now()//

	if (ishere) {
		logd("isOneDir here-->")
		succ:=parseFiles(work_path)
		if (!succ){
			loge("ERROR: ",work_path)
			return;
		}
		return
	}
	if (isOneDir ) {
		logd("isOneDir-->")
		succ:=parseFiles(work_path)
		if (!succ){
			loge("ERROR: Not data files in this Dir: ",work_path)
			return;
		}
	} else {
		logd("list file---->")
		succ:=listFile(work_path)
		if (!succ){
			loge("ERROR: Not SDT1_... dir: ",work_path)
			return;
		}
	}

	hint:=""
	runtime_string:=fmt.Sprintf("Runtime: %s. ",time.Now().Sub(runtime_start))
	logfilename:= run_path +"/"+path.Base(run_pathfile)+".result"
	getCmdOutput("rm",logfilename)
	f, err := os.OpenFile(logfilename, os.O_CREATE|os.O_RDWR|os.O_APPEND, os.ModeAppend|os.ModePerm)
	defer f.Close()
	if err != nil {
		loge("read log file Fail!",log.Ldate|log.Ltime)
	} else {
		str:=path.Base(args[0])+"\t"+time.Now().Format("2006-01-02 15:04:05")
		per:=0
		if (len(filelist)>0){
			per=fileSucc*100/len(filelist)
		}
		hint=fmt.Sprintf("\tFile Count: %d/%d (%d%%), Fail: %d. ",fileSucc,len(filelist),per,fileFail)
		str+=hint+""+runtime_string+"\n"
		f.WriteString(str)
		for i := 0; i < len(filelist); i++ {
			f.WriteString(fmt.Sprintf("%5d", i+1)+"\t"+filelist[i]+"\n")
		}
	}
	//log.New(f,"\r\n",log.Ldate|log.Ltime)
	if (isOneDir) {
	} else {
		getCmdOutput(run_path+"/"+"sdt_parser_finish.py",run_path,work_path)
	}
	loga()
	logi("[Done]!!! ",runtime_string)
	logi("  log file: " + logfilename,",",hint)
}

func createFile(filepathname string,comment string){
	f, err := os.OpenFile(filepathname, os.O_CREATE|os.O_RDWR|os.O_APPEND, os.ModeAppend|os.ModePerm)
	defer f.Close()
	if err != nil {
		loge("Fail to write file !",filepathname)
	} else {
		f.WriteString(comment)
	}
}


func parser(pathdir string) (){
	logd("pathdir: ",pathdir)
}
func processSettingsiniLine(line []byte) {
	sline:=string(line[:])
	//logd(line[:])
	//logd(sline)
	if (len(sline)>=len("version") && sline[0:len("version")]=="version"){
		logd("version:")
	}else if (strings.Index(sline,"cmd=")>=0 && strings.Index(sline,"cmd=")<5){
		p:=strings.Index(sline,"cmd=")
		ss:=strings.TrimSpace(sline[p+len("cmd="):len(sline)])
		//ss = strings.Replace(ss, "\n", "", -1)  //
		cmdlist=append(cmdlist, ss)
		logd("-->cmdlist:["+ss+"]")
	}
}
func listFile(folder string) bool{
	zipfilenameLen1 :=len("SDT1_12202127212120282126252622229");//demo format
	files, _ := ioutil.ReadDir(folder)
	n:=0
	for _, file := range files {
		if (file.IsDir()) {
			dirname := file.Name();
			if (len(dirname)== zipfilenameLen1 && (strings.Index(dirname,"SDT1_")==0)) {
				//logw(folder + "/" + file.Name())
				logd(file.Name(), "------------------>>>")
				//reset var:
				//getFile(data_from_dir, file.Name());
				parseFiles(folder+"/"+file.Name())
				n++
				logw(file.Name(), "------------------<<<")
			}
		}
	}
	if (n==0){
		return false
	}
	return true
}
func checkCmd() bool {
	ret:=true
	for _, value := range cmdlist {
		//logw(value)
		cmd := strings.SplitN(value, ",", -1)
		if (len(cmd)<3) {
			continue;
		}
		if (len(cmd[2])>10){
			cmd[2]=cmd[2][0:10]
		}
		if (!fileIsExists(run_path +"/"+cmd[0])){
			loge(cmd[0],"\t[Fail]\tFile not found."); //--> sdt_mobileinfo.py MobileInfo.txt mobileinfo
			ret=false
		}else{
			logi(cmd[0],"\t[Ok]"); //--> sdt_mobileinfo.py MobileInfo.txt mobileinfo
		}
	}
	return ret
}

func parseFiles(destPath string) bool{
	errs:=""
	logd("destDir: ", destPath)
	filename_short :=path.Base(destPath)
	//filename_zip:=filename_short+".zip"
	//if (strings.Index(filename_short, ".zip") == (len(filename_short) - len(".zip"))) {
	//	filename_short = strings.Replace(filename_short, ".zip", "", -1) /////
	//}
	logi("filename: ", filename_short)

	filename_imeidatetime := filename_short
	if (strings.Index(filename_imeidatetime, "SDT1_") == 0) {
		filename_imeidatetime = strings.Replace(filename_imeidatetime, "SDT1_", "", -1) /////
	}
	result:=0
	result,g_imei,g_datetime,g_zipkey=getImeiDatetime(filename_imeidatetime);
	logd("=:  ",result,g_imei,g_datetime,g_zipkey)

	ReadLine(destPath+"/MobileInfo.txt",processLineMobileinfo)
	dt:=fmtDatetime2(g_datetime) //dt:=filenameReplace(g_datetime)
	hint_file_name:=fmt.Sprintf("@~%s,%s,%s",g_projectname,dt,g_username)
	hint_filepathname_1:=destPath+"/"+hint_file_name+"_[succ].sdt_parser"
	hint_filepathname_2:=destPath+"/"+hint_file_name+"_[FAIL].sdt_parser"
	logd(hint_filepathname_1)
	if (fileIsExists(hint_filepathname_1) || fileIsExists(hint_filepathname_2)){
		if (!needForce) {
			filelist =append(filelist,path.Base(destPath)+"\t"+"[NONE]")
			return false
		}
		getCmdOutput("rm",hint_filepathname_1)
		getCmdOutput("rm",hint_filepathname_2)
	}
	outstr_head:=fmt.Sprintf("%s   %s   %s", time.Now().Format("2006-01-02 15:04:05"), run_pathfile, VERSION)
	for i, value := range cmdlist {
		//logw(value)
		cmd := strings.SplitN(value, ",", -1)
		if (len(cmd)<3) {
			continue;
		}
		if (len(cmd[2])>10){
			cmd[2]=cmd[2][0:10]
		}
		str:=fmt.Sprintf("--> %d/%d %s %s %s", i+1,len(cmdlist),cmd[0],cmd[1],cmd[2])
		logd(str); //--> sdt_mobileinfo.py MobileInfo.txt mobileinfo
		outstr:=fmt.Sprintf("--> %d/%d %s %s %s %s", i+1,len(cmdlist),run_path+"/"+cmd[0],destPath,cmd[1],cmd[2])
		jdata,ret:=parseCmdJson(run_path+"/"+cmd[0],destPath,cmd[1],cmd[2],"");
		if (ret){
			logd("=" + jdata)
		}else { //error
			errs+=jdata
			jdata="{\"comment\":\" "+jdata+"\", \"result\": 0}"
			logw("=" + jdata+"\n")
		}
		if (i>0){
			outstr="\n\n"+outstr
		}else{
			outstr=outstr_head+"\n"+outstr
		}
		outstr+="\n"+jdata
		createFile(hint_filepathname_1,outstr)
		if (needSave2db) {
			save2db(jdata, cmd[2], filename_short+".zip")
		}
	}

	hint:=fmt.Sprintf("(%s,%s,%s)", g_projectname,g_datetime, g_username)
	if (len(errs)==0){
		filelist =append(filelist,path.Base(destPath)+"\t"+"[succ] "+hint+"")
		fileSucc++
	}else{
		filelist =append(filelist,path.Base(destPath)+"\t"+"[FAIL] "+hint+","+errs)
		fileFail++
		if (fileIsExists(hint_filepathname_1)){
			logd(hint_filepathname_2)
			getCmdOutput("mv",hint_filepathname_1,hint_filepathname_2)
		}
		return true
	}
	return true
}

func save2db(jdata string,key string,filename string){
	intype:=key //"mobileinfo" // max size 10 chars
	if (len(intype)>10){
		intype=intype[0:10]
	}
	inimei:=g_imei
	filetime:=g_datetime //"2018-05-08 17:05:21"
	//jdata:=//"{\"aa\":22}"  // max size 6000 chars
	jfilesave:="{\"from\":\""+filename+"\"}" // max size 200 chars
	logw("save2db():",intype) // max size 10 chars
	logw("save2db():",inimei)
	logw("save2db():",filetime)
	logw("save2db():",jdata) // max size 6000 chars
	logw("save2db():",jfilesave) // max size 200 chars
	if (needSave2db) {
		loge("save2db --->") //,"my"+"sql", MSSTRING())
		//os.Exit(0)
		db, err := sql.Open("my"+"sql", MSSTRING())
		if err != nil {
			panic(err)
			return
		}
		handle, err := db.Prepare("CALL bd_json_in(?, ?, ?, ?, ?,?)")
		if err != nil {
			panic(err.Error())
		}
		defer handle.Close()
		//call procedure
		var result sql.Result
		logw("=", filename, intype, inimei, filetime, jdata, jfilesave)
		result, err = handle.Exec(filename, intype, inimei, filetime, jdata, jfilesave)
		if err != nil {
			//panic(err.Error())
			loge(err.Error())
		}
		logw(result)
		defer db.Close()
	}
}
func processLineMobileinfo(line []byte) {
	sline:=string(line[:])
	logd(sline)
	if (len(sline)>=len("Project Name") && strings.Index(sline, "Project Name:")==0){
		p:=strings.Index(sline,":")
		g_projectname =strings.TrimSpace(sline[p+1:len(sline)])
		logw("g_projectname:"+ g_projectname)
	}else if(len(sline)>=len("Product Name")&& strings.Index(sline, "Product Name:")==0){
		p:=strings.Index(sline,":")
		g_productname =strings.TrimSpace(sline[p+1:len(sline)])
		logw("g_projectname:"+ g_productname)
	}else if(len(sline)>=len("Username") &&  strings.Index(sline, "Username:")==0){
		p:=strings.Index(sline,":")
		g_username =strings.TrimSpace(sline[p+1:len(sline)])
		logw("g_username:"+ g_username)
	}
}

func parseCmdJson(cmd_filename string,doDir string,do_filename string,jsonKey string,param string) (string,bool) {
	if (len(cmd_filename) > 0 && fileIsExists(cmd_filename)) {
	} else {
		loge("File not found: " + cmd_filename)
		return "File not found: " + path.Base(cmd_filename),false
	}
	filename := doDir + "/" + do_filename;
	if (len(filename) > 0 && fileIsExists(filename)) {
	} else {
		loge("File not found: " + do_filename)
		return "File not found: " + do_filename,false
	}
	logd("cmd:"+cmd_filename, filename);
	str := getRunCommandData(cmd_filename, filename);
	logd("parseCmdJson():",str)
	if (true){
		return str,true
	}else {
		var dat map[string]interface{} //var dat map[string]interface{}
		if err := json.Unmarshal([]byte(str), &dat); err == nil {
			//logd(dat)
			result := dat["result"].(float64)
			if (result == 1) {
				data := dat["data"]
				logd("parseCmdJson():",data)
				return data.(string),true
			} else {
				//data:=dat["comment"]
				logw("parseCmdJson():"+dat["comment"].(string)+" [ "+cmd_filename, filename, ""+" ]\n");
				return "",false //data.(string)
			}
		} else {
			loge(err)
			loge("parseCmdJson():",err);
			return "",false
		}
	}
}
func filenameReplace(filename string) string{
	//filename not chars:	\ / : * ? # ” < > |
	filename = strings.Replace(filename, "\\", "", -1)  // \
	filename = strings.Replace(filename, "/", "", -1)
	filename = strings.Replace(filename, "*", "", -1)
	filename = strings.Replace(filename, ":", "", -1)
	filename = strings.Replace(filename, "?", "", -1)
	filename = strings.Replace(filename, "#", "", -1)
	filename = strings.Replace(filename, "<", "", -1)
	filename = strings.Replace(filename, ">", "", -1)
	filename = strings.Replace(filename, "|", "", -1)
	return filename
}
/*
func mount_ext4(pathname string) (bool){
	f_tctpersist:=pathname+"/Tctpersist.ext4"
    d_tctpersist:=pathname+"/Tctpersist"
    m_tctpersist:=pathname+"/Tctpersist~" //"mount_ext4"
	logw("f_tctpersist:  ",f_tctpersist)
	logw("d_tctpersist:  ",d_tctpersist)
	logw("m_tctpersist:  ",m_tctpersist)
    if (fileIsExists(m_tctpersist)){
		getCmdOutput("umount",m_tctpersist)
		getCmdOutput("rm","-rf",m_tctpersist)
	}
	if (!fileIsExists(m_tctpersist)){
		getCmdOutput("mkdir",m_tctpersist)
	}
	if (!fileIsExists(d_tctpersist)){
		//getCmdOutput("mkdir",d_tctpersist)
	}
	outstring,err:=getCmdOutput("mount",f_tctpersist,m_tctpersist)
	logw("cp","-r",m_tctpersist+"/",d_tctpersist)
	getCmdOutput("cp","-r",m_tctpersist+"/",d_tctpersist)
	getCmdOutput("umount",m_tctpersist)
	getCmdOutput("rm","-rf",m_tctpersist)
	if (!err){
		createFile(pathname+"/"+"@~mount error,"+"[Tctpersist.ext4]",outstring)
	}
	return err
}
*/
func getCmdOutput(c string,arg ...string) (string,bool) {
	//cmd := exec.Command("/w2/chaofei/tclbigdata/sdt/sdt_filename_parser","12202127212121212120202020269")
	cmd := exec.Command(c, arg...)
	cmd.Stdin = strings.NewReader("")
	var out bytes.Buffer
	cmd.Stdout = &out
	err := cmd.Run()
	if err != nil {
		loge(err)
		//log.Fatal(err)
		logw(out.String())
		logw("cmd:",c)
		return "",false
	}
	return out.String(),true
}


func fmtDatetime2(t string) string{
	fmt_datetime:="2006-01-02 15:04:05"
	stime1, _ := time.Parse(fmt_datetime, t)
	fmt_datetime="2006-01-02_150405"
	stime:=stime1.Format(fmt_datetime)
	return stime
}
func fmtDatetime(t string) string{
	fmt_datetime:="20060102150405"
	stime1, _ := time.Parse(fmt_datetime, t)
	fmt_datetime="2006-01-02 15:04:05"
	stime:=stime1.Format(fmt_datetime)
	return stime
}
func getImeiDatetime(imei_datetime string) ( int, string ,string, string) {
	logw("imei_datetime 1:  ", imei_datetime)
	if (strings.Index(imei_datetime, "SDT1_") == 0) {
		imei_datetime = strings.Replace(imei_datetime, "SDT1_", "", -1) /////
	}
	if (strings.Index(imei_datetime, ".zip") == (len(imei_datetime) - len(".zip"))) {
		imei_datetime = strings.Replace(imei_datetime, ".zip", "", -1) /////
	}
	logw("imei_datetime 2:  ", imei_datetime)
	if ( len(imei_datetime)!=len("32504128403800960107203715549") ){
		return 0,"","","" ////
	}
	var imei string     //[16]byte
	var datetime string //[16]byte
	for i := 0; i < len(imei_datetime); i++ {
		if (i%2 == 0) {
			imei += imei_datetime[i : i+1];
		} else {
			datetime += imei_datetime[i : i+1];
		}
	}
	zipkey :=""
	if (len(imei) > 0) {
		zipkey = imei[0:3] + imei[len(imei)-3:len(imei)];
	}
	//
	datetime=fmtDatetime(datetime)

	logw("imei:  ", imei)
	logw("datetime:  ", datetime)
	//logw("%s\n",TCL_RESULT_KEY);
	//logw("{\"result\":1,\"imei\":\"%s\",\"time\":\"%s\"}", imei, datetime);
	//logw("\n");

	//print json string
	type jsonR struct {
		Result   int    `json:"result"`
		Imei     string `json:"imei"`
		Datetime string `json:"datetime"`
		Zipkey string `json:"zipkey"`
	}
	result:=1
	if (len(imei) == 15) {
		result=0
	}
	json1 := jsonR{result, imei, datetime,zipkey}
	b, err := json.Marshal(json1)
	if err != nil {
		loge(err,imei_datetime)//log.Fatalln(err)
	}else {
		var out bytes.Buffer
		err = json.Indent(&out, b, "", "\t")
		if err != nil {
			loge(err,imei_datetime) //log.Fatalln(err)
		}
	}
	//out.WriteTo(os.Stdout)
	return result, imei, datetime,zipkey
}

func getRunCommandData(c string,arg ...string) (string) {
	//cmd := exec.Command("/wcf/tclbigdata/sdt/sdt_filename_parser","12202127212121212120202020269")
	cmd := exec.Command(c, arg...)
	cmd.Stdin = strings.NewReader("")
	var out bytes.Buffer
	cmd.Stdout = &out
	err := cmd.Run()
	if err != nil {
		loge(err)//log.Fatal(err)
		return ""
	}
	//logd(out.String())
	data := out.String()
	p := strings.LastIndex(data, TCL_BIGDATA)
	if (p >= 0) {
		data = data[p+len(TCL_BIGDATA) : len(data)]
		data=strings.Replace(data,"\n","",-1)  /////
		data=strings.Replace(data,"	","",-1)
		data=strings.Replace(data,"	,}","}",-1)
		logd(data)
		//logw("getRunCommandData()",data)
	}else{
		loge("getRunCommandData()","not found key:"+TCL_BIGDATA+"!")
		data =""
	}
	return data
}


func ReadLine(filePth string, hookfn func([]byte)) error {
	f, err := os.Open(filePth)
	if err != nil {
		return err
	}
	defer f.Close()

	bfRd := bufio.NewReader(f)
	for {
		line, err := bfRd.ReadBytes('\n')
		hookfn(line)
		if err != nil {
			if err == io.EOF {
				return nil
			}
			return err
		}
	}
	return nil
}
func fileIsExists(filePath string) (bool) {
	_, err := os.Stat(filePath)
	if err == nil {
		return true }
	if os.IsNotExist(err) {
		return false }
	return true
}

//-------
func loge( a ...interface{}) {
	fmt.Println("[E]: ", a)
	//os.Stdout.Write("ERROR: "+ format)
}

func logw( a ...interface{}){
	if (needDebug) {
		fmt.Println("[W]: ", a)
	}
}
func logd(a ...interface{}){
	if (needDebug) {
		fmt.Println("[D]: ", a)
	}
}
func logi(a ...interface{}){
	fmt.Println("[I]: ",a)
}
func loga(){
	fmt.Println("[I]: ")
}
//--------
func __func__(){
	pc,file,line,ok := runtime.Caller(2)
	log.Println(pc)
	log.Println(file)
	log.Println(line)
	log.Println(ok)
	f := runtime.FuncForPC(pc)
	log.Println(f.Name())
}
func pipe_demo() {
	generator := exec.Command("cmd1")
	consumer := exec.Command("cmd2")
	pipe, err := consumer.StdinPipe()
	generator.Stdout = pipe
	logd(err)
}