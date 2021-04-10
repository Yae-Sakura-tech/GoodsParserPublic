package service.parse.service;

import java.io.IOException;
import java.util.Map;

public interface ParseService {
    String parse(String url, String category, Map<String, String> cookies) throws IOException;
}
