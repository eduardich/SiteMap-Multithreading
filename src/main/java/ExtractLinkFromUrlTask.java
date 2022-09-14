import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ExtractLinkFromUrlTask extends RecursiveAction {

    private final String url;
    private final int level;

    public static volatile HashSet<String> allLinks = new HashSet<>();

    private static volatile HashMap<String, String> hashMapLinkParentLink = new HashMap<>();

    public ExtractLinkFromUrlTask(final String url, final int level) {
        this.url = url;
        this.level = level;
    }

    public static HashMap<String, String> getHashMapLinkParentLink() {
        return new HashMap<String, String> (hashMapLinkParentLink);
    }

    @Override
    protected void compute() {
        if (level == 0) hashMapLinkParentLink.put(url, "null");
        allLinks.add(url);

        TreeSet<String> resultSetOneLink;
        resultSetOneLink = getLinksFromOnePage();
        if (resultSetOneLink.isEmpty()) {

        } else {
            List<ExtractLinkFromUrlTask> taskList = new ArrayList<>();
            for (String innerLink : resultSetOneLink){
                hashMapLinkParentLink.put(innerLink, url); // потом из этого собрать карту
                ExtractLinkFromUrlTask extractLinkFromUrlTask = new ExtractLinkFromUrlTask(innerLink, level + 1 );
                taskList.add(extractLinkFromUrlTask);
            }
            ForkJoinTask.invokeAll(taskList);
        }

    }

    public TreeSet<String> getLinksFromOnePage(){

        TreeSet<String> treeLinks = new TreeSet<>();
        Document document = null;
        Elements elements = null;

        try {
            Thread.sleep(150);
            document = Jsoup.connect(url).get();
            elements = document.body().select("a[href^=/]");
        } catch (HttpStatusException e ) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (elements == null) return new TreeSet<>();

        for (Element e : elements){
//            String innerLink = url.concat(e.attr("href")); // Получение относительной ссылки из элемента
            String innerLink = e.attr("abs:href"); // Получение абсолютной ссылки, а не относительной

            if (innerLink.endsWith("#$")) continue;
            if (!innerLink.startsWith(url)) continue;
            if (allLinks.contains(innerLink)) continue;

            treeLinks.add(innerLink);
        }


        return treeLinks;
    }

}
