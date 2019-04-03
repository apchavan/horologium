package amey.clock.cities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

public class CitiesDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cities_db";

    public CitiesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Cities.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", Cities.getTableName()));
        onCreate(sqLiteDatabase);
    }

    public void insertCity(String cityName, String countryName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // First we'll check whether the 'city' already exist or not (returns back to the caller) in database.
        String CHECK_QUERY =
                String.format("SELECT city, country FROM cities WHERE city = '%s' AND country = '%s'", cityName, countryName);

        Cursor cursor = this.getReadableDatabase().rawQuery(CHECK_QUERY, null);
        int count = cursor.getCount();

        cursor.close();
        if (count > 0)
            return;

        // If 'city' already not exists then execute the code below.
        ContentValues contentValues = new ContentValues();

        contentValues.put(Cities.getCityColumn(), cityName);
        contentValues.put(Cities.getCountryColumn(), countryName);

        sqLiteDatabase.insert(Cities.getTableName(), null, contentValues);
        sqLiteDatabase.close();

    }

    /* The method below 'getCitiesCount()' is currently not used but may required for future use,
     * so just un-comment & call it !
     */
    /*
    public Cities getCity(String city) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                Cities.getTableName(),
                new String[]{Cities.getCityColumn(), Cities.getCountryColumn()},
                Cities.getCityColumn() + "=?",
                new String[]{city},
                null, null, null, null
        );

        Cities cities = null;
        if (cursor != null) {
            cursor.moveToFirst();
            cities = new Cities(
                    cursor.getString(cursor.getColumnIndex(Cities.getCityColumn())),
            cursor.getString(cursor.getColumnIndex(Cities.getCountryColumn()))
            );
        }   // 'if (cursor != null)' closed.
        Objects.requireNonNull(cursor).close();

        return cities;
    }
    */

    List<Cities> getAllCities() {
        List<Cities> citiesList = new ArrayList<>();

        String SELECT_QUERY = String.format(" SELECT * FROM %s", Cities.getTableName());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cities cities = new Cities();
                cities.setCityName(cursor.getString(cursor.getColumnIndex(Cities.getCityColumn())));
                cities.setCountryName(cursor.getString(cursor.getColumnIndex(Cities.getCountryColumn())));
                citiesList.add(cities);
            } while (cursor.moveToNext());
        }   // 'if(cursor.moveToFirst())' closed.
        cursor.close();
        sqLiteDatabase.close();
        return citiesList;
    }

    int getCitiesCount() {
        String SELECT_QUERY = String.format(" SELECT * FROM %s", Cities.getTableName());
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    void deleteCity(Cities cities) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(
                Cities.getTableName(),
                Cities.getCityColumn() + "=?",
                new String[]{cities.getCityName()});
        sqLiteDatabase.close();
    }
}
