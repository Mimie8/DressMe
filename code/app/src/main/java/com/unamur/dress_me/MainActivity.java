package com.unamur.dress_me;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.unamur.dress_me.DressingActivity.Type.BASKET;
import static com.unamur.dress_me.DressingActivity.Type.BOOTS;
import static com.unamur.dress_me.DressingActivity.Type.DRESS;
import static com.unamur.dress_me.DressingActivity.Type.HIGHHEEL;
import static com.unamur.dress_me.DressingActivity.Type.PANTS;
import static com.unamur.dress_me.DressingActivity.Type.PULLOVER;
import static com.unamur.dress_me.DressingActivity.Type.SANDAL;
import static com.unamur.dress_me.DressingActivity.Type.SHIRT;
import static com.unamur.dress_me.DressingActivity.Type.SHOES;
import static com.unamur.dress_me.DressingActivity.Type.SHORTS;
import static com.unamur.dress_me.DressingActivity.Type.SKIRT;
import static com.unamur.dress_me.DressingActivity.Type.SWEATSHIRT;
import static com.unamur.dress_me.DressingActivity.Type.TANKTOP;
import static com.unamur.dress_me.DressingActivity.Type.TONG;
import static com.unamur.dress_me.DressingActivity.Type.TSHIRT;
import static com.unamur.dress_me.DressingActivity.restorePants;
import static com.unamur.dress_me.DressingActivity.restoreShoes;
import static com.unamur.dress_me.DressingActivity.restoreTops;

/**
 * @author Amélie Dieudonné
 */
