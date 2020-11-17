package main.controller;

import java.util.ArrayList;
import java.util.List;

public class RssAnalysisInput {
    private List<String> urls;

    public RssAnalysisInput() {
        urls = new ArrayList<>();
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
