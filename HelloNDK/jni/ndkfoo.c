#include <string.h>
#include <jni.h>

jstring Java_com_vm_hellondk_MainActivity_invokeNativeFunction(JNIEnv* env,
		jobject javaThis) {
	return (*env)->NewStringUTF(env, "Manoj Behera: Hello!");
}

JNIEXPORT jbyteArray JNICALL Java_com_vm_hellondk_MainActivity_returnArray(JNIEnv *env,
		jobject o) {
	jbyte a[] = {1,2,3,4,5,6};
	jbyteArray ret = (*env)->NewByteArray(env, 6);
	(*env)->SetByteArrayRegion(env, ret, 0, 6, a);
	return ret;
}
