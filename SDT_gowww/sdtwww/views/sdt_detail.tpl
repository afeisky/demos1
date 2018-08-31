<!DOCTYPE html>

<html>
<head>
  <title>SDT data</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <style type="text/css">
 body{
	margin-top:0px;
	margin-left:auto;
	margin-right:auto;
	width:98%;
	}

    a{ text-decoration: none;}
     table {
      text-align:left;
	 width:"100%"
	 }
     table.table1 {
         font-family: verdana,arial,sans-serif;
         font-size:11px;
         color:#333333;
         border-width: 1px;
         border-color: #a9c6c9;
         border-collapse: collapse;
     }
     table.table1 th {
         border-width: 1px;
         padding: 8px;
         border-style: solid;
         border-color: #a9c6c9;
     }
     table.table1 td {
         border-width: 1px;
         padding: 8px;
         border-style: solid;
         border-color: #a9c6c9;
     }     
     .td1 {
         background-color:#E3EAEB;
     }

     </style>
</head>

<body>
     <div class="search">&nbsp;&nbsp;&nbsp;&nbsp;<font  color="#0000cc"><a href="/">SDT Data</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
            </div>
<table class="table1" id="table11">
  <tr>
	<td class="td1">filename</td><td>{{str2html .filename}}</td><td>&nbsp;</td>
	<td class="td1">filetime</td><td>{{str2html .filetime}}</td><td>&nbsp;</td>
	<td class="td1">productname</td><td>{{str2html .productname}}</td> 
 </tr>
   <tr>
	<td class="td1">imei1</td>	<td>{{str2html .imei1}}</td>	<td>&nbsp;</td>
	<td class="td1">imei2</td>	<td>{{str2html .imei2}}</td>	<td>&nbsp;</td>
	<td class="td1">idcard</td>	<td>{{str2html .idcard}}</td>
  </tr>
  <tr>
	<td class="td1">perso_system</td>	<td>{{.perso_system}}</td>	<td>&nbsp;</td>
	<td class="td1">bt_mac</td>	<td>{{.bt_mac}}</td>	<td>&nbsp;</td>
	<td class="td1">wifi_mac</td>	<td>{{.wifi_mac}}</td>
  </tr>

    <tr>
	<td class="td1">commercial_ref</td>	<td>{{str2html .commercial_ref}}</td>	<td>&nbsp;</td>
	<td class="td1">fec_record</td>	<td>{{.fec_record}}</td>	<td>&nbsp;</td>
	<td class="td1">lifetime</td>	<td>{{.lifetime}}</td>
  </tr>

   <tr>
	<td class="td1">used_time</td>	<td>{{.used_time}}</td>	<td>&nbsp;</td>
	<td class="td1">sim_time</td>	<td>{{.sim_time}}</td>	<td>&nbsp;</td>
	<td class="td1">wifi_time</td>	<td>{{.wifi_time}}</td>
  </tr>
  
<tr>
	<td class="td1">productmodel</td>	<td>{{str2html .productmodel}}</td>	<td>&nbsp;</td>
	<td class="td1">pcba_sn</td>	<td>{{str2html .pcba_sn}}</td>	<td>&nbsp;</td>
	<td class="td1">mini_ver</td>	<td>{{str2html .mini_ver}}</td>
  </tr>

  <tr>
	<td class="td1">system_ver</td>	<td>{{str2html .system_ver}}</td>	<td>&nbsp;</td>
	<td class="td1">bt_addr</td>	<td>{{str2html .bt_addr}}</td>	<td>&nbsp;</td>
	<td class="td1">wifi_addr</td>	<td>{{str2html .wifi_addr}}</td>
  </tr>
  <tr>
	<td class="td1">username</td>	<td>{{str2html .username}}</td>	<td>&nbsp;</td>
	<td class="td1">tool</td>	<td>{{str2html .tool}}</td>	<td>&nbsp;</td>
	<td class="td1">rooted</td>	<td>{{str2html .rooted}}</td>
  </tr>
    <tr>
	<td class="td1">battery_v</td>	<td>{{str2html .battery_v}}</td>	<td>&nbsp;</td>
	<td class="td1">update_time</td>	<td>{{str2html .update_time}}</td>	<td>&nbsp;</td>
	<td class="td1">&nbsp;</td>	<td>&nbsp;</td>
  </tr>

     <tr>
	<td class="td1">parser_mail</td>	<td>{{str2html .parser_mail}}</td>	<td>&nbsp;</td>
	<td class="td1">parser_time</td>	<td>{{str2html .parser_time}}</td>	<td>&nbsp;</td>
	<td class="td1">parser_step</td>	<td>{{str2html .parser_step}}</td>
  </tr>
       <tr>
	<td class="td1">launcher_time</td>	<td colspan="5"><pre>{{str2html .arr_lkla}}</pre></td>	
	<td class="td1">arr_bootreason</td>	<td><pre>{{str2html .arr_bootreason}}</pre></td>
  </tr>
</table>
<PRE>{{str2html .parser_text}}</PRE>
  <form method="post" action="update">
 <div>comment:<input type="hidden" name="p" value="1"><font size="" color="#ff0033">{{str2html .success_text}}</font></div>
	<textarea name="text" rows="10" cols="100"></textarea></br>
	<input type="hidden" name="p" value="1">
	<input type="hidden" name="filename" value="{{str2html .filename}}">
	<div>Email: <input type="text" name="mail" id="mail" value="">&nbsp;&nbsp;<input type="submit" value="submit" onclick="return toclick(this.from);"> <span id="hint" name="hint"></span></div>
  </form>
  <PRE>{{str2html .parser_history}}</PRE>
</body>
<script type="text/javascript">
	 function toclick(){
	 var mail = document.getElementById("mail").value;
                 if( mail.length>10){
                    return true;
                }
                else{
                    alert("请输入mail!");		   
                    return false;
                }
	}
 </script>
</html>
