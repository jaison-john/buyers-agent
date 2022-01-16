package com.buyersAgent.domainSiteApi;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ListingsApi;

public class CallDomain {

    public static void main(String args[]){
        ApiClient apiClient= createApiClient();
        ListingsApi listingsApi = new ListingsApi();
        listingsApi.setApiClient(apiClient);
        listingsApi.listingsGet(2017412596);
    }

    private static ApiClient createApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("https://api.domain.com.au/");
        apiClient.setApiKey("key_6a7a40bcbcd676098ce9280256e98b45");
        return apiClient;
    }
}
