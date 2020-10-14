#include <string.h>
#include <stdlib.h>
#include <time.h>
#include"com_woshidaniu_license_LicenseOps.h"
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
static char *readLicense(const char *);
static void writeLicense(const char*,const char *);

static char *readLicense(const char *path)
{
	FILE * fp;
	int size;
	char *buffer;
	char *auth_data_base64decode;
	char *auth_data_orgi;
	if(path == NULL){
		return NULL;
	}
	//读取文件信息
	fp = fopen(path,"rb");
	if(fp == NULL){
		return NULL;
	}
	fseek(fp , 0 , SEEK_END);
    size = ftell(fp);
    rewind (fp);
	buffer = (char*) malloc(sizeof(char)*(size+1));
    fread (buffer,1, size, fp);
    buffer[size] = 0;
    auth_data_base64decode = base64_decode(buffer,strlen(buffer));
    decrypt(auth_data_base64decode,strlen(auth_data_base64decode));
    auth_data_orgi = base64_decode(auth_data_base64decode,strlen(auth_data_base64decode));
    fclose(fp);
    free(buffer);
    free(auth_data_base64decode);
	return auth_data_orgi;
}

static void writeLicense(const char* str,const char *path)
{
	if(path == NULL){
		return;
	}

	FILE * ptr;
	ptr = fopen(path,"wb");
	if(ptr != NULL){
		char *str_encode_1 = base64_encode(str,strlen(str));
		encrypt(str_encode_1,strlen(str_encode_1));
		char *str_encode_2 = base64_encode(str_encode_1,strlen(str_encode_1));
		fputs(str_encode_2,ptr);
		fclose(ptr);
		free(str_encode_1);
		free(str_encode_2);
	}
	return;
}


/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getDevMode
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getDevMode
	(JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);
	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"_DEV_MODE")->valuestring);
	cJSON_Delete(root);
	free(data);
	free(path_str);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getAuthId
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getAuthId
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);
	//printf("path_str = %s\n",path_str);
	cJSON * root;
	char ret_val[1024] = {'\0'};

	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"_AUTH_ID")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getAuthUser
 * Signature: ()[B
 * 读取授权文件的authuser字段
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getAuthUser
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);
	//printf("license file path : %s\n", path_str);
	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"AUTH_USER")->valuestring);
	cJSON_Delete(root);
	free(data);
	free(path_str);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getAuthUserCode
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getAuthUserCode
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);
	//printf("path_str = %s\n",path_str);
	cJSON * root;
	char ret_val[1024] = {'\0'};

	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"AUTH_USER_CODE")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}


