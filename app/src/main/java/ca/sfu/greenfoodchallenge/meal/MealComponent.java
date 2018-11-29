package ca.sfu.greenfoodchallenge.meal;


/*
 * Used to track how much of a protein is in a meal.
 * It stores a protein and the quantity of it in Kg.
 */
public class MealComponent {

    private Protein componentProtein;
    private double servingSize; //Kg

    public MealComponent(Protein componentProtein, double servingSize){
        this.componentProtein = componentProtein;
        this.servingSize = servingSize;
    }

    public MealComponent(MealComponent other){
        this.componentProtein = other.componentProtein;
        this.servingSize = other.servingSize;
    }

    public Protein getComponentProtein() {
        return componentProtein;
    }

    public void setComponentProtein(Protein componentProtein) {
        this.componentProtein = componentProtein;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    //getCarbon assumes that input for servingSize is in kg. Can be updated if that changes.

    public double getCarbon(){

        double result =  (servingSize) * componentProtein.getCarbonPerKg();
        return result;
    }

}
