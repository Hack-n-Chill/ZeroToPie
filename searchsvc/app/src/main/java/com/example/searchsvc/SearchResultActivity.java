package com.example.searchsvc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements  View.OnClickListener
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        TextView txtView =(TextView) findViewById(R.id.textmsg);
        List<String> res=getIntent().getStringArrayListExtra("RESULTS");
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<res.size();i++)
        {
            sb.append(res.get(i)+"\n");
        }
        Button backBtn=(Button) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(this);
        txtView.setText(sb.toString());
    }

    @Override
    public void onClick(View view)
    {
        long id=view.getId();
        if(id==R.id.backBtn)
        {
            SearchResultActivity.this.finish();
        }
    }
}
