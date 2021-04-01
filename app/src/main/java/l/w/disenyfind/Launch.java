package l.w.disenyfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Launch extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
        //LOCATION_REQUEST_CODE);
        Button letsGo = findViewById(R.id.LetsGo);

        letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Launch.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}