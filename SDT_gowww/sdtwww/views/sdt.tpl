<!DOCTYPE html>

<html>
<head>
  <title>SDT data</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <style type="text/css">
 body{
	background-color:#d9d8d0;
	text-align:center;
	margin-top:0px;
	margin-left:auto;
	margin-right:auto;
	width:98%;
	}

     a{ text-decoration: none;}
     table {
	    width:100%;
	 }
     table.altrowstable {
         font-family: verdana,arial,sans-serif;
         font-size:11px;
         color:#333333;
         border-width: 1px;
         border-color: #a9c6c9;
         border-collapse: collapse;
     }
     table.altrowstable th {
         border-width: 1px;
         padding: 8px;
         border-style: solid;
         border-color: #a9c6c9;
     }
     table.altrowstable td {
         border-width: 1px;
         padding: 8px;
         border-style: solid;
         border-color: #a9c6c9;
     }
     .oddrowcolor{
         background-color:#E3EAEB;
     }
     .evenrowcolor{
         background-color:#FFFFFF;
     }
      .headcolor{
         background-color:#00cc00;
     }
       .search{
        text-align:left;
         background-color:#ffffff;
     }
     </style>
</head>

<body>
<form method="post" action="" name="form1" id="form1">
     <div class="search"><img src="static/images/tcl.gif" width="200" height="60" border="0" alt=""><font  color="#0000cc">SDT Data&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
	  <select name="query_type" id="query_type">
	<option value="filename">filename</option>
	<option value="imei">imei</option>
	<option value="productname">productname</option>
</select>
            <input name="query_text" type="text" id="query_text" style="width:130px;" />
            <input type="submit" name="submit" value="Find" id="submit" style="width:60px;" />

            </div>
</form>
<form method="post" action="" name="form2" id="form2">
<table class="altrowstable" id="alternatecolor">
{{str2html .Hdata}}
</table>
</form>
</body>
 <script type="text/javascript">
	 function select(filename){
		alert(filename);
	}
     function altRows(id){
         if(document.getElementsByTagName){
              var table = document.getElementById(id);
             var rows = table.getElementsByTagName("tr");

             for(i = 0; i < rows.length; i++){
			     if (i==0) {
				     rows[i].className = "headcolor";
			     }else if(i % 2 == 0){
                     rows[i].className = "evenrowcolor";
                 }else{
                     rows[i].className = "oddrowcolor";
                 }
             }
         }
     }

     window.onload=function(){
         altRows('alternatecolor');
     }

 </script>
</html>
