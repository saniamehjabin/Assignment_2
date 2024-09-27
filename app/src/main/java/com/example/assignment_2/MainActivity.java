package com.example.assignment_2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CheckBox applePie, pudding, pastry, cake;
    ArrayList<String> arr = new ArrayList<>();

    private RadioGroup radioGroup, paymentGroup;  // Added paymentGroup for payment method
    private RadioButton radioButton, paymentButton; // Added paymentButton to track selected payment method

    private TextView quantityTextView, priceTextView, dessert, ratingText, tipText;
    private Button increment, decrement, placeOrder;
    private int quantity = 0;
    private int price = 0;

    private AlertDialog.Builder builder;
    private RatingBar ratingBar;


    private Switch takeawaySwitch;
    private SeekBar tipSeekBar;
    private boolean isTakeaway = false;
    private int tipPercentage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // For CheckBox
        applePie = findViewById(R.id.applePie);
        pudding = findViewById(R.id.pudding);
        pastry = findViewById(R.id.pastry);
        cake = findViewById(R.id.cake);
        dessert = findViewById(R.id.dessert);

        // For RadioGroup
        radioGroup = findViewById(R.id.radioGroup);

        // Payment method group
        paymentGroup = findViewById(R.id.paymentGroup);

        // For Increment Decrement
        quantityTextView = findViewById(R.id.quantityTextView);
        priceTextView = findViewById(R.id.priceTextView);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);

        placeOrder = findViewById(R.id.order_btn);
        builder = new AlertDialog.Builder(this);

        // For RatingBar
        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.rating);

        // Initialize Switch and SeekBar
        takeawaySwitch = findViewById(R.id.takeaway_switch);
        tipSeekBar = findViewById(R.id.tipSeekBar);
        tipText = findViewById(R.id.tipText);

        // Handle Switch (Takeaway/Dine-in)
        takeawaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                takeawaySwitch.setText("Takeaway");
                isTakeaway = true;
            } else {
                takeawaySwitch.setText("Dine-in");
                isTakeaway = false;
            }
        });

        // Handle SeekBar (Tip Selection)
        tipSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipPercentage = progress;
                tipText.setText("Tip: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // CheckBox Listeners
        applePie.setOnCheckedChangeListener((buttonView, isChecked) -> check(buttonView, isChecked));
        pudding.setOnCheckedChangeListener((buttonView, isChecked) -> check(buttonView, isChecked));
        pastry.setOnCheckedChangeListener((buttonView, isChecked) -> check(buttonView, isChecked));
        cake.setOnCheckedChangeListener((buttonView, isChecked) -> check(buttonView, isChecked));

        // Increment and Decrement Logic
        increment.setOnClickListener(v -> {
            quantity++;
            price = quantity * 400;
            quantityTextView.setText("" + quantity);
            priceTextView.setText("BDT " + price);
        });

        decrement.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                price = quantity * 400;
                quantityTextView.setText("" + quantity);
                priceTextView.setText("BDT " + price);
            } else {
                Toast.makeText(getApplicationContext(), "Quantity can't be negative", Toast.LENGTH_SHORT).show();
            }
        });

        // RadioGroup and Payment method selection listeners
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> radioButton = findViewById(checkedId));
        paymentGroup.setOnCheckedChangeListener((group, checkedId) -> paymentButton = findViewById(checkedId));

        // RatingBar change listener
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingText.setText("Rating: " + rating));

        // Place Order Button Logic
        placeOrder.setOnClickListener(v -> {
            try {
                if (arr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select a dessert!!", Toast.LENGTH_SHORT).show();
                } else if (quantity == 0) {
                    Toast.makeText(getApplicationContext(), "Please add quantity!!", Toast.LENGTH_SHORT).show();
                } else if (paymentButton == null) {
                    Toast.makeText(getApplicationContext(), "Please select a payment method!", Toast.LENGTH_SHORT).show();
                } else {
                    String radioValue = radioButton.getText().toString();
                    String paymentValue = paymentButton.getText().toString();
                    String takeawayOption = isTakeaway ? "Takeaway" : "Dine-in";
                    int totalWithTip = price + (price * tipPercentage / 100);

                    builder.setTitle("Order Placed!!")
                            .setMessage("Order Summary:\n" +
                                    "Desserts: " + arr + "\n" +
                                    "Dessert Size: " + radioValue + "\n" +
                                    "Quantity: " + quantity + "\n" +
                                    "Total Price: BDT " + price + "\n" +
                                    "Payment Method: " + paymentValue + "\n" +
                                    "Option: " + takeawayOption + "\n" +
                                    "Tip: " + tipPercentage + "%\n" +
                                    "Total with Tip: BDT " + totalWithTip + "\n" +
                                    "Rating: " + ratingBar.getRating() + "\nThank you!!")
                            .setCancelable(false)
                            .setPositiveButton("Okay", (dialog, which) -> {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Order Placed!!", Toast.LENGTH_SHORT).show();
                                resetForm();
                            }).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please Select dessert Size!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void check(CompoundButton buttonView, Boolean isChecked) {
        if (isChecked) {
            arr.add(buttonView.getText().toString());
            Log.d("array", String.valueOf(arr));
        } else {
            arr.remove(buttonView.getText().toString());
        }
        dessert.setText(String.valueOf(arr));
    }

    private void resetForm() {
        quantity = 0;
        price = 0;
        quantityTextView.setText("0");
        priceTextView.setText("BDT 0");
        dessert.setText("");
        applePie.setChecked(false);
        pudding.setChecked(false);
        pastry.setChecked(false);
        cake.setChecked(false);
        radioGroup.clearCheck();
        paymentGroup.clearCheck(); // Reset payment method selection
        ratingBar.setRating(0);
        takeawaySwitch.setChecked(false);  // Reset takeaway switch
        tipSeekBar.setProgress(0);  // Reset tip percentage
        tipText.setText("Tip: 0%");
    }
}
