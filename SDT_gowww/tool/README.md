

useage:

./sdt_main.go tcl_bigdata.sys <test/save2db>

#go run sdt_main.go ../tcl_bigdata.sys save2db



sshfs -o allow_other root@10.92.33.85:/local/simplex_library/SDT/ /automount/sdt_data 
sshfs -o allow_other root@10.92.33.85:/local/simplex_library/ziplog/ /automount/tcllog_data 

--------
#chaofei.wu.h@tcl.com add ver1.0.1 2018-05-14


#--ubuntu 18.04 add boot tcl_sdt.service -----
假如你要建立的服务名为  tcl_sdt
1. 在 /lib/systemd/system下建立 tcl_sdt.service
输入  (ExecStart=你的执行命令或者脚本,ExecStop=可写可不写)
	[Unit]
	Description=tcl_sdt
	After=network.target

	[Service]
	ExecStart=echo "xxx" > /1.log
	ExecStop=

	[Install]
	WantedBy=multi-user.target

3. 添加启动  systemctl start tcl_sdt
4. 刷新配置 systemctl daemon-reload
5. 执行 sudo systemctl start tcl_sdt    #start service
6. 让开机后执行:
   ln -s /lib/systemd/system/tcl_sdt.service /etc/systemd/system/multi-user.target.wants/tcl_sdt.service
7. 如果文本成功写入则成功，reboot开机测试一下

--------------------------------------------------


--------开机，以root运行一个程序的设置:
Ubuntu 16.04设置rc.local开机启动命令/脚本的方法（通过update-rc.d管理Ubuntu开机启动程序/服务）
注意：rc.local脚本里面启动的用户默认为root权限。
rc.local脚本是一个Ubuntu开机后会自动执行的脚本，我们可以在该脚本内添加命令行指令。该脚本位于/etc/路径下，需要root权限才能修改。
该脚本具体格式如下：
# By default this script does nothing.
sshfs -o allow_other user@10.92.35.16:/local/opengrok/repo_mirror /automount/repo_mirror 
/wcf/tclbigdata/sdt/web
exit 0

-----Ubuntu环境下自动定时启动任务  /etc/crontab
crontab命令的功能是在一定的时间间隔调度一些命令的执行。
在/etc目录下有一个crontab文件，这里存放有系统运行的一些调度程序(还有cron.d/ cron.deny cron.monthly/ cron.weekly/cron.daily/ cron.hourly/ crontab)。
每个用户可以建立自己的调度crontab(/var/spool/cron 每个用户的文件以自己的名字命名 crontab -u someone -e)。
crontab命令有三种形式的命令行结构：
crontab [-u user] [file]
crontab [-u user] [-e|-l|-r]
crontab -l -u [-e|-l|-r]

crontab -u //设定某个用户的cron服务，一般root用户在执行这个命令的时候需要此参数
crontab -l //列出某个用户cron服务的详细内容
crontab -r //删除没个用户的cron服务
crontab -e //编辑某个用户的cron服务

root@tcl:/# gedit /etc/crontab    #修改内容
root@tcl:/# crontab -e    #修改内容
crontab: installing new crontab

# m h dom mon dow user	command
10  1	* * *	root	/wcf/tclbigdata/sdt/sdt_main /wcf/tclbigdata/sdt/tcl_bigdata.sys save2db >> /wcf/tclbigdata/sdt/sdt_main.log # 意思是每天01：10运行... 
root@tcl:/etc/init.d# ./cron reload
root@tcl:/etc/init.d# ./cron status


#sudu crontab -e add below:
sshfs -o allow_other root@10.92.33.85:/simplex/SDT/ /automount/ssh_sdt_data 
/disk2/tclbigdata/sdt/sdt_copy_from_3385 /automount/ssh_sdt_data /disk2/tclbigdata/data/bak_from

#run this:
go build sdt_unzip.go && sudo ./sdt_unzip /disk2/tclbigdata/data/bak_from debug  # need sudo ,because mount Tctpersist.ext4
go build sdt_parser.go && ./sdt_parser /disk2/tclbigdata/data/out test debug

# ----plan to run some command,crontab in ubuntu,----------------
root@bigdata:~# crontab -l      #view crontab
root@bigdata:~# gedit /tmp/root.crontab  # Create one crontab and Modify crontab
    # m h  dom mon dow   command
    */5 * * * *   SDT1 sshfs > /automount/ssh_sdt_data.log              # every 5 minutes to run SDT1....
root@tcl:/# crontab -e 
root@tcl:/etc/init.d# ./cron reload
root@tcl:/etc/init.d# ./cron status

root@bigdata:~# crontab /tmp/root.crontab   #install crontab
root@bigdata:~# crontab -e   #modify crontab

