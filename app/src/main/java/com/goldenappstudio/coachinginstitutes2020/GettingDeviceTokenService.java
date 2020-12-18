package com.goldenappstudio.coachinginstitutes2020;

import com.google.firebase.iid.FirebaseInstanceId;


public class GettingDeviceTokenService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        android.util.Log.d("Device Token: ",deviceToken);
    }
}
