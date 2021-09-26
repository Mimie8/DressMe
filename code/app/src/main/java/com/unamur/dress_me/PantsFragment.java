package com.unamur.dress_me;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * @author Amélie Dieudonné
 * A fragment in which every pants are displayed, the fragment is present in the PantsActivity
 * A simple {@link Fragment} subclass.
 */
public class PantsFragment extends Fragment {

    public static View ActivityView;

    /**
     * @author Amélie Dieudonné
     * Contructor
     */
    public PantsFragment() {
        // Required empty public constructor
    }

    /**
     * @author Amélie Dieudonné
     * @param inflater allowing to inflate the layout to show the layout in the activity
     * @param container container containing the layout
     * @param savedInstanceState previous state of the fragment
     * @return mView = the view of the fragment
     * onCreateView is call on a loop and not just once, making every change in the list of pants appearing on the screen right away
     * Display every pants
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_pants, container, false);

        LinearLayout LLCasual = mView.findViewById(R.id.LLCasual);
        LinearLayout LLChic = mView.findViewById(R.id.LLChic);
        LinearLayout LLSport = mView.findViewById(R.id.LLSport);

        // restore the Pants'list
        DressingActivity.restorePants();

        //For every pants in the pants list, display it
        for (final Pants pants : Pants.getListPants()) {
            //Generate the displaying of the right sections (Casual , chic and sport) depending on the kind of the pants already created
            generateSections(mView);

            //-----------------------------------CLOTHE RL--------------------------------------------
            final RelativeLayout clotheRL = new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams paramsRL = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            clotheRL.setLayoutParams(paramsRL);


            //-------------------------------CLOTHE BACKGROUND----------------------------------------

            ImageButton background = new ImageButton(getActivity());
            RelativeLayout.LayoutParams paramsBG = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 250);
            paramsBG.setMargins(35, 0, 35, 0);
            background.setLayoutParams(paramsBG);
            background.setClickable(false);
            Drawable roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default, null);
            roundDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            background.setBackground(roundDrawable);

            clotheRL.addView(background);


            //----------------------------------CLOTHE COLOR------------------------------------------
            ImageButton colorImg = new ImageButton(getActivity());
            RelativeLayout.LayoutParams paramsCB = new RelativeLayout.LayoutParams(100, 210);
            paramsCB.addRule(RelativeLayout.ALIGN_PARENT_START);
            paramsCB.setMargins(60, 20, 0, 20);
            colorImg.setLayoutParams(paramsCB);
            Drawable roundDrawable2;
            if (pants.getColor() == Color.parseColor("#ffffff")) {
                roundDrawable2 = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_black_border, null);
            } else {
                roundDrawable2 = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default, null);
                roundDrawable2.setColorFilter(pants.getColor(), PorterDuff.Mode.SRC_ATOP);
            }
            colorImg.setBackground(roundDrawable2);

            clotheRL.addView(colorImg);


            //----------------------------------CLOTHE TYPE------------------------------------------
            ImageView imgType = new ImageView(getActivity());
            RelativeLayout.LayoutParams paramsIT = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 190);
            paramsIT.addRule(RelativeLayout.ALIGN_PARENT_START);
            paramsIT.setMargins(190, 35, 0, 25);
            imgType.setLayoutParams(paramsIT);

            switch (pants.getType()) {
                case PANTS:
                    imgType.setImageResource(R.drawable.pants);
                    break;
                case SHORTS:
                    imgType.setImageResource(R.drawable.shorts);
                    break;
                case SKIRT:
                    imgType.setImageResource(R.drawable.skirt);
                    break;
            }
            imgType.setAdjustViewBounds(true);
            imgType.setScaleType(ImageView.ScaleType.FIT_CENTER);

            clotheRL.addView(imgType);

            //---------------------------------- CROSS (to delete the cloth --------------------------
            ImageButton imgCross = new ImageButton(getActivity());
            RelativeLayout.LayoutParams paramsIC = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 190);
            paramsIC.addRule(RelativeLayout.ALIGN_PARENT_END);
            paramsIC.setMargins(0, 30, 70, 30);
            imgCross.setLayoutParams(paramsIC);
            imgCross.setImageResource(R.drawable.cross);
            imgCross.setBackgroundResource(R.drawable.button_background);
            imgCross.setAdjustViewBounds(true);
            imgCross.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgCross.setId(View.generateViewId());

            clotheRL.addView(imgCross);

            //---------------------------------CLOTHE KIND-----------------------------------------

            LinearLayout LLView = new LinearLayout(getActivity());

            switch (pants.getKind()) {
                case CASUAL:
                    LLView = LLCasual;
                    break;
                case CHIC:
                    LLView = LLChic;
                    break;
                case SPORT:
                    LLView = LLSport;
                    break;
            }
            LLView.addView(clotheRL);

            //Create a shallow copy of the LLView to make it final and access it within the clicklistener
            final LinearLayout LLViewBis = LLView;


            //-----------------------------------DELETE CLOTHE------------------------------------

            imgCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mDeleteBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = (getActivity()).getLayoutInflater();
                    View v = inflater.inflate(R.layout.popup_dialog_delete, null);
                    //Allow to see the dialog on the screen
                    mDeleteBuilder.setView(v);
                    // Create the alert dialog
                    final AlertDialog deleteDialog = mDeleteBuilder.create();

                    Button ok = v.findViewById(R.id.ok_btn);
                    Button cancel = v.findViewById(R.id.cancel_btn);

                    //When the suppresion is validated, erase every trace of the pants deleted
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), R.string.success_delete_msg, Toast.LENGTH_SHORT).show();

                            LLViewBis.removeView(clotheRL);

                            ArrayList<Pants> listPants = Pants.getListPants();
                            listPants.remove(pants);
                            Pants.setListPants(listPants);

                            if (pants.getKind()==DressingActivity.Kind.CASUAL){
                                Pants.setMinusOneNumberPantsCasual();
                            }
                            if (pants.getKind()==DressingActivity.Kind.CHIC){
                                Pants.setMinusOneNumberPantsChic();
                            }
                            if (pants.getKind()==DressingActivity.Kind.SPORT){
                                Pants.setMinusOneNumberPantsSport();
                            }

                            generateSections(mView);

                            //Save the pants List in the shared preferences
                            DressingActivity.savePants();

                            deleteDialog.cancel();

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteDialog.cancel();
                        }
                    });

                    deleteDialog.show();
                }
            });

        }


        return mView;

    }

    /**
     * @author Amélie Dieudonné
     * @param savedInstanceState Previous state of the fragment
     * onCreate is called only once every time the fragment is created (so every time we enter in the Pants Activity)
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @author Amélie Dieudonné
     * @param view current view
     * @param savedInstanceState Previous state of the fragment
     * Called after the onCreate function, used to specifies the view
     */
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * @author Amélie Dieudonné
     * @param view current view
     * Generate the display of the section text view (Casual, chic, sport) depending of the kind of pants created (Ex: no casual pants means no casual sections)
     */
    public void generateSections(View view) {

        //Get every text view back from the XML
        TextView TVnoPants = view.findViewById(R.id.noPants);
        TextView TVCasual = view.findViewById(R.id.casualTitle);
        TextView TVChic = view.findViewById(R.id.chicTitle);
        TextView TVSport = view.findViewById(R.id.sportTitle);

        //Update the nbr of pants display under the toolbar
        TextView TVnbrPants = ActivityView.findViewById(R.id.TVnbrPants);
        int nbrPants = Pants.getNbrPants();
        TVnbrPants.setText(""+nbrPants);


        //-------------------Decide to show the msg specifying that there is no pants yet ----------
        if (Pants.getNumberPantsCasual()==0 && Pants.getNumberPantsChic()==0 && Pants.getNumberPantsSport()==0) {
            TVnoPants.setVisibility(View.VISIBLE);
        } else {
            TVnoPants.setVisibility(View.GONE);
        }


        //if the nbr of clothes in a section is > 0 and if the section isn't showed yet then show it
        if (Pants.getNumberPantsCasual() > 0) {
            TVCasual.setVisibility(View.VISIBLE);
        } else {
            TVCasual.setVisibility(View.GONE);
        }

        if (Pants.getNumberPantsChic() > 0) {
            TVChic.setVisibility(View.VISIBLE);
        } else {
            TVChic.setVisibility(View.GONE);
        }

        if (Pants.getNumberPantsSport() > 0) {
            TVSport.setVisibility(View.VISIBLE);
        } else {
            TVSport.setVisibility(View.GONE);
        }
    }

}
