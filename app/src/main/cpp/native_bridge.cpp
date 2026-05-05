#include <sys/wait.h>
#include <jni.h>
#include <unistd.h>
#include <sys/ptrace.h>
#include <dlfcn.h>

// Глобальные настройки читов
static bool esp_box = false;
static bool snaplines = false;
static bool silent_aim = false;
static int aim_fov = 90;
static bool chams = false;
static bool bunnyhop = false;

extern "C" {

JNIEXPORT void JNICALL Java_com_xiotty_dlc_NativeBridge_setESPBox(JNIEnv*, jobject, jboolean val) { esp_box = val; }
JNIEXPORT void JNICALL Java_com_xiotty_dlc_NativeBridge_setSnaplines(JNIEnv*, jobject, jboolean val) { snaplines = val; }
JNIEXPORT void JNICALL Java_com_xiotty_dlc_NativeBridge_setSilentAim(JNIEnv*, jobject, jboolean val) { silent_aim = val; }
JNIEXPORT void JNICALL Java_com_xiotty_dlc_NativeBridge_setAimFOV(JNIEnv*, jobject, jint val) { aim_fov = val; }
JNIEXPORT void JNICALL Java_com_xiotty_dlc_NativeBridge_setChams(JNIEnv*, jobject, jboolean val) { chams = val; }
JNIEXPORT void JNICALL Java_com_xiotty_dlc_NativeBridge_setBunnyHop(JNIEnv*, jobject, jboolean val) { bunnyhop = val; }

JNIEXPORT jboolean JNICALL Java_com_xiotty_dlc_NativeBridge_injectLibrary(JNIEnv* env, jobject, jint pid, jstring path) {
    const char* lib_path = env->GetStringUTFChars(path, nullptr);
    if (ptrace(PTRACE_ATTACH, pid, nullptr, nullptr) == -1) return JNI_FALSE;
    waitpid(pid, nullptr, 0);
    void* dlopen_addr = dlsym(RTLD_DEFAULT, "dlopen");
    ptrace(PTRACE_POKEDATA, pid, dlopen_addr, (void*)lib_path);
    ptrace(PTRACE_DETACH, pid, nullptr, nullptr);
    env->ReleaseStringUTFChars(path, lib_path);
    return JNI_TRUE;
}

}
