package main

import (
	"strings"
	"time"
	"os"
	"fmt"
	"runtime"
	"log"
	"os/exec"
	"bytes"
	"path"
	"bufio"
	"io"
	"path/filepath"
)
var (
	needDebug=true
	needSave2db=false
	data_from_path string
	data_out_path string
)

func readme(){
	loga()
	logi("!!! ./sdt_loopcheckfile  /disk2/tclbigdata/data/bak_from   /disk2/tclbigdata/data/out")
	logi("!!! need exe_program: sdt_unzip & sdt_parser")
}

func main() {
	args := os.Args
	whoami, ret := getCmdOutput("whoami")
	logd("whoami:", whoami)
	if (ret) {
		if (strings.Index(whoami[0:4], "root") != 0) {
			loge("Error: only root can run it!!!!")
			return
		}
	}
	for i, a := range args {
		if (strings.Index(a, "debug") >= 0) {
			needDebug=true
			logi("debug=true")
		}
		if (strings.Index(a, "save2db") >= 0) {
			needSave2db=true
			logi("needSave2db=true")
		}
		logd("param", i, a)
	}

	if len(args)>2 {
		data_from_path=args[1]
		data_out_path=args[2]
	}else {
		data_from_path = "/disk2/tclbigdata/data/bak_from"
		data_out_path = "/disk2/tclbigdata/data/out"
	}

	logd("data_from_path=", data_from_path)
	logd("data_to_path=", data_out_path)

	if (true) {
		//return
		//os.Exit(0)
	}

	if (!fileIsExists(data_from_path)) {
		loge("not found dir/file: " + data_from_path)
		readme()
		return
	}
	if (!fileIsExists(data_out_path)) {
		loge("not found dir : " + data_out_path)
		readme()
		return
	}
	run_dir:=path.Dir(data_from_path)
	logi("run_dir=",run_dir)
	sdt_copy:=run_dir+"/sdt_copy"
	sdt_unzip:=run_dir+"/sdt_unzip"
	sdt_parser:=run_dir+"/sdt_parser"

	sdt_log_dir:=run_dir+"/log"
	if (!fileIsExists(sdt_log_dir)) {
		getCmdOutput("mkdir","sdt_log_dir")
	}
	sdt_log_ff:=run_dir+"/log/_filelist"
	logi("sdt_unzip=",sdt_unzip)
	logi("sdt_parser=",sdt_parser)
	logi("sdt_log_dir=",sdt_log_dir)
	save2db:=""
	if (needSave2db){
		save2db="save2db"
	}
	if (true){
		for {
			str := time.Now().Format("2006-01-02 15:04:05")
			str1 := filenameReplace(str) + ".log"
			logfilename := sdt_log_dir + "/" + str1
			logi("logfilename=", logfilename)
			f, _ := os.OpenFile(sdt_log_ff, os.O_CREATE|os.O_RDWR|os.O_APPEND, os.ModeAppend|os.ModePerm)
			f.WriteString(str1 + "\n")
			f.Close()

			f, _ = os.OpenFile(logfilename, os.O_CREATE|os.O_RDWR|os.O_APPEND, os.ModeAppend|os.ModePerm)
			str = "service start: " + str
			logi(str + "\n")
			f.WriteString(str + "\n")
			if (needDebug){
				f.WriteString("debug=true" + "\n")
			}
			if (needSave2db){
				f.WriteString("save2db=true" + "\n")
			}
			s, _ := getCmdOutput(sdt_copy, "/automount/ssh_sdt_data", data_from_path)
			f.WriteString(s + "\n")
			f.WriteString(sdt_unzip + " " + data_from_path + "  " + data_out_path + "\n")
			s, _ = getCmdOutput(sdt_unzip, data_from_path, data_out_path)
			f.WriteString(s + "\n")
			f.WriteString(sdt_parser + " " + data_out_path + "  " + save2db + "\n")
			s, _ = getCmdOutput(sdt_parser, data_out_path, save2db)
			f.WriteString(s + "\n")
			str = time.Now().Format("2006-01-02 15:04:05")
			str = "service end: " + str
			logi(str + "\n")
			f.WriteString(str + "\n")
			logi("")
			f.Close()

			//f.WriteString(ReadLine(sdt_log_ff,sdt_log_dir))
			max:=20000
			list, _ := getFilList(sdt_log_dir)
			if (len(list) > max){
				for i, v := range list {
					if (i<len(list)-max) {
						str := sdt_log_dir + "/" + v
						if err := os.Remove(str); err != nil {
						}
						//logi("[delete] " + str + "\n")
					}
				}
			}
			logi(" ",len(list),"\n")
			time.Sleep(3 * 1e10)
		}
		return
	}
}

func ReadLine(filePth string,sdt_log_dir string) string {
	f, err := os.Open(filePth)
	//logd("----->111 ")
	if err != nil {
		return ""
	}
	defer f.Close()
	//logd("----->222 ")
	bfRd := bufio.NewReader(f)
	lines:=0
	filelist:= []string{}
	for {
		line, _,err := bfRd.ReadLine()
		lines++
		//logd("----->33 , ",lines," , ", string(line[:]))
		if err != nil {
			if err == io.EOF {
				break
			}
		}else{
			filelist=append(filelist, string(line[:]))
		}
	}
	max:=20000
	if (lines>max) {
		for i:=0;i<lines-max;i++{
			str:=sdt_log_dir+"/"+filelist[i]
			if err := os.Remove(str); err != nil {
			}
			logi("[delete] "+str+"\n")
		}
		return "log file count: "+fmt.Sprint(lines-max)
	}
	return ""
}

func getFilList(dirpath string) ([]string, error) {
	var dir_list []string
	dir_err := filepath.Walk(dirpath,
		func(path string, f os.FileInfo, err error) error {
			//logi(f.Name())
			if f == nil {
				return err
			}
			if !f.IsDir() {
				if (len(f.Name())==len("2018-08-23 150032.log")) {
					dir_list = append(dir_list, path)
				}
				return nil
			}
			return nil
		})
	return dir_list, dir_err
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
	logi(c,out.String())
	return out.String(),true
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
