# TicTacBiometricIdentity
Easy Biometric Identity Library


 * This class wraps the biometric identity of {@link BiometricManager} library.
 * use for more convenient and easy-to-implement way.
 
 
 just look how to set the biometric dialog and do authentication easy..
 
         BiometricLogin biometricLogin = new BiometricLogin.Builder(this)
                .setDescription("my description")
                .setSubtitle("my subtitle")
                .setConfirmationRequired(false)
                .setCancel("my close button text")
                .setTitle("my title")
                .build();

        biometricLogin.authenticate(this);
        
        
for getting biometric status callback just implement IBiometricLoginResult interfate and pass it into authenticate(callback) method

    @Override
    public void biometricResultStatus(boolean isSuccess, String status) {
        Log.i(TAG, "isSuccess = " + isSuccess + ", status = " + status);
    }    
    
and that's it!    
