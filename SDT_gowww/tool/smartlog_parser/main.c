#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include "version.h"

#define FILE_NAME	"Smartlog.bin"
#define OUT_FILE	"Smartlog.bin_@bd.json"
#define PL_LOG_FILE	"Smartlog.bin_@preloader.log"
#define LK_LOG_FILE	"Smartlog.bin_@lk.log"
#define KERNEL_LOG_FILE	"Smartlog.bin_@kernel.log"
#define BOOT_MODE_AND_REASON_MASK  0x1000

/* typedefs */
typedef enum {
	NORMAL_BOOT = 0,
	META_BOOT = 1,
	RECOVERY_BOOT = 2,
	SW_REBOOT = 3,
	FACTORY_BOOT = 4,
	ADVMETA_BOOT = 5,
	ATE_FACTORY_BOOT = 6,
	ALARM_BOOT = 7,
	KERNEL_POWER_OFF_CHARGING_BOOT = 8,
	LOW_POWER_OFF_CHARGING_BOOT = 9,
	FASTBOOT = 99,
	DOWNLOAD_BOOT = 100,
	UNKNOWN_BOOT
} BOOTMODE;

typedef enum {
	BR_POWER_KEY = 0,
	BR_USB,
	BR_RTC,
	BR_WDT,
	BR_WDT_BY_PASS_PWK,
	BR_TOOL_BY_PASS_PWK,
	BR_2SEC_REBOOT,
	BR_UNKNOWN,
	BR_KERNEL_PANIC,
	BR_WDT_SW,
	BR_WDT_HW
} boot_reason_t;

#pragma pack(push)
#pragma pack(1)
struct smart_partion_version {
    unsigned short version1;
    unsigned short version2;
    unsigned short version3;
};
#pragma pack(pop)

static int tab_cnt = 0;
static int fd_in = -1;
static int fd_out = -1;
static int pl_log_fd = -1;
static int lk_log_fd = -1;
static int kernel_log_fd = -1;
static char log[0x1000000];
static int fake_version_flag = 0;

// something to do with smart version
static struct symbol_list *first_symbol;
static int first_symbol_size;
static int pl_log_addr;
static int pl_log_size;
static int lk_log_addr;
static int lk_log_size;
static int kernel_log_addr;
static int kernel_log_size;

int check_version(void);
void parse_begin(void);
void parse_end(void);
void parse_second_begin(void);
void parse_second_end(void);
void parse_symbol(struct symbol_list *symbol, int base);
void output_tabs(int n);
void parse_smart_to_txt(int fd_in, int fd_out, int addr, int size);
char *get_boot_mode(BOOTMODE mode);
char *get_boot_reason(boot_reason_t reason);

