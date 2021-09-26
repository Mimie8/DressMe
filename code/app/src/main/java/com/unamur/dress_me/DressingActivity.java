package com.unamur.dress_me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * @author Amélie Dieudonné
 * @version 2.5
 */
public class DressingActivity extends AppCompatActivity {

    //Initialisation of variables and enumeration types
    enum Kind{
        CASUAL, CHIC, SPORT
    }

    enum Type{
        TSHIRT, SHIRT, SWEATSHIRT, TANKTOP, PULLOVER, DRESS, PANTS, SHORTS, SKIRT, SHOES, BOOTS, TONG, BASKET, HIGHHEEL, SANDAL
    }

    private final static String LIST_TOPS_KEY = "ListTopsKey";
    private final static String LIST_PANTS_KEY = "ListPantsKey";
    private final static String LIST_SHOES_KEY = "ListShoesKey";

    private final static String NBR_TOPS_CASUAL_KEY = "nbrTopsCasualKey";
    private final static String NBR_TOPS_CHIC_KEY = "nbrTopsChicKey";
    private final static String NBR_TOPS_SPORT_KEY = "nbrTopsSportKey";

    private final static String NBR_PANTS_CASUAL_KEY = "nbrPantsCasualKey";
    private final static String NBR_PANTS_CHIC_KEY = "nbrPantsChicKey";
    private final static String NBR_PANTS_SPORT_KEY = "nbrPantsSportKey";

    private final static String NBR_SHOES_CASUAL_KEY = "nbrShoesCasualKey";
    private final static String NBR_SHOES_CHIC_KEY = "nbrShoesChicKey";
    private final static String NBR_SHOES_SPORT_KEY = "nbrShoesSportKey";

    public static SharedPreferences mPreferences;
    private static final String sharedPrefFile = "com.unamur.dressme.dressmeSharedPrefs";

    /**
     * @author Amélie Dieudonné
     * @param savedInstanceState the previous state of the activity, if there was one
     * onCreate is called when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dressing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //------------------------TOOLBAR------------------------------
        //Configure toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_dressing);

        //Set the arrow back icon
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //Exit the activity if the back arrow is clicked
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //------------------------BUTTONS------------------------------
        //Button tops
        ImageButton btn_tops = findViewById(R.id.btn_tops);

        btn_tops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the tops activity
                Intent layout_tops = new Intent(getApplicationContext(), TopsActivity.class);
                startActivity(layout_tops);
            }
        });

        //Button pants
        ImageButton btn_pants = findViewById(R.id.btn_pants);

        btn_pants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the pants activity
                Intent layout_pants = new Intent(getApplicationContext(), PantsActivity.class);
                startActivity(layout_pants);
            }
        });

        //Button shoes
        ImageButton btn_shoes = findViewById(R.id.btn_shoes);

        btn_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the shoes activity
                Intent layout_shoes = new Intent(getApplicationContext(), ShoesActivity.class);
                startActivity(layout_shoes);
            }
        });
    }

    /**
     * @author Amélie Dieudonné
     * Save the ArrayList<Tops> containing all the tops created in the app
     */
    public static void saveTops(){

        //Initialisation
        mPreferences =  allowStaticContext.getAppContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPreferences.edit();

        //Save the list
        Gson gson = new Gson();
        String json = gson.toJson(Tops.getListTops());
        prefsEditor.putString(LIST_TOPS_KEY, json);

        //Save the counters
        prefsEditor.putInt(NBR_TOPS_CASUAL_KEY, Tops.getNumberTopsCasual());
        prefsEditor.putInt(NBR_TOPS_CHIC_KEY, Tops.getNumberTopsChic());
        prefsEditor.putInt(NBR_TOPS_SPORT_KEY, Tops.getNumberTopsSport());

        prefsEditor.apply();
    }

