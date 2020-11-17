package main.controller;

import main.database.data.RssAnalysisResult;

public class ServiceResult {
    ServiceError error;
    RssAnalysisResult data;

    public ServiceError getError() {
        return error;
    }

    public RssAnalysisResult getData() {
        return data;
    }

    public ServiceResult(RssAnalysisResult data) {
        this.data = data;
    }

    public ServiceResult(ServiceError error) {
        this.error = error;
    }

    public void setError(ServiceError error) {
        this.error = error;
    }

    public void setData(RssAnalysisResult data) {
        this.data = data;
    }
}
