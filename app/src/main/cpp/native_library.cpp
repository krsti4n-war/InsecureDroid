#include <jni.h>
#include <string>
#include <sstream>
#include <iomanip>

// XOR Encryption method
std::string encrypt(JNIEnv *env, jobject context, jstring inputString) {
    const char *input = env->GetStringUTFChars(inputString, nullptr);
    const char *xorKey = "XOR"; // Hardcoded XOR key

    std::string encryptedString;
    size_t inputLength = strlen(input);
    size_t keyLength = strlen(xorKey);

    for (size_t i = 0; i < inputLength; i++) {
        char encryptedChar = input[i] ^ xorKey[i % keyLength];
        encryptedString += encryptedChar;
    }

    // Convert encrypted string to hexadecimal format
    std::stringstream hexStream;
    hexStream << std::hex << std::setfill('0');
    for (char c : encryptedString) {
        hexStream << std::setw(2) << static_cast<int>(c);
    }
    std::string encryptedHex = hexStream.str();

    // Release JNI objects and return the encrypted string in hexadecimal format
    env->ReleaseStringUTFChars(inputString, input);
    return encryptedHex;
}

extern "C" JNIEXPORT jobject JNICALL
Java_lab_insecuredroid_challenges_NativeLibrary_checkPass(JNIEnv *env, jclass /*thiz*/,
                                                jobject context, jstring inputString) {
    std::string storedPass = "0b3a026b1d016b0c206b1b012c1d3b1608";
    std::string encryptedHex = encrypt(env, context, inputString);

    jboolean result = storedPass == encryptedHex ? JNI_TRUE : JNI_FALSE;
    jclass booleanClass = env->FindClass("java/lang/Boolean");
    jmethodID valueOfMethod = env->GetStaticMethodID(booleanClass, "valueOf", "(Z)Ljava/lang/Boolean;");
    jobject booleanObject = env->CallStaticObjectMethod(booleanClass, valueOfMethod, result);

    return booleanObject;
}