package com.example.skinmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    TextView greetingsText;
    RecyclerView productView;
    ImageButton profile;
    ImageButton logout;

    public HomeFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
       // ProductAdapter productAdapter = new ProductAdapter(getContext())

        greetingsText = (TextView) view.findViewById(R.id.greetingsText);
       // greetingsText.setText(Feature.getTimeGreetings() + ", " + User.currentUser.getFirstName().split(" ")[0] + "!");


//        logout = (ImageButton) view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(e -> {
            getActivity().finish();
            User.setUser(null);
        });

        return view;
    }
}
