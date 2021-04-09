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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static constant.Constants.*;

public class JSoupParseService implements ParseService {

    @Override
    public String parse(String url, String category) {
        Document listPageDocument = getDocumentByUrl(url);
        List<String> goodsListURLs = createListOfGoodsURLs(listPageDocument);
        List<Document> goodsDocumentList = createListOfGoodsDocuments(goodsListURLs);
        List<Good> goodList = mapGoodDocumentListToEntityList(goodsDocumentList, category);
        List<Good> goodListWithDiscount = goodList.stream().filter(Good::isDiscount).collect(Collectors.toList());
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

    private List<Document> createListOfGoodsDocuments(List<String> goodsListURLs) {
        List<Document> documentList = new ArrayList<>();
        for (String url : goodsListURLs) {
            Document document;
            document = getDocumentByUrl(url);
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

    private Document getDocumentByUrl(String url) {
        Document doc = null;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("gdpr_permission_given", "1");
        cookies.put("_cmuid", "0e41fc62-d334-4f10-b521-25ab801ecd4f");
        cookies.put("datadome", "datadome\t8J2j.mCF1GPkj8n4k5sBtaq4ZdEgCZY~9kdcr.SnzrGPnHV-mvhgio0DH~MntVpJWXsEyQ8BGmbV2j~hQLEPrUiVYegCTHSDwa_Hb_TCcE");

        try {
            doc = Jsoup.connect(url)
                    .cookies(cookies)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
