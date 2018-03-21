使用adb 命令，操作downx.apk去下载文件。
先建一个文件_down，写上url和要保存的文件名
然后push这个文件到/sdcard/Downx/下。
运行down.apk(或使用adb发广播)会下载。成功，返回在_downx中写了0，失败写1。

-usage: send adb broadcast to download file:

create file: _down
[url]http://www.baidu.com/aa.jpeg
[name]a.jpeg

cmd:
adb push _downx /sdcard/Downx/
adb shell am broadcast  -a demo.afei.downx.start

