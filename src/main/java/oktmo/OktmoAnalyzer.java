package oktmo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OktmoAnalyzer {

    private static List<String> listNameUsePattern = new ArrayList<String>();

    public static List<String> findNameUsePattern(List<Place> list, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        String name;
        for (Place place: list){
            name = place.getName();
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()){
                listNameUsePattern.add(name);
            }
        }
        return listNameUsePattern;
    }
}
