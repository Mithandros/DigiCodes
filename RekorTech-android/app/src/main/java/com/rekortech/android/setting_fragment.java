package com.rekortech.android;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link setting_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link setting_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class setting_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public Class fragmentClass;
    public Fragment fragment;
    static  Switch restrict;
    public static Switch distance;


    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public setting_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment setting_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static setting_fragment newInstance(String param1, String param2) {
        setting_fragment fragment = new setting_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View setting_view = inflater.inflate(R.layout.fragment_setting_fragment, container, false);
        ImageButton triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.GONE);
        restrict = (Switch)setting_view.findViewById(R.id.restrict);
        distance = (Switch)setting_view.findViewById(R.id.distance);
        try{
            restrict.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat));
        }catch (Exception re){}
        try{
            distance.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat));
        }catch (Exception re){}

        if(PreferenceUtils.getisAllowToLab(getContext())!=null)
            if("1".equals(PreferenceUtils.getisAllowToLab(getContext())))
                restrict.setChecked(true);
            else
                restrict.setChecked(false);
      /*  if(PreferenceUtils.getDistanceIn(getContext()).equals("K")){
            distance.setText("Directory search in Kilometer");
        }
        else{
            distance.setText("Directory search in Miles");
        }*/
        distance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    distance.setText("Directory search in Kilometer");
                }
                else{
                    distance.setText("Directory search in Miles");
                }
//                commonClass.apiIntegration(getContext(),"user/setdistancerange", "", PreferenceUtils.getToken(getContext()), "distance");
            }
        });

        restrict.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    PreferenceUtils.SaveisAllowToLab("1",getActivity());
                }
                else
                {
                    PreferenceUtils.SaveisAllowToLab("0",getActivity());
                }
//                commonClass.apiIntegration(getContext(),"user/allowtolab", "", PreferenceUtils.getToken(getContext()), "restrictSharing");
            }
        });
        return setting_view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static void changeText(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(distance!=null){
                    if(PreferenceUtils.getDistanceIn(activity).equals("K")){
                        distance.setText("Directory search in Kilometer");
                        APIServices.toastMessage("Directory search in Kilometer",activity);
                    }
                    else{
                        distance.setText("Directory search in Miles");
                        APIServices.toastMessage("Directory search in Miles",activity);

                    }
                }
            }
        });
    }
}
