package com.example.searchsvc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private  Spinner specificationSpinner;
    private Spinner areaSpinner;
    private Spinner serviceSpinner;
    private ArrayAdapter<String> areaAdapter;
    private ArrayAdapter<String>  serviceAdapter;
    private ArrayAdapter<String> specificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView areaText=(TextView) findViewById(R.id.areaTextView);
        TextView serviceText=(TextView) findViewById(R.id.serviceTextView);
        TextView specificationText=(TextView) findViewById(R.id.specificationTextView);

        areaSpinner=(Spinner) findViewById(R.id.areaSpinner);
        serviceSpinner=(Spinner) findViewById(R.id.serviceSpinner);
        specificationSpinner=(Spinner) findViewById(R.id.specificationSpinner);

        Button searchBtn=(Button) findViewById(R.id.button) ;

        List<String> areaList=DBAccess.getAreas(this);
        List<String> serviceList=DBAccess.getServices(this);
       /* for(int i=0;i<areaList.size();i++)
        {
            Log.i("qry",areaList.get(i));
        }*/
        areaList.add(0,"SELECT AREA");
        serviceList.add(0,"SELECT SERVICE");

        areaAdapter=new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, areaList);

        areaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        serviceAdapter=new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, serviceList)   ;
        serviceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);

        areaSpinner.setSelected(false);
        serviceSpinner.setSelected(false);
        specificationSpinner.setSelected(false);

        serviceSpinner.setOnItemSelectedListener(this);
        searchBtn.setOnClickListener(this);

        //serviceSpinner.setOnItemClickListener(this);
        //getResults(Context ctx,String area,String serviceType,String specification);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        specificationSpinner.setEnabled(true);
        String serviceType=adapterView.getSelectedItem().toString(); //gets the service type selected
        List<String> specificationList=DBAccess.getSpecifications(this,serviceType);
        for(int k=0;k<specificationList.size();k++)
        {
            Log.i("list",specificationList.get(i));
        }
        specificationList.add(0,"SELECT TYPE");
        specificationAdapter=new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, specificationList)   ;
        specificationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        specificationSpinner.setAdapter(specificationAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        specificationSpinner.setEnabled(false);
        Toast.makeText(getApplicationContext(),"Select a service first",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view)
    {
        long id=view.getId();
        if(id==R.id.button)
        {
            List<String> searchResults=null;
            String area=areaSpinner.getSelectedItem().toString();
            String service=serviceSpinner.getSelectedItem().toString();
            String specification=specificationSpinner.getSelectedItem().toString();
            if(area.equals("SELECT AREA"))
            {
                Toast.makeText(getApplicationContext(),"An area must be selected",Toast.LENGTH_SHORT).show();
                return;
            }
            if(service.equals("SELECT SERVICE"))
            {
                Toast.makeText(getApplicationContext(),"A service must be selected",Toast.LENGTH_SHORT).show();
                return;
            }
            if(specification.equals("SELECT TYPE"))
            {
                Toast.makeText(getApplicationContext(),"A specification must be selected",Toast.LENGTH_SHORT).show();
                return;
            }

            // For the time being
            if (!(service.equals("RESTAURANTS") && (area.equals("SALT LAKE") ||area.equals("RAJARHAT") ))) {                Toast.makeText(getApplicationContext(),
                        "Search only allowed for Restaurants in Salt Lake and Rajarhat",
                        Toast.LENGTH_SHORT).show();
                return;
            }

                searchResults=DBAccess.getResults(this,area,service,specification);
                Intent myIntent=new Intent( MainActivity.this, SearchResultActivity.class);
                myIntent.putStringArrayListExtra("RESULTS", (ArrayList<String>) searchResults);
                startActivity(myIntent);
                return;


        }

    }
}