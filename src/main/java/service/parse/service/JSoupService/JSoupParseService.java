package service.parse.service.JSoupService;

import entity.Good;
import entity.Property;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.parse.service.ParseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static constant.Constants.*;

public class JSoupParseService implements ParseService {

    @Override
    public String parse(String url, String category, Map<String, String> cookies) throws IOException {
        Document listPageDocument = getDocumentByUrl(url, cookies);
        List<String> goodsListURLs = createListOfGoodsURLs(listPageDocument);
        List<Document> goodsDocumentList = loadListOfGoodsDocuments(goodsListURLs, cookies);
        List<Good> goodList = mapGoodDocumentListToEntityList(goodsDocumentList, category);
        List<Good> goodListWithDiscount = goodList.stream()
                .filter(Good::isDiscount)
                .collect(Collectors.toList());
        return generateCSV(goodListWithDiscount);
    }

    private String generateCSV(List<Good> goodList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Good good : goodList) {
            stringBuilder.append(good.getCSVString());
        }
        return stringBuilder.toString();
    }

    private List<Good> mapGoodDocumentListToEntityList(List<Document> goodsDocumentList, String category) {
        List<Good> goodList = new ArrayList<>();
        for (Document document : goodsDocumentList) {
            goodList.add(mapDocumentToGood(document, category));
        }
        return goodList;
    }

    private Good mapDocumentToGood(Document document, String category) {
        String goodName = parseGoodField(document, GOOD_NAME_ELEMENT_CLASS_NAME);
        String goodPrice = parseGoodField(document, GOOD_PRICE_ELEMENT_CLASS_NAME);
        boolean isDiscount = parseIsDiscount(document);
        List<Property> props = parseGoodProperties(document);
        return new Good(category, goodName, goodPrice, isDiscount, props);
    }

    private boolean parseIsDiscount(Document document) {
        String fieldValue = parseGoodField(document, GOOD_DISCOUNT_ELEMENT_CLASS_NAME);
        return !fieldValue.equals("");
    }

    private List<Property> parseGoodProperties(Document document) {
        Elements elements = document.getElementsByClass(GOOD_PROPERTY_ELEMENT_CLASS_NAME);
        List<Property> propertyList = new ArrayList<>();

        for (Element element : elements) {
            String propString = element.text();
            String propName = propString.substring(0, propString.indexOf(':'));
            String propValue = propString.substring(propString.indexOf(':') + 2);
            Property property = new Property(propName, propValue);
            propertyList.add(property);
        }
        return propertyList;
    }

    private String parseGoodField(Document document, String elementName) {
        return document.getElementsByClass(elementName).text();
    }

    private List<Document> loadListOfGoodsDocuments(List<String> goodsListURLs, Map<String, String> cookies) throws IOException {
        List<Document> documentList = new ArrayList<>();
        for (String url : goodsListURLs) {
            Document document;
            document = getDocumentByUrl(url, cookies);
            if (document != null) {
                documentList.add(document);
            }
        }
        return documentList;
    }

    private List<String> createListOfGoodsURLs(Document document) {
        List<String> urlList = new ArrayList<>();
        Elements goodsListElements = document.getElementsByClass(LIST_ELEMENT_CLASS_NAME);
        for (Element element : goodsListElements)
            urlList.add(element.select("a").attr("href"));
        return urlList;
    }

    private Document getDocumentByUrl(String url, Map<String, String> cookies) throws IOException {
        Document doc;
        doc = Jsoup.connect(url)
                .cookies(cookies)
                .get();
        return doc;
    }
}
