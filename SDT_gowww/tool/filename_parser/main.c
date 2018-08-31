//chaofei.wu.hz@tcl.com add for TCT SDT Data Parser 2017-11-01

#include<stdio.h>
#include "version.h"
#include "filename_parser.h"

int main(int argc, char *argv[])
{
  int i;
    //printf("Total %d argv\n", argc);
    printf("vesion: %s\n",VERSION);
    for(i=0; i<argc; i++)
    {
        printf("argv[%d]: %s\n", i, argv[i]);
    }
    if (argc==2){       
        filename_parser_v10(argv[1]);
        return 0;
    }
    return 0;
}
