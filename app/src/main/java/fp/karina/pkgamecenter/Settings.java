package fp.karina.pkgamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Settings extends AppCompatActivity {

    private DatabaseHelper db;

    private ImageButton bEdit;
    private Button bRegister;
    private Button bLogin;
    private Button bLogout;
    private TextView tUser;

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new DatabaseHelper(this);

        user = db.getActiveUser();

        bEdit = findViewById(R.id.edit_nick);
        bRegister = findViewById(R.id.user_register);
        bLogin = findViewById(R.id.user_login);
        bLogout = findViewById(R.id.user_logout);
        tUser = findViewById(R.id.tUser);

        if (user == null) {
            bEdit.setEnabled(false);
            bRegister.setEnabled(true);
            bLogin.setEnabled(true);
            bLogout.setEnabled(false);
            tUser.setText(R.string.guest);
        } else {
            bEdit.setEnabled(true);
            bRegister.setEnabled(false);
            bLogin.setEnabled(false);
            bLogout.setEnabled(true);
            tUser.setText(user);
        }

        bEdit.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_edit_nick);
            dialog.show();

            TextInputLayout editNick = dialog.findViewById(R.id.euser);
            editNick.getEditText().setText(user);

            Button edit = dialog.findViewById(R.id.eapply);
            edit.setOnClickListener(view1 -> {
                if (db.editUser(user, editNick.getEditText().getText().toString())) {
                    dialog.dismiss();
                    user = editNick.getEditText().getText().toString();
                    tUser.setText(user);
                } else {
                    editNick.setError("That nickname already exists");
                    editNick.getEditText().setText(user);
                }
            });

        });

        bRegister.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_register);
            dialog.show();

            TextInputLayout ruser = dialog.findViewById(R.id.ruser);
            TextInputLayout rpass = dialog.findViewById(R.id.rpassword);
            TextInputLayout cpass = dialog.findViewById(R.id.cpassword);

            Button rCancel = dialog.findViewById(R.id.rCancel);
            rCancel.setOnClickListener(view1 -> dialog.dismiss());
            
            Button rButton = dialog.findViewById(R.id.rButton);
            rButton.setOnClickListener(view1 -> {
                if (rpass.getEditText().getText().toString().equals(cpass.getEditText().getText().toString())) {
                    if (db.registerUser(ruser.getEditText().getText().toString(), rpass.getEditText().getText().toString())) {
                        db.login(ruser.getEditText().getText().toString(), rpass.getEditText().getText().toString());
                        dialog.dismiss();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else {
                        ruser.setError("That nickname already exists");
                    }
                } else {
                    cpass.setError("Passwords don't match");
                }
            });

        });

        bLogin.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_login);
            dialog.show();

            TextInputLayout luser = dialog.findViewById(R.id.luser);
            TextInputLayout lpass = dialog.findViewById(R.id.lpassword);

            Button lCancel = dialog.findViewById(R.id.lCancel);
            lCancel.setOnClickListener(view1 -> dialog.dismiss());

            Button lButton = dialog.findViewById(R.id.lButton);
            lButton.setOnClickListener(view1 -> {
                if (db.login(luser.getEditText().getText().toString(), lpass.getEditText().getText().toString())) {
                    dialog.dismiss();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    luser.getEditText().setText("");
                    luser.setError("User or password is wrong or doesn't exist.");
                    lpass.getEditText().setText("");
                    lpass.setError("User or password is wrong or doesn't exist.");
                }
            });
        });

        bLogout.setOnClickListener(view -> {
            db.logout();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });


    }
}