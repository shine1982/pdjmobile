package com.pdj.client.model.ardoise;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.pdj.client.model.Restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fengqin on 14/11/13.
 */
@ParseClassName("Ardoise")
public class Ardoise extends ParseObject {

    public static final String TITLE="title";
    public static final String FORMULE_PRICE_LIST="formulePriceList";
    public static final String DISHES_BLOC_LIST="dishesBlocList";
    public static final String DISH_LIST="dishList";
    public static final String TEXT_LIST="textList";

    private List<ArdoiseItem> formulePriceList;
    private List<ArdoiseItem> dishesBlocsList;
    private List<ArdoiseItem> dishList;
    private List<ArdoiseItem> textList;

    public String getTitle() {
        return getString(TITLE);
    }

    public List<ArdoiseItem> getFormulePriceList() {
        if(formulePriceList==null){
            formulePriceList = new ArrayList<ArdoiseItem>();
            final List<ParseObject> parseObjects = getList(FORMULE_PRICE_LIST);
            setArdoiseItemList(formulePriceList,parseObjects);
        }

        return formulePriceList;
    }

    public List<ArdoiseItem> getDishesBlocsList(){
        if(dishesBlocsList == null){
            dishesBlocsList = new ArrayList<ArdoiseItem>();
            final List<ParseObject> parseObjects = getList(DISHES_BLOC_LIST);
            setArdoiseItemList(dishesBlocsList,parseObjects);
        }
        return dishesBlocsList;
    }

    public List<ArdoiseItem> getDishList(){
        if(dishList == null){
            dishList = new ArrayList<ArdoiseItem>();
            final List<ParseObject> parseObjects = getList(DISH_LIST);
            setArdoiseItemList(dishList,parseObjects);
        }

        return dishList;
    }

    public List<ArdoiseItem> getTextList(){
        if(textList == null ){
            textList = new ArrayList<ArdoiseItem>();
            final List<ParseObject> parseObjects = getList(TEXT_LIST);
            setArdoiseItemList(textList,parseObjects);
        }

        return textList;
    }


    public List<ArdoiseItem> getFormulePriceListForVisu(){

        final List<ArdoiseItem> resultList = new ArrayList<ArdoiseItem>();
        for(final ArdoiseItem ardoiseItem: getFormulePriceList()){
            if(ardoiseItem.getPrice()!=null && !"".equals(ardoiseItem.getPrice())){
                resultList.add(ardoiseItem);
            }
        }
        return resultList;
    }

    public List<ArdoiseItem> getDishesBlocForVisu(){
        final List<ArdoiseItem> resultList = new ArrayList<ArdoiseItem>();
        for(final ArdoiseItem dishesBloc:  getDishesBlocsList()){
            for(final ArdoiseItem dish:getDishList()){
                if(dishesBloc.getId().equals(dish.getExtraField1())){
                    resultList.add(dishesBloc);
                    break;
                }
            }
        }
        return resultList;
    }

    public List<ArdoiseItem> getAllDatas(){

        List<ArdoiseItem> result = new ArrayList<ArdoiseItem>();

        result.addAll(getFormulePriceListForVisu());
        for(final ArdoiseItem dishesBloc : getDishesBlocForVisu()){
            dishesBloc.setShouldUnderline(true); // nom du bloc, avec la underline!!
            result.add(dishesBloc);
            for(final ArdoiseItem dish: getDishList()){
                if(dishesBloc.getId().equals(dish.getExtraField1())){
                    result.add(dish);
                }
            }
        }
        result.addAll(getTextList());

        return result;
    }



    private void setArdoiseItemList(final List<ArdoiseItem> listToBeAdded, final List<ParseObject> parseObjects){

        for(final ParseObject po: parseObjects){
            final ArdoiseItem ardoiseItem = new ArdoiseItem();
            ardoiseItem.setLabel(po.getString(ardoiseItem.LABEL));
            ardoiseItem.setPrice(po.getString(ardoiseItem.PRICE));
            ardoiseItem.setOrder(po.getNumber(ardoiseItem.ORDER));
            ardoiseItem.setId(po.getObjectId());
            ardoiseItem.setExtraField1(po.getString(ardoiseItem.EXTRA_FILED1));
            listToBeAdded.add(ardoiseItem);
        }
    }


    public static ParseQuery<Ardoise> getQuery() {
        return ParseQuery.getQuery(Ardoise.class);
    }

    public static ParseQuery<Ardoise> getFullQuery(final Restaurant resto, final Date date) {
        final ParseQuery<Ardoise> fullQuery = ParseQuery.getQuery(Ardoise.class);
        fullQuery.include(FORMULE_PRICE_LIST);
        fullQuery.include(DISHES_BLOC_LIST);
        fullQuery.include(DISH_LIST);
        fullQuery.include(TEXT_LIST);
        if(resto!=null){
            fullQuery.whereEqualTo("resto",resto);
        }
        if(date!=null){
            fullQuery.whereEqualTo("date", date);
        }
        return fullQuery;
    }
}
