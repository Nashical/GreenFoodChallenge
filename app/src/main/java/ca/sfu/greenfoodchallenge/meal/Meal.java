package ca.sfu.greenfoodchallenge.meal;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*Class contains array of meal components, a meal name set by the user, and a method to get carbon
emissions for whole meal.
 */
public class Meal {

    private String mealName;
    private int municipality = 0;
    private String restaurant;
    private String location;
    private String imageUrl;
    private String description;

    private boolean shared = false;

    private ArrayList<MealComponent> componentList;



    public Meal(String mealName) {
        this.componentList = new ArrayList<>();
        this.mealName = mealName;
    }


    public Meal(String mealName,int municipality,String restaurant, String location, String imageUrl, String description) {
        this.mealName = mealName;
        this.municipality = municipality;
        this.location = location;
        this.imageUrl = imageUrl;
        this.description = description;
        this.componentList = new ArrayList<>();
        this.mealName = mealName;
    }

    public Meal(String mealName,int municipality,String restaurant, String location, String imageUrl, String description, boolean shared) {
        this.mealName = mealName;
        this.municipality = municipality;
        this.location = location;
        this.imageUrl = imageUrl;
        this.description = description;
        this.componentList = new ArrayList<>();
        this.mealName = mealName;
        this.shared = shared;
    }

    public Meal(Meal other) {
        mealName = other.mealName;
        description = other.getDescription();
        restaurant = other.getRestaurant();
        municipality = other.getMunicipality();
        imageUrl = other.getImageUrl();
        location = other.getLocation();
        shared = other.isShared();

        componentList = new ArrayList<>();
        for (MealComponent component : other.componentList) {
            componentList.add(new MealComponent(component));
        }
    }


    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public double getCarbon() {
        double carbonTotal = 0;
        for (int i = 0; i < componentList.size(); i++) {

            carbonTotal += componentList.get(i).getCarbon();
        }

        return carbonTotal;

    }

    public ArrayList<MealComponent> getComponentList() {
        return componentList;
    }

    public void addComponent(MealComponent component){

        componentList.add(component);
    }

    public void removeComponent(int index) {

        componentList.remove(index);
    }



    public int getComponentCount() {
        return componentList.size();
    }




    public MealComponent getComponentByProteinName(String proteinName)
    {

        for (MealComponent component: componentList) {
            if(component.getComponentProtein().getName().equals(proteinName)){
                return component;
            }
        }
        return null;
    }



    public double getKilogramsByProteinName(String proteinName){

        double total = 0;

        for (MealComponent component: componentList) {
            if(component.getComponentProtein().getName().equals(proteinName)){
                total += component.getServingSize();
            }
        }

        return total;
    }

    public double getGramsByProteinName(String proteinName){
        return getKilogramsByProteinName(proteinName)*1000;
    }


    public Map<String, Double> proteinAmountsByName() {
        Map<String, Double> amountsByName = new TreeMap<>();


        for(MealComponent mealComponent: componentList) {
            String proteinName = mealComponent.getComponentProtein().getName();
            Double servingSize =mealComponent.getServingSize();

            //check to see if the key already exist in protein.
            if(amountsByName.containsKey(proteinName)) {
                servingSize += amountsByName.get(proteinName);
            }
            amountsByName.put(proteinName, servingSize);

        }

        return amountsByName;
    }


    public double totalServing()
    {
        double totalServing = 0;
        for (MealComponent mealComponent: componentList) {
            totalServing += mealComponent.getServingSize();
        }
        return totalServing;
    }


    public Protein getMainProtein(){
        if(getComponentCount()==0){
            return null;
        }
        MealComponent maxComponent = getComponentList().get(0);
        for (MealComponent mealComponent: getComponentList()) {
            if(mealComponent.getServingSize()>maxComponent.getServingSize()) {
                maxComponent = mealComponent;
            }
        }
        return maxComponent.getComponentProtein();
    }


    public int getMunicipality() {
        return municipality;
    }

    public void setMunicipality(int municipality) {
        this.municipality = municipality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}

