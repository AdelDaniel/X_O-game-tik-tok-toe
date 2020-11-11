package com.example.x_ogame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button singlePlayerButton ,multiPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getSupportActionBar().hide();
        singlePlayerButton = findViewById(R.id.single_player_button);
        multiPlayerButton = findViewById(R.id.multi_player_button);


        final Intent intent = new Intent(this, MainActivity.class);

        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.single_player_button){
                    intent.putExtra("Type" , "Single"  );
                    startActivity(intent);
                }else if(v.getId()== R.id.multi_player_button){

                    intent.putExtra("Type" , "Multi"  );
                    startActivity(intent);
                }
            }
        };

        singlePlayerButton.setOnClickListener(clickListener);
        multiPlayerButton.setOnClickListener(clickListener);
    }
}