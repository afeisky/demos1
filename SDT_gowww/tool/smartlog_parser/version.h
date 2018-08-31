#ifndef VERSION_H
#define VERSION_H

#define VERSION "v2.0.0"
#define TCL_RESULT_KEY "[TCL*BIGDATA]"

enum type {
	UCHAR = 0,
	USHORT,
	UINT,
	CHAR,
	SHORT,
	INT,
	STRUCT,
	STRING,
	STRUCT_ARRAY,
	UNKNOWN,
};

#pragma pack(push)
#pragma pack(1)
struct symbol_list {
	char name[100];
	int type;
	int offset;
	int size;
	int array_size;
	struct symbol_list *next_list;
};
#pragma pack(pop)

#include "1_1_0.h"
#include "1_0_0.h"
#include "2_0_0.h"

#endif

