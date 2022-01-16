package com.buyersAgent.model;

import org.openapitools.client.model.DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing;
import org.openapitools.client.model.DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchLocation;
import org.openapitools.client.model.DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters;

import java.util.List;

public class ListingSearchCriteria {

    private float minBedrooms;

    private float maxBathrooms;

    private Integer minCarspaces;

    private Integer maxCarspaces;

    private DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing.ListingTypeEnum listingTypeEnum;

    private DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.PropertyTypesEnum propertyTypesEnum;

    private List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.PropertyFeaturesEnum> propertyFeatures;

    private List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.ListingAttributesEnum> listingAttributes;

    private Integer maxLandArea;

    private Integer minLandArea;

    private List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchLocation> locations = null;

    public float getMinBedrooms() {
        return minBedrooms;
    }

    public void setMinBedrooms(float minBedrooms) {
        this.minBedrooms = minBedrooms;
    }

    public float getMaxBathrooms() {
        return maxBathrooms;
    }

    public void setMaxBathrooms(float maxBathrooms) {
        this.maxBathrooms = maxBathrooms;
    }

    public Integer getMinCarspaces() {
        return minCarspaces;
    }

    public void setMinCarspaces(Integer minCarspaces) {
        this.minCarspaces = minCarspaces;
    }

    public Integer getMaxCarspaces() {
        return maxCarspaces;
    }

    public void setMaxCarspaces(Integer maxCarspaces) {
        this.maxCarspaces = maxCarspaces;
    }

    public DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing.ListingTypeEnum getListingTypeEnum() {
        return listingTypeEnum;
    }

    public void setListingTypeEnum(DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing.ListingTypeEnum listingTypeEnum) {
        this.listingTypeEnum = listingTypeEnum;
    }

    public DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.PropertyTypesEnum getPropertyTypesEnum() {
        return propertyTypesEnum;
    }

    public void setPropertyTypesEnum(DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.PropertyTypesEnum propertyTypesEnum) {
        this.propertyTypesEnum = propertyTypesEnum;
    }

    public List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.PropertyFeaturesEnum> getPropertyFeatures() {
        return propertyFeatures;
    }

    public void setPropertyFeatures(List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.PropertyFeaturesEnum> propertyFeatures) {
        this.propertyFeatures = propertyFeatures;
    }

    public List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.ListingAttributesEnum> getListingAttributes() {
        return listingAttributes;
    }

    public void setListingAttributes(List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters.ListingAttributesEnum> listingAttributes) {
        this.listingAttributes = listingAttributes;
    }

    public Integer getMaxLandArea() {
        return maxLandArea;
    }

    public void setMaxLandArea(Integer maxLandArea) {
        this.maxLandArea = maxLandArea;
    }

    public Integer getMinLandArea() {
        return minLandArea;
    }

    public void setMinLandArea(Integer minLandArea) {
        this.minLandArea = minLandArea;
    }

    public List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchLocation> locations) {
        this.locations = locations;
    }
}
