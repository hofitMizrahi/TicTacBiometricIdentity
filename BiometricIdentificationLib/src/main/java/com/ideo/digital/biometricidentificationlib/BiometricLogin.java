package com.ideo.digital.biometricidentificationlib;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Hofit Shalom
 *
 * This class wraps the biometric identity of {@link BiometricManager} library,
 * in a more convenient and easy-to-implement way.
 *
 */
public class BiometricLogin {

    private String title;
    private String description;
    private String cancel;
    private boolean needConfirmation;
    private String subTitle;
    private IBiometricLoginResult callback;

    private BiometricLogin(){
    }

    public static class Builder{

        private String title;
        private String description;
        private String cancel;
        private boolean needConfirmation;
        private String subTitle;
        private IBiometricLoginResult callback;

        /**
         * set dialog title
         *
         * @param title - dialog title text
         * @return this
         */
        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        /**
         * set dialog subtitle
         *
         * @param subTitle - dialog sub title text
         * @return this
         */
        public Builder setSubtitle(String subTitle){
            this.subTitle = subTitle;
            return this;
        }

        /**
         * set dialog confirmation
         *
         * @param needConfirmation - default is true
         * @return this
         */
        public Builder setConfirmationRequired(boolean needConfirmation){
            this.needConfirmation = needConfirmation;
            return this;
        }

        /**
         * set dialog cancel text button
         *
         * @param cancel - dialog cancel button text
         * @return this
         */
        public Builder setCancel(String cancel){
            this.cancel = cancel;
            return this;
        }

        /**
         * set dialog description
         *
         * @param description - dialog description text
         * @return
         */
        public Builder setDescription(String description){
            this.description = description;
            return this;
        }

        /**
         * constructor with callback param - must to implements {@link IBiometricLoginResult}
         *
         * @param callback - callback status
         */
        public Builder(IBiometricLoginResult callback) {
            this.callback = callback;
        }

        /**
         * @return {@link BiometricLogin} instance
         */
        public BiometricLogin build(){
            BiometricLogin biometricLogin = new BiometricLogin();
            biometricLogin.title = this.title;
            biometricLogin.description = this.description;
            biometricLogin.needConfirmation = this.needConfirmation;
            biometricLogin.cancel = this.cancel;
            biometricLogin.subTitle = this.subTitle;
            biometricLogin.callback = this.callback;
            return biometricLogin;
        }
    }

    public void authenticate(Context context){

        Pair<Boolean, String> haveBiometricsOption = BiometricsUtils.checkBiometricsAvailable(context);

        if(haveBiometricsOption.first){
            startAuthenticate(context);
        }else {
            callback.biometricResultStatus(false, haveBiometricsOption.second);
        }
    }

    private void startAuthenticate(Context context) {

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setDescription(subTitle)
                .setConfirmationRequired(needConfirmation)
                .setDescription(description)
                .setNegativeButtonText(cancel)
                .setConfirmationRequired(false)
                .build();

        Executor newExecutor = Executors.newSingleThreadExecutor();

        if(context instanceof FragmentActivity) {
            BiometricPrompt myBiometricPrompt = new BiometricPrompt((FragmentActivity) context, newExecutor, authenticationCallback);
            myBiometricPrompt.authenticate(promptInfo);
        }
    }

    private final BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.i("HofitTestLogin", "error = " + errString);
            callback.biometricResultStatus(false, getBiometricErrorMessage(errorCode));
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Log.i("HofitTestLogin", "error = " + "onAuthenticationFailed()");
            callback.biometricResultStatus(false, "biometric is valid but not recognized");
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Log.i("HofitTestLogin", "onAuthenticationSucceeded()");
            callback.biometricResultStatus(true, "SUCCESS");
        }
    };

    private String getBiometricErrorMessage(int errCode) {
        switch (errCode) {
            case BiometricPrompt.ERROR_CANCELED:
                return "SystemCancel";
            case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                return "FingerprintScannerNotSupported";
            case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                return "FingerprintScannerNotAvailable";
            case BiometricPrompt.ERROR_LOCKOUT:
                return "DeviceLocked";
            case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                return "DeviceLockedPermanent";
            case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                return "UserCancel";
            case BiometricPrompt.ERROR_NO_BIOMETRICS:
                return "FingerprintScannerNotEnrolled";
            case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                return "PasscodeNotSet";
            case BiometricPrompt.ERROR_NO_SPACE:
                return "DeviceOutOfMemory";
            case BiometricPrompt.ERROR_TIMEOUT:
                return "AuthenticationTimeout";
            case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                return "AuthenticationProcessFailed";
            case BiometricPrompt.ERROR_USER_CANCELED:
                return "UserFallback";
            case BiometricPrompt.ERROR_VENDOR:
                return "HardwareError";
            default:
                return "FingerprintScannerUnknownError";
        }
    }
}

