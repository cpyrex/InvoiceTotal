package com.chazknox.invoicetotal;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.content.SharedPreferences.Editor;

import java.text.NumberFormat;

public class MainActivity extends ActionBarActivity
implements  OnEditorActionListener, OnClickListener{

    private EditText subtotalEditText;
    private TextView percentTextView;
    private TextView discountTotalTextView;
    private TextView totalTextView;
    private Button submitButton;

    private String subtotalString = "";

    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to the widgets
        subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        discountTotalTextView = (TextView) findViewById(R.id.discountTotalTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        submitButton = (Button) findViewById(R.id.submitButton);

        // set the listener
        subtotalEditText.setOnEditorActionListener(this);
        submitButton.setOnClickListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED)  {

        }
        return false;
    }

    private void calculateAndDisplay() {

        subtotalString = subtotalEditText.getText().toString();
        float subtotal;
        if(subtotalString.equals("")) {
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString);
        }

        // get discount percent
        float discountPercent = 0;
        if(subtotal >= 200) {
            discountPercent = .2f;
        } else if(subtotal >= 100) {
            discountPercent = .1f;
        } else {
            discountPercent = 0;
        }

        //calculate discount
        float discountAmount = subtotal * discountPercent;
        float total = subtotal - discountAmount;

        //send totals to widgets
        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(discountPercent));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        discountTotalTextView.setText(currency.format(discountAmount));

        NumberFormat price = NumberFormat.getCurrencyInstance();
        totalTextView.setText(price.format(total));


    }

    @Override
    public void onClick(View v) {


        switch(v.getId()) {
            case R.id.submitButton:
                calculateAndDisplay();
                break;
        }

    }

    //onPause and onResume used to save values. Editor stores values from the subtotal editView
    @Override
    protected void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        subtotalString = savedValues.getString("subtotalString", "");
        subtotalEditText.setText(subtotalString);
        calculateAndDisplay();
    }
}
