package com.example.arbitragetracker.welcomePage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arbitragetracker.R;

/**
 * This class represents the WelcomeFragment which displays the opening content
 * @author Tej Baidwan
 */
public class Welcome extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Welcome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Welcome.
     */
    public static Welcome newInstance(String param1, String param2) {
        Welcome fragment = new Welcome();
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
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        //Find Buttons and set intents when clicked
        Button webLink = view.findViewById(R.id.webLinkOne);
        webLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.junglescout.com/blog/amazon-retail-arbitrage/"));
                startActivity(i);
            }
        });

        //Getting the phone number entered, verifying it, and then sending an sms with the book link to that phone number
        //This uses an sms ACTION_SENDTO intent
        Button textLink = view.findViewById(R.id.textIntentButton);
        EditText phoneNumber = view.findViewById(R.id.telephone);

        textLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookLink = "https://books.google.ca/books/about/Retail_Arbitrage.html?id=8DXRygAACAAJ&redir_esc=y";
                String textNumber = phoneNumber.getText().toString();

                if(textNumber.length() == 10 && textNumber!= null) {
                    Intent i = new Intent(Intent.ACTION_SENDTO,
                            Uri.parse("smsto:" + textNumber));
                    i.putExtra("sms_body", "Check out the book: " + bookLink);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "Invalid phone number. No area code or dashes allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}