package crazy.developer.landlord_sgp_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import crazy.developer.landlord_sgp_project.R;


public class DashboardActivity extends AppCompatActivity {

    TextView text_view_holder2,text_view_holder4,text_view_holder22,text_view_holder33;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

      text_view_holder2=findViewById(R.id.text_view_holder2);
      text_view_holder4=findViewById(R.id.text_view_holder4);
      text_view_holder22=findViewById(R.id.text_view_holder22);
      text_view_holder33=findViewById(R.id.text_view_holder33);

        text_view_holder2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(DashboardActivity.this,PropertyActivity.class);
              intent.putExtra("target","2");
              startActivity(intent);
          }
        });


        text_view_holder4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,ViewPropertyActivity.class);
                intent.putExtra("target","1");
                startActivity(intent);
            }
        });

        text_view_holder22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,ImagesActivity.class);
                intent.putExtra("target","1");
                startActivity(intent);
            }
        });

        text_view_holder33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,AdvertActivity.class);
                intent.putExtra("target","2");
                startActivity(intent);
            }
        });



    }
}
