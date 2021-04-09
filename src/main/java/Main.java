import constant.Constants;
import service.file.service.FileService;
import service.file.service.FileServiceImpl;
import service.parse.service.JSoupService.JSoupParseService;
import service.parse.service.ParseService;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        ParseService parseService = new JSoupParseService();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.CSV_HEADER);

        for (int i = 1; i <= 2; i++) {
            String paginatedUrl = String.format("https://allegro.pl/kategoria/elektronika?string=bargain_zone&bmatch=cl-e2101-d3681-c3682-40-ele-1-3-0319&p=%s", i);
            stringBuilder.append(parseService.parse(paginatedUrl, Constants.CATEGORY_ELECTRIC));
        }

        for (int i = 1; i <= 2; i++) {
            String paginatedUrl = String.format("https://allegro.pl/kategoria/dom-i-ogrod?string=bargain_zone&bmatch=e2101-d3681-c3682-hou-1-3-0319&p=%s", i);
            stringBuilder.append(parseService.parse(paginatedUrl, Constants.CATEGORY_GARDEN));
        }

        for (int i = 1; i <= 2; i++) {
            String paginatedUrl = String.format("https://allegro.pl/kategoria/dziecko?string=bargain_zone&bmatch=e2101-d3681-c3682-bab-1-3-0319&p=%s", i);
            stringBuilder.append(parseService.parse(paginatedUrl, Constants.CATEGORY_KIDS));
        }

        FileService fileService = new FileServiceImpl();
        fileService.writeCSVFile(new File("file.csv"), stringBuilder.toString());
    }
}
