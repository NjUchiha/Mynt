package com.example.mynt.collectionsActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mynt.R;
import com.example.mynt.dataAccessLayer.Database_Lite;
import com.example.mynt.collectionsActivity.models.Model_User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Fragment_Login extends Fragment {

    private EditText email;
    private EditText password;
    private ImageButton login;
    private ImageButton close;
    private View loginView;
    private Database_Lite db;
    private Model_User model_user;
    private ArrayList<Model_User> users;
    private String size;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginView = inflater.inflate(R.layout.fragment_login, container, false);


        email = loginView.findViewById(R.id.LoginEmail_EditText);
        password = loginView.findViewById(R.id.LoginPassword_EditText);
        login = loginView.findViewById(R.id.LoginEmail_Button);
        close = loginView.findViewById(R.id.LoginClose_button);


        Login();
        ReturnToRegister();


        return loginView;
    }

    private void Login(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db = new Database_Lite(getContext());
                model_user = new Model_User();
                model_user.setEmail(email.getText().toString());
                model_user.setPassword(password.getText().toString());
                users = new ArrayList<>();
                users = db.getAllUsers();
                size = users.size() + " this";
                Log.d("loginPage", size);

                if(email.getText().toString().length()==0){
                    //Additional User Feedback
                    Toast.makeText(getContext(),"ERROR: A email address has not been entered.",Toast.LENGTH_LONG).show();//(Reference This) (M.Ngetu)
                    Toast.makeText(getContext(),"Please enter a email address to proceed.",Toast.LENGTH_SHORT).show();//(Reference This) (M.Ngetu)
                }


                    if(password.getText().toString().length()==0){

                        //Additional User Feedback
                        Toast.makeText(getContext(),"ERROR: A password has not been entered",Toast.LENGTH_LONG).show();//(Reference This) (M.Ngetu)
                        Toast.makeText(getContext(),"Please enter a password to proceed.",Toast.LENGTH_SHORT).show();//(Reference This) (M.Ngetu)
                    }

                if(email.getText().toString().length()>0 && password.getText().toString().length()>0){
                    for (int i=0; i<users.size(); i++) {
                        if(users.get(i).getEmail().equals(model_user.getEmail()))
                        {
                            if(users.get(i).getPassword().equals(model_user.getPassword()))
                            {
                                //update user state
                                db.updateState(model_user);
                                Intent login = new Intent(getContext(), Activity_Collections.class);
                                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(login);

                                //Additional User Feedback
                                Toast.makeText(getContext(),model_user.getEmail()+ " has logged in successfully.",Toast.LENGTH_LONG).show();//(Reference This) (M.Ngetu)
                            }


                        }else{


                        }
                    }
                }


                }


        });

    }

    private void ReturnToRegister(){
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(loginView).navigate(R.id.action_fragment_Login_to_fragment_Register);
            }
        });


    }

}