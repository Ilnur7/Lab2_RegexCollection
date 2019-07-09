
package oktmo;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OktmoMain {
    public static void main(String[] args) throws IOException {
        OktmoReader oktmoReader = new OktmoReader();
        OktmoData oktmoData = new OktmoData();
        OktmoData oktmoDataRegex = new OktmoData();
        oktmoReader.readPlaces("data-201710.csv", oktmoData);
        oktmoReader.readPlacesUsePattern("data-201710.csv", oktmoDataRegex);
        List<String> listWithFilter1 = OktmoAnalyzer.findNameUsePattern(oktmoData.getListPlace(), "(\\w){0,2}(ово)$");
        List<String> listWithFilter2 = OktmoAnalyzer.findNameUsePattern(oktmoData.getListPlace(), "^(?i)([^аеёиоуыэюя]).*\\1$");
        //printCollection(oktmoDataRegex.getSetStatus());
        //printCollection(listWithFilter2);

        int countSamePlace=0;
        for (int i = 0; i < oktmoData.getListPlace().size(); i++) {
            Place place = oktmoData.getListPlace().get(i);
            Place placeRegex = oktmoDataRegex.getListPlace().get(i);
            if (place.equals(placeRegex)){
                countSamePlace++;
            }
        }

        System.out.println("\nКоличество идентичных населенных пунктов: " + countSamePlace + " из " + oktmoData.getListPlace().size());
        System.out.println("Количество статусов, созданных c помощью методов: "+oktmoData.getSetStatus().size());
        System.out.println("Количество статусов, созданных регулярным выражением: "+oktmoDataRegex.getSetStatus().size());

    }

    public static void printCollection(Collection collection){

        Iterator<Collection> iterator = collection.iterator();
        while (iterator.hasNext()){
            Object o = iterator.next();
            if (o instanceof Place){
                Place foo = (Place) o;
                System.out.println(foo.toString());
            }else if (o instanceof String){
                String foo = (String) o;
                System.out.println(foo);
            }
        }
    }
}
