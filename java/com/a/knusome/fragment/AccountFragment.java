package com.a.knusome.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.a.knusome.R;
import com.a.knusome.model.UserModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class AccountFragment extends Fragment {
    static ImageView imageView;
    TextView textView_name;
    TextView textView_age;
    String uName;
    String uAge;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_account,container,false);

        imageView = (ImageView) view.findViewById(R.id.account_image);
        textView_name = (TextView) view.findViewById(R.id.account_name);
        textView_age = (TextView) view.findViewById(R.id.account_age);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    if(userModel.uid.equals(uid)){
                        uName= userModel.userName;
                        uAge = userModel.userAge;
                        textView_name.setText(uName);
                        textView_age.setText(uAge);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseStorage.getInstance().getReference().child("userImages").child(uid).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>(){
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())

                    Glide.with
                            (AccountFragment.imageView.getContext())
                            .load(task.getResult())
                            .apply(new RequestOptions().circleCrop())
                            .into(AccountFragment.imageView);

            }
        });


        return view;
    }
}
