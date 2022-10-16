package com.maksof.linkapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;



public class directory_home extends Fragment implements  fragmentLifeCycle{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static ListView dirList;
    static DirectoryAdapter directoryAdapter;
    static ArrayList<JSONObject> directoryList;
    static ExpandedHeightGridView dirBsnsImageGridView;
    ImageButton triggerSearchBar;
    public SearchView occupation;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String occupationText = "";
    public Spinner areaSpinner;
    String hit = "";
    private OnFragmentInteractionListener mListener;

    public directory_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment directory_home.
     */
    // TODO: Rename and change types and number of parameters
    public static directory_home newInstance(String param1, String param2) {
        directory_home fragment = new directory_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceUtils.save(null,"directorySearch",getContext());
    }

    @Override
    public void onResume() {
        PreferenceUtils.save("resume","directorySearch",getContext());
        if(triggerSearchBar!=null)
        triggerSearchBar.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View directoryView = inflater.inflate(R.layout.directory_fragment, container, false);
        final SwipeRefreshLayout pullToRefresh = directoryView.findViewById(R.id.phoneHome2);
        occupation = (SearchView)directoryView.findViewById(R.id.dirSearch);
        dirList = (ListView) directoryView.findViewById(R.id.dirList);
        dirList.setHorizontalScrollBarEnabled(false);
        dirList.setVerticalScrollBarEnabled(false);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("occupation",occupation.getQuery());
                    String dis[] = areaSpinner.getSelectedItem().toString().split(" ");
                    int distance = 0;
                    if (!dis[0].equals("All")&&(areaSpinner.getSelectedItemPosition()!=0))
                        distance = Integer.parseInt(dis[0])*Constants.distance;
                    else if(dis[0].equals("All"))
                        distance = 100*Constants.distance;
                    else distance = 10*Constants.distance;
                    jsonObject.put("distance", distance);
//                    commonClass.apiIntegration(getContext(), "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(getContext()), "directorySearch2");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pullToRefresh.setRefreshing(false);
            }
        });
        dirList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (dirList.getChildAt(0) != null) {
                    pullToRefresh.setEnabled(dirList.getFirstVisiblePosition() == 0 && dirList.getChildAt(0).getTop() == 0);
                    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("occupation",occupation.getQuery());
                                String dis[] = areaSpinner.getSelectedItem().toString().split(" ");
                                int distance = 0;
                                if (!dis[0].equals("All")&&(areaSpinner.getSelectedItemPosition()!=0))
                                    distance = Integer.parseInt(dis[0])*Constants.distance;
                                else if(dis[0].equals("All"))
                                    distance = 100*Constants.distance;
                                else distance = 10*Constants.distance;
                                jsonObject.put("distance", distance);
//                                commonClass.apiIntegration(getContext(), "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(getContext()), "directorySearch2");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pullToRefresh.setRefreshing(false);
                        }
                    });

                }
            }
        });
        triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        areaSpinner = (Spinner) directoryView.findViewById(R.id.areaSpinner_1);
        areaSpinner.setSelection(0);
        int searchPlateId = occupation.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = occupation.findViewById(searchPlateId);
        if (searchPlate!=null) {
            searchPlate.setBackgroundColor (Color.TRANSPARENT);
            int searchTextId = searchPlate.getContext ().getResources ().getIdentifier ("android:id/search_src_text", null, null);
        }
        occupation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Button createDirectoryProfile = (Button) directoryView.findViewById(R.id.createDirectoryProfile);
        occupation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                occupationText = newText;
                if(!occupationText.equals(""))
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("occupation",occupationText);
                    if(areaSpinner.getSelectedItemPosition()==0) {
                        areaSpinner.setSelection(1);
                    }
                        String dis[] = areaSpinner.getSelectedItem().toString().split(" ");
                        int distance = 0;
                        if (!dis[0].equals("All"))
                            distance = Integer.parseInt(dis[0])*Constants.distance;
                        else distance = 100*Constants.distance;
                        jsonObject.put("distance", distance);
