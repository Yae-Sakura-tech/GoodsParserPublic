package entity;

import java.util.List;

public class Good {
    private final String category;
    private final String name;
    private final String price;
    private final boolean isDiscount;
    private final List<Property> props;

    public Good(String category, String name, String price, boolean isDiscount, List<Property> props) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.isDiscount = isDiscount;
        this.props = props;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public List<Property> getProps() {
        return props;
    }

    public String getCSVString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.category);
        stringBuilder.append("|");
        stringBuilder.append(this.name);
        stringBuilder.append("|");
        stringBuilder.append(this.price);
        stringBuilder.append("|");
        stringBuilder.append("[");

        for (Property property : this.props) {
            stringBuilder.append(property.getStringNameAndValue());
        }
        stringBuilder.append("]\n");
        return stringBuilder.toString();
    }
}
