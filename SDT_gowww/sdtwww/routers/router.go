package routers

import (
	"github.com/beego/wuchaofei/sdtwww/controllers"
	"github.com/astaxie/beego"
)

func init() {
    beego.SetStaticPath("static/images","img") 
    beego.SetStaticPath("static/css","css")
    beego.SetStaticPath("static/js","js")
    beego.SetStaticPath("X:/tclbigdata/data/out", "download")

    beego.Router("/", &controllers.Sdt1Controller{})
    //beego.Router("/filedownload", &controllers.SdtDownloadController{})
    beego.Router("/filedownload", &controllers.Sdt1Controller{},"Get:Download")
    beego.Router("/update", &controllers.Sdt1Controller{},"Post:ParserText")
}