//                        commonClass.apiIntegration(getContext(), "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(getContext()), "directorySearch");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        createDirectoryProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createDirectory = new Intent(getContext(),create_directory.class);
                getContext().startActivity(createDirectory);

            }
        });

        final ArrayAdapter<String> areaAdapter;
        String[] dis;
        if(PreferenceUtils.getDistanceIn(getContext()).equals("M"))
        {
            dis = getResources().getStringArray(R.array.distanceM);
            Constants.distance = 1600;
        }
        else {
            Constants.distance = 1000;
            dis = getResources().getStringArray(R.array.distance);
        }
        areaAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_top,dis){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return true;
                }
                else
                { return true;
                }
            }
        };
        areaAdapter.setDropDownViewResource( R.layout.spinner_view);
        areaSpinner.setAdapter(areaAdapter);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupation.clearFocus();
                if(position!=0){
                    if(!hit.equals(areaSpinner.getSelectedItem().toString())){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("occupation", occupationText);
                    if (areaSpinner.getSelectedItemPosition() != 0) {
                        hit = areaSpinner.getSelectedItem().toString();
                        String dis[] = areaSpinner.getSelectedItem().toString().split(" ");
                        int distance = 0;
                        if (!dis[0].equals("All"))
                            distance = Integer.parseInt(dis[0])*Constants.distance;
                        else distance = 100*Constants.distance;
                        jsonObject.put("distance", distance);
//                        commonClass.apiIntegration(getContext(), "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(getContext()), "directorySearch");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> color = new ArrayList<>();
        if(PreferenceUtils.getGroupNamesJSON("directorySecondList",getActivity())!=null){
        ArrayList<JSONObject> list = PreferenceUtils.getGroupNamesJSON("directorySecondList",getActivity());
        for(int i =0 ; i<list.size();i++){
            color.add(commonClass.randomColors());
        }
        directoryAdapter = new DirectoryAdapter(directoryList,color,getActivity(),getContext());
        directoryAdapter.swapItems(list);
        if(dirList!=null){

                    dirList.setAdapter(directoryAdapter);
                    directoryAdapter.notifyDataSetChanged();
        }}
        return directoryView;
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

    @Override
    public void onPauseFragment() {
        Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    static int inc;
    public static void images(final Activity activity){
        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist",activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap resized;
                if(dirBsnsImageGridView!=null)
                {
                    //remove images if previously uploaded
                    for( inc = 0 ;inc<imgs.size();inc++){
                        ArrayList<String> imgurl = new ArrayList<>();
                            for( inc = 0 ;inc<imgs.size();inc++) {
                                imgurl.add(com.maksof.linkapp.commonClass.urlBitmap(activity, inc));
                            }
                        dirBsnsImageGridView.setExpanded(true);
                        lab_home_fragment.ImageAdapter imageAdapter = new lab_home_fragment.ImageAdapter(imgurl,activity);
                        dirBsnsImageGridView.setAdapter(imageAdapter);

                    }
                }
            }
        });

    }
    static class DirectoryAdapter extends BaseAdapter {

        ArrayList<JSONObject> directoryList;
        ArrayList<String> colorList;
        Activity activity;
        Context cxt;

        DirectoryAdapter(ArrayList<JSONObject> directoryList,ArrayList<String> colorList,Activity activity,Context cxt){
            this.directoryList = directoryList;
            this.colorList = colorList;
            this.activity = activity;
            this.cxt = cxt;
        }

        @Override
        public int getCount() {
            if (this.directoryList!=null)
                return this.directoryList.size();
            return 0;
        }



        @Override
        public JSONObject getItem(int position) {
            return this.directoryList.get(position);
        }


        public String getColor(int position) {
            return this.colorList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = activity.getLayoutInflater().inflate(R.layout.directory_list, null);

            final ImageView dirFLetter = (ImageView) convertView.findViewById(R.id.dirFLetter);
            final TextView dirName = (TextView) convertView.findViewById(R.id.dirName);
            final TextView dirDistance = (TextView) convertView.findViewById(R.id.dirDistance);
            final TextView dirOccupation = (TextView) convertView.findViewById(R.id.dirOccupation);
            final TextView dirDesc = (TextView) convertView.findViewById(R.id.dirDesc);
            RelativeLayout dirListCon = convertView.findViewById(R.id.directoryListCon);
            final JSONObject dirProf = (JSONObject) getItem(position);

            try {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
                Drawable d = activity.getDrawable(R.drawable.ic_profile);
                String s =  dirProf.getString("profileImage");
                dirFLetter.setImageDrawable(activity.getDrawable(R.drawable.ic_profile));
                try{
                    if(s!=null&&(!s.equals("")))
                        Picasso.with(activity).load("http://lynk-app.com/api/cdn/images/"+s).placeholder(circularProgressDrawable).into(dirFLetter);
                } catch (IllegalArgumentException ie){
                    dirFLetter.setImageDrawable(activity.getDrawable(R.drawable.ic_profile));
                }

                dirName.setText(dirProf.getString("contactName"));
                Float dis = Float.parseFloat(dirProf.getString("distance"));
                if(dis/Constants.distance!=0)
                {
                    if(dis/Constants.distance<0.1)
                        dirDistance.setText("0.1");
                    else
                        dirDistance.setText(dis/Constants.distance+"");
                }
                else  dirDistance.setText("0.0");
                dirOccupation.setText(dirProf.getString("occupation"));
                String description = "";
                try{
                    description = dirProf.getString("description");
                }
                catch (JSONException je){

                }
                if(description.equals("") || description.length() == 0) dirDesc.setVisibility(View.GONE);
                else dirDesc.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            dirListCon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("contactId",dirProf.getString("contactId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    commonClass.apiIntegration(activity, "profile/getdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(activity),"geDirectoryProfile");

                }
            });
            return convertView;
        }
        public  void swapItems(ArrayList<JSONObject> items) {
            directoryList = items;
        }
    }


    public static  void onResume(Activity act,Context context) {
        if(PreferenceUtils.getGroupNamesJSON("directorySecondList",act)!=null){
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(act.getCurrentFocus()!=null)
                imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
            ArrayList<String> color = new ArrayList<>();
            ArrayList<JSONObject> list = PreferenceUtils.getGroupNamesJSON("directorySecondList",act);
            for(int i =0 ; i<list.size();i++){
                color.add(commonClass.randomColors());
            }
            directoryAdapter = new DirectoryAdapter(directoryList,color,act,context);
                directoryAdapter.swapItems(list);
                if(dirList!=null){
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dirList.setAdapter(directoryAdapter);
                            directoryAdapter.notifyDataSetChanged();
                        }
                    });
                }
        }
    }

}
