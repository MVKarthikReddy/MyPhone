package com.example.my_helper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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

    TextView user_manual,contact_format;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help, container, false);

        user_manual = v.findViewById(R.id.user_manual);
        contact_format = v.findViewById(R.id.contact_format);



        contact_format.setText(" Myphone { 4 digit code } getcontact contact_name ");

        user_manual.setText("    Any phone with basic sms feature can be used to access your phone. Suppose your phone got lost or you forgot your mobile at your home then you can do the following -\n" +
                "\n" +
                "1. You can get contact numbers from your phone contacts by sending msg in specific format to your mobile number then your mobile will send you contact number.\n" +
                "\n2. An sms can help you change the sound profile of your phone (silent to normal).\n" +
                "\n3. We can ring our phone by sending an sms.\n"+
                "\n4. It help you to get location of your mobile.\n" +
                "\n5. It can lock your mobile if mobile doesn't have. And many more.");
        return v;
    }
}