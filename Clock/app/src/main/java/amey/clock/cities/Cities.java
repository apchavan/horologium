package amey.clock.cities;

public class Cities {

    private static final String TABLE_NAME = "cities", CITY_COLUMN= "city", COUNTRY_COLUMN = "country";
    private String cityName, countryName;

    public Cities(){}

    public Cities(String cityName, String countryName){
        this.cityName = cityName;
        this.countryName = countryName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public static String getCityColumn() {
        return CITY_COLUMN;
    }

    public static String getCountryColumn() {
        return COUNTRY_COLUMN;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getCreateTableQuery(){
        return ("CREATE TABLE " + TABLE_NAME +
        "( "+ CITY_COLUMN + " TEXT, " + COUNTRY_COLUMN + " TEXT )");
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryName() {
        return countryName;
    }
}

