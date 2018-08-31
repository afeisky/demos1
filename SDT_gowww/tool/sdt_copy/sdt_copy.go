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
	"runtime"
	"flag"
)



const (
	VERSION= "v1.0.1"
	TCL_BIGDATA ="[TCL*BIGDATA]"
	//bigdata_user:Aa123456@tcp(10.92.33.85:3306)/ans_pms_db?charset=utf8

	MS6="charset=utf8"
)

var (
	copyForce=false
	needDebug=false

	alldata map[string]interface{}
	g_imei1=""
	g_imei2=""
	g_time=""
	cmdlist []string
	param  = flag.String("save2db", "", "save2db: input json to mysql database table.")  //flag.PrintDefaults()
)

//sshfs -o allow_other root@10.92.33.85:/local/simplex_library/SDT/ /automount/sdt_data
// ip: 10.92.33.85, port:22 , user:root , password:SWDadmin@123    # data ssave path: /local/simplex_library/SDT/
//"mysql:  bigdata_user:Aa123456@tcp(10.92.33.85:3306)/ans_pms_db?charset=utf8")

func scp_file_from_server(){

}

func main() {
	///////logw(MSSTRING())
	args := os.Args
	var data_from_path string
	var data_to_path string

	if len(args) >= 2 {
		data_from_path= args[1]
		data_to_path=args[2]
	} else {
		logw("usage: sdt_copy <data_from_path> <data_to_path>")
		logw("usage: sdt_copy /automount/ssh_sdt_data /disk2/tclbigdata/data/bak_from")
	}

	for i,a:= range args {
		if (strings.Index(a, "force") >= 0) {
			logw("param: save2db. # need save to database.")
			copyForce = true
		}
		logw("param",i,a)
	}
	logw("data_from_path=",data_from_path)
	logw("data_to_path=",data_to_path)

	if (true){
		//os.Exit(0)
	}

	if (!fileIsExists(data_from_path)){
		loge("not found Dir! ["+data_from_path+"]")
		return
	}
	if (!fileIsExists(data_to_path)){
		loge("not found Dir! ["+data_to_path+"]")
		return
	}
	listFile(data_from_path,data_to_path)
}

func getCmdOutput(c string,arg ...string) (string,bool) {
	//cmd := exec.Command("/wcf/tclbigdata/sdt/sdt_filename_parser","12202127212121212120202020269")
	cmd := exec.Command(c, arg...)
	cmd.Stdin = strings.NewReader("")
	var out bytes.Buffer
	cmd.Stdout = &out
	err := cmd.Run()
	if err != nil {
		loge(err)
		log.Fatal(err)
		logd(out.String())
		return "",false
	}
	return out.String(),true
}

func listFile(fromFolder string,toFolder string) {
	logw("------------------>>>")
	zipfilenameLen1 :=len("SDT1_12202127212120282126252622229.zip");//demo format
	files, _ := ioutil.ReadDir(fromFolder)
	total := 0
	success_count := 0
	fail_count := 0
	for _, file := range files {
		if (!file.IsDir()) {
			fname := file.Name();

			if (len(fname)== zipfilenameLen1 && (strings.Index(fname,"SDT1_")==0) &&
				(strings.LastIndex(fname,".zip")==len(fname)-len(".zip"))) {
				//logw(fromFolder + "/" + file.Name())
				total++
				//reset var:
				//if (strings.Index(fname, "SDT1_52505158505751505158505253548.zip") == -1) {
				//	continue
				//}
				if (fileIsExists(toFolder+"/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/done/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/Done/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/DONE/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/2018/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/2019/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/2020/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/2021/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				if (fileIsExists(toFolder+"/2022/"+file.Name())) {
					continue
					//if (sameFile(fromFolder, file.Name(), toFolder)){
					//	continue
					//}
				}
				success := copyFile(fromFolder, file.Name(), toFolder);
				if (success) {
					success_count++
					logw(file.Name(), "   Succ")
				} else {
					fail_count++
					loge(file.Name(), "   FAIL!")
				}
			}
		}
	}
	if (total>0) {
		logw("Success:",success_count, ", Fail:",fail_count, ", Total:",total, " [", (success_count+fail_count)*100/total, "% ]",);
	}else{
		logw("File Total: 0");
	}
	logw("------------------<<<")

}
func copyFile(fromFolder string,filename string,toFolder string) (bool) {
	from:=fromFolder+"/"+filename
	to:=toFolder+"/"
	cmd := exec.Command("cp",from,to)
	cmd.Stdin = strings.NewReader("")
	var out bytes.Buffer
	cmd.Stdout = &out
	err := cmd.Run()
	if err != nil {
		loge(err)
		log.Fatal(err)
		logd(out.String())
		return false
	}
	//logd(out.String())
	return true
}
func getMd5sum(filepathname string) (string) {
	md5sum1,ret1:=getCmdOutput("md5sum",filepathname)
	if (ret1){
		n:=strings.Index(md5sum1, " ");
		if (n>10) {
			return md5sum1[0:n]
		}
	}
	return ""
}
func sameFile(fromFolder string,filename string,toFolder string) (bool) {
	from:=fromFolder+"/"+filename
	to:=toFolder+"/"+filename
	md1:=getMd5sum(from)
	if (len(md1)>0){
		md2:=getMd5sum(to)
		if (len(md2)==len(md1) && strings.Index(md1, md2)==0){
			return true
		}
	}
	return false
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
func loge( a ...interface{}){
	fmt.Println("[E]: ", a)
	//os.Stdout.Write("ERROR: "+ format)
}

func logw( a ...interface{}){
	fmt.Println("[W]: ", a)
}
func logd(a ...interface{}){
	if (needDebug) {
		fmt.Println("[D]: ", a)
	}
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
