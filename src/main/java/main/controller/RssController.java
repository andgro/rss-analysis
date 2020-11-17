package main.controller;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import main.database.DatabaseManager;
import main.database.data.RssAnalysisResult;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class RssController {

    private DatabaseManager databaseManager;

    public RssController() {
        databaseManager = new DatabaseManager();
    }

    @PostMapping("/analyse/new")
    String readRss(@RequestBody RssAnalysisInput rssAnalysisInput) {

        //Example urls:
        //"https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss"
        //"https://today.rtl.lu/rss/news"
        //"https://rss.nytimes.com/services/xml/rss/nyt/World.xml"

        //validate input
        if((rssAnalysisInput == null) || (rssAnalysisInput.getUrls().size() < 2)) {
            return "Incorrect data.";
        }

        //(k, v) = (word, counter)
        //example: ("TRUMP", 5), ("COVID-19", 7),...
        Map<String, Integer> wordCount = new HashMap<>();

        //fill this set with words that have to be ignored in analysis (TO-DO specify them in another file)
        Set<String> ignoredWords = new HashSet<>(Arrays.asList("THE", "FROM", "AND", "WITH", "FOR", "NEW", "THEN", "THAN", "WHERE", "WHY"));

        int i = 0;
        //iterate over input urls, read rss data for each url and count the words in titles
        while (i < rssAnalysisInput.getUrls().size()) {
            try (XmlReader reader = new XmlReader(new URL(rssAnalysisInput.getUrls().get(i)))) {
                SyndFeed feed = new SyndFeedInput().build(reader);

                    for (SyndEntry entry : feed.getEntries()) {
                        String title = entry.getTitle();
                        String[] words = title.split(" ");
                        for (String word : words) {
                            //ignore words shorter than 3 letters (a, an, of, is, ...)
                            if ((word.length() > 2) && (!ignoredWords.contains(word.toUpperCase()))) {
                                if (wordCount.containsKey(word.toUpperCase())) {
                                    wordCount.computeIfPresent(word.toUpperCase(), (k, v) -> ++v);
                                } else
                                    wordCount.put(word.toUpperCase(), 1);
                            }
                        }
                    }
                    i++;
                } catch (Exception exception) {
                    exception.getMessage();
                }
            }

        //list of top 3 most common words
        List<Map.Entry<String, Integer>> topWords = wordCount.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3).collect(Collectors.toList());

        //generate unique ID
        UUID uuid = UUID.randomUUID();

        //RssAnalysisResult = uuid, urls, topic1, topic2, topic3
        RssAnalysisResult rssAnalysisResult;

        if(topWords == null || topWords.size() < 3) {
            rssAnalysisResult = new RssAnalysisResult(uuid.toString(), String.join(",", rssAnalysisInput.getUrls()), "", "", "");
        } else {
             rssAnalysisResult = new RssAnalysisResult(uuid.toString(), String.join(",", rssAnalysisInput.getUrls()), topWords.get(0).getKey(), topWords.get(1).getKey(), topWords.get(2).getKey());
        }


        //insert into DB...
        databaseManager.insertIntoAnalyse(rssAnalysisResult);

        return uuid.toString();

    }

    @GetMapping("frequency/{id}")
    ServiceResult getFrequency(@PathVariable("id") String id) {
        //validate ID...
        if(id == null || id.trim().length()!= 36) {
            return new ServiceResult(new ServiceError("Invalid ID"));
        }

        RssAnalysisResult rssAnalysisResult = null;
        rssAnalysisResult = databaseManager.getRssAnalysisById(id);

        if(rssAnalysisResult == null) {
            //no data found in DB
            return new ServiceResult(new ServiceError("No data found"));
        }

        return new ServiceResult(rssAnalysisResult);
    }

}
