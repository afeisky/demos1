#ifndef __2_0_0_H
#define __2_0_0_H

#define PL_LOG_ADDR_2_0_0		0x800000
#define PL_LOG_SIZE_2_0_0		0x100000
#define LK_LOG_ADDR_2_0_0		0x900000
#define LK_LOG_SIZE_2_0_0		0x100000
#define KERNEL_LOG_ADDR_2_0_0	0xA00000
#define KERNEL_LOG_SIZE_2_0_0	0x500000

#pragma pack(push)
#pragma pack(1)

struct symbol_list second_symbol_v2_0_0[] = {
	{"version1", USHORT, 0, 2, 1, second_symbol_v2_0_0+1},
	{"version2", USHORT, 2, 2, 1, second_symbol_v2_0_0+2},
	{"version3", USHORT, 4, 2, 1, NULL},
	{"emmc_life_time", UINT, 0, 4, 1, NULL},
	{"lk1_bad_count", USHORT, 0, 2, 1, second_symbol_v2_0_0+5},
	{"lk1_correct_count", USHORT, 2, 2, 1, second_symbol_v2_0_0+6},
	{"lk2_bad_count", USHORT, 4, 2, 1, second_symbol_v2_0_0+7},
	{"lk2_correct_count", USHORT, 6, 2, 1, second_symbol_v2_0_0+8},
	{"boot1_bad_count", USHORT, 8, 2, 1, second_symbol_v2_0_0+9},
	{"boot1_correct_count", USHORT, 10, 2, 1, second_symbol_v2_0_0+10},
	{"boot2_bad_count", USHORT, 12, 2, 1, second_symbol_v2_0_0+11},
	{"boot2_correct_count", USHORT, 14, 2, 1, second_symbol_v2_0_0+12},
	{"recovery1_bad_count", USHORT, 16, 2, 1, second_symbol_v2_0_0+13},
	{"recovery1_correct_count", USHORT, 18, 2, 1, second_symbol_v2_0_0+14},
	{"recovery2_bad_count", USHORT, 20, 2, 1, second_symbol_v2_0_0+15},
	{"recovery2_correct_count", USHORT, 22, 2, 1, NULL},
	{"fec_decode_time", STRING, 0, 20, 20, second_symbol_v2_0_0+17},
	{"fec_failed_time", STRING, 20, 20, 20, second_symbol_v2_0_0+18},
	{"fec_repair_time", STRING, 40, 20, 20, NULL},
	{"lk_start_time", STRING, 0, 20, 20, second_symbol_v2_0_0+20},
	{"laucher_start_time", STRING, 20, 20, 20, NULL},
	{"e2fsck_count", USHORT, 0, 2, 1, second_symbol_v2_0_0+22},
	{"e2fsck_time", STRING, 2, 20, 20, NULL},
	{"data_ro_cnt", USHORT, 0, 2, 1, second_symbol_v2_0_0+24},
	{"data_ro_time", STRING, 2, 20, 20, NULL},
	{"boot_mode", USHORT, 0, 2, 1, second_symbol_v2_0_0+26},
	{"boot_reason", USHORT, 2, 2, 1, NULL},
};

struct symbol_list first_symbol_v2_0_0[] = {
	{"version", STRUCT, 4, 6, 1, second_symbol_v2_0_0},
	{"life_time_info", STRUCT, 1066, 4, 1, second_symbol_v2_0_0+3},
	{"system_abnormal_write", STRING, 0x400000, 128, 128, NULL},
	{"damage_record", STRUCT, 0x400000+128, 24, 1, second_symbol_v2_0_0+4},
	{"fec_record", STRUCT, 0x400000+128+24, 60, 1, second_symbol_v2_0_0+16},
	{"lk_laucher_time", STRUCT_ARRAY, 0x400000+128+24+60, 800, 20, second_symbol_v2_0_0+19},
	{"e2fsck_info", STRUCT, 0x400000+128+24+60+800, 22, 1, second_symbol_v2_0_0+21},
	{"data_ro_info", STRUCT, 0x400000+128+24+60+800+22, 22, 1, second_symbol_v2_0_0+23},
	{"boot_mode_and_reason", STRUCT_ARRAY, 0x400000+128+24+60+800+22+22, 80, 20, second_symbol_v2_0_0+25},
};

#pragma pack(pop)

#endif