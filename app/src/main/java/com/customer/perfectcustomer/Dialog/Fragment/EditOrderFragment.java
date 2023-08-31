package com.customer.perfectcustomer.Dialog.Fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.customer.perfectcustomer.R;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EditOrderFragment extends DialogFragment {
    Valueselect valueselect;
    NumberPicker numberPicker;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditOrderFragment newInstance(String param1, String param2) {
        EditOrderFragment fragment = new EditOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditOrderFragment() {
        // Required empty public constructor
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
        View view= inflater.inflate(R.layout.fragment_edit_order, container, false);

        intializeWidget(view);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                if (i1>0)
                {
                    valueselect.selectData(i1);

                }
            }
        });
        return view;
    }

    void intializeWidget(View view)
    {

        numberPicker=view.findViewById(R.id.number_order);

    }



    public interface Valueselect extends Serializable {

         void selectData(int val);
    }
}