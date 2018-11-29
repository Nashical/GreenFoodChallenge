package ca.sfu.greenfoodchallenge.meal;



import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Right now the proteins available for users to be in their meals are those set ones, so untill
 * we provide functionality to customize proteins, this class provides the default ones.
 */
public class DefaultProteins {

    public final static int NUMBER_OF_PROTEINS=7;

    // The proteins have a default order, this list of constants defines what the index of each
    // protein is.
    // NOTE: Be careful when changing the order of proteins, it may break something somewhere
    public final static int BEEF_INDEX = 0;
    public final static int PORK_INDEX = 1;
    public final static int CHICKEN_INDEX = 2;
    public final static int FISH_INDEX = 3;
    public final static int EGGS_INDEX = 4;
    public final static int BEANS_INDEX = 5;
    public final static int VEGGIES_INDEX = 6;


    public final static String NAME_BEEF = "Beef";
    public final static String NAME_PORK = "Pork";
    public final static String NAME_CHICKEN = "Chicken";
    public final static String NAME_FISH = "Fish";
    public final static String NAME_EGGS = "Eggs";
    public final static String NAME_BEANS = "Beans";
    public final static String NAME_VEGGIES = "Vegetables";

    public final static String[] NAMES = {
            NAME_BEEF,
            NAME_PORK,
            NAME_CHICKEN,
            NAME_FISH,
            NAME_EGGS,
            NAME_BEANS,
            NAME_VEGGIES
    };

    public final static double BEEF_CARBON_PER_KG = 27;
    public final static double PORK_CARBON_PER_KG = 21.1;
    public final static double CHICKEN_CARBON_PER_KG = 6.9;
    public final static double FISH_CARBON_PER_KG = 6.1;
    public final static double EGGS_CARBON_PER_KG = 4.8;
    public final static double BEANS_CARBON_PER_KG = 2;
    public final static double VEGGIES_CARBON_PER_KG = 2;

    public final static String BEEF_DESCRIPTION = "Steak, ribs...";
    public final static String PORK_DESCRIPTION = "Bacon, sausage...";
    public final static String CHICKEN_DESCRIPTION = "Breast, thighs...";
    public final static String FISH_DESCRIPTION = "Sushi, white fish fillet, fish and chips...";
    public final static String EGGS_DESCRIPTION = "Quiche, hard-boiled, fried...";
    public final static String BEANS_DESCRIPTION = "Falafel, hoummus, tofu...";
    public final static String VEGGIES_DESCRIPTION = "Pasta, veggies, soup...";

    public static Protein[] array() {
        Protein[] defaultProteins = {
                new Protein(NAME_BEEF, BEEF_CARBON_PER_KG, BEEF_DESCRIPTION),
                new Protein(NAME_PORK, PORK_CARBON_PER_KG, PORK_DESCRIPTION),
                new Protein(NAME_CHICKEN, CHICKEN_CARBON_PER_KG, CHICKEN_DESCRIPTION),
                new Protein(NAME_FISH, FISH_CARBON_PER_KG, FISH_DESCRIPTION),
                new Protein(NAME_EGGS, EGGS_CARBON_PER_KG, EGGS_DESCRIPTION),
                new Protein(NAME_BEANS, BEANS_CARBON_PER_KG, BEANS_DESCRIPTION),
                new Protein(NAME_VEGGIES, VEGGIES_CARBON_PER_KG, VEGGIES_DESCRIPTION)
        };

        return defaultProteins;
    }




    public static int[] COLORS = {
            toAndroidColor(230, 35, 50),
            toAndroidColor(255, 150, 130),
            toAndroidColor(245, 215, 195),
            toAndroidColor(50, 205, 230),
            toAndroidColor(255, 250, 225),
            toAndroidColor(100, 50, 0),
            toAndroidColor(0, 255, 55)};


    public static Map<String,Protein> map(){
        Map<String,Protein> map = new LinkedHashMap<>();
        for (Protein protein: DefaultProteins.array()){
            map.put(protein.getName(),protein);
        }

        return map;
    }


    public static Protein byName(String name){
        return (Protein) DefaultProteins.map().get(name);
    }


    //Format used by android.graphics.color
    private static int toAndroidColor(int red, int green, int blue){
        return 0xff000000 | (red << 16) | (green << 8) | blue;
    }




}
