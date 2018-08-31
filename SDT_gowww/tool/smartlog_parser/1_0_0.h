#ifndef __1_0_0_H
#define __1_0_0_H

#define PL_LOG_ADDR_1_0_0		0x1000
#define PL_LOG_SIZE_1_0_0		(0x100000-0x1000)
#define LK_LOG_ADDR_1_0_0		0x100000
#define LK_LOG_SIZE_1_0_0		0x100000
#define KERNEL_LOG_ADDR_1_0_0	0x200000
#define KERNEL_LOG_SIZE_1_0_0	(0x1000000-0x200000)

#pragma pack(push)
#pragma pack(1)

struct symbol_list second_symbol_v1_0_0[] = {
	{"version1", USHORT, 0, 2, 1, second_symbol_v1_0_0+1},
	{"version2", USHORT, 2, 2, 1, second_symbol_v1_0_0+2},
	{"version3", USHORT, 4, 2, 1, NULL},
	{"lk1_bad_count", USHORT, 0, 2, 1, second_symbol_v1_0_0+4},
	{"lk1_correct_count", USHORT, 2, 2, 1, second_symbol_v1_0_0+5},
	{"lk2_bad_count", USHORT, 4, 2, 1, second_symbol_v1_0_0+6},
	{"lk2_correct_count", USHORT, 6, 2, 1, second_symbol_v1_0_0+7},
	{"boot1_bad_count", USHORT, 8, 2, 1, second_symbol_v1_0_0+8},
	{"boot1_correct_count", USHORT, 10, 2, 1, second_symbol_v1_0_0+9},
	{"boot2_bad_count", USHORT, 12, 2, 1, second_symbol_v1_0_0+10},
	{"boot2_correct_count", USHORT, 14, 2, 1, second_symbol_v1_0_0+11},
	{"recovery1_bad_count", USHORT, 16, 2, 1, second_symbol_v1_0_0+12},
	{"recovery1_correct_count", USHORT, 18, 2, 1, second_symbol_v1_0_0+13},
	{"recovery2_bad_count", USHORT, 20, 2, 1, second_symbol_v1_0_0+14},
	{"recovery2_correct_count", USHORT, 22, 2, 1, NULL},
	{"fec_decode_time", STRING, 0, 20, 20, second_symbol_v1_0_0+16},
	{"fec_failed_time", STRING, 20, 20, 20, second_symbol_v1_0_0+17},
	{"fec_repair_time", STRING, 40, 20, 20, NULL},
	{"lk_start_time", STRING, 0, 20, 20, second_symbol_v1_0_0+19},
	{"laucher_start_time", STRING, 20, 20, 20, NULL},
	{"e2fsck_count", USHORT, 0, 2, 1, second_symbol_v1_0_0+21},
	{"e2fsck_time", STRING, 2, 20, 20, NULL},
	{"data_ro_cnt", USHORT, 0, 2, 1, second_symbol_v1_0_0+23},
	{"data_ro_time", STRING, 2, 20, 20, NULL},
};

struct symbol_list first_symbol_v1_0_0[] = {
	{"version", STRUCT, 4, 6, 1, second_symbol_v1_0_0},
	{"system_abnormal_write", STRING, 10, 128, 128, NULL},
	{"damage_record", STRUCT, 138, 24, 1, second_symbol_v1_0_0+3},
	{"fec_record", STRUCT, 162, 60, 1, second_symbol_v1_0_0+15},
	{"lk_laucher_time", STRUCT_ARRAY, 222, 800, 20, second_symbol_v1_0_0+18},
	{"e2fsck_info", STRUCT, 1022, 22, 1, second_symbol_v1_0_0+20},
	{"data_ro_info", STRUCT, 1044, 22, 1, second_symbol_v1_0_0+22},
};

#pragma pack(pop)


#endif