int main(int argc, char **argv)
{
	int ret = 0;
	int i;
	char out_str[200];
	char path[200] = {0};
	char file[200];


	/* begin */
	if (argc <= 1) {
		printf("usage: parse <directory>");
		ret = -1;
		goto err2;
	}

	/*fds*/
	sprintf(path, "%s", argv[1]);
	sprintf(file, "%s/Smartlog.bin", path);
	fd_in = open(file, 0);
	if (fd_in < 0) {
		printf("can't open file:%s\n", file);
		ret = -1;
		goto err2;
	}
	sprintf(file, "%s/%s", path, OUT_FILE);
	fd_out = open(file, O_CREAT | O_RDWR | O_TRUNC, 0644);
	if (fd_out < 0) {
		printf("can't open file:%s\n", file);
		ret = -1;
		goto err1;
	}
	sprintf(file, "%s/%s", path, PL_LOG_FILE);
	pl_log_fd = open(file, O_CREAT | O_RDWR | O_TRUNC, 0644);
	if (pl_log_fd < 0) {
		printf("can't open file:%s\n", file);
		ret = -1;
		goto err1;
	}
	sprintf(file, "%s/%s", path, LK_LOG_FILE);
	lk_log_fd = open(file, O_CREAT | O_RDWR | O_TRUNC, 0644);
	if (lk_log_fd < 0) {
		printf("can't open file:%s\n", file);
		ret = -1;
		goto err1;
	}
	sprintf(file, "%s/%s", path, KERNEL_LOG_FILE);
	kernel_log_fd = open(file, O_CREAT | O_RDWR | O_TRUNC, 0644);
	if (kernel_log_fd < 0) {
		printf("can't open file:%s\n", file);
		ret = -1;
		goto err1;
	}


	/* check version */
	if (check_version()) {
		printf("smart version unspecified! please check!\n");
		goto err1;
	}

	/* parse the smart info into json file */
	printf("parsing smart info...\n");
	parse_begin();
	for (i = 0; i < first_symbol_size; ++i) {
		parse_symbol(&first_symbol[i], 0);
	}
	parse_end();

	/* parse pl log */
	printf("parsing pl log...\n");
	parse_smart_to_txt(fd_in, pl_log_fd, pl_log_addr, pl_log_size);

	/* parse lk log */
	printf("parsing lk log...\n");
	parse_smart_to_txt(fd_in, lk_log_fd, lk_log_addr, lk_log_size);

	/* parse kernel log */
	printf("parsing kernel log...\n");
	parse_smart_to_txt(fd_in, kernel_log_fd, kernel_log_addr, kernel_log_size);

	printf("all parsing done!\n");

	/* go out */
err1:
	if (fd_in >= 0) {
		close(fd_in);
	}
	if (fd_out >= 0) {
		close(fd_out);
	}
	if (pl_log_fd >= 0) {
		close(pl_log_fd);
	}
	if (lk_log_fd >= 0) {
		close(lk_log_fd);
	}
	if (kernel_log_fd >= 0) {
		close(kernel_log_fd);
	}
err2:
	return ret;
}

/* check smart info version */
int check_version(void)
{
	int ret;
	struct smart_partion_version version;
	if(lseek(fd_in, 4, SEEK_SET) == -1) {
		printf("lseek error!\n");
		return -1;
	}
	if(read(fd_in, &version, sizeof(struct smart_partion_version)) != sizeof(struct smart_partion_version)) {
		printf("read error!\n");
		return -1;
	}
	lseek(fd_in, 0, SEEK_SET);

	//printf("version: %d.%d.%d\n", version.version1, version.version2, version.version3);

	// History problem: sometimes the smart version read out is not true because of the updating bug.
	// Now check more to ensure the true smart version.
	// Only exists when updated from 1.x.x to 2.0.0
	if (version.version1 == 1) {
		// read the address of lk_laucher_time in 2.0.0, if not zero, then the true version is 2.0.0
		int lk_laucher_time_addr = 0x400000+128+24+60;
		char data[2];
		if (lseek(fd_in, lk_laucher_time_addr-1, SEEK_SET) == -1) {
			printf("lseek error!\n");
			return -1;
		}
		if (read(fd_in, data, 2) != 2) {
			printf("read error!\n");
			return -1;
		}
		// check two bytes, if the byte before lk_laucher_time_addr is 0 and the byte at lk_laucher_time_addr is not 0,
		// then the true version is 2.0.0
		if (data[0] == 0 && data[1] != 0) {
			printf("read version is %d.%d.%d\n", version.version1, version.version2, version.version3);
			printf("true version is 2.0.0\n");
			fake_version_flag = 1;
			version.version1 = 2;
			version.version2 = 0;
			version.version3 = 0;
		}
	}

	/* choose the right symbol_list */
	if (version.version1 == 1 && version.version2 == 0 && version.version3 == 0) {
		first_symbol = first_symbol_v1_0_0;
		first_symbol_size = sizeof(first_symbol_v1_0_0)/sizeof(struct symbol_list);
		pl_log_addr = PL_LOG_ADDR_1_0_0;
		pl_log_size = PL_LOG_SIZE_1_0_0;
		lk_log_addr = LK_LOG_ADDR_1_0_0;
		lk_log_size = LK_LOG_SIZE_1_0_0;
		kernel_log_addr = KERNEL_LOG_ADDR_1_0_0;
		kernel_log_size = KERNEL_LOG_SIZE_1_0_0;
	}
	else if (version.version1 == 1 && version.version2 == 1 && version.version3 == 0) {
		first_symbol = first_symbol_v1_1_0;
		first_symbol_size = sizeof(first_symbol_v1_1_0)/sizeof(struct symbol_list);
		pl_log_addr = PL_LOG_ADDR_1_1_0;
		pl_log_size = PL_LOG_SIZE_1_1_0;
		lk_log_addr = LK_LOG_ADDR_1_1_0;
		lk_log_size = LK_LOG_SIZE_1_1_0;
		kernel_log_addr = KERNEL_LOG_ADDR_1_1_0;
		kernel_log_size = KERNEL_LOG_SIZE_1_1_0;
	}
	else if (version.version1 == 2 && version.version2 == 0 && version.version3 == 0) {
		first_symbol = first_symbol_v2_0_0;
		first_symbol_size = sizeof(first_symbol_v2_0_0)/sizeof(struct symbol_list);
		pl_log_addr = PL_LOG_ADDR_2_0_0;
		pl_log_size = PL_LOG_SIZE_2_0_0;
		lk_log_addr = LK_LOG_ADDR_2_0_0;
		lk_log_size = LK_LOG_SIZE_2_0_0;
		kernel_log_addr = KERNEL_LOG_ADDR_2_0_0;
		kernel_log_size = KERNEL_LOG_SIZE_2_0_0;
	}
	else {
		return -1;
	}

	return 0;
}

