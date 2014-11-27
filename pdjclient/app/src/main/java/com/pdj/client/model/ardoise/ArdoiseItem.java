package com.pdj.client.model.ardoise;

/**
 * Created by fengqin on 14/11/13.
 */
public class ArdoiseItem {


    public static final String LABEL="label";
    public static final String PRICE="priceEuro";
    public static final String ORDER="order";
    public static final String EXTRA_FILED1="idDishesBloc";


    private String id;
    private String label;
    private String price;
    private Number order;
    private String extraField1;

    private boolean shouldUnderline;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Number getOrder() {
        return order;
    }

    public void setOrder(Number order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public boolean isShouldUnderline() {
        return shouldUnderline;
    }

    public void setShouldUnderline(boolean shouldUnderline) {
        this.shouldUnderline = shouldUnderline;
    }
}
