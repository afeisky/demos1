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
)

const (
	VERSION= "v1.0.1"
	TCL_BIGDATA ="[TCL*BIGDATA]"
	//bigdata_user:Aa123456@tcp(10.92.33.85:3306)/ans_pms_db?charset=utf8
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
	needDebug=false
	loopservice=false
	filelist = []string{}
	fileNone = 0
	fileSucc = 0
	fileFail = 0
 	data_from_path string
	data_out_path string
	run_app string
)

//sshfs -o allow_other root@10.92.33.85:/simplex/SDT/ /automount/sdt_data
// ip: 10.92.33.85, port:22 , user:root , password:SWDadmin@123    # data ssave path: /local/simplex_library/SDT/
//"mysql:  bigdata_user:Aa123456@tcp(10.92.33.85:3306)/ans_pms_db?charset=utf8")

func readme(){
	logi("Usage:", "sdt_unzip (bak_from) (out)")
	logi("VERSION:", VERSION)
	logi("    sdt_unzip  <Some SDT1_XXXX.zips' DirPath>/<SDT1_xxxx.zip's Path>")
	logi("    sdt_unzip  /disk2/tclbigdata/data/bak_from")
	logi("    sdt_unzip  /disk2/tclbigdata/data/bak_from/SDT1_32504148504701972180515951757.zip")
	logi("    sdt_unzip  /disk2/tclbigdata/data/bak_from  /disk2/tclbigdata/data/out")
	logi("    sdt_unzip  /disk2/tclbigdata/data/bak_from/SDT1_32504148504701972180515951757.zip  /disk2/tclbigdata/data/out")
}

func main() {

	whoami, ret := getCmdOutput("whoami")
	logd("whoami:", whoami)
	if (ret) {
		if (strings.Index(whoami[0:4], "root") != 0) {
			loge("Error: only root can run it!!!!")
			return
		}
	}

	///////logw(MSSTRING())

	args := os.Args
	run_app=args[0]
	for i, a := range args {
		logi("param", i, a)
		if (strings.Index(a, "debug") >= 0) {
			needDebug=true
			continue
		}
		if (strings.Index(a, "loopservice") >= 0) {
			loopservice=true
			continue
		}
		if (i==1){
			data_from_path = args[1]
		}else if(i==2){
			data_out_path = args[2]
		}
	}
    if (len(args)>1 && len(data_out_path) ==0) {
		data_from_path = args[1]
		data_out_path = path.Dir(run_app) + "/out"
	}

	logd("data_from_path=", data_from_path)
	logd("data_to_path=", data_out_path)

	if (true) {
		//return
		//os.Exit(0)
	}

	if (!fileIsExists(data_from_path)) {
		loge("not found dir/file: " + data_from_path)
		readme();
		return
	}
	if (!fileIsExists(data_out_path)) {
		getCmdOutput("mkdir", data_out_path)
		if (!fileIsExists(data_out_path)) {
			loge("not found dir : " + data_out_path)
			readme();
			return
		}
	}

	if (loopservice){
		logi("service start:--->")
		for {
			logi("service start: " + time.Now().Format("2006-01-02 15:04:05"))
			do()
			logi("service end: " + time.Now().Format("2006-01-02 15:04:05"))
			time.Sleep(10 * 1e10)
		}
		return
	}else{
		logi("--->")
		do()
	}

}
func runservice() {
	for {
		logi("service start: " + time.Now().Format("2006-01-02 15:04:05"))
		do()
		logi("service end: " + time.Now().Format("2006-01-02 15:04:05"))
		time.Sleep(10 * 1e10)
	}
}


