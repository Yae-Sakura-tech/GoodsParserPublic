import config.Configuration;
import constant.Constants;
import service.file.service.FileService;
import service.file.service.FileServiceImpl;
import service.parse.service.JSoupService.JSoupParseService;
import service.parse.service.ParseService;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Application {
    private final Configuration configuration = Configuration.getConfiguration();
    private final FileService fileService = new FileServiceImpl();
    private final ParseService parseService = new JSoupParseService();

    public void start() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.CSV_HEADER);
        Map<String, String> categoryAndUrl = configuration.getCategoryAndUrl();

        for (Map.Entry<String, String> entry : categoryAndUrl.entrySet()) {
            String category = entry.getKey();
            String url = entry.getValue();

            for (int i = 1; i <= configuration.getAmountPagesToParse(); i++) {
                String paginatedUrl = String.format(url, configuration.getAmountPagesToParse());
                try {
                    stringBuilder.append(parseService.parse(paginatedUrl, category, configuration.getCookies()));
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

        }
        fileService.writeCSVFile(new File("file.csv"), stringBuilder.toString());
    }
}
