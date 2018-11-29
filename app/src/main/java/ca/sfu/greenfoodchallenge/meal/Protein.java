package ca.sfu.greenfoodchallenge.meal;

/*Protein class models a protein which has a name and description
* and an associated amount of emitted carbon dioxide per kilogram of consumed protein.
 */
public class Protein implements Comparable {

    private String proteinName;
    private double carbonPerKg;
    private String description;

    final static String NON_EMPTY_SPACE = "";
    final static double DEFAULT_CARBON_PER_KG = 10;

    public Protein(String proteinName, double carbonPerKg, String proteinDescription) {
        this.proteinName = proteinName;
        this.carbonPerKg = carbonPerKg;
        this.description = proteinDescription;
    }

    public String getName() {
        return this.proteinName;
    }


    public void setProteinName(String proteinName) {
        if(!proteinName.equals(NON_EMPTY_SPACE)){
            this.proteinName = proteinName;
        }
        else {
            //Do not change name;
        }
    }

    public double getCarbonPerKg() {
        return this.carbonPerKg;
    }


    public void setCarbonPerKg(double carbonPerKg) {
        if(carbonPerKg >= 0){
            this.carbonPerKg = carbonPerKg;
        } else {
            this.carbonPerKg = DEFAULT_CARBON_PER_KG; //arbitrary default value
        }
    }

    public String getDescription() {
        return this.description;
    }




    public void setDescription(String proteinDescription) {
        this.description = proteinDescription;
    }




    public int compareTo(Object other){
        return proteinName.compareTo(((Protein) other).proteinName);
    }

    /*public int compareTo(Object other) {
        return proteinName.compareTo(((Protein) other).proteinName);
    }*/

}
