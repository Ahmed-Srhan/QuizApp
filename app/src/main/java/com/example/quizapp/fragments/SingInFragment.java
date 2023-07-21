package com.example.quizapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.quizapp.R;
import com.example.quizapp.databinding.FragmentSingInBinding;
import com.example.quizapp.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;


public class SingInFragment extends Fragment {
    private FragmentSingInBinding binding;
    private AuthViewModel viewModel;
    private NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSingInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailEditSignIN.getText().toString();
                String pass = binding.passEditSignIn.getText().toString();
                if (!email.isEmpty() && !pass.isEmpty()) {
                    viewModel.signIn(email, pass);
                    Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    viewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
                        @Override
                        public void onChanged(FirebaseUser firebaseUser) {
                            navController.navigate(R.id.action_signInFragment_to_listFragment);
                        }
                    });

                } else
                    Toast.makeText(getContext(), "Please Enter Your Email and password", Toast.LENGTH_SHORT).show();

            }
        });

        binding.signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });
    }
}