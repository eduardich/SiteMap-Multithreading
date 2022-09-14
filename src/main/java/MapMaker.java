import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class MapMaker extends RecursiveAction {

    private static Map<String, String> hashMapLinkParentLink;
    public static LinkedList<String> linkedList = new LinkedList<>();
    private final String url;
    private final int level;

    public MapMaker(Map<String, String> hashMapLinkParentLink) {
        MapMaker.hashMapLinkParentLink = hashMapLinkParentLink;
        this.url = hashMapLinkParentLink.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals("null"))
                .findFirst()
                .get()
                .getKey();
        this.level = 0;
    }

    private MapMaker(String url, int level) {
        this.url = url;
        this.level = level;
    }

    private String urlWithTabs() {
        String link = this.url;
        for (int t = 0; t < this.level; t++) {
            link = "\t".concat(link);
        }
        return link;
    }

    @Override
    protected void compute() {

        linkedList.add(urlWithTabs());

        Set<String> set = hashMapLinkParentLink
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(url))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!set.isEmpty()){
            for (String currentLink : set) {

                new MapMaker(currentLink, this.level + 1).invoke();

            }
        }

    }

}
