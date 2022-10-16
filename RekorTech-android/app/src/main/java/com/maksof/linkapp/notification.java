package com.maksof.linkapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link notification.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notification extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static Context cxt;
    public Class fragmentClass;
    public Fragment fragment;
    ArrayList<contactModel> contactModal = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static ListView notificationList=null;
    static NotificationAdapter notificationAdapter;
    public notification() {
    }
    // TODO: Rename and change types and number of parameters
    public static notification newInstance(String param1, String param2) {
        notification fragment = new notification();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.cxt = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View notificationView = inflater.inflate(R.layout.fragment_notification, container, false);
        PreferenceUtils.save(null,"openNotification",getContext());
        Switch switch1 = (Switch)notificationView.findViewById(R.id.switch1);
        ImageButton triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.GONE);
        TextView noNotifications = (TextView)notificationView.findViewById(R.id.noNotification);
        //switch1.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat));
        if(PreferenceUtils.getNotificationStatus(getContext())!=null) {
            if(PreferenceUtils.getNotificationStatus(getContext()).equals("true"))
                switch1.setChecked(true);
        }
        else switch1.setChecked(false);
        PreferenceUtils.saveNotificationStatus(switch1.isChecked()+"",getActivity());
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtils.saveNotificationStatus(switch1.isChecked()+"",getActivity());
                if(isChecked){
                    //hit notification event listner
                    NotificationEventReceiver.setupAlarm(getContext());
                    APIServices.toastMessage("Notification service enabled",getActivity());
                }
                else{
                    NotificationEventReceiver.stopAlarm(getActivity());
                    APIServices.toastMessage("Notification service disabled",getActivity());
                }
            }
        });
        notificationList = (ListView) notificationView.findViewById(R.id.notificationList);
            ArrayList<JSONObject> notifications = PreferenceUtils.getGroupNamesJSON("notifications",getActivity());
                ArrayList<String> colors = new ArrayList<>();
                if(notifications!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(notifications.size()==0){
                                notificationList.setVisibility(View.GONE);
                                noNotifications.setVisibility(View.VISIBLE);
                            }else {
                                notificationList.setVisibility(View.VISIBLE);
                                noNotifications.setVisibility(View.GONE);
                            }
                        }
                    });

                    for(int i=0;i<notifications.size();i++){
                        colors.add(commonClass.randomColors());
                    }
                    for(int i=0;i<notifications.size();i++){
                        contactModal.add(new contactModel(notifications.get(i),false));
                    }
                    notificationAdapter = new NotificationAdapter(getActivity(),contactModal,colors);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificationList.setAdapter(notificationAdapter);
                        }
                    });

                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificationList.setVisibility(View.GONE);
                            noNotifications.setVisibility(View.VISIBLE);
                        }
                    });

                }

       return notificationView;
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
}