    /**
     * @author Amélie Dieudonné
     * Restore the ArrayList<Tops> of every tops created in the app
     */
    public static void restoreTops(){

        mPreferences =  allowStaticContext.getAppContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

        //Restore the list
        Gson gson = new Gson();
        String json = mPreferences.getString(LIST_TOPS_KEY, null);
        java.lang.reflect.Type type = new TypeToken<ArrayList<Tops>>(){}.getType();
        ArrayList<Tops> mListTops = gson.fromJson(json, type);
        if (mListTops==null){
            mListTops = new ArrayList<>();
        }

        Tops.setListTops(mListTops);

        //Restores the counters
        Tops.setNumberTopsCasual(mPreferences.getInt(NBR_TOPS_CASUAL_KEY, 0));
        Tops.setNumberTopsChic(mPreferences.getInt(NBR_TOPS_CHIC_KEY, 0));
        Tops.setNumberTopsSport(mPreferences.getInt(NBR_TOPS_SPORT_KEY, 0));
    }

    /**
     * @author Amélie Dieudonné
     * Save the ArrayList<Pants> containing all the pants created in the app
     */
    public static void savePants(){

        //Initialisation
        mPreferences =  allowStaticContext.getAppContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPreferences.edit();

        //Save the list
        Gson gson = new Gson();
        String json = gson.toJson(Pants.getListPants());
        prefsEditor.putString(LIST_PANTS_KEY, json);

        //Save the counters
        prefsEditor.putInt(NBR_PANTS_CASUAL_KEY, Pants.getNumberPantsCasual());
        prefsEditor.putInt(NBR_PANTS_CHIC_KEY, Pants.getNumberPantsChic());
        prefsEditor.putInt(NBR_PANTS_SPORT_KEY, Pants.getNumberPantsSport());

        prefsEditor.apply();
    }

    /**
     * @author Amélie Dieudonné
     * Restore the ArrayList<Pants> containing all the pants created in the app
     */
    public static void restorePants(){

        mPreferences =  allowStaticContext.getAppContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

        //Restore the list
        Gson gson = new Gson();
        String json = mPreferences.getString(LIST_PANTS_KEY, null);
        java.lang.reflect.Type type = new TypeToken<ArrayList<Pants>>(){}.getType();
        ArrayList<Pants> mListPants = gson.fromJson(json, type);
        if (mListPants==null){
            mListPants = new ArrayList<>();
        }

        Pants.setListPants(mListPants);

        //Restores the counters
        Pants.setNumberPantsCasual(mPreferences.getInt(NBR_PANTS_CASUAL_KEY, 0));
        Pants.setNumberPantsChic(mPreferences.getInt(NBR_PANTS_CHIC_KEY, 0));
        Pants.setNumberPantsSport(mPreferences.getInt(NBR_PANTS_SPORT_KEY, 0));
    }

    /**
     * @author Amélie Dieudonné
     * Save the ArrayList<Shoes> containing all the shoes created in the app
     */
    public static void saveShoes(){

        //Initialisation
        mPreferences =  allowStaticContext.getAppContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPreferences.edit();

        //Save the list
        Gson gson = new Gson();
        String json = gson.toJson(Shoes.getListShoes());
        prefsEditor.putString(LIST_SHOES_KEY, json);

        //Save the counters
        prefsEditor.putInt(NBR_SHOES_CASUAL_KEY, Shoes.getNumberShoesCasual());
        prefsEditor.putInt(NBR_SHOES_CHIC_KEY, Shoes.getNumberShoesChic());
        prefsEditor.putInt(NBR_SHOES_SPORT_KEY, Shoes.getNumberShoesSport());

        prefsEditor.apply();
    }

    /**
     * @author Amélie Dieudonné
     * Restore the ArrayList<Shoes> containing all the shoes created in the app
     */
    public static void restoreShoes(){

        mPreferences =  allowStaticContext.getAppContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

        //Restore the list
        Gson gson = new Gson();
        String json = mPreferences.getString(LIST_SHOES_KEY, null);
        java.lang.reflect.Type type = new TypeToken<ArrayList<Shoes>>(){}.getType();
        ArrayList<Shoes> mListShoes = gson.fromJson(json, type);
        if (mListShoes==null){
            mListShoes = new ArrayList<>();
        }

        Shoes.setListShoes(mListShoes);

        //Restores the counters
        Shoes.setNumberShoesCasual(mPreferences.getInt(NBR_SHOES_CASUAL_KEY, 0));
        Shoes.setNumberShoesChic(mPreferences.getInt(NBR_SHOES_CHIC_KEY, 0));
        Shoes.setNumberShoesSport(mPreferences.getInt(NBR_SHOES_SPORT_KEY, 0));
    }

}
