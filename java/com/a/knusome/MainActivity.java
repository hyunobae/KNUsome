package com.a.knusome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.a.knusome.fragment.AccountFragment;
import com.a.knusome.fragment.ChatFragement;
import com.a.knusome.fragment.PeopleFragment;


public class MainActivity extends AppCompatActivity {

    Button peoplebutton;
    Button chatbutton;
    Button accountbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrame,new PeopleFragment()).commit();

        peoplebutton = (Button) findViewById(R.id.peoplebutton);
        chatbutton = (Button) findViewById(R.id.chatbutton);
        accountbutton = (Button) findViewById(R.id.accountbutton);
        peoplebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrame,new PeopleFragment()).commit();

            }
        });
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrame,new ChatFragement()).commit();

            }
        });
        accountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrame,new AccountFragment()).commit();

            }
        });

    }


}