void parse_begin(void)
{
	write(fd_out, "{\n", 2);
	tab_cnt++;
}

void parse_end(void)
{
	output_tabs(--tab_cnt);
#ifdef USE_CONSOLE_OUT
	write(fd_out, "},\n", 3);
#else
	write(fd_out, "}\n", 2);
#endif
}

void parse_second_begin(void)
{
	write(fd_out, "{\n", 2);
	tab_cnt ++;
}

void parse_second_end(void)
{
	output_tabs(--tab_cnt);
	write(fd_out, "},\n", 3);
}

void output_tabs(int n)
{
	while(n--) {
		write(fd_out, "\t", 1);
	}
}

void parse_symbol(struct symbol_list *symbol, int base)
{
	char outstr[100];
	unsigned int read_int = 0;
	char read_str[200];
	int i;
	struct symbol_list * p_symbol;
	if (symbol == NULL)
		return;
	output_tabs(tab_cnt);
	memset(outstr, 0, sizeof(outstr));
	sprintf(outstr, "\"%s\":", symbol->name);
	write(fd_out, outstr, strlen(outstr));
	switch (symbol->type) {

	case UCHAR:
	case USHORT:
	case UINT:
		lseek(fd_in, symbol->offset+base, SEEK_SET);
		read(fd_in, &read_int, symbol->size);

		// special process to version, boot mode and boot reason
		if (strncmp(symbol->name, "version1", strlen(symbol->name)) == 0) {
			if (fake_version_flag) {
				read_int = 2;
			}
			sprintf(outstr, "%d,\n", read_int);
		}
		else if (strncmp(symbol->name, "version2", strlen(symbol->name)) == 0) {
			if (fake_version_flag) {
				read_int = 0;
			}
			sprintf(outstr, "%d,\n", read_int);
		}
		else if (strncmp(symbol->name, "boot_mode", strlen(symbol->name)) == 0) {
			sprintf(outstr, "\"%s\",\n", get_boot_mode(read_int-BOOT_MODE_AND_REASON_MASK));
		}
		else if (strncmp(symbol->name, "boot_reason", strlen(symbol->name)) == 0) {
			sprintf(outstr, "\"%s\",\n", get_boot_reason(read_int-BOOT_MODE_AND_REASON_MASK));
		}
		else {
			sprintf(outstr, "%d,\n", read_int);
		}

		write(fd_out, outstr, strlen(outstr));
		break;
	case STRING:
		lseek(fd_in, symbol->offset+base, SEEK_SET);
		memset(read_str, 0, sizeof(read_str));
		read(fd_in, read_str, symbol->size);
		sprintf(outstr, "\"%s\",\n", read_str);
		write(fd_out, outstr, strlen(outstr));
		break;
	case STRUCT:
		p_symbol = symbol->next_list;
		parse_second_begin();
		while (p_symbol) {
			parse_symbol(p_symbol, symbol->offset);
			p_symbol = p_symbol->next_list;
		}
		parse_second_end();
		break;
	case STRUCT_ARRAY:
		write(fd_out, "[\n", 2);
		for (i = 0; i < symbol->array_size; i++) {
			output_tabs(tab_cnt);
			parse_second_begin();
			p_symbol = symbol->next_list;
			while (p_symbol) {
				parse_symbol(p_symbol, symbol->offset + i*symbol->size/symbol->array_size);
				p_symbol = p_symbol->next_list;
			}
			parse_second_end();
		}
		output_tabs(tab_cnt);
		write(fd_out,"],\n", 3);
		break;
	default:
		break;
	}
}


