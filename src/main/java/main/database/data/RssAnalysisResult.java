package main.database.data;

public class RssAnalysisResult {
    String uuid;
    String urls;  //urls = "url1,url2,url3,..."
    String topic1;
    String topic2;
    String topic3;

    public RssAnalysisResult(String uuid, String urls, String topic1, String topic2, String topic3) {
        this.uuid = uuid;
        this.urls = urls;
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.topic3 = topic3;
    }

    public RssAnalysisResult() {
    }

    public String getUuid() {
        return uuid;
    }

    public String getUrls() {
        return urls;
    }

    public String getTopic1() {
        return topic1;
    }

    public String getTopic2() {
        return topic2;
    }

    public String getTopic3() {
        return topic3;
    }
}
