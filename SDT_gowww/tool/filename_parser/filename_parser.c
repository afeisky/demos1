//chaofei.wu.hz@tcl.com add for TCT SDT Data Parser 2017-11-01
#include "stdio.h"
#include <string.h>
#include <memory.h>
#include "version.h"
#include "filename_parser.h"
//12202127212023212125252025209
//12345678901234567890123456789

void filename_parser_v10(char *filepatchname){
    char imei[16];
    char datetime[16];
    int i=0;
    printf("%s\n",filepatchname);
    memset(imei,0,16);
    memset(datetime,0,16);
    for (i=0;i<strlen(filepatchname);i++)
    {
        //printf("%c,",*(filepatchname+i));
        if (i % 2){
            datetime[i/2]=*(filepatchname+i);            
        }else{
            imei[i/2]=*(filepatchname+i);
	}
        
    }

    for (i=0;i<strlen(imei);i++)
    {        
        printf("%c,",*(imei+i));
    }
    printf("\n");
    for (i=0;i<strlen(datetime);i++)
    {        
        printf("%c,",*(datetime+i));
    }
    printf("\n");
    printf("%s\n",TCL_RESULT_KEY);
    printf("{\"result\":1,\"imei\":\"%s\",\"time\":\"%s\"}",imei,datetime);
    printf("\n");
}


