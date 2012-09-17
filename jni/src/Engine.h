#ifndef _ENGINE_H
#define _ENGINE_H

#include "mr_helper.h"
#include <android/log.h>


#define SCNW 240
#define SCNH 320
#define SCNBIT 16

#define SCNBUF_COUNT	2

#define BITMAPMAX  30
#define SPRITEMAX  10
#define TILEMAX    3
#define SOUNDMAX   5

#define DSM_MEM_SIZE   (2*1024*1024)	//DSMÄÚ´æ´óÐ¡20M

#define START_FILE_NAME "cfunction.ext"

///
#define MOD_MMI 0
#define kal_prompt_trace(module_type, fmt, ...) \
	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, __VA_ARGS__)
#define LOG_TAG "Mrpoid"
#define LOGI(...) \
	((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#define LOGW(...) \
	((void)__android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__))
#define LOGE(...) \
	((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))


extern const mr_table mr_sys_table;
extern const mr_internal_table mr_sys_internal_tabl;
extern const mr_c_port_table mr_sys_c_port_table;

void mr_getScreenSize(int32 *w, int32 *h);
uint16 *w_getScreenBuffer(void);
void *mr_readFileFromMrp(const char *filename, int32 *filelen, int32 lookfor);

#endif