#include <string.h>
#include <stdlib.h>
#include"com_woshidaniu_license_admin_LicenseAdminOps.h"
#include"c_base64.h"
#include"cJSON.h"
#include"cipher.h"

//__asm__(".symver memcpy,memcpy@GLIBC_2.2.5");

/**
 * jstring to char*
 */
static char *jstring2charArray(JNIEnv *env, jstring js){
	char* rtn = NULL;
	jclass jeclass = (*env)->FindClass(env,"java/lang/String");
	jstring jsencode = (*env)->NewStringUTF(env,"utf-8");
	jmethodID mid = (*env)->GetMethodID(env, jeclass, "getBytes", "(Ljava/lang/String;)[B");
	jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env, js, mid, jsencode);
	jsize alen = (*env)->GetArrayLength(env,barr);
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0)
	   {
			 rtn = (char*)malloc(alen + 1);
			 memcpy(rtn, ba, alen);
			 rtn[alen] = 0;
	   }
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0);
	return rtn;
}


/*
 * Class:     com_woshidaniu_license_admin_LicenseAdminOps
 * Method:    generateLicenseFile
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_admin_LicenseAdminOps_generateLicenseFile
  (JNIEnv *env, jobject jthis, jstring authid, jstring authuser, jstring authusercode, jstring authdate, jstring startdate, jstring expiredate, jint usage, jint usagecount, jint alert, jstring devmode, jstring productname)
{
	char *_authid = jstring2charArray(env, authid);
	char *_authuser = jstring2charArray(env, authuser);
	char *_authusercode = jstring2charArray(env, authusercode);
	char *_authdate = jstring2charArray(env, authdate);
	char *_startdate = jstring2charArray(env, startdate);
	char *_expiredate = jstring2charArray(env, expiredate);
	char *_devmode = jstring2charArray(env, devmode);
	char *_productname = jstring2charArray(env, productname);

	int _init = 1;

	cJSON *root = cJSON_CreateObject();
	cJSON_AddStringToObject(root,"_AUTH_ID",_authid);
	cJSON_AddStringToObject(root,"AUTH_USER",_authuser);
	cJSON_AddStringToObject(root,"AUTH_USER_CODE",_authusercode);
	cJSON_AddStringToObject(root,"_AUTH_DATE",_authdate);
	cJSON_AddStringToObject(root,"START_DATE",_startdate);
	cJSON_AddStringToObject(root,"EXPIRE_DATE",_expiredate);
	cJSON_AddNumberToObject(root,"INIT",_init);
	cJSON_AddNumberToObject(root,"USAGE",(int)usage);
	cJSON_AddNumberToObject(root,"USAGE_COUNT",(int)usagecount);
	cJSON_AddNumberToObject(root,"_ALERT",(int)alert);
	cJSON_AddStringToObject(root,"_DEV_MODE",_devmode);
	cJSON_AddStringToObject(root,"_PRODUCT_NAME",_productname);

	char _cur_time_s[20],_usage_s[20],_usage_count_s[20];
	sprintf(_cur_time_s,"%d", time(NULL));
	sprintf(_usage_s,"%d",usage);
	sprintf(_usage_count_s,"%d",usagecount);
	//计算SHA
	char * sha_ = malloc(strlen(_authid) +
				   strlen(_authuser) +
				   strlen(_authusercode) +
				   strlen(_usage_s) +
				   strlen(_usage_count_s) +
				   strlen(_cur_time_s) +
				   strlen(_productname) + 1);
	strcpy(sha_, _authid);
	strcat(sha_, _authuser);
	strcat(sha_, _authusercode);
	strcat(sha_, _usage_s);
	strcat(sha_, _usage_count_s);
	strcat(sha_, _cur_time_s);
	strcat(sha_, _productname);
	printf("sha_原值:%s\n",sha_);
	char *sha_base64 = base64_encode(sha_,strlen(sha_));
	//printf("sha_base64后的值:%s\n",sha_base64);
	encrypt(sha_base64,strlen(sha_base64));
	//printf("sha_base64加密后的值:%s\n",sha_base64);
	char *sha_base64_encrypt_base64 = base64_encode(sha_base64,strlen(sha_base64));
	//printf("sha_加密哈希后的值:%s\n",sha_base64_encrypt_base64);
	cJSON_AddStringToObject(root,"_ENCRYPT_SHA", sha_base64_encrypt_base64);
	cJSON_AddStringToObject(root,"_LAST_ACCESS", _cur_time_s);
	char *out = cJSON_Print(root);
	//printf("JSON原值:%s\n",out);
	free(sha_);
	char *_s_encode = base64_encode(out,strlen(out));
	//printf("JSON编码后的值:%s\n",_s_encode);
	encrypt(_s_encode,strlen(_s_encode));
	//printf("JSON编码加密后的值:%s\n",_s_encode);
	char *finan_ret = base64_encode(_s_encode,strlen(_s_encode));
	//printf("JSON编码加密编码后的值:%s\n",finan_ret);

	char _final[10240] = {'\0'};

	strcpy(_final,finan_ret);

	/***
	 * call free release memory
	 */
	free(finan_ret);
	free(_s_encode);
	free(sha_base64_encrypt_base64);
	free(sha_base64);
	free(_authid);
	free(_authuser);
	free(_authusercode);
	free(_authdate);
	free(_startdate);
	free(_expiredate);
	free(_devmode);
	free(_productname);
	return (*env)->NewStringUTF(env, _final);
}

