<!DOCTYPE html>

<html>
<head>
  <title>SDT data</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <style type="text/css">
     a{ text-decoration: none;}
     table {
	 width:"100%"
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
         background-color:#ccbf66;
     }
     </style>
</head>

<body>
     <div class="search">&nbsp;&nbsp;&nbsp;&nbsp;<font  color="#0000cc"><a href="/">SDT Data</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
            </div>
<table class="altrowstable" id="alternatecolor">
{{str2html .Hdata}}
</table>
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
