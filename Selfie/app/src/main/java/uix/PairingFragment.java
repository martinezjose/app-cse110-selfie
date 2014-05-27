package uix;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import cse110.selfie.app.UI.R;

/**
 * Created by JuanJ on 5/23/2014.
 * Screen to set code to match table with table.
 * If pairing code is less than 5 characters long will display and error message.
 */
public class PairingFragment extends FragmentActivity {

    EditText linkCodeET;
    TextView errorMessage;
    Button linkButton;
    String linkCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pairing_screen);

        errorMessage = (TextView) findViewById(R.id.error_text);
        errorMessage.setVisibility(TextView.INVISIBLE);

        linkCodeET = (EditText) findViewById(R.id.pairingId);

        linkButton = (Button) findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linkCodeET.getText().length() < 5) {
                    errorMessage.setVisibility(TextView.VISIBLE);
                    errorMessage.setText("Too Short");
                    linkCodeET.setText("");
                }
                else {
                    errorMessage.setVisibility(TextView.INVISIBLE);
                    linkCode = linkCodeET.getText().toString();
                    Order.setTableId(Integer.parseInt(linkCode));
                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
