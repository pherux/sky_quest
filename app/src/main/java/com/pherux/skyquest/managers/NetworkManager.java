package com.pherux.skyquest.managers;

import com.pherux.skyquest.Constants;
import com.pherux.skyquest.network.WebService;

import retrofit.RestAdapter;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager networkManager;
    private RestAdapter restAdapter;
    private WebService service;

    private NetworkManager() {
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.MOBILE_SERVICE_HOST)
                    .build();

        }
    }

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    public WebService getService() {
        if (service == null) {
            service = restAdapter.create(WebService.class);
        }
        return service;
    }

}
