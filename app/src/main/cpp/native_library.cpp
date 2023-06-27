#include <jni.h>
#include <string>
#include <sstream>
#include <iomanip>

// XOR Encryption method
std::string encrypt(JNIEnv *env, jstring inputString) {
    const char *input = env->GetStringUTFChars(inputString, nullptr);
    const char *xorKey = "XOR";

    std::string encryptedString;
    size_t inputLength = strlen(input);
    size_t keyLength = strlen(xorKey);

    for (size_t i = 0; i < inputLength; i++) {
        char encryptedChar = input[i] ^ xorKey[i % keyLength];
        encryptedString += encryptedChar;
    }

    std::stringstream hexStream;
    hexStream << std::hex << std::setfill('0');
    for (char c : encryptedString) {
        hexStream << std::setw(2) << static_cast<int>(c);
    }
    std::string encryptedHex = hexStream.str();

    env->ReleaseStringUTFChars(inputString, input);
    return encryptedHex;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_lab_insecuredroid_challenges_NativeLibrary_checkPass(JNIEnv *env, jclass /*thiz*/,
                                                jobject context, jstring inputString) {
    std::string storedPass = "0b3a026b1d016b0c206b1b012c1d3b1608";
    std::string encryptedHex = encrypt(env, inputString);

    bool result = (storedPass == encryptedHex);
    return result;
}