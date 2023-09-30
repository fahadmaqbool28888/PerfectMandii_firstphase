package com.vendor.perfectmandii.ProfileFragment;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Model.StoreData.StoreModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment
{
    List<StoreModel> store_list;
    TextView next_button_address;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_address, container, false);
       intializeWidget(view);
        @SuppressLint("WrongConstant") SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        String s1 = sh.getString("storecphone", "");
       // int a = sh.getInt("age", 0);

        Toast.makeText(getContext(), s1, Toast.LENGTH_SHORT).show();

       next_button_address.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               FragmentManager fragmentManager = getFragmentManager();
               FragmentTransaction transaction = fragmentManager.beginTransaction();
               AttachDFragment hallsForState = new AttachDFragment();
               transaction.replace(((ViewGroup)getView().getParent()).getId(), hallsForState);
               transaction.addToBackStack(null);
               transaction.commit();
               ProfileInformation profileInformation=(ProfileInformation) getActivity();
               profileInformation.Recieve_Data("Done_3",store_list);
           }
       });


       return view;
    }
    void intializeWidget(View view)
    {
        next_button_address=view.findViewById(R.id.next_button_address);
    }
}