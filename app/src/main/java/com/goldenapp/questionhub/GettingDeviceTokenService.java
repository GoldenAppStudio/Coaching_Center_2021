package com.goldenapp.questionhub;

import com.google.firebase.iid.FirebaseInstanceId;


public class GettingDeviceTokenService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        android.util.Log.d("Device Token: ",deviceToken);
    }
}
