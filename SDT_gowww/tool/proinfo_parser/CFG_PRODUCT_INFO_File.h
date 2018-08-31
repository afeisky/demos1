/*****************************************************************************
*  Copyright Statement:
*  --------------------
*  This software is protected by Copyright and the information contained
*  herein is confidential. The software may not be copied and the information
*  contained herein may not be used or disclosed except with the written
*  permission of MediaTek Inc. (C) 2008
*
*  BY OPENING THIS FILE, BUYER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
*  THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
*  RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO BUYER ON
*  AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
*  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
*  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
*  NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
*  SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
*  SUPPLIED WITH THE MEDIATEK SOFTWARE, AND BUYER AGREES TO LOOK ONLY TO SUCH
*  THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. MEDIATEK SHALL ALSO
*  NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE RELEASES MADE TO BUYER'S
*  SPECIFICATION OR TO CONFORM TO A PARTICULAR STANDARD OR OPEN FORUM.
*
*  BUYER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND CUMULATIVE
*  LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
*  AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
*  OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY BUYER TO
*  MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
*
*  THE TRANSACTION CONTEMPLATED HEREUNDER SHALL BE CONSTRUED IN ACCORDANCE
*  WITH THE LAWS OF THE STATE OF CALIFORNIA, USA, EXCLUDING ITS CONFLICT OF
*  LAWS PRINCIPLES.  ANY DISPUTES, CONTROVERSIES OR CLAIMS ARISING THEREOF AND
*  RELATED THERETO SHALL BE SETTLED BY ARBITRATION IN SAN FRANCISCO, CA, UNDER
*  THE RULES OF THE INTERNATIONAL CHAMBER OF COMMERCE (ICC).
*
*****************************************************************************/


/*******************************************************************************
 *
 * Filename:
 * ---------
 *   CFG_PRODUCT_INFO_File.h
 *
 * Project:
 * --------
 *   YuSu
 *
 * Description:
 * ------------
 *    header file of main function
 *
 * Author:
 * -------
 *   Yuchi Xu(MTK81073)
 *
 *------------------------------------------------------------------------------
 *
 *******************************************************************************/

// Begin. Modified by jinming.xiang for 5185798

#ifndef _CFG_PRODUCT_INFO_FILE_H
#define _CFG_PRODUCT_INFO_FILE_H


// the record structure define of PRODUCT_INFO nvram file

#define _PRODUCT_INFO_SIZE_ 1024
typedef struct
{
	/*
	PCBA Info
	*/
	char ref_pcba[12];
	char short_code[4];
	char ics[2];
	char site_fac_pcba;
	char line_fac_pcba;
	char date_prod_pcba[3];
	char sn_pcba[4];

	/*
	Handset Info
	*/
	char indus_ref_handset[12];
	char info_ptm[2];
	char site_fac_handset;
	char line_fac_handset;
	char date_prod_handset[3];
	char sn_handset[4];

	/*
	Mini Info
	*/
	char info_pts_mini[3];
	char info_name_mini[20];
	char info_tech_mini[20];

	/*
	Golden Sample
	*/
	char info_golden_flag;
	char info_golden_date[3];

	/*
	HDTB(Reworked PCBA download)
	*/
	char info_id_baie_hdtb[3];
	char info_date_pass_hdtb[3];

	/*
	PT1 Test
	*/
	char info_prod_baie_para_sys[3];
	char info_status_para_sys;
	char info_nbre_pass_para_sys;
	char info_date_pass_para_sys[3];

	/*
	PT2 Test
	*/
	char info_prod_baie_para_sys_2[3];
	char info_status_para_sys_2;
	char info_nbre_pass_para_sys_2;
	char info_date_pass_para_sys_2[3];

	/*
	Bluetooth Test;
	From <MTK 3G&4G Smartphone&Tablet Trceability_Management V2.8>,
	this field means: PT3 Test(HW test station or LTE test station)
	*/
	char info_prod_baie_para_sys_3[3];
	char info_status_para_sys_3;
	char info_nbre_pass_para_sys_3;
	char info_date_pass_para_sys_3[3];

	/*
	Wifi Test
	*/
	char info_prod_baie_bw[3];
	char info_status_bw;
	char info_nbre_pass_bw;
	char info_date_baie_bw[3];

	/*
	GPS Test
	*/
	char info_prod_baie_gps[3];
	char info_status_gps;
	char info_nbre_pass_gps;
	char info_date_baie_gps[3];

	/*
	MMI Test
	*/
	char info_status_mmi_test;

	/*
	Final Test(Antenna Test)
	*/
	char info_prod_baie_final[3];
	char info_status_final;
	char info_nbre_pass_final;
	char info_date_baie_final[3];

	/*
	Final Test2(Antenna Test)
	*/
	char info_prod_baie_final_2[3];
	char info_status_final_2;
	char info_nbre_pass_final_2;
	char info_date_baie_final_2[3];

	/*
	HDT (CU perso download)
	*/
	char info_id_baie_hdt[3];
	char info_date_pass_hdt[3];

	/*
	CU SW Info
	*/
	char info_comm_ref[20];
	char info_pts_appli[3];
	char info_name_appli[20];
	char info_name_perso1[20];
	char info_name_perso2[20];
	char info_name_perso3[20];
	char info_name_perso4[20];
	char info_spare_region[20];
	/*
	test bit
	*/
	//int test;
}ap_nvram_trace_config_struct;