public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;

    TextView degreeMeteo, descriptionMeteo, minDegree, maxDegree, sunglassesAdvice, scarfGlovesAdvice, umbrellaAdvice, sunscreenAdvice, noWeather;
    ImageView iconMeteo;
    RelativeLayout currentWeather;

    Button btn_casual,  btn_chic, btn_sport;
    String desired_kind;
    List<ArrayList<DressingActivity.Type>> listOfTypes;
    HashMap<String, DressingActivity.Type> hmp_allTops = new HashMap<>();
    HashMap<String, DressingActivity.Type> hmp_allPants = new HashMap<>();
    HashMap<String, DressingActivity.Type> hmp_allShoes = new HashMap<>();

    HashMap<Integer, ArrayList<Integer>> hmap_colorMatch = new HashMap<>();

    String degree = "", sMinDegree = "", sMaxDegree = "", descriptionWeather = "";

    String sLatitude, sLongitude;

    LinearLayout topLayout;
    LinearLayout pantsLayout;
    LinearLayout shoesLayout;
    TextView noGeneratedOutfit;
    LinearLayout generatedOutfitLayout;

    /**
     * @author Amélie Dieudonné
     * @param savedInstanceState restore the state of the previous MainActivity , if there was one
     * Called when the activity is created (just once after the start activity)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Build the hash map containing every color and the list of color they are matching with
        /*
                Structure of the JSON:
                {
                    "#ffffff": [
                                "#000000",
                                "#555555",
                                ...
                                ]
                }
         */
        getJsonColor();

        //Build the hash map doing the link between the type in String and the type in enumeration type
        hmp_allTops = new HashMap<>();
        hmp_allTops.put("tshirt", DressingActivity.Type.TSHIRT);
        hmp_allTops.put("shirt", DressingActivity.Type.SHIRT);
        hmp_allTops.put("sweatshirt", DressingActivity.Type.SWEATSHIRT);
        hmp_allTops.put("tanktop", DressingActivity.Type.TANKTOP);
        hmp_allTops.put("pullover", DressingActivity.Type.PULLOVER);
        hmp_allTops.put("dress", DRESS);
        hmp_allPants = new HashMap<>();
        hmp_allPants.put("pants", DressingActivity.Type.PANTS);
        hmp_allPants.put("shorts", DressingActivity.Type.SHORTS);
        hmp_allPants.put("skirt", DressingActivity.Type.SKIRT);
        hmp_allShoes = new HashMap<>();
        hmp_allShoes.put("shoes", DressingActivity.Type.SHOES);
        hmp_allShoes.put("boots", DressingActivity.Type.BOOTS);
        hmp_allShoes.put("tong", DressingActivity.Type.TONG);
        hmp_allShoes.put("basket", DressingActivity.Type.BASKET);
        hmp_allShoes.put("highheel", DressingActivity.Type.HIGHHEEL);
        hmp_allShoes.put("sandal", DressingActivity.Type.SANDAL);

        //Get the layout and element used for the outfit generation
        topLayout = findViewById(R.id.generatedTopLayout);
        pantsLayout = findViewById(R.id.generatedPantsLayout);
        shoesLayout = findViewById(R.id.generatedShoesLayout);
        noGeneratedOutfit = findViewById(R.id.noGeneratedOutfit);
        generatedOutfitLayout = findViewById(R.id.generatedOutfitLayout);

        //--------------get longitude and latitude-----------------------------

        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);

        ImageButton btn_actualise = findViewById(R.id.btn_actualise);
        btn_actualise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        noWeather = findViewById(R.id.TVnoWeather);
                        currentWeather = findViewById(R.id.currentWeather);

                        if (location != null){

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            sLatitude = String.format(Locale.getDefault(), "%.2f", latitude);
                            sLongitude = String.format(Locale.getDefault(), "%.2f", longitude);

                            //----------------------------Meteo layout-------------------------------
                            degreeMeteo = findViewById(R.id.degreeMeteo);
                            descriptionMeteo = findViewById(R.id.descriptionMeteo);
                            iconMeteo = findViewById(R.id.iconMeteo);
                            minDegree = findViewById(R.id.minDegree);
                            maxDegree = findViewById(R.id.maxDegree);

                            sunglassesAdvice = findViewById(R.id.advice_sunglasses);
                            sunscreenAdvice = findViewById(R.id.advice_sunscreen);
                            scarfGlovesAdvice = findViewById(R.id.advice_scarfGloves);
                            umbrellaAdvice = findViewById(R.id.advice_umbrella);

                            findWeather();

                        } else {

                            noWeather.setVisibility(VISIBLE);
                            currentWeather.setVisibility(GONE);

                            //cannot get location
                            Toast.makeText(MainActivity.this, R.string.noLocation, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btn_casual = findViewById(R.id.btn_generate_casual);
        btn_casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.noWeather, Toast.LENGTH_SHORT).show();

                noWeather.setVisibility(VISIBLE);
                currentWeather.setVisibility(GONE);
            }
        });

        btn_chic = findViewById(R.id.btn_generate_chic);
        btn_chic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.noWeather, Toast.LENGTH_SHORT).show();

                noWeather.setVisibility(VISIBLE);
                currentWeather.setVisibility(GONE);
            }
        });

        btn_sport = findViewById(R.id.btn_generate_sport);
        btn_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.noWeather, Toast.LENGTH_SHORT).show();

                noWeather.setVisibility(VISIBLE);
                currentWeather.setVisibility(GONE);
            }
        });

        //-----------------------------Get the dressing button-------------------------------
        Button btn_dressing = findViewById(R.id.btn_dressing);

        //if click on the dressing button
        btn_dressing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //start the dressing activity
                Intent layout_dressing = new Intent(getApplicationContext(), DressingActivity.class);
                startActivity(layout_dressing);
            }
        });
    }

    /**
     * @author Amélie Dieudonné
     * Ask for the permission of getting the phone location
     */
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    /**
     * @author Amélie Dieudonné
     * Find the weather with the URL and the API
     */
    public void findWeather(){

        //Get the json with the weather of the current location (based on latitude and longitude)
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + sLatitude + "&lon=" + sLongitude + "&appid=cc2c60afbd0487e8ce2b2bc146881b37&units=metric";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String newDegree = String.valueOf(main_object.getDouble("temp"));
                    String newSMinDegree = String.valueOf((int)main_object.getDouble("temp_min"));
                    String newSMaxDegree = String.valueOf((int)main_object.getDouble("temp_max"));
                    String newDescription = object.getString("description");

                    //If the weather values aren't empty
                    if (!newDegree.isEmpty() && !newSMinDegree.isEmpty() && !newSMaxDegree.isEmpty() && !newDescription.isEmpty()) {

                        if (degree.equals(newDegree) && sMinDegree.equals(newSMinDegree) && sMaxDegree.equals(newSMaxDegree) && descriptionWeather.equals(newDescription)){
                            //weather already up to date
                            Toast.makeText(MainActivity.this, R.string.upToDate, Toast.LENGTH_SHORT).show();
                        }
                        //Build the weather layout
                        noWeather.setVisibility(GONE);
                        currentWeather.setVisibility(VISIBLE);

                        degree = newDegree;
                        sMinDegree = newSMinDegree;
                        sMaxDegree = newSMaxDegree;
                        descriptionWeather = newDescription;

                        double temp = Double.parseDouble(degree);
                        int temp_int = (int) temp;
                        degreeMeteo.setText(String.valueOf(temp_int));
                        descriptionMeteo.setText(descriptionWeather);
                        minDegree.setText(sMinDegree);
                        maxDegree.setText(sMaxDegree);



                        String icon = object.getString("icon");
                        switch (icon){
                            case "01d":
                                iconMeteo.setImageResource(R.drawable.day01);
                                break;
                            case "01n":
                                iconMeteo.setImageResource(R.drawable.night01);
                                break;
                            case "02d":
                                iconMeteo.setImageResource(R.drawable.day02);
                                break;
                            case "02n":
                                iconMeteo.setImageResource(R.drawable.night02);
                                break;
                            case "03d":
                                iconMeteo.setImageResource(R.drawable.day03);
                                break;
                            case "03n":
                                iconMeteo.setImageResource(R.drawable.night03);
                                break;
                            case "04d":
                                iconMeteo.setImageResource(R.drawable.day04);
                                break;
                            case "04n":
                                iconMeteo.setImageResource(R.drawable.night04);
                                break;
                            case "09d":
                                iconMeteo.setImageResource(R.drawable.day09);
                                break;
                            case "09n":
                                iconMeteo.setImageResource(R.drawable.night09);
                                break;
                            case "10d":
                                iconMeteo.setImageResource(R.drawable.day10);
                                break;
                            case "10n":
                                iconMeteo.setImageResource(R.drawable.night10);
                                break;
                            case "11d":
                                iconMeteo.setImageResource(R.drawable.day11);
                                break;
                            case "11n":
                                iconMeteo.setImageResource(R.drawable.night11);
                                break;
                            case "13d":
                                iconMeteo.setImageResource(R.drawable.day13);
                                break;
                            case "13n":
                                iconMeteo.setImageResource(R.drawable.night13);
                                break;
                            case "50d":
                                iconMeteo.setImageResource(R.drawable.day50);
                                break;
                            case "50n":
                                iconMeteo.setImageResource(R.drawable.night50);
                                break;
                        }

                        //------------------------Advice conditions-------------------------------------

                        //Sunglasses if temperature > 22°C and clear sky
                        if (temp_int > 22 && descriptionWeather.equals("clear sky")){
                            sunglassesAdvice.setVisibility(VISIBLE);
                        } else {
                            sunglassesAdvice.setVisibility(GONE);
                        }
                        //Sunscreen if temperature > 28°C and clear sky
                        if (temp_int > 28 && descriptionWeather.equals("clear sky")){
                            sunscreenAdvice.setVisibility(VISIBLE);
                        } else {
                            sunscreenAdvice.setVisibility(GONE);
                        }
                        //Umbrella if rain
                        if (descriptionWeather.contains("rain") || descriptionWeather.contains("snow") || descriptionWeather.contains("drizzle")){
                            umbrellaAdvice.setVisibility(VISIBLE);
                        } else {
                            umbrellaAdvice.setVisibility(GONE);
                        }
                        //Scarf and gloves if (temperature < 5 and not clear sky) or (temperature <= 0)
                        if ((temp_int < 5 && !descriptionWeather.equals("clear sky")) || temp_int <= 0 || descriptionWeather.contains("snow")){
                            scarfGlovesAdvice.setVisibility(VISIBLE);
                        } else {
                            scarfGlovesAdvice.setVisibility(GONE);
                        }

                        //-------------------------Generate outfits------------------------------------------------
                        generateOutfit(temp_int, descriptionWeather);

                    } else {

                        noWeather.setVisibility(VISIBLE);
                        currentWeather.setVisibility(GONE);

                        //info not available
                        Toast.makeText(MainActivity.this, R.string.notAvailable, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //noWifi to update the weather
                Toast.makeText(MainActivity.this, R.string.noWifi, Toast.LENGTH_SHORT).show();
                noGeneratedOutfit.setVisibility(VISIBLE);
                topLayout.setVisibility(GONE);
                pantsLayout.setVisibility(GONE);
                shoesLayout.setVisibility(GONE);
                generatedOutfitLayout.setVisibility(GONE);

                noWeather.setVisibility(VISIBLE);
                currentWeather.setVisibility(GONE);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

    /**
     * @author Amélie Dieudonné
     * @param degree current degree
     * @param descriptionWeather current weather description
     * Set the difference of the generated outfit based on the 3 different kinds (casual/chic/sport)
     */
    public void generateOutfit(final int degree, final String descriptionWeather){

        btn_casual = findViewById(R.id.btn_generate_casual);
        btn_chic = findViewById(R.id.btn_generate_chic);
        btn_sport = findViewById(R.id.btn_generate_sport);

        listOfTypes = new ArrayList<>();

        btn_casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desired_kind = "casual";
                listOfTypes = getPossibleTypes(degree, descriptionWeather, desired_kind);

                //Sort the list of possible types to remove the types which are not present in the dressing
                List<ArrayList<DressingActivity.Type>> newListOfType = sortListOfTypes(listOfTypes, DressingActivity.Kind.CASUAL);

                //If there are no top types available , no shoes type available or (no pants type available and no dress) for the current weather, impossible to get an outfit.
                if (newListOfType.get(0).isEmpty() || newListOfType.get(2).isEmpty() || (newListOfType.get(1).isEmpty() && !(newListOfType.get(0).contains(DRESS)))) {

                    //Show a toaster for 1 second
                    final Toast toast;

                    toast = Toast.makeText(MainActivity.this, R.string.notEnoughClothes, Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                    noGeneratedOutfit.setVisibility(VISIBLE);
                    topLayout.setVisibility(GONE);
                    pantsLayout.setVisibility(GONE);
                    shoesLayout.setVisibility(GONE);
                    generatedOutfitLayout.setVisibility(GONE);
                }
                //sinon
                else {
                    //Generate the outfit
                     generateRandomOutfit(newListOfType, DressingActivity.Kind.CASUAL);
                }

            }
        });

        btn_chic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desired_kind = "chic";
                listOfTypes = getPossibleTypes(degree, descriptionWeather, desired_kind);

                //Sort the list of possible types to remove the types which are not present in the dressing
                List<ArrayList<DressingActivity.Type>> newListOfType = sortListOfTypes(listOfTypes, DressingActivity.Kind.CHIC);

                //If there are no top types available , no shoes type available or (no pants type available and no dress) for the current weather, impossible to get an outfit.
                if (newListOfType.get(0).isEmpty() || newListOfType.get(2).isEmpty() || (newListOfType.get(1).isEmpty() && !(newListOfType.get(0).contains(DRESS)))) {
                    //Show a toaster for 1 second
                    final Toast toast;

                    toast = Toast.makeText(MainActivity.this, R.string.notEnoughClothes, Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                    noGeneratedOutfit.setVisibility(VISIBLE);
                    topLayout.setVisibility(GONE);
                    pantsLayout.setVisibility(GONE);
                    shoesLayout.setVisibility(GONE);
                    generatedOutfitLayout.setVisibility(GONE);
                }
                //sinon
                else {
                    //Generate the outfit
                    generateRandomOutfit(newListOfType, DressingActivity.Kind.CHIC);
                }
            }
        });

        btn_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desired_kind = "sport";
                listOfTypes = getPossibleTypes(degree, descriptionWeather, desired_kind);

                //Sort the list of possible types to remove the types which are not present in the dressing
                List<ArrayList<DressingActivity.Type>> newListOfType = sortListOfTypes(listOfTypes, DressingActivity.Kind.SPORT);

                //If there are no top types available , no shoes type available or (no pants type available and no dress) for the current weather, impossible to get an outfit.
                if (newListOfType.get(0).isEmpty() || newListOfType.get(2).isEmpty() || (newListOfType.get(1).isEmpty() && !(newListOfType.get(0).contains(DRESS)))) {
                    //Show a toaster for 1 second
                    final Toast toast;

                    toast = Toast.makeText(MainActivity.this, R.string.notEnoughClothes, Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                    noGeneratedOutfit.setVisibility(VISIBLE);
                    topLayout.setVisibility(GONE);
                    pantsLayout.setVisibility(GONE);
                    shoesLayout.setVisibility(GONE);
                    generatedOutfitLayout.setVisibility(GONE);
                }
                else {
                    //Generate the outfit
                    generateRandomOutfit(newListOfType, DressingActivity.Kind.SPORT);
                }
            }
        });
    }

    /**
     * @author Amélie Dieudonné
     * @param degree current degree
     * @param descriptionWeather current weather description
     * @param desired_kind type of outfit desired by the user (based on button click)
     * @return listAllowedTypes = list of 3 array lists, 1 for the types of top allowed by the weather, 1 for the pants one and one for the shoes one
     */
    public List<ArrayList<DressingActivity.Type>> getPossibleTypes(int degree, String descriptionWeather, String desired_kind){

        ArrayList<DressingActivity.Type> listPossibleTops = new ArrayList<>();
        ArrayList<DressingActivity.Type> listPossiblePants = new ArrayList<>();
        ArrayList<DressingActivity.Type> listPossibleShoes = new ArrayList<>();
        List<ArrayList<DressingActivity.Type>> listAllowedTypes = new ArrayList<>();

        //get the json
        String json;
        try{
            InputStream is = getAssets().open("weatherConstraints.json");
            int size = is.available();
            byte [] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            JSONObject jsonObj = new JSONObject(json);
            //get the jsonObject in the desired_kind key
            JSONObject jsonKindObj = jsonObj.getJSONObject(desired_kind);
            Iterator<String> keys = jsonKindObj.keys();

            //for every key of the desired kind object ("tshirt","shirt", ....)
            while(keys.hasNext()){
                String key = keys.next();

                JSONArray array = jsonKindObj.getJSONArray(key);
                //check if the weather conditions are respected
                boolean respectedCond = checkConditions(array, degree, descriptionWeather);

                if (respectedCond){
                    if (hmp_allTops.containsKey(key)) {
                        listPossibleTops.add(hmp_allTops.get(key));
                    } else if (hmp_allPants.containsKey(key)){
                        listPossiblePants.add(hmp_allPants.get(key));
                    } else {
                        listPossibleShoes.add(hmp_allShoes.get(key));
                    }
                }

            }
            listAllowedTypes.add(listPossibleTops);
            listAllowedTypes.add(listPossiblePants);
            listAllowedTypes.add(listPossibleShoes);

        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "IOException", Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "JSONException", Toast.LENGTH_SHORT).show();
        }
        return listAllowedTypes;
    }

    /**
     * @author Amélie Dieudonné
     * @param array JSON Array containing the weather conditions for the type
     * @param degree current degree
     * @param description current weather description
     * @return true if the weather conditions are respected for the type, false otherwise
     */
    public boolean checkConditions(JSONArray array, int degree, String description){

        try{
            JSONObject myJsonObject;
            int i = 0;

            // if the array is empty (means that there are no conditions), condition is ok
            if (array.length()==0){
                return true;
            }

            //for every JSON object in the JSONArray
            while (i < array.length()){
                myJsonObject = array.getJSONObject(i);

                //for every key in the json object
                Iterator<String> keys = myJsonObject.keys();

                //Default init value for AND is true
                boolean matchesConditions = true;

                //For each group in the array (?)
                while(keys.hasNext()) {
                    String underKey = keys.next();
                    if (underKey.equals("maxTemp")){
                        if (degree <= myJsonObject.getInt(underKey) ){
                            //Condition verified, go to the next condition in the group
                            continue;
                        } else {
                            //Condition doesn't match, go to next element directly
                            matchesConditions = false;
                            break;
                        }
                    }
                    if (underKey.equals("minTemp")){
                        if (degree >= myJsonObject.getInt(underKey)){
                            //Condition verified, go to the next condition in the group
                            continue;
                        } else {
                            //Condition doesn't match, go to next element directly
                            matchesConditions = false;
                            break;
                        }
                    }
                    if (underKey.equals("weathers")){
                        JSONArray weatherArray = myJsonObject.getJSONArray(underKey);
                        if (weatherArray.toString().contains(description)){
                            //Condition verified, go to the next condition in the group
                            continue;
                        } else {
                            //Condition doesn't match, go to next element directly
                            matchesConditions = false;
                            break;
                        }
                    }
                }

                //All conditions from one of the groups are met
                if (matchesConditions) {
                    return true;
                }

                i++;
            }

        } catch (JSONException e){
            e.printStackTrace();
        }


        //No conditions were met
        return false;
    }

    /**
     * @author Amélie Dieudonné
     * @param list list of 3 array lists, one for the weather allowed types of tops, one for the pants one and one for the shoes one
     * @param desired_kind the desired kind chosen by the user for the generated outfit
     * @return newListOfTypes = the list in the parameter sorted
     * Delete from the parameter "list" the types not present in the desired kind in the dressing
     */
    public List<ArrayList<DressingActivity.Type>> sortListOfTypes(List<ArrayList<DressingActivity.Type>> list, DressingActivity.Kind desired_kind){
        restoreTops();
        restorePants();
        restoreShoes();

        ArrayList<Tops> topsInDressing = new ArrayList<>(Tops.getListTops());
        ArrayList<Pants> pantsInDressing = new ArrayList<>(Pants.getListPants());
        ArrayList<Shoes> shoesInDressing = new ArrayList<>(Shoes.getListShoes());

        ArrayList<DressingActivity.Type> typeOfTopsInDressing = new ArrayList<>();
        ArrayList<DressingActivity.Type> typeOfPantsInDressing = new ArrayList<>();
        ArrayList<DressingActivity.Type> typeOfShoesInDressing = new ArrayList<>();

        ArrayList<ArrayList<DressingActivity.Type>> newListOfTypes = new ArrayList<>();

        int index = 0;
        //generate the list of tops type present in the dressing and of the right kind
        while (index < topsInDressing.size()){
            Tops top = topsInDressing.get(index);
            //if the kind of the top in the dressing is the desired_kind and the list of top types present in the dressing don't have this type yet, add it to the list.
            if ((top.getKind() == desired_kind) && (!typeOfTopsInDressing.contains(top.getType()))){
                typeOfTopsInDressing.add(top.getType());
            }
            index++;
        }

        index = 0;
        //generate the list of pants type present in the dressing and of the right kind
        while (index < pantsInDressing.size()){
            Pants pants = pantsInDressing.get(index);
            //if the kind of the pants is the desired_kind and the list of pants types present in the dressing don't have this type yet, add it to the list.
            if ((pants.getKind() == desired_kind) && (!typeOfPantsInDressing.contains(pants.getType()))){
                typeOfPantsInDressing.add(pants.getType());
            }
            index++;
        }

        index = 0;
        //generate the list of shoes type present in the dressing and of the right kind
        while (index < shoesInDressing.size()){
            Shoes shoes = shoesInDressing.get(index);
            //if the kind of the shoes is the desired_kind and the list of shoes types present in the dressing don't have this type yet, add it to the list.
            if ((shoes.getKind() == desired_kind) && (!typeOfPantsInDressing.contains(shoes.getType()))){
                typeOfShoesInDressing.add(shoes.getType());
            }
            index++;
        }

        ArrayList<DressingActivity.Type> possibleTops = list.get(0);
        ArrayList<DressingActivity.Type> possiblePants = list.get(1);
        ArrayList<DressingActivity.Type> possibleShoes = list.get(2);

        ArrayList<DressingActivity.Type> newPossibleTops = new ArrayList<>(possibleTops);
        ArrayList<DressingActivity.Type> newPossiblePants = new ArrayList<>(possiblePants);
        ArrayList<DressingActivity.Type> newPossibleShoes = new ArrayList<>(possibleShoes);

        //Delete from the possible tops the type of tops not present in the dressing
        for (int j = 0; j < possibleTops.size(); j++){
            if (!(typeOfTopsInDressing.contains(possibleTops.get(j)))){
                newPossibleTops.remove(possibleTops.get(j));
            }
        }
        //Delete from the possible pants the type of pants not present in the dressing
        for (int j = 0; j < possiblePants.size(); j++){
            if (!(typeOfPantsInDressing.contains(possiblePants.get(j)))){
                newPossiblePants.remove(possiblePants.get(j));
            }
        }
        //Delete from the possible shoes the type of shoes not present in the dressing
        for (int j = 0; j < possibleShoes.size(); j++){
            if (!(typeOfShoesInDressing.contains(possibleShoes.get(j)))){
                newPossibleShoes.remove(possibleShoes.get(j));
            }
        }
        newListOfTypes.add(newPossibleTops);
        newListOfTypes.add(newPossiblePants);
        newListOfTypes.add(newPossibleShoes);

        return  newListOfTypes;
    }

    /**
     * @author Amélie Dieudonné
     * @param list list of 3 array lists, one for the weather allowed types of tops, one for the pants one and one for the shoes one
     * @param desired_kind the desired kind chosen by the user for the generated outfit
     * Build a list of possible outfits which respected all the conditions, pick a random outfit in the list and display it
     */
    public void generateRandomOutfit(List<ArrayList<DressingActivity.Type>> list, DressingActivity.Kind desired_kind) {
        ArrayList<Tops> topsInDressing = new ArrayList<>(Tops.getListTops());
        ArrayList<Pants> pantsInDressing = new ArrayList<>(Pants.getListPants());
        ArrayList<Shoes> shoesInDressing = new ArrayList<>(Shoes.getListShoes());

        ArrayList<DressingActivity.Type> possibleTops = list.get(0);
        ArrayList<DressingActivity.Type> possiblePants = list.get(1);
        ArrayList<DressingActivity.Type> possibleShoes = list.get(2);

        /*          PSEUDO-CODE

                    for every top in the dressing
                        if top.Type == OK and top.Kind == OK
                            for every pants in the dressing
                                if pants.Type == OK and pants.Kind == OK and pants.Color == OK
                                    for every shoes in the dressing
                                        if shoes.Type == OK and shoes.Kind == OK and shoes.Color == OK
                                            if top.Type == DRESS
                                                don't save the pants
                                            else
                                                outfitList.add(top,pants,shoes)

                    pick a random outfit in the list outfitList


        */

        ArrayList<List<Clothes>> outfitList = new ArrayList<>();

        for (int i = 0; i < topsInDressing.size(); i++){
            Tops top = topsInDressing.get(i);
            if (possibleTops.contains(top.getType()) && top.getKind() == desired_kind){
                for (int j = 0; j < pantsInDressing.size(); j++){
                    Pants pants = pantsInDressing.get(j);
                    if (possiblePants.contains(pants.getType()) && pants.getKind() == desired_kind && matchColor(pants.getColor(), top.getColor()) && pants.getColor() != top.getColor()){
                        for (int k = 0; k < shoesInDressing.size(); k++){
                            Shoes shoes = shoesInDressing.get(k);
                            if (possibleShoes.contains(shoes.getType()) && shoes.getKind() == desired_kind && matchColor(shoes.getColor(), pants.getColor()) && matchColor(shoes.getColor(), top.getColor())){
                                List<Clothes> outfit = new ArrayList<>();
                                if (top.getType() != DRESS){
                                    outfit.add(top);
                                    outfit.add(pants);
                                    outfit.add(shoes);
                                    outfitList.add(outfit);
                                } else {
                                    outfit.add(top);
                                    outfit.add(null);
                                    outfit.add(shoes);
                                    outfitList.add(outfit);
                                }
                            }
                        }
                    }
                }
            }
        }

        //if there is no outfit to generate
        if (outfitList.size()==0){
            final Toast toast;
            //Show a toaster for 1 second
            toast = Toast.makeText(MainActivity.this, R.string.noMatching, Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1000);

        } else {
            //Pick an outfit and display it
            Random randomGenerator = new Random();

            int index = randomGenerator.nextInt(outfitList.size());
            List<Clothes> generatedOutfit = outfitList.get(index);
            Tops randomTop = (Tops) generatedOutfit.get(0);
            Pants randomPants = (Pants) generatedOutfit.get(1);
            Shoes randomShoes = (Shoes) generatedOutfit.get(2);


            //---------------------------Build an hashmap to get the right image to show-----------------
            HashMap<DressingActivity.Type, Integer> hmapTypeImg = new HashMap<>();
            hmapTypeImg.put(TSHIRT, R.drawable.tshirt);
            hmapTypeImg.put(SHIRT, R.drawable.shirt);
            hmapTypeImg.put(SWEATSHIRT, R.drawable.sweatshirt);
            hmapTypeImg.put(TANKTOP, R.drawable.tanktop);
            hmapTypeImg.put(PULLOVER, R.drawable.pullover);
            hmapTypeImg.put(DRESS, R.drawable.dress);

            hmapTypeImg.put(PANTS, R.drawable.pants);
            hmapTypeImg.put(SHORTS, R.drawable.shorts);
            hmapTypeImg.put(SKIRT, R.drawable.skirt);

            hmapTypeImg.put(SHOES, R.drawable.shoes);
            hmapTypeImg.put(BOOTS, R.drawable.boots);
            hmapTypeImg.put(TONG, R.drawable.tong);
            hmapTypeImg.put(BASKET, R.drawable.basket);
            hmapTypeImg.put(HIGHHEEL, R.drawable.highheel);
            hmapTypeImg.put(SANDAL, R.drawable.sandal);

            //---------------------------Show the generated outfit-----------------------------------

            ImageView topImg = findViewById(R.id.generatedTop);
            ImageView pantsImg = findViewById(R.id.generatedPants);
            ImageView shoesImg = findViewById(R.id.generatedShoes);

            Button topBtn = findViewById(R.id.generatedTopColor);
            Button pantsBtn = findViewById(R.id.generatedPantsColor);
            Button shoesBtn = findViewById(R.id.generatedShoesColor);

            noGeneratedOutfit.setVisibility(GONE);
            generatedOutfitLayout.setVisibility(VISIBLE);

            for (Map.Entry<DressingActivity.Type, Integer> entry : hmapTypeImg.entrySet()) {
                if (randomTop.getType()==entry.getKey()){
                    topLayout.setVisibility(VISIBLE);
                    topImg.setImageResource(entry.getValue());

                    Drawable roundDrawable;
                    if (randomTop.getColor() == Color.parseColor("#ffffff")){
                        roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_black_border, null);
                    } else {
                        roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default, null);
                        roundDrawable.setColorFilter(randomTop.getColor(), PorterDuff.Mode.SRC_ATOP);
                    }
                    topBtn.setBackground(roundDrawable);
                }

                if (randomPants!=null){
                    if (randomPants.getType()==entry.getKey()){
                        pantsLayout.setVisibility(VISIBLE);
                        pantsImg.setImageResource(entry.getValue());

                        Drawable roundDrawable;
                        if (randomPants.getColor() == Color.parseColor("#ffffff")){
                            roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_black_border, null);
                        } else {
                            roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default, null);
                            roundDrawable.setColorFilter(randomPants.getColor(), PorterDuff.Mode.SRC_ATOP);
                        }
                        pantsBtn.setBackground(roundDrawable);
                    }
                }

                if (randomShoes.getType()==entry.getKey()){
                    shoesLayout.setVisibility(VISIBLE);
                    shoesImg.setImageResource(entry.getValue());

                    Drawable roundDrawable;
                    if (randomShoes.getColor() == Color.parseColor("#ffffff")){
                        roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_black_border, null);
                    } else {
                        roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default, null);
                        roundDrawable.setColorFilter(randomShoes.getColor(), PorterDuff.Mode.SRC_ATOP);
                    }
                    shoesBtn.setBackground(roundDrawable);
                }
            }

            final Toast toast;
            Handler handler;
            switch (desired_kind){
                case CASUAL:
                    //Show a toaster for 1 second
                    toast = Toast.makeText(MainActivity.this, R.string.casualGenerated, Toast.LENGTH_SHORT);
                    toast.show();

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);

                    break;
                case CHIC:
                    //Show a toaster for 1 second
                    toast = Toast.makeText(MainActivity.this, R.string.chicGenerated, Toast.LENGTH_SHORT);
                    toast.show();

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                    break;
                case SPORT:
                    //Show a toaster for 1 second
                    toast = Toast.makeText(MainActivity.this, R.string.sportGenerated, Toast.LENGTH_SHORT);
                    toast.show();

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                    break;
            }
        }

    }

    /**
     * @author Amélie Dieudonné
     * Build the hash map containing a key for every color and for each color, as value, an array list of every colors matching with it
     */
    public void getJsonColor(){
        hmap_colorMatch = new HashMap<>();
        //get the json
        String json;
        try{
            InputStream is = getAssets().open("colorsConstraints.json");
            int size = is.available();
            byte [] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            JSONObject jsonObj = new JSONObject(json);
            Iterator<String> keys = jsonObj.keys();

            //for every key of the desired kind object (for every color key)
            while(keys.hasNext()){
                String key = keys.next();
                int intKey = Color.parseColor(key);

                JSONArray jArray = jsonObj.getJSONArray(key);

                ArrayList<Integer> array = new ArrayList<>();

                if (jArray != null) {
                    for (int j = 0; j<jArray.length(); j++){
                        array.add(Color.parseColor(jArray.getString(j)));
                    }
                }

                //Build the hmap
                hmap_colorMatch.put(intKey,array);

            }

        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "IOException", Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "JSONException", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @author Amélie Dieudonné
     * @param currentClothColor color of the cloth we have to see if the color match
     * @param otherClothColor color of the cloth which we have to compare the currenclothColor color with
     * @return true if the otherClothColor color match with the currentClothColor color
     */
    public boolean matchColor(int currentClothColor, int otherClothColor){

        //check if the color match
        ArrayList<Integer> array = hmap_colorMatch.get(otherClothColor);

        return array.contains(currentClothColor);
    }
}
