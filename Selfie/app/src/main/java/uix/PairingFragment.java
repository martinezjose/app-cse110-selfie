package uix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import classes.WebAPI;
import cse110.selfie.app.UI.R;
import database.InsertToDatabaseException;
import database.ItemDataSource;
import database.SelfieDatabase;

/**
 * Created by JuanJ on 5/23/2014.
 * Screen to set code to match table with table.
 * If pairing code is less than 5 characters long will display and error message.
 */
public class PairingFragment extends FragmentActivity {

    private EditText linkCodeET;
    private TextView errorMessage;
    private Button linkButton;
    private String linkCode;

    @Override
    //instantiate the components and listens to link button
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pairing_screen);

        errorMessage = (TextView) findViewById(R.id.error_text);
        errorMessage.setVisibility(TextView.INVISIBLE);

        linkCodeET = (EditText) findViewById(R.id.pairingId);

        linkButton = (Button) findViewById(R.id.link_button);

        final ItemDataSource itemDataSource = new ItemDataSource(this);
        final Context context = this;

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = linkCodeET.getText().toString();
                int code = 0;

                //check if the input is integer
                try {
                    code = Integer.parseInt(input);
                } catch (Exception e) {
                    errorMessage.setVisibility(TextView.VISIBLE);
                    errorMessage.setText("Wrong Input");
                    linkCodeET.setText("");
                    return;
                }



                //check if it's less than 4
                if(input.length() < 4) {
                    errorMessage.setVisibility(TextView.VISIBLE);
                    errorMessage.setText("Too Short");
                    linkCodeET.setText("");
                }
                //check if it's greater than 5
                else if(input.length() > 5) {
                    errorMessage.setVisibility(TextView.VISIBLE);
                    errorMessage.setText("Too Long");
                    linkCodeET.setText("");
                }
                //right answer
                else {
                    final SelfieDatabase database = new SelfieDatabase(context);
                    context.deleteDatabase(database.getDatabaseName());

                    linkButton.setEnabled(false);
                   final ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setCancelable(false);
                    dialog.setMessage("Downloading database, please wait.");

                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                long tableID = WebAPI.sendPairingCode(Integer.parseInt(
                                        linkCodeET.getText().toString()));
                                if(tableID == -1)
                                    throw  new InterruptedException("Pairing code is not mapped to " +
                                            "a table, please try again.");

                                // Get a handler that can be used to post to the main thread
                                Handler mainHandler = new Handler(context.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                };
                                // This is your code
                                mainHandler.post(myRunnable);
                                itemDataSource.setUpFromWebAPI(context);
                                myRunnable = new Runnable() {
                                    @Override
                                    public void run() {

                                        dialog.hide();

                                    }
                                };
                                // This is your code
                                mainHandler.post(myRunnable);
                                errorMessage.setVisibility(TextView.INVISIBLE);
                                Order.setTableId(tableID);
                                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                startActivity(intent);
                            }
                            catch (InterruptedException e) {
                                final InterruptedException ex = e;

                                // Get a handler that can be used to post to the main thread
                                Handler mainHandler = new Handler(context.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(context)
                                                .setTitle("Invalid Pairing Code").setMessage(ex.getMessage())
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                })
                                                .show();
                                    }
                                }; // This is your code
                                mainHandler.post(myRunnable);
                            }
                            catch (InsertToDatabaseException e) {
                                Log.e("ITEMDATASOURCE", "SETUP EXCEPTION");
                            }
                            finally {
                                // Get a handler that can be used to post to the main thread
                                Handler mainHandler = new Handler(context.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        linkButton.setEnabled(true);

                                    }
                                }; // This is your code
                                mainHandler.post(myRunnable);
                            }
                        }
                    };
                    thread.start();
                }
            }
        });
    }
}
