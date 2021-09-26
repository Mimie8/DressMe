package com.unamur.dress_me;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;

import static com.unamur.dress_me.DressingActivity.Kind.SPORT;

/**
 * @author Amélie Dieudonné
 */
public class PantsActivity extends AppCompatActivity {

    //Initialisation
    private int PANTS_LIMIT_NBR = 50; //Maximum number of pants which we can add in the app

    private DressingActivity.Type type_cloth;
    private int color_cloth;
    private DressingActivity.Kind kind_cloth;

    private boolean hasSelectedColor;

    private ViewFlipper flipper;

    PantsFragment newPantsFragment;

    /**
     * @author Amélie Dieudonné
     * @param savedInstanceState Previous state of the activity
     * Called once when the activity is created (every time the PantsActivity is open)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pants);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (savedInstanceState == null) {
            newPantsFragment = new PantsFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.RLfragment, newPantsFragment).commit();
        }


        //------------------------TOOLBAR------------------------------
        //Configure toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_pants);

        //Set the arrow back icon
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //Exit the activity if the back arrow is clicked
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //------------------------NUMBER OF PANTS------------------------------

        //Update the nbr of pants
        TextView TVnbrPants = findViewById(R.id.TVnbrPants);
        int nbrPants = Pants.getNbrPants();
        TVnbrPants.setText(""+nbrPants);

        PantsFragment.ActivityView = findViewById(android.R.id.content);

        //------------------------ALERT DIALOG-------------------------
        //When the floating button is clicked
        ImageButton btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Pants.getNbrPants()< PANTS_LIMIT_NBR){

                    //Open alert dialog
                    openAlertDialog();

                } else {
                    //If there are too many pants
                    Toast.makeText(PantsActivity.this, R.string.too_many_pants_msg, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * @author Amélie Dieudonné
     * Open the dialog which manages the adding of a new pants
     */
    public void openAlertDialog(){

        //Initialise the alert dialog
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(PantsActivity.this);
        LayoutInflater inflater = (PantsActivity.this).getLayoutInflater();
        View mView = inflater.inflate(R.layout.popup_activity_pants, null);

        //Initialise the elements
        flipper = mView.findViewById(R.id.viewFlipper);

        ImageButton mPants = mView.findViewById(R.id.pants);
        ImageButton mShorts = mView.findViewById(R.id.shorts);
        ImageButton mSkirt = mView.findViewById(R.id.skirt);

        HashMap<ImageButton, DressingActivity.Type> hmp_pants = new HashMap<>();
        hmp_pants.put(mPants, DressingActivity.Type.PANTS);
        hmp_pants.put(mShorts, DressingActivity.Type.SHORTS);
        hmp_pants.put(mSkirt, DressingActivity.Type.SKIRT);

        Button mCasual = mView.findViewById(R.id.btn_casual);
        Button mChic = mView.findViewById(R.id.btn_chic);
        Button mSport = mView.findViewById(R.id.btn_sport);
        ArrayList<Button> list_kind = new ArrayList<>();
        list_kind.add(mCasual);
        list_kind.add(mChic);
        list_kind.add(mSport);

        Button mOK = mView.findViewById(R.id.btn_ok);
        Button mCancel = mView.findViewById(R.id.btn_cancel);

        // Create the alert dialog
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();


        //Get the cloth type
        getType(hmp_pants);

        //Get the cloth color
        getColor(mView);

        //Get the cloth kind
        getKind(list_kind);

        //OK button
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasSelectedColor && kind_cloth != null && type_cloth != null) {
                    //If the field is fill then ok
                    Toast.makeText(PantsActivity.this, R.string.success_add_msg, Toast.LENGTH_SHORT).show();

                    //add the cloth
                    new Pants(type_cloth, color_cloth, kind_cloth);

                    //refresh the fragment
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(newPantsFragment).attach(newPantsFragment).commit();

                    //Save the pants List in the shared preferences
                    DressingActivity.savePants();

                    //Close the dialog and reset the variables
                    type_cloth = null;
                    hasSelectedColor = false;
                    kind_cloth = null;
                    dialog.cancel();

                }else{
                    //If the field isn't fill then ko
                    Toast.makeText(PantsActivity.this, R.string.error_add_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Cancel button
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_cloth = null;
                hasSelectedColor = false;
                kind_cloth = null;
                dialog.cancel();
            }
        });

        //Allow to see the dialog on the screen
        dialog.show();
    }

    /**
     * @author Amélie Dieudonné
     * @param hmap Hash map linking the button to his enumeration type (TSHIRT, SHIRT, ..., PANTS, ..., BASKET, ...)
     * Get the Type of the cloth which the user want to add and stock it into the global type_cloth variable
     */
    public void getType(final HashMap<ImageButton, DressingActivity.Type> hmap){

        //For every type button
        for (HashMap.Entry obj : hmap.entrySet()) {

            final ImageButton btn = (ImageButton) obj.getKey();
            //if a button is clicked
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Create a Hash map with the not selected ones
                    HashMap<ImageButton, DressingActivity.Type> btn_not_selected = new HashMap<>(hmap);

                    for (ImageButton btn_img : hmap.keySet()){

                        //Identify the selected button and the not selected ones
                        if (view==btn_img){
                            type_cloth = hmap.get(btn_img); //Assign the type to the instance var
                            btn_not_selected.remove(btn_img);

                            //Change the background color as red for the selected button and as default for all the others
                            btn_img.setBackgroundTintList(ContextCompat.getColorStateList(PantsActivity.this, R.color.red));
                            for(ImageButton btn_NS : btn_not_selected.keySet()){
                                btn_NS.setBackgroundTintList(ContextCompat.getColorStateList(PantsActivity.this, R.color.btnDefault));
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * @author Amélie Dieudonné
     * @param mView Current view (mainly useful for the element recuperation from the XML
     * Get the Color of the cloth which the user want to add and stock it into the global color_cloth variable
     */
    public void getColor(View mView){


        final Button mSelectedColor = mView.findViewById(R.id.color);
        final ImageView mNoColor = mView.findViewById(R.id.noColor);

        hasSelectedColor = false;

        //Create a list with all buttons of the color table
        ArrayList<Button> btnList = createColorList(mView);

        //Loop on every button
        for (final Button btn : btnList) {
            //if the button is clicked
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hasSelectedColor = true;
                    //get the color selected
                    color_cloth = Color.parseColor((String) btn.getTag());
                    //set that color to the button of the selected color
                    mSelectedColor.setBackgroundColor(color_cloth);
                    //put the no color image in invisible
                    mNoColor.setVisibility(View.INVISIBLE);
                    //get back to the dialog
                    previousView(view);
                }
            });
        }
    }

    /**
     * @author Amélie Dieudonné
     * @param list List of the 3 buttons defining the 3 kind of clothes (CASUAL, CHIC and SPORT)
     * Get the Kind of the cloth which the user want to add and stock it into the global kind_cloth variable
     */
    public void getKind(ArrayList<Button> list){

        final Button mCasual = list.get(0);
        final Button mChic = list.get(1);
        final Button mSport = list.get(2);

        for (final Button btn : list) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Initialise the 3 buttons as the default situation
                    Button selectedButton = mCasual;
                    Button notSelectedButton1 = mChic;
                    Button notSelectedButton2 = mSport;

                    //set the different buttons correctly
                    if (view == mCasual) {
                        selectedButton = mCasual;
                        notSelectedButton1 = mChic;
                        notSelectedButton2 = mSport;
                        //set the kind at casual
                        kind_cloth = DressingActivity.Kind.CASUAL;
                    }
                    if (view == mChic) {
                        selectedButton = mChic;
                        notSelectedButton1 = mCasual;
                        notSelectedButton2 = mSport;
                        //set the kind at casual
                        kind_cloth = DressingActivity.Kind.CHIC;
                    }
                    if (view == mSport) {
                        selectedButton = mSport;
                        notSelectedButton1 = mCasual;
                        notSelectedButton2 = mChic;
                        //set the kind at casual
                        kind_cloth = SPORT;
                    }

                    //Apply the modifications on the correct button
                    selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(PantsActivity.this, R.color.red));
                    selectedButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    notSelectedButton1.setBackgroundTintList(ContextCompat.getColorStateList(PantsActivity.this, R.color.btnDefault));
                    notSelectedButton1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    notSelectedButton2.setBackgroundTintList(ContextCompat.getColorStateList(PantsActivity.this, R.color.btnDefault));
                    notSelectedButton2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                }
            });
        }
    }

    /**
     * @author Amélie Dieudonné
     * @param mView current view for get the button back from the XML
     * @return colorListButton list of every color buttons
     * Get every color buttons of the color table popup layout and put them in a array list to loop on it later
     */
    public ArrayList<Button> createColorList(View mView){
        //get all the buttons of the color table
        //first row
        Button btnColorYellow = mView.findViewById(R.id.btn_color_yellow);
        Button btnColorMoutarde = mView.findViewById(R.id.btn_color_moutarde);
        Button btnColorLightOrange = mView.findViewById(R.id.btn_color_lightOrange);
        Button btnColorDarkOrange = mView.findViewById(R.id.btn_color_darkOrange);
        Button btnColorBeige = mView.findViewById(R.id.btn_color_beige);
        //second row
        Button btnColorLightRed = mView.findViewById(R.id.btn_color_lightRed);
        Button btnColorRed = mView.findViewById(R.id.btn_color_red);
        Button btnColorBordeaux = mView.findViewById(R.id.btn_color_bordeaux);
        Button btnColorLightBrown = mView.findViewById(R.id.btn_color_lightBrown);
        Button btnColorDarkBrown = mView.findViewById(R.id.btn_color_darkBrown);
        //third row
        Button btnColorLightPink = mView.findViewById(R.id.btn_color_lightPink);
        Button btnColorPink = mView.findViewById(R.id.btn_color_pink);
        Button btnColorMagenta = mView.findViewById(R.id.btn_color_magenta);
        Button btnColorLightMauve = mView.findViewById(R.id.btn_color_lightMauve);
        Button btnColorMauve = mView.findViewById(R.id.btn_color_mauve);
        //fourth row
        Button btnColorLightBlue = mView.findViewById(R.id.btn_color_lightBlue);
        Button btnColorBlueSky = mView.findViewById(R.id.btn_color_blueSky);
        Button btnColorDarkBlue = mView.findViewById(R.id.btn_color_darkBlue);
        Button btnColorLightGreen = mView.findViewById(R.id.btn_color_lightGreen);
        Button btnColorDarkGreen = mView.findViewById(R.id.btn_color_darkGreen);
        //fifth row
        Button btnColorWhite = mView.findViewById(R.id.btn_color_white);
        Button btnColorLightGrey = mView.findViewById(R.id.btn_color_lightGrey);
        Button btnColorDarkGrey = mView.findViewById(R.id.btn_color_darkGrey);
        Button btnColorBlack = mView.findViewById(R.id.btn_color_black);
        Button btnColorKaki = mView.findViewById(R.id.btn_color_kaki);

        //add every button to a list
        ArrayList<Button> colorListButton = new ArrayList<Button>();
        //first row
        colorListButton.add(btnColorYellow);
        colorListButton.add(btnColorMoutarde);
        colorListButton.add(btnColorLightOrange);
        colorListButton.add(btnColorDarkOrange);
        colorListButton.add(btnColorBeige);
        //second row
        colorListButton.add(btnColorLightRed);
        colorListButton.add(btnColorRed);
        colorListButton.add(btnColorBordeaux);
        colorListButton.add(btnColorLightBrown);
        colorListButton.add(btnColorDarkBrown);
        //thrid row
        colorListButton.add(btnColorLightPink);
        colorListButton.add(btnColorPink);
        colorListButton.add(btnColorMagenta);
        colorListButton.add(btnColorLightMauve);
        colorListButton.add(btnColorMauve);
        //fourth row
        colorListButton.add(btnColorLightBlue);
        colorListButton.add(btnColorBlueSky);
        colorListButton.add(btnColorDarkBlue);
        colorListButton.add(btnColorLightGreen);
        colorListButton.add(btnColorDarkGreen);
        //fifth row
        colorListButton.add(btnColorWhite);
        colorListButton.add(btnColorLightGrey);
        colorListButton.add(btnColorDarkGrey);
        colorListButton.add(btnColorBlack);
        colorListButton.add(btnColorKaki);

        return colorListButton;
    }

    /**
     * @author Amélie Dieudonné
     * @param view current flipper view
     * Go to the previous flipper view
     */
    public void previousView(View view){
        flipper.showPrevious();
    }

    /**
     * @author Amélie Dieudonné
     * @param view current flipper view
     * Go to the next flipper view
     */
    public void nextView(View view){
        flipper.showNext();
    }

}
