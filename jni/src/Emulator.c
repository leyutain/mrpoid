#include <jni.h>

#include <android/log.h>
#include <android/bitmap.h>
#include <android/keycodes.h>
#include <malloc.h>
#include <string.h>

#include "mr_helper.h"
#include "Engine.h"




//#define  LOG_TAG    "libplasma"
//#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
//#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


extern unsigned char ic_dir[512];

static JavaVM		*gs_jvm=NULL;
static jobject		selfobj, bitmap;
static jmethodID	id_flush; //刷新画布函数 ID


static int transKeycode(int andcode)
{
	switch(andcode)
	{
	case AKEYCODE_DPAD_UP: return MR_KEY_UP;
	}
}

JNIEXPORT int JNICALL 
Java_com_tszy_core_Emulator_loadMrp(JNIEnv * env, jobject self, jstring path)
{
	const char *str;

	str = (*env)->GetStringUTFChars(env, path, FALSE);
	if(str){
		//LOGI("start dsm: %s", str);

		mr_start_dsm(str);	
		(*env)->ReleaseStringUTFChars(env, path, str);
		return 0;
	}

	return -1; //失败返回-1
}

//初始化模拟器
//一张函数表
JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_init(JNIEnv * env, jobject self)
{
	jclass cls;

	//Returns “0” on success; returns a negative value on failure. 
	(*env)->GetJavaVM(env, &gs_jvm);
	
	//直接保存obj到DLL中的全局变量是不行的,应该调用以下函数:
	selfobj = (*env)->NewGlobalRef(env, self);

	cls = (*env)->GetObjectClass(env, selfobj);
	id_flush = (*env)->GetMethodID(env, cls, "flush", "(IIII)V");
}

JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_setBitmap(JNIEnv * env, jobject self, jobject jbitmap)
{
	bitmap = (*env)->NewGlobalRef(env, jbitmap);
}

JNIEXPORT int JNICALL 
Java_com_tszy_core_Emulator_getScnWidth(JNIEnv * env, jobject self)
{
	return SCNW;
}

JNIEXPORT int JNICALL 
Java_com_tszy_core_Emulator_getScnHeight(JNIEnv * env, jobject self)
{
	return SCNH;
}

JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_touchDown(JNIEnv * env, jobject self, jint x, jint y)
{
	LOGI("touchDown %d,%d", x, y);
}

JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_touchMove(JNIEnv * env, jobject self, jint x, jint y)
{
	LOGI("touchMove %d,%d", x, y);
}

JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_touchUp(JNIEnv * env, jobject self, jint x, jint y)
{
	LOGI("touchUp %d,%d", x, y);
}

JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_onKeyDown(JNIEnv * env, jobject self, jint key)
{
	LOGI("onKeyDown %d", key);
}

JNIEXPORT void JNICALL 
Java_com_tszy_core_Emulator_onKeyUp(JNIEnv * env, jobject self, jint key)
{
	LOGI("onKeyUp %d", key);
}

///////////////////////////////////////////////////////////////////////////
void drawBitmap(uint16* data, int x, int y, int w, int h)
{
	AndroidBitmapInfo  info;
	void*			 pixels;
	int                ret, i;
	int				x1, y1, r, b;
	int32			sw, sh;
	JNIEnv			*env;
	uint16			*p;
	
	(*gs_jvm)->AttachCurrentThread(gs_jvm, &env, NULL);

	mr_getScreenSize(&sw, &sh);

	if(x>=sw || y>=sh || w<=0 || h<=0)
		return;

	r = sw-1, b = sh-1;
	x1 = x+w-1, y1 = y+h-1;
	clip_rect(&x, &y, &x1, &y1, r, b);
	w = x1-x+1, h = y1-y+1;
	
	if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
		return;
	}
	if (info.format != ANDROID_BITMAP_FORMAT_RGB_565) {
		LOGE("Bitmap format is not RGB_565 !");
		return;
	}
	if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
		LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	p = (uint16 *)pixels;
	for (i=y; i<y1; i++)
		memcpy((p + (sw*i) + x), (data + (sw*i) + x), w*2);

	AndroidBitmap_unlockPixels(env, bitmap);

	//刷新到屏幕
	if (id_flush != NULL) {
		(*env)->CallVoidMethod(env, selfobj, id_flush,
			x, y, w, h);
	}

	LOGI("flush(x:%d, y:%d, w:%d, h:%d)", x,y,w,h);
}