/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getStartDate
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getStartDate
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"START_DATE")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getExpireDate
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getExpireDate
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"EXPIRE_DATE")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getAuthDate
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getAuthDate
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"_AUTH_DATE")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getEncryptSHA
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getEncryptSHA
	(JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"_ENCRYPT_SHA")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getEncryptSHA
 * Signature: ()[B
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_getProductName
	(JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char ret_val[1024] = {'\0'};
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return NULL;
	}
	root = cJSON_Parse(data);
	strcpy(ret_val,cJSON_GetObjectItem(root,"_PRODUCT_NAME")->valuestring);
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return (*env)->NewStringUTF(env,ret_val);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getUsage
 * Signature: ()[B
 */
JNIEXPORT jint JNICALL Java_com_woshidaniu_license_LicenseOps_getUsage
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char *data = readLicense(path_str);
	if(data ==	NULL){
		free(path_str);
		free(data);
		return -1;
	}
	root = cJSON_Parse(data);
	jint ret = (jint)cJSON_GetObjectItem(root,"USAGE")->valueint;
	cJSON_Delete(root);
	free(path_str);
	free(data);
	return ret;
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getUsageCount
 * Signature: ()[B
 */
JNIEXPORT jint JNICALL Java_com_woshidaniu_license_LicenseOps_getUsageCount
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

	cJSON * root;
	char *data = readLicense(path_str);
	if(data == NULL){
		free(path_str);
		free(data);
		return -1;
	}
	root = cJSON_Parse(data);
	jint ret = (jint)cJSON_GetObjectItem(root,"USAGE_COUNT")->valueint;
	cJSON_Delete(root);
	free(data);
	return ret;
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    increseUsageCount
 * Signature: ()V
 */
JNIEXPORT jstring JNICALL Java_com_woshidaniu_license_LicenseOps_increseUsageCount
  (JNIEnv *env, jobject jthis){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);
	//printf("path:%s\n",path_str);
	char _cur_time_s[20],_usage_s[20],_usage_count_s[20];
	sprintf(_cur_time_s,"%d", (int)time(NULL));
	//1.修改LAST_ACCES
	//2.先检查是否过期（USAGE == USAGE_COUNT）
	//3.如果已过期，Nothing
	//4.如果没过期，USAGE_COUNT++,
	//5.计算SHA
	//6.返回SHA值
	char *_data_ = readLicense(path_str);

	if(_data_ == NULL){
		free(path_str);
		free(_data_);
		return (*env)->NewStringUTF(env,"-1");
	}
	cJSON *_c_data_ = cJSON_Parse(_data_);
	char *_AUTH_ID = cJSON_GetObjectItem(_c_data_,"_AUTH_ID")->valuestring;
	char *AUTH_USER = cJSON_GetObjectItem(_c_data_,"AUTH_USER")->valuestring;
	char *AUTH_USER_CODE = cJSON_GetObjectItem(_c_data_,"AUTH_USER_CODE")->valuestring;
	int  USAGE=  cJSON_GetObjectItem(_c_data_,"USAGE")->valueint;
	int  USAGE_COUNT=  cJSON_GetObjectItem(_c_data_,"USAGE_COUNT")->valueint;
	int  INIT=  cJSON_GetObjectItem(_c_data_,"INIT")->valueint;
	/**
	 * 如果是初始化状态，改变状态为已初始化
	 */
	if(INIT == 1){
		cJSON_GetObjectItem(_c_data_,"INIT")->valueint = 0;
		cJSON_GetObjectItem(_c_data_,"INIT")->valuedouble = 0;
	}
	/**
	 * 如果没过期
	 */
	if(USAGE > USAGE_COUNT){
		cJSON_GetObjectItem(_c_data_,"USAGE_COUNT")->valueint = (USAGE_COUNT+1);
		cJSON_GetObjectItem(_c_data_,"USAGE_COUNT")->valuedouble = (USAGE_COUNT+1);
	}
	sprintf(_usage_s,"%d",USAGE);
	sprintf(_usage_count_s,"%d",USAGE_COUNT);
	char *sha_ = malloc(strlen(_AUTH_ID)+strlen(AUTH_USER)+strlen(AUTH_USER_CODE)+strlen(_usage_s)+strlen(_usage_count_s)+strlen(_cur_time_s)+1);
	char ret_arr[1024] = {'\0'};
	strcpy(sha_, _AUTH_ID);
	strcat(sha_, AUTH_USER);
	strcat(sha_, AUTH_USER_CODE);
	strcat(sha_, _usage_s);
	strcat(sha_, _usage_count_s);
	strcat(sha_, _cur_time_s);
	char *sha_base64 = base64_encode(sha_,strlen(sha_));
	encrypt(sha_base64,strlen(sha_base64));
	char *ret = base64_encode(sha_base64,strlen(sha_base64));
	strcpy(ret_arr,ret);

	//memery leak!!!
	//cJSON_GetObjectItem(_c_data_,"_ENCRYPT_SHA")->valuestring = ret;
	//cJSON_GetObjectItem(_c_data_,"_LAST_ACCESS")->valuestring = _cur_time_s;
	//memery leak!!!

	cJSON_ReplaceItemInObject(_c_data_,"_ENCRYPT_SHA",cJSON_CreateString(ret));
	cJSON_ReplaceItemInObject(_c_data_,"_LAST_ACCESS",cJSON_CreateString(_cur_time_s));

	char *rendered = cJSON_Print(_c_data_);
	writeLicense(rendered,path_str);

	free(path_str);
	free(_data_);
    free(sha_);
	free(sha_base64);
	free(rendered);
	cJSON_Delete(_c_data_);
	return (*env)->NewStringUTF(env,ret_arr);
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    checkStatus
 * Signature: ()[B
 */
JNIEXPORT jint JNICALL Java_com_woshidaniu_license_LicenseOps_checkStatus
  (JNIEnv *env, jobject jthis, jstring _SHA, jstring _DATE){
	jclass clazz = (*env)->GetObjectClass(env,jthis);
	jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
	jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
	char *path_str = jstring2charArray(env,m_fid_license_file_path_str);
	//printf("path:%s\n",path_str);
	jint code = -1;
	char *_SHA_CHAR = jstring2charArray(env,_SHA);
	char *_DATE_CHAR = jstring2charArray(env,_DATE);
	char *_data_ = readLicense(path_str);
	if(_data_ == NULL){
		free(_SHA_CHAR);
		free(_DATE_CHAR);
		free(path_str);
		free(_data_);
	  	return -1;
	}
	cJSON *_c_data_ = cJSON_Parse(_data_);
	char *_ENCRYPT_SHA = cJSON_GetObjectItem(_c_data_,"_ENCRYPT_SHA")->valuestring;
	char *_START_DATE = cJSON_GetObjectItem(_c_data_,"START_DATE")->valuestring;
	char *_EXPIRE_DATE = cJSON_GetObjectItem(_c_data_,"EXPIRE_DATE")->valuestring;
	int  USAGE=  cJSON_GetObjectItem(_c_data_,"USAGE")->valueint;
	int  USAGE_COUNT=  cJSON_GetObjectItem(_c_data_,"USAGE_COUNT")->valueint;
	int  _ALERT=  cJSON_GetObjectItem(_c_data_,"_ALERT")->valueint;
	int  _INIT=  cJSON_GetObjectItem(_c_data_,"INIT")->valueint;
	//1.检查INIT标志位
	if(_INIT == 1){
		code = 0;
	//2.检查_SHA的值是否匹配
	}else if(strcmp(_SHA_CHAR,_ENCRYPT_SHA) != 0){
		code = 4;
	//3.计算是否过期
	}else if((strcmp(_START_DATE,_DATE_CHAR)>0) || (strcmp(_EXPIRE_DATE,_DATE_CHAR)<0)){
		code = 3;
	//4.计算使用时间是否超出
	}else if(USAGE <= USAGE_COUNT){
		code = 3;
	}else if((USAGE-USAGE_COUNT) <= _ALERT){
		code = 2;
	}else if((USAGE-USAGE_COUNT) > _ALERT){
		code = 1;
	}
	free(path_str);
	free(_data_);
	free(_SHA_CHAR);
	free(_DATE_CHAR);
	cJSON_Delete(_c_data_);
	return code;
}

/*
 * Class:     com_woshidaniu_license_LicenseOps
 * Method:    getAvialableTime
 * Signature: ()[B
 */
JNIEXPORT jint JNICALL Java_com_woshidaniu_license_LicenseOps_getAvilableTime
  (JNIEnv *env, jobject jthis){
	    jclass clazz = (*env)->GetObjectClass(env,jthis);
		jfieldID  m_fid_license_file_path = (*env)->GetFieldID(env, clazz, "licenseFilePath", "Ljava/lang/String;");
		jstring m_fid_license_file_path_str = (jstring)(*env)->GetObjectField(env, jthis, m_fid_license_file_path);
		char *path_str = jstring2charArray(env,m_fid_license_file_path_str);

		char *_data_ = readLicense(path_str);
		if(_data_ == NULL){
			free(path_str);
			free(_data_);
			return -1;
		}
		cJSON *_c_data_ = cJSON_Parse(_data_);
		int  USAGE=  cJSON_GetObjectItem(_c_data_,"USAGE")->valueint;
		int  USAGE_COUNT=  cJSON_GetObjectItem(_c_data_,"USAGE_COUNT")->valueint;
		free(path_str);
		free(_data_);
		cJSON_Delete(_c_data_);
		return USAGE-USAGE_COUNT;
}

