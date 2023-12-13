package com.example.zakatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar myToolbar;
    private Button btnCalc;
    private Button btnClear;
    private EditText goldWeightEditText;
    private RadioGroup goldTypeRadioGroup;
    private EditText goldValueEditText;
    private TextView totalValueTextView;
    private TextView zakatValueTextView;
    private TextView zakatAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(myToolbar);

        goldWeightEditText = findViewById(R.id.goldWeightEditText);
        goldTypeRadioGroup = findViewById(R.id.goldTypeRadioGroup);
        goldValueEditText = findViewById(R.id.goldValueEditText);
        totalValueTextView = findViewById(R.id.totalValueTextView);
        zakatValueTextView = findViewById(R.id.zakatValueTextView);
        zakatAmountTextView = findViewById(R.id.zakatAmountTextView);

        btnCalc = findViewById(R.id.btnCalc);
        btnClear = findViewById(R.id.btnClear);

        btnCalc.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        // Set the visibility of the output views and labels to GONE initially
        findViewById(R.id.labelTotalValue).setVisibility(View.GONE);
        findViewById(R.id.totalValueTextView).setVisibility(View.GONE);
        findViewById(R.id.labelZakatValue).setVisibility(View.GONE);
        findViewById(R.id.zakatValueTextView).setVisibility(View.GONE);
        findViewById(R.id.labelZakatAmount).setVisibility(View.GONE);
        findViewById(R.id.zakatAmountTextView).setVisibility(View.GONE);

        getSupportActionBar().setTitle("Zakat Calculator");
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnCalc) {
            calculateZakat();
        } else if (viewId == R.id.btnClear) {
            clearFields();
        }
    }

    private void calculateZakat() {
        // Check if gold weight, current gold value, and gold type are empty
        if (goldWeightEditText.getText().toString().isEmpty() &&
                goldValueEditText.getText().toString().isEmpty() &&
                goldTypeRadioGroup.getCheckedRadioButtonId() == -1) {
            // Display a message to the user to enter all values
            Toast.makeText(this, "Please enter values for gold weight, current gold value, and select gold type.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if gold weight is empty
        if (goldWeightEditText.getText().toString().isEmpty()) {
            // Display a message to the user to enter gold weight
            Toast.makeText(this, "Please enter the gold weight.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if current gold value is empty
        if (goldValueEditText.getText().toString().isEmpty()) {
            // Display a message to the user to enter current gold value
            Toast.makeText(this, "Please enter the current gold value.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if gold type is not selected
        if (goldTypeRadioGroup.getCheckedRadioButtonId() == -1) {
            // Display a message to the user to select gold type
            Toast.makeText(this, "Please select a gold type (Keep or Wear).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Continue with the calculation if all required values are entered
        double goldWeight = Double.parseDouble(goldWeightEditText.getText().toString());
        int goldTypeRadioButtonId = goldTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(goldTypeRadioButtonId);
        String goldType = selectedRadioButton.getText().toString().toLowerCase();
        double goldValue = Double.parseDouble(goldValueEditText.getText().toString());

        int exemptionThreshold = goldType.equals("keep") ? 85 : 200;

        double totalValue = goldWeight * goldValue;
        double zakatValue = Math.max(goldWeight - exemptionThreshold, 0) * goldValue;
        double zakatAmount = 0.025 * zakatValue;

        // Set the visibility of the output views and labels to VISIBLE
        findViewById(R.id.labelTotalValue).setVisibility(View.VISIBLE);
        findViewById(R.id.totalValueTextView).setVisibility(View.VISIBLE);
        findViewById(R.id.labelZakatValue).setVisibility(View.VISIBLE);
        findViewById(R.id.zakatValueTextView).setVisibility(View.VISIBLE);
        findViewById(R.id.labelZakatAmount).setVisibility(View.VISIBLE);
        findViewById(R.id.zakatAmountTextView).setVisibility(View.VISIBLE);

        totalValueTextView.setText(String.format("%.2f", totalValue));
        zakatValueTextView.setText(String.format("%.2f", zakatValue));
        zakatAmountTextView.setText(String.format("%.2f", zakatAmount));
    }

    private void clearFields() {
        goldWeightEditText.setText("");
        goldValueEditText.setText("");
        goldTypeRadioGroup.clearCheck();

        // Set the visibility of the output views and labels to GONE
        findViewById(R.id.labelTotalValue).setVisibility(View.GONE);
        findViewById(R.id.totalValueTextView).setVisibility(View.GONE);
        findViewById(R.id.labelZakatValue).setVisibility(View.GONE);
        findViewById(R.id.zakatValueTextView).setVisibility(View.GONE);
        findViewById(R.id.labelZakatAmount).setVisibility(View.GONE);
        findViewById(R.id.zakatAmountTextView).setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_share){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Visit ZakatCalc github - https://github.com/alyashada/ZakatCalc.git");
            startActivity(Intent.createChooser(shareIntent,null));

            return true;
        }else if (item.getItemId()==R.id.item_about){
            Intent aboutIntent = new Intent(this,AboutPage.class);
            startActivity(aboutIntent);
        }
        return false;
    }
}
