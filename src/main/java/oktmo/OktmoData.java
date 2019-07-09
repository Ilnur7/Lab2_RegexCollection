package oktmo;

import java.util.*;

public class OktmoData {

    private List<Place> listPlace = new ArrayList<Place>();
    private Set<String> setStatus = new HashSet<String>();
    private List<Place> sortedPlaces = new ArrayList<Place>();

    static class SortedByName implements Comparator<Place> {

        public int compare(Place obj1, Place obj2) {

            String str1 = obj1.getName();
            String str2 = obj2.getName();

            return str1.compareTo(str2);
        }
    }

    public List<Place> getListPlace() {
        return listPlace;
    }

    public Set<String> getSetStatus() {
        return setStatus;
    }

    public List<Place> getSortedByNamePlaces() {
        sortedPlaces.addAll(listPlace);
//        Collections.sort(sortedPlaces, new SortedByName());
        Collections.sort(sortedPlaces, Comparator.comparing(Place::getName));

        return sortedPlaces;
    }

    public void addPlace(Place place){
        getListPlace().add(place);
    }

    public void addStatus(String status){
        getSetStatus().add(status);
    }

}
