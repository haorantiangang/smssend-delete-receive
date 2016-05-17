package com.example.testsms;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {
  private Button mBut;
  private EditText  mEdit,mMessage;
  private Button mDelete;
  
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmSUtils.init(this);
        mBut = (Button) this.findViewById(R.id.send_sms);
        mEdit = (EditText) this.findViewById(R.id.send_number);
        mMessage = (EditText) this.findViewById(R.id.send_message);
        mDelete = (Button)this.findViewById(R.id.delete_sms);
        mBut.setOnClickListener(new View.OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				SmSUtils.sendSMS(MainActivity.this,mEdit.getText().toString(), mMessage.getText().toString());
			}
		});
        mDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 SmSUtils.deleteSms(MainActivity.this);
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
