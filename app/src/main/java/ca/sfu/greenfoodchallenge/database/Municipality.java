package ca.sfu.greenfoodchallenge.database;


public class Municipality {

    public static final String NAME_NONE = "None";

    public static final String NAME_ABBOTSFORD = "City of Abbotsford";
    public static final String NAME_ANMORE = "Village of Anmore";
    public static final String NAME_BELCARRA = "Village of Belcarra";
    public static final String NAME_BOWEN = "Bowen Island Municipality";
    public static final String NAME_BURNABY= "City of Burnaby";
    public static final String NAME_COQUITLAM= "City of Coquitlam";
    public static final String NAME_DELTA= "Corporation of Delta";
    public static final String NAME_LANGLEY= "City of Langley";
    public static final String NAME_TOWNSHIP_LANGLEY = "Township of Langley";
    public static final String NAME_LIONS_BAY = "Village of Lions Bay";
    public static final String NAME_MAPLE_RIDGE = "District of Maple Ridge";
    public static final String NAME_NEW_WESTMINSTER = "City of New Westminster";
    public static final String NAME_CITY_NORTHVAN = "City of North Vancouver";
    public static final String NAME_DISTRICT_NORTHVAN = "District of North Vancouver";
    public static final String NAME_PITT_MEADOWS = "City of Pitt Meadows";
    public static final String NAME_PORT_COQUITLAM = "City of Port Coquitlam";
    public static final String NAME_PORT_MOODY = "City of Port Moody";
    public static final String NAME_RICHMOND = "City of Richmond";
    public static final String NAME_SURREY = "City of Surrey";
    public static final String NAME_TSAWWASSEN = "Tsawwassen";
    public static final String NAME_CITY_VANCOUVER = "City of Vancouver";
    public static final String NAME_DISTRICT_WESTVAN = "District of West Vancouver";
    public static final String NAME_WHITE_ROCK= "City of White Rock";

    public static final String[] LIST = {
            NAME_NONE,
            NAME_ABBOTSFORD,
            NAME_ANMORE,
            NAME_BELCARRA,
            NAME_BOWEN,
            NAME_BURNABY,
            NAME_COQUITLAM,
            NAME_DELTA,
            NAME_LANGLEY,
            NAME_TOWNSHIP_LANGLEY,
            NAME_LIONS_BAY,
            NAME_MAPLE_RIDGE,
            NAME_NEW_WESTMINSTER,
            NAME_CITY_NORTHVAN,
            NAME_DISTRICT_NORTHVAN,
            NAME_PITT_MEADOWS,
            NAME_PORT_COQUITLAM,
            NAME_PORT_MOODY,
            NAME_RICHMOND,
            NAME_SURREY,
            NAME_TSAWWASSEN,
            NAME_CITY_VANCOUVER,
            NAME_DISTRICT_WESTVAN,
            NAME_WHITE_ROCK,

    };

    public static String filter(String municipality) {
        // TODO check that the municipality exists if not null
        if(municipality == null){
            return NAME_NONE;
        }
        return municipality;
    }
}
