package com.ideo.digital.biometricidentificationlib;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.StringRes;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;

public class BiometricsUtils {

    private BiometricsUtils() {
    }

    private static boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static Pair<Boolean, String> checkBiometricsAvailable(Context context) {

        boolean canPerformBiometricLogin = false;
        String errorMessage = "";

        if (isSdkVersionSupported()) {

            int status = BiometricManager.from(context).canAuthenticate();

            switch (status) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    //no need message
                    canPerformBiometricLogin = true;
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    errorMessage = "FingerprintScannerNotSupported";
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    errorMessage = "FingerprintScannerNotEnrolled";
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    errorMessage = "FingerprintScannerNotAvailable";
                    break;
                default:
                    errorMessage = "fail";
            }
        } else {
            errorMessage = "biometricErrorVersionNotSupported";
        }

        return new Pair<>(canPerformBiometricLogin, errorMessage);
    }
}
