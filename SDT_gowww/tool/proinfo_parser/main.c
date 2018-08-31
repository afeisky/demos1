//chaofei.wu.hz@tcl.com add for TCT SDT Data Parser 2017-11-01

#include <stdio.h>
#include <unistd.h>
#include "version.h"
#include "CFG_PRODUCT_INFO_File.h"
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/types.h>
#include <fcntl.h>


int usage(){
  fprintf(stderr, "usage: tct_sdt_parser proinfo_file\n");
  return -1;
}


int main(int argc, char **argv)
{
    int fp = -1;
	int len = 0;
	PRODUCT_INFO proinfo;
	unsigned char pcba[16];
	unsigned char wifi_addr[32];
	unsigned char bt_addr[32];
	unsigned char system_ver[32];
	unsigned char comm_ref[32];
	unsigned char mini_ver[8];
	unsigned char app_ver[8];

     printf("vesion: %s\n",VERSION); // donnot delete
	if (argc != 2)
		return usage();
	
    if(access(argv[1], R_OK) != 0)
	{
		fprintf(stderr, "%s is not exist or can not be access, return\n", argv[1]);
		return -2;
	}
	
	fp = open((const char *)argv[1], O_RDONLY, 0666);
	if(fp < 0)
	{
		fprintf(stderr, "open %s failed, return\n", argv[1]);
		return -3;
	}
	
	len = lseek(fp, 0, SEEK_END);
	lseek(fp, 0, SEEK_SET);	
	read(fp, (void *)&proinfo, (sizeof(PRODUCT_INFO) <= len) ? sizeof(PRODUCT_INFO) : len);
	close(fp);
	
	// pcba sn
	memset(pcba, 0x00, 16);
        memcpy(pcba, proinfo.trace_nvram_data.short_code, 15);
	
	// system version
	memset(system_ver, 0x00, 32);
	memcpy(system_ver, proinfo.trace_nvram_data.info_name_appli, 12);
	
	// Mini version
	memset(mini_ver, 0x00, 8);
	memcpy(mini_ver, proinfo.trace_nvram_data.info_pts_mini, 3);
	
	// Application version
	memset(app_ver, 0x00, 8);
	memcpy(app_ver, proinfo.trace_nvram_data.info_pts_appli, 3);
	
	// Commercial ref
	memset(comm_ref, 0x00, 32);
	memcpy(comm_ref, proinfo.trace_nvram_data.info_comm_ref, 13);//"commercial_ref:": "5090Y-2AALWE1"
	
	// wifi mac address & bluetooth address
	sprintf(bt_addr, "%0.2x:%0.2x:%0.2x:%0.2x:%0.2x:%0.2x", proinfo.bt_addr[0], proinfo.bt_addr[1], proinfo.bt_addr[2], proinfo.bt_addr[3], proinfo.bt_addr[4], proinfo.bt_addr[5]);
	sprintf(wifi_addr, "%0.2x:%0.2x:%0.2x:%0.2x:%0.2x:%0.2x", proinfo.wifi_addr[0], proinfo.wifi_addr[1], proinfo.wifi_addr[2], proinfo.wifi_addr[3], proinfo.wifi_addr[4], proinfo.wifi_addr[5]);
	    
	// print information
        printf("%s\n",TCL_RESULT_KEY);  // donnot delete
	printf("{\"result\":1,");// donnot delete
	printf("\"data\":{");// donnot delete
	printf("\"version\":\"v1.0.1\"," );
	printf("\"pcba_sn\":\"%s\",", pcba);
	printf("\"system_ver\":\"%s\",", system_ver);
	printf("\"mini_ver\":\"%s\",", mini_ver);
	printf("\"app_ver\":\"%s\",", app_ver);
	printf("\"commercial_ref\":\"%s\",", comm_ref);
	printf("\"bt_addr\":\"%s\",", bt_addr);
	printf("\"wifi_addr\":\"%s\"", wifi_addr);
	// add more:
	printf("}}\n");// donnot delete
	
    return 0;
}
	
