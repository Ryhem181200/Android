package com.example.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

public class FragmentGestion6 extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText etUsername, etPassword;
    private Button btnLogin;

    public FragmentGestion6() {
        // Constructeur vide obligatoire
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext()); // Initialisation de la base de données
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Créer la vue de connexion
        View view = inflater.inflate(R.layout.fragment_gestion6, container, false);

        // Initialisation des composants
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        // Gestion du clic sur le bouton de connexion
        btnLogin.setOnClickListener(v -> authenticateUser());

        return view;
    }

    private void authenticateUser() {
        // Récupérer les données saisies
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Vérifier si les champs sont vides
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier l'utilisateur dans la base de données
        boolean isValidUser = dbHelper.authenticateUser(username, password);

        // Afficher le résultat
        if (isValidUser) {
            Toast.makeText(getContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();

            //storing the user data to shared file
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);  // Save username
            editor.apply();
            // Charger un fragment complet pour l'écran principal
            FragmentHome fragmentHome = new FragmentHome();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, fragmentHome)  // REMPLACE le fragment actuel par celui-ci
                    .addToBackStack(null)  // Ajouter à la pile pour revenir en arrière
                    .commit();
        } else {
            Toast.makeText(getContext(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();
        }
    }
}
