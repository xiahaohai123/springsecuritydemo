package com.example.springsecuritydemo;


import com.example.springsecuritydemo.databind.AliasProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionRequest {

    private Integer offset;

    private Integer limit;

    @AliasProperty(alias = "user_name")
    @JsonProperty("user_name")
    private String username;

    @AliasProperty(alias = "resource_name")
    @JsonProperty("resource_name")
    private String resourceName;

    @JsonProperty("account_name")
    private String accountName;

    private String type;

    @JsonProperty("projectId")
    private String tenantId;

    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("end_time")
    private Long endTime;

    @JsonProperty("resource_address")
    private String resourceAddress;

    private String protocol;

    @JsonProperty("source_address")
    private String sourceAddress;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getResourceAddress() {
        return resourceAddress;
    }

    public void setResourceAddress(String resourceAddress) {
        this.resourceAddress = resourceAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    @Override
    public String toString() {
        return "SessionRequest{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", username='" + username + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", type='" + type + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", resourceAddress='" + resourceAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", sourceAddress='" + sourceAddress + '\'' +
                '}';
    }
}
