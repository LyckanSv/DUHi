package sv.com.lyckan.duhi.login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sv.com.lyckan.duhi.MainActivity;
import sv.com.lyckan.duhi.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LogInFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        mAuth = FirebaseAuth.getInstance();
        Button loginBtn = (Button) view.findViewById(R.id.login_buttom);
        Button createUserBtn = (Button) view.findViewById(R.id.register_button);

        EditText email = (EditText) view.findViewById(R.id.email);
        EditText password = (EditText) view.findViewById(R.id.password);

        createUserBtn.setOnClickListener((View v) -> {
            createUser();
        });
        loginBtn.setOnClickListener((View v) ->{

            if (isValidEmail(email.getText().toString()) && isValidPassword(password.getText().toString())) {
                logIn(email.getText().toString(), password.getText().toString());
            }else {
                Toast.makeText(getActivity().getApplication().getBaseContext(), "Alguno de los campos esta incorrecto", Toast.LENGTH_LONG).show();
            }
        });

        if (mAuth.getCurrentUser() != null){
            Log.d("USER", "email: "+ mAuth.getCurrentUser().getEmail());
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void createUser(){

        Log.d("User", "Nuevo usuario");
        CreateUserFragment fragment = new CreateUserFragment();

        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_right)
                .addToBackStack(null)
                .commit();

    }

    private void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("USER", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("USER", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity().getApplication().getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidPassword(String password){
        return (password.length() >= 8 && password.length() <= 36);
    }
}
