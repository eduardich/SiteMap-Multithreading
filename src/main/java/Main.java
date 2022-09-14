import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {

    // You can set required values for SITE_MAP_PATH file and URL below
    // or obtain it another way

    private static final String SITE_MAP_PATH = "data/siteMap-Lenta.ru.txt";
    private static final String URL = "https://lenta.ru/";

//    private static final String SITE_MAP_PATH = "data/siteMap-Jsoup.org.txt";
//    private static final String URL = "https://jsoup.org/";

//    private static final String SITE_MAP_PATH = "data/siteMap-jhy.io.txt";
//    private static final String URL = "https://jhy.io/";



    public static void main(String[] args) {

//        long start = System.currentTimeMillis();
        try {

            createDirectoryAndFile(SITE_MAP_PATH);

            HashMap<String, String> map = getSiteLinksMap(URL);

            writeMapToFile(map);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(System.currentTimeMillis() - start);

    }

    private static void writeMapToFile(HashMap<String, String> HashMapLinkParentLink) throws FileNotFoundException {

        MapMaker mapMaker = new MapMaker(HashMapLinkParentLink);

        ForkJoinPool forkJoinPool = new ForkJoinPool(getPoolSize());
        forkJoinPool.invoke(mapMaker);

        PrintWriter printWriter = new PrintWriter(SITE_MAP_PATH);

        MapMaker.linkedList.forEach(s -> {
            printWriter.write(s + "\n");
        });


        printWriter.flush();
        printWriter.close();


    }

    public static HashMap<String, String> getSiteLinksMap(String url) throws IOException {

        ExtractLinkFromUrlTask extractLinkFromUrlTask = new ExtractLinkFromUrlTask(url, 0);

        ForkJoinPool forkJoinPool = new ForkJoinPool(getPoolSize());
        forkJoinPool.invoke(extractLinkFromUrlTask);

        return ExtractLinkFromUrlTask.getHashMapLinkParentLink();
    }

    public static void createDirectoryAndFile(String pathToFile) throws IOException {
        File dir = new File(pathToFile.substring(0, pathToFile.lastIndexOf('/')));
        if (!dir.exists()) dir.mkdirs();
        File file = new File(pathToFile.substring(pathToFile.lastIndexOf('/')));
        file.createNewFile();
    }


    //    Возвращает количество потоков в зависимости от количества доступных процессоров в виртуальной машине Java.
    public static int getPoolSize() {
        try {
            // Количество доступных процессоров JVM
            int cores = Runtime.getRuntime().availableProcessors();
            return cores;
        } catch (Throwable e) {
            // При возникновении исключения возвращаемся пул из 1 потока
            return 2;
        }
    }
}