void parse_smart_to_txt(int fd_in, int fd_out, int addr, int size)
{
	int i = 0;

	if(lseek(fd_in, addr, SEEK_SET) == -1) {
		printf("lseek error!\n");
		return;
	}

	if (read(fd_in, log, size) != size) {
		printf("read error!\n");
		return;
	}
	lseek(fd_in, 0, SEEK_SET);

	while (log[i]) {
		write(fd_out, &log[i], 1);
		++i;
	}
}

char *get_boot_mode(BOOTMODE mode)
{
	char *ret = "unknown";
	switch (mode) {
		case NORMAL_BOOT:
			ret = "normal_boot";
			break;
		case META_BOOT:
			ret = "meta_boot";
			break;
		case RECOVERY_BOOT:
			ret = "recovery_boot";
			break;
		case SW_REBOOT:
			ret = "sw_reboot";
			break;
		case FACTORY_BOOT:
			ret = "factory_boot";
			break;
		case ADVMETA_BOOT:
			ret = "advmeta_boot";
			break;
		case ATE_FACTORY_BOOT:
			ret = "ate_factory_boot";
			break;
		case ALARM_BOOT:
			ret = "alarm_boot";
			break;
		case KERNEL_POWER_OFF_CHARGING_BOOT:
			ret = "kernel_power_off_charging_boot";
			break;
		case LOW_POWER_OFF_CHARGING_BOOT:
			ret = "low_power_off_charging_boot";
			break;
		case FASTBOOT:
			ret = "fastboot";
			break;
		case DOWNLOAD_BOOT:
			ret = "download_boot";
			break;
		default:
			break;
	}
	return ret;
}

char *get_boot_reason(boot_reason_t reason)
{
	char *ret = "null";
	switch (reason) {
		case BR_POWER_KEY:
			ret = "power_key";
			break;
		case BR_USB:
			ret = "usb";
			break;
		case BR_RTC:
			ret = "rtc";
			break;
		case BR_WDT:
			ret = "wdt";
			break;
		case BR_WDT_BY_PASS_PWK:
			ret = "wdt_by_pass_pwk";
			break;
		case BR_TOOL_BY_PASS_PWK:
			ret = "tool_by_pass_pwk";
			break;
		case BR_2SEC_REBOOT:
			ret = "2sec_reboot";
			break;
		case BR_UNKNOWN:
			ret = "unknown";
			break;
		case BR_KERNEL_PANIC:
			ret = "kernel_panic";
			break;
		case BR_WDT_SW:
			ret = "wdt_sw";
			break;
		case BR_WDT_HW:
			ret = "wdt_hw";
			break;
		default:
			break;
	}
	return ret;
}
