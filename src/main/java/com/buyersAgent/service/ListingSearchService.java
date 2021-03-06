package com.buyersAgent.service;

import org.openapitools.client.model.DomainSearchServiceV2ModelDomainSearchContractsV2SearchResult;
import org.openapitools.client.model.DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters;
import org.springframework.web.client.RestClientException;

import java.util.List;

public interface ListingSearchService {

    List<DomainSearchServiceV2ModelDomainSearchContractsV2SearchResult> listingsDetailedResidentialSearchWithHttpInfo(DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters domainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters) throws RestClientException ;
}
