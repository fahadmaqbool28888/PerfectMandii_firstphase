package com.vendor.perfectmandii.ProductFragment.AddProductFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vendor.perfectmandii.Model.StoreData.StoreModel;
import com.vendor.perfectmandii.ProfileFragment.ContactFragment;
import com.vendor.perfectmandii.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralInformationFragment extends Fragment {

    TextView textView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GeneralInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneralInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralInformationFragment newInstance(String param1, String param2) {
        GeneralInformationFragment fragment = new GeneralInformationFragment();
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
        View view= inflater.inflate(R.layout.fragment_general_information, container, false);
       intialize_Widget(view);

       textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               click_next();
           }
       });
        return  view;
    }
    void intialize_Widget(View view)
    {
        textView=view.findViewById(R.id.next_button_1);
    }

    void click_next()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SelectCategoryFragment hallsForState = new SelectCategoryFragment();
/*        Bundle args = new Bundle();
        args.putString("storename", store_name.getText().toString());
        args.putString("sa", store_abbr.getText().toString());
        args.putString("storedesc", store_desc.getText().toString());
        args.putString("ShippingAddress", shipping_address.getText().toString());
        args.putString("WarehouseAddress", warehouse_address.getText().toString());
        hallsForState.setArguments(args);*/
        transaction.replace(((ViewGroup) getView().getParent()).getId(), hallsForState);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}