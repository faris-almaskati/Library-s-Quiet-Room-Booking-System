package com.example.librarysquietroombookingsystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity2 extends AppCompatActivity {

    EditText inputText;
    EditText inputText2;
    EditText inputText3;
    Button submitButton;
    private TableLayout tableLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        inputText = findViewById(R.id.inputText);
        inputText2 = findViewById(R.id.inputText2);
        inputText3 = findViewById(R.id.inputText3);
        submitButton = findViewById(R.id.submitButton);
        tableLayout = findViewById(R.id.tableLayout);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateValue = inputText.getText().toString();
                int pplValue = Integer.parseInt(inputText2.getText().toString());
                String timeValue = inputText3.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                try {
                    Date date = dateFormat.parse(dateValue);
                    Date time = timeFormat.parse(timeValue);

                    updateText(date, time, pplValue);
                    assert date != null;
                    assert time != null;
                    addTableRow(date, time, pplValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Properties properties = new Properties();
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.port", "587");
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");

                    Session session = Session.getInstance(properties);

                    MimeMessage message = new MimeMessage(session);

                    message.setFrom(new InternetAddress("faris.almaskati@gmail.com"));

                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("f.almaskati.2414@bayanschool.edu.bh"));

                    message.setSubject("A User Has Booked!");

                    message.setText("Dear Client,\n\n" +
                            "A user has booked into the application! Please look into the booking details and schedule to see if you would like to make further rearrangements.\n" +
                            "This is an automated message.");

                    Transport.send(message);

                    System.out.println("Email sent successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateText(Date date, Date time, int ppl) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(time);

        DatabaseReference parentRef = FirebaseDatabase.getInstance().getReference().child("iacomputerscience");

        String newChildKey = parentRef.push().getKey();

        assert newChildKey != null;
        DatabaseReference newChildRef = parentRef.child(newChildKey);

        newChildRef.child("date").setValue(formattedDate);
        newChildRef.child("time").setValue(formattedTime);
        newChildRef.child("number_of_people").setValue(ppl);
    }

    public void addTableRow(Date date, Date time, int ppl) {
        TableRow row = new TableRow(MainActivity2.this);

        TextView timeTextView = new TextView(MainActivity2.this);
        timeTextView.setText(time.toString());
        timeTextView.setTextSize(18);
        timeTextView.setPadding(8, 8, 8, 8);
        row.addView(timeTextView);

        TextView peopleTextView = new TextView(MainActivity2.this);
        peopleTextView.setText(String.valueOf(ppl));
        peopleTextView.setTextSize(18);
        peopleTextView.setPadding(8, 8, 8, 8);
        row.addView(peopleTextView);

        TextView dateTextView = new TextView(MainActivity2.this);
        dateTextView.setText(date.toString());
        dateTextView.setTextSize(18);
        dateTextView.setPadding(8, 8, 8, 8);
        row.addView(dateTextView);

        tableLayout.addView(row);
    }
}