func do(){

	isZipFile := false

	if (strings.LastIndex(data_from_path, ".zip")+len(".zip") == len(data_from_path)) {
		isZipFile = true
	}
	//exec.Command("chmod","777",filename)
	//----
	if (true) {
		//return
	}

	runtime_start := time.Now()//

	if (isZipFile) {
		unzip(data_from_path, data_out_path)
	} else {
		listFile(data_from_path, data_out_path)
	}
	hint:=""
	runtime_string:=fmt.Sprintf("Runtime: %s. ",time.Now().Sub(runtime_start))
	logfilename:=path.Dir(data_out_path)+"/"+path.Base(run_app)+".result"
	getCmdOutput("rm",logfilename)
	f, err := os.OpenFile(logfilename, os.O_CREATE|os.O_RDWR|os.O_APPEND, os.ModeAppend|os.ModePerm)
	defer f.Close()
	if err != nil {
		loge("read log file Fail!",log.Ldate|log.Ltime)
	} else {
		str:=path.Base(run_app)+"\t"+time.Now().Format("2006-01-02 15:04:05")
		per:=0
		if (len(filelist)>0){
			per=fileSucc*100/len(filelist)
		}
		hint=fmt.Sprintf("\tFile Count: %d/%d (%d%%), Fail: %d. ",fileSucc,len(filelist),per,fileFail)
		str+=hint+""+runtime_string+"\n"
		f.WriteString(str)
		logi("   ",str)
		for i := 0; i < len(filelist); i++ {
			str=fmt.Sprintf("%5d", i+1)+"\t"+filelist[i]
			f.WriteString(str+"\n")
			logi("   ",str)
		}
	}
	//log.New(f,"\r\n",log.Ldate|log.Ltime)
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

var projectname string
var productname string
var Username string
func processLineMobileinfo(line []byte) {
	sline:=string(line[:])
	logd(sline)
	if (len(sline)>=len("Project Name") && strings.Index(sline, "Project Name:")==0){
		p:=strings.Index(sline,":")
		projectname=strings.TrimSpace(sline[p+1:len(sline)])
		logw("projectname:"+projectname)
	}else if(len(sline)>=len("Product Name")&& strings.Index(sline, "Product Name:")==0){
		p:=strings.Index(sline,":")
		productname=strings.TrimSpace(sline[p+1:len(sline)])
		logw("projectname:"+productname)
	}else if(len(sline)>=len("Username") &&  strings.Index(sline, "Username:")==0){
		p:=strings.Index(sline,":")
		Username=strings.TrimSpace(sline[p+1:len(sline)])
		logw("Username:"+Username)
	}
}
func unzip(filepathname string, topath string) (){
	logw("from: ",filepathname)
	logw("to:  ",topath)
	filename_short :=path.Base(filepathname)
	errs:=""
	if (strings.Index(filename_short, ".zip") == (len(filename_short) - len(".zip"))) {
		filename_short = strings.Replace(filename_short, ".zip", "", -1) /////
	}
	logi("filename: ", filename_short)
	filename_imeidatetime := filename_short
	if (strings.Index(filename_imeidatetime, "SDT1_") == 0) {
		filename_imeidatetime = strings.Replace(filename_imeidatetime, "SDT1_", "", -1) /////
	}
	logd("filename_imeidatetime: ", filename_imeidatetime)
	result,imei,datetime,zipkey:=getImeiDatetime(filename_imeidatetime);
	logw("=:  ",result,imei,datetime)
	if (result==0){
		do_pathname:=topath+"/"+ filename_short
		logd("do_pathname:  ",do_pathname)
		if (fileIsExists(do_pathname)){
			logw("The dir is exist! Do nonthing! ",do_pathname)
			filelist =append(filelist,path.Base(filepathname)+"\t"+"[None] ")
			return
		}
		_,err:=getCmdOutput("rm","-rf",do_pathname) //,"&&","mkdir",do_pathname)
		_,err=getCmdOutput("mkdir",do_pathname)
		logw("unzip","-P",zipkey,"-x",filepathname,"-d",do_pathname)
		_,err=getCmdOutput("unzip","-P",zipkey,"-x",filepathname,"-d",do_pathname)
		err=mount_ext4(do_pathname)
		if (err){
		}else{
			errs="mount error"
		}
		_,err=getCmdOutput("chmod","777","-R",do_pathname)
		projectname=" "
		productname=" "
		Username=" "
		ReadLine(do_pathname+"/MobileInfo.txt",processLineMobileinfo)
		projectname = filenameReplace(projectname)
		dt := fmtDatetime2(datetime)  //dt := filenameReplace(datetime)
		Username = filenameReplace(Username)
		hint_file_name:=fmt.Sprintf("@~%s,%s,%s",projectname,dt,Username)
		createFile(do_pathname+"/"+hint_file_name+".sdt_unzip",hint_file_name+"\nimei:"+imei+"\ndatetime:"+datetime)
	}else{
		errs="zipkey error"
	}
	hint:=fmt.Sprintf("(%s,%s,%s)",projectname,datetime,Username)
	if (len(errs)==0){
		filelist =append(filelist,path.Base(filepathname)+"\t"+"[succ] "+hint+"")
		fileSucc++
	}else{
		filelist =append(filelist,path.Base(filepathname)+"\t"+"[FAIL] "+hint+","+errs)
		fileFail++
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
		createFile(pathname+"/"+"@~mount error,"+"[Tctpersist.ext4].sdt_unzip",outstring)
	}
	return err
}
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
func getImeiDatetime(imei_datetime string) ( int, string ,string, string){
	if (strings.Index(imei_datetime, "SDT1_") == 0) {
		imei_datetime = strings.Replace(imei_datetime, "SDT1_", "", -1) /////
	}
	if (strings.Index(imei_datetime, ".zip") == (len(imei_datetime) - len(".zip"))) {
		imei_datetime = strings.Replace(imei_datetime, ".zip", "", -1) /////
	}
	logw("imei_datetime:  ", imei_datetime)
	var imei string     //[16]byte
	var datetime string //[16]byte
	for i := 0; i < len(imei_datetime); i++ {
		if (i%2 == 0) {
			imei += imei_datetime[i : i+1];
		} else {
			datetime += imei_datetime[i : i+1];
		}
	}
	zipkey := imei[0:3] + imei[len(imei) - 3: len(imei)];
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

func listFile(folder string,topath string) {
	zipfilenameLen1 :=len("SDT1_12202127212120282126252622229.zip");//demo format
	logw("-->",folder)
	files, _ := ioutil.ReadDir(folder)
	for _, file := range files {
		if (!file.IsDir()) {
			fname := file.Name();
			if (len(fname)== zipfilenameLen1 && (strings.Index(fname,"SDT1_")==0) &&
				(strings.LastIndex(fname,".zip")==len(fname)-len(".zip"))) {
				//logw(folder + "/" + file.Name())
				logw(file.Name(), "------------------>>>")
				//reset var:
				unzip(folder+"/"+file.Name(),topath)
				logw(file.Name(), "------------------<<<")
			}
		}
	}
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
