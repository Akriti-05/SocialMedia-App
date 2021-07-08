package com.example.socialmediaapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class fragment1 extends Fragment {



    public fragment1() {
        // Required empty public constructor
    }

    ImageView imageView;
    TextView nameEt, profEt, bioEt, emailEt, webEt;
    ImageButton ib_edit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageView= getActivity().findViewById(R.id.iv_f1);
        nameEt=getActivity().findViewById(R.id.tv_name_f1);
        profEt=getActivity().findViewById(R.id.tv_prof_f1);
        bioEt=getActivity().findViewById(R.id.tv_bio_f1);
        emailEt=getActivity().findViewById(R.id.tv_email_f1);
        webEt=getActivity().findViewById(R.id.tv_web_f1);

        ib_edit=getActivity().findViewById(R.id.ib_edit_f1);



        ib_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment1, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();

        reference=firestore.collection("user").document(currentid);

        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){

                            String nameResult =task.getResult().getString("name");
                            String bioResult= task.getResult().getString("bio");
                            String emailResult= task.getResult().getString("email");
                            String webResult= task.getResult().getString("web");
                            String profResult= task.getResult().getString("prof");
                            String url= task.getResult().getString("url");

                            Picasso.get().load(url).into(imageView);
                            nameEt.setText(nameResult);
                            bioEt.setText(bioResult);
                            emailEt.setText(emailResult);
                            webEt.setText(webResult);
                            profEt.setText(profResult);
                        }else{
                            Intent intent = new Intent(getActivity(), CreateProfile.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}