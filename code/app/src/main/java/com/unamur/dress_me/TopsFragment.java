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
 * A simple {@link Fragment} subclass.
 */
public class TopsFragment extends Fragment {

    public static View ActivityView;

    /**
     * @author Amélie Dieudonné
     * Constructor
     */
    public TopsFragment() {
        // Required empty public constructor
    }

    /**
     * @author Amélie Dieudonné
     * @param inflater allowing to inflate the layout to show the layout in the activity
     * @param container container containing the layout
     * @param savedInstanceState previous state of the fragment
     * @return mView = the view of the fragment
     * onCreateView is call on a loop and not just once, making every change in the list of tops appearing on the screen right away
     * Display every tops
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_tops, container, false);

        LinearLayout LLCasual = mView.findViewById(R.id.LLCasual);
        LinearLayout LLChic = mView.findViewById(R.id.LLChic);
        LinearLayout LLSport = mView.findViewById(R.id.LLSport);

        // restore the Tops'list
        DressingActivity.restoreTops();

        for (final Tops top : Tops.getListTops()) {

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
            if (top.getColor() == Color.parseColor("#ffffff")) {
                roundDrawable2 = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default_black_border, null);
            } else {
                roundDrawable2 = ResourcesCompat.getDrawable(getResources(), R.drawable.button_default, null);
                roundDrawable2.setColorFilter(top.getColor(), PorterDuff.Mode.SRC_ATOP);
            }
            colorImg.setBackground(roundDrawable2);

            clotheRL.addView(colorImg);


            //----------------------------------CLOTHE TYPE------------------------------------------
            ImageView imgType = new ImageView(getActivity());
            RelativeLayout.LayoutParams paramsIT = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 190);
            paramsIT.addRule(RelativeLayout.ALIGN_PARENT_START);
            paramsIT.setMargins(190, 35, 0, 25);
            imgType.setLayoutParams(paramsIT);

            switch (top.getType()) {
                case TSHIRT:
                    imgType.setImageResource(R.drawable.tshirt);
                    break;
                case SHIRT:
                    imgType.setImageResource(R.drawable.shirt);
                    break;
                case SWEATSHIRT:
                    imgType.setImageResource(R.drawable.sweatshirt);
                    break;
                case TANKTOP:
                    imgType.setImageResource(R.drawable.tanktop);
                    break;
                case PULLOVER:
                    imgType.setImageResource(R.drawable.pullover);
                    break;
                case DRESS:
                    imgType.setImageResource(R.drawable.dress);
                    break;
            }
            imgType.setAdjustViewBounds(true);
            imgType.setScaleType(ImageView.ScaleType.FIT_CENTER);

            clotheRL.addView(imgType);

            //---------------------------------- CROSS ------------------------------------------
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

            switch (top.getKind()) {
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

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), R.string.success_delete_msg, Toast.LENGTH_SHORT).show();

                            LLViewBis.removeView(clotheRL);

                            ArrayList<Tops> listTops = Tops.getListTops();
                            listTops.remove(top);
                            Tops.setListTops(listTops);

                            if (top.getKind()==DressingActivity.Kind.CASUAL){
                                Tops.setMinusOneNumberTopsCasual();
                            }
                            if (top.getKind()==DressingActivity.Kind.CHIC){
                                Tops.setMinusOneNumberTopsChic();
                            }
                            if (top.getKind()==DressingActivity.Kind.SPORT){
                                Tops.setMinusOneNumberTopsSport();
                            }

                            generateSections(mView);

                            //Save the tops List in the shared preferences
                            DressingActivity.saveTops();

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
     * onCreate is called only once every time the fragment is created (so every time we enter in the Tops Activity)
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
     * Generate the display of the section text view (Casual, chic, sport) depending of the kind of tops created (Ex: no casual tops means no casual sections)
     */
    public void generateSections(View view) {

        //Get every text view back from the XML
        TextView TVnoTops = view.findViewById(R.id.noTops);
        TextView TVCasual = view.findViewById(R.id.casualTitle);
        TextView TVChic = view.findViewById(R.id.chicTitle);
        TextView TVSport = view.findViewById(R.id.sportTitle);

        //Update the nbr of tops
        TextView TVnbrTops = ActivityView.findViewById(R.id.TVnbrTops);
        int nbrTops = Tops.getNbrTops();
        TVnbrTops.setText(""+nbrTops);


        //-----------------------------Decide to show the title or not ---------------------------
        if (Tops.getNumberTopsCasual()==0 && Tops.getNumberTopsChic()==0 && Tops.getNumberTopsSport()==0) {
            TVnoTops.setVisibility(View.VISIBLE);
        } else {
            TVnoTops.setVisibility(View.GONE);
        }


        //if the nbr of clothes in a section is > 0 and if the section isn't showed then show it
        if (Tops.getNumberTopsCasual() > 0) {
            TVCasual.setVisibility(View.VISIBLE);
        } else {
            TVCasual.setVisibility(View.GONE);
        }

        if (Tops.getNumberTopsChic() > 0) {
            TVChic.setVisibility(View.VISIBLE);
        } else {
            TVChic.setVisibility(View.GONE);
        }

        if (Tops.getNumberTopsSport() > 0) {
            TVSport.setVisibility(View.VISIBLE);
        } else {
            TVSport.setVisibility(View.GONE);
        }
    }

}
