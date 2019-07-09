package oktmo;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OktmoReader {

    public void readPlacesUsePattern(String fileName, OktmoData data){
        long start = System.nanoTime();
        int lineCount = 0;
        String s;
        //String regex = "\"(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"\\d+\";\"\\d+\";\"(?!(?:Насел[её]нные\\sпункты))([^А-Я]*)\\s+([А-Я][^\"]*)\";.*";
        String regex = "\"(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"(?!(?:000))(\\d+)\";\"\\d+\";\"\\d+\";\"(?!(?:Насел[её]нные\\s+пункты))" +
                "(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";
        try{
            File file = new File(fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "cp1251"));
            Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
            while (((s = reader.readLine()) != null )) {
                lineCount++;
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()){
                    long codeSettlement = Long.parseLong(matcher.group(1)+matcher.group(2)+matcher.group(3)+matcher.group(4));
                    String statusSettlement = matcher.group(5);
                    String nameSettlement = matcher.group(6);
                    nameSettlement = nameSettlement.replace("\"", "");
                    Place place = new Place(codeSettlement, statusSettlement, nameSettlement);
                    data.addPlace(place);
                    data.addStatus(statusSettlement);
                }
            }
        }catch (IOException e) {
            System.out.println("Error in line " + lineCount);
            System.out.println(data.getListPlace().get(data.getListPlace().size()-1));
            e.printStackTrace();
        }
        long time = System.nanoTime() - start;
        double timeOfWork =((double)time) / 1000000000.0;
        System.out.println("Время работы с регулярным выражением: " + timeOfWork + " сек.");
        System.out.println("Количество записей с регулярным выражением: " + data.getListPlace().size());
    }

    public void readPlaces(String fileName, OktmoData data) throws IOException {
        int lineCount = 0;
        long start = System.nanoTime();
        String s;
        String [] arrStr = null;
        try{
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "cp1251"));
            while (((s = reader.readLine()) != null )) {
                lineCount++;
                s = s.replace("\"", "");
                arrStr = s.split(";");
                Place place = writePlace(arrStr);
                if (place != null){
                    data.addPlace(place);
                    data.addStatus(place.getStatus());
                }
            }
        }catch (Exception e) {
            System.out.println("Error in line " + lineCount);
            System.out.println(data.getListPlace().get(data.getListPlace().size()-1));
            e.printStackTrace();
        }
        long time = System.nanoTime() - start;
        double timeOfWork = (double)time / 1000000000.0;
        System.out.println("Время работы с методами: " + timeOfWork + " сек.");
        System.out.println("Количество записей с методами: " + data.getListPlace().size());
    }

    public Place writePlace(String[] arrStr){

        long codeSettlement = getCodeSettlement(arrStr);
        String nameSettlement = getNameSettlement(arrStr);
        String statusSettlement;
        Place place = null;

        if ((codeSettlement!=0) && (!(nameSettlement.startsWith("Населенные пункты")))){
            int indexEnd = findIndex(nameSettlement);
            statusSettlement = nameSettlement.substring(0, indexEnd).trim();
            nameSettlement = nameSettlement.substring(indexEnd).trim();
            place = new Place(codeSettlement, statusSettlement, nameSettlement);

        }
        return place;
    }

    public long getCodeSettlement(String[] arrStr){
        long codeSettlement;
        if (!((arrStr[3].equals("000")))){
            codeSettlement = Long.parseLong((arrStr[0] + arrStr[1] + arrStr[2] + arrStr[3]).trim());
            return codeSettlement;
        }else {
            return 0;
        }
    }

    public String getNameSettlement(String[] arrStr){
        String nameSettlement = arrStr[6];
       /* String[] str=null;
        if ((nameSettlement.contains("свх "))){
            str = nameSettlement.split(" ");
            nameSettlement = "";
            for (int i = 0; i < str.length; i++){
                if ((str[i].equals("свх"))){
                    str[i+1] = "\"" + str[i+1];
                    str[str.length-1] = str[str.length-1] + "\"";
                    break;
                }
            }
            for (String s: str){
                nameSettlement += s + " ";
            }
        }*/
        return nameSettlement;
    }

    public int findIndex(String nameSettlement){
        int indexEnd=0;
        char[]arrCharNameTown = nameSettlement.toCharArray();
        for (int i = 0; i < arrCharNameTown.length; i++) {
            if ((Character.isUpperCase(arrCharNameTown[i])) | (Character.isDigit(arrCharNameTown[i]))) {
                indexEnd = i;
                break;
            }
        }
        return indexEnd;
    }
}