typedef struct
{
	unsigned char bt_addr[6];
	unsigned char wifi_addr[6];

} BT_WIFI_ADDR;
/*susheng.ye add for pro info function 20130427 end*/

// the record structure define of PRODUCT_INFO nvram file
typedef struct
{
    unsigned char imei[8];
    unsigned char svn;
    unsigned char pad;
} nvram_ef_imei_imeisv_struct;



typedef struct
{
	unsigned char latamimei_trigger[8];
	unsigned char tablet_sn_addr[15];
	unsigned char ppe_flag;
	unsigned char uart_log_flag;
	unsigned char kph_flag;
	unsigned char suid[32];
} LATAMIMEI_SN_PPE_UART_KPH_FLAG;

/*
typedef struct{
		unsigned char barcode[64];
		nvram_ef_imei_imeisv_struct IMEI[4];
		unsigned char reserved[1024-40-64];
}PRODUCT_INFO;
*/

typedef struct
{
	unsigned char info_dl_tool_id;
	unsigned char info_dl_tool_time[3];
	unsigned char reserved[10];
}ap_nvram_cc_zone_struct;

// Added by jinming.xiang for traceability ver 4.9.1 4593307
// psensor data
typedef struct
{
	unsigned char org_ps[2];
	unsigned char high_threshold[2];
	unsigned char low_threshold[2];
	unsigned char offset[2];
}psensor_data_struct;

// Minimode sFlag, Customer Zone, Meid, HCK Flag, ManuTestFlag, BOMTree Ver for traceability ver 4.9.1 4593307
typedef struct 
{
	unsigned char none;   // for traceability doc 4.9.1 (delete MINIMODE flag)
	unsigned char minimode_sflag[4];
	unsigned char c_zone[30];
	unsigned char meid[14];
	unsigned char hck_flag;
	unsigned char manutestflag[16];
	unsigned char bomtree_ver[2];
}minimode_c_zone_medi_hck_manutest_bomtree;

// Smart PA
typedef struct
{
	unsigned char ohm[4];
	unsigned char freq[4];
}smart_pa_struct;

// Sensor data
typedef struct 
{
	unsigned char gsensor_x_offset[8];
	unsigned char gsensor_y_offset[8];
	unsigned char gsensor_z_offset[8];
	unsigned char gyrosope_x_offset[8];
	unsigned char gyrosope_y_offset[8];
	unsigned char gyrosope_z_offset[8];
}sensor_data_struct;

// SmartPA2
typedef struct
{
	unsigned char ohm2[4];
	unsigned char freq2[4];
}smart_pa2_struct;

/*
typedef struct
 {
	unsigned char space[_PRODUCT_INFO_SIZE_\
		                -sizeof(ap_nvram_trace_config_struct)\
		                -sizeof(BT_WIFI_ADDR) \
				-4*sizeof(nvram_ef_imei_imeisv_struct)\
				-sizeof(LATAMIMEI_SN_PPE_UART_KPH_FLAG)\
				-sizeof(ap_nvram_cc_zone_struct)\
				-sizeof(psensor_data_struct)\
				-sizeof(minimode_c_zone_medi_hck_manutest_bomtree)\
				-sizeof(smart_pa_struct)\
				-sizeof(sensor_data_struct)\
				-sizeof(smart_pa2_struct)];
} RESERVED_S;
*/

#define RESERVED_S unsigned char reserved[_PRODUCT_INFO_SIZE_ \
		                -sizeof(ap_nvram_trace_config_struct) \
		                -sizeof(BT_WIFI_ADDR)  \
				-4*sizeof(nvram_ef_imei_imeisv_struct) \
				-sizeof(LATAMIMEI_SN_PPE_UART_KPH_FLAG) \
				-sizeof(ap_nvram_cc_zone_struct) \
				-sizeof(psensor_data_struct) \
				-sizeof(minimode_c_zone_medi_hck_manutest_bomtree)\
				-sizeof(smart_pa_struct) \
				-sizeof(sensor_data_struct) \
				-sizeof(smart_pa2_struct)];

typedef struct
{
	  ap_nvram_trace_config_struct trace_nvram_data;
	  unsigned char bt_addr[6];
	  unsigned char wifi_addr[6];
	  nvram_ef_imei_imeisv_struct imei_svn[4];
	  unsigned char latamimei_trigger[8];
	  unsigned char tablet_sn_addr[15];
	  unsigned char ppe_flag;
	  unsigned char uart_log_flag;
	  unsigned char kph_flag;
      ap_nvram_cc_zone_struct   cc_zone_data; 
      unsigned char suid[32];
      unsigned char none;   // for traceability doc 4.9.1 (delete MINIMODE flag)
      psensor_data_struct psensor_data;
      unsigned char minimode_sflag[4];
      unsigned char c_zone[30];
      smart_pa_struct smart_pa;
      unsigned char meid[14];
      unsigned char hck_flag;
      sensor_data_struct sensor_data;
      smart_pa2_struct smart_pa2;
      unsigned char manutestflag[16];
      unsigned char bomtree_ver[2];
	// RESERVED_S reserved;
	//  RESERVED_S
	
}PRODUCT_INFO;    //JRD TRACE STRUCT

// End. Modified by jinming.xiang for 5185798
//the record size and number of PRODUCT_INFO nvram file
#define CFG_FILE_PRODUCT_INFO_SIZE    sizeof(PRODUCT_INFO)
#define CFG_FILE_PRODUCT_INFO_TOTAL   1

#endif /* _CFG_PRODUCT_INFO_FILE_H */
