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
                    errorMessage.setVisibility(TextView.INVISIBLE);
                    linkCode = linkCodeET.getText().toString();
                    Order.setPairing_Code(Integer.parseInt(linkCode));
                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
