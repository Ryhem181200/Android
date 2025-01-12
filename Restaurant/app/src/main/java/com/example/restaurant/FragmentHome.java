package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {

    public FragmentHome() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des boutons
        ImageButton btnAdd = view.findViewById(R.id.btnAdd);
        //ImageButton btnEdit = view.findViewById(R.id.btnEdit);
        //ImageButton btnDelete = view.findViewById(R.id.btnDelete);
        ImageButton btnLogout = view.findViewById(R.id.btnLogout);
        ImageButton btnViewOrders = view.findViewById(R.id.btnViewOrders);

        // Listener pour le bouton Ajouter
        btnAdd.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new AddFragment())
                .addToBackStack(null)
                .commit());

        /* Listener pour le bouton Modifier
        btnEdit.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new EditFragment())
                .addToBackStack(null)
                .commit());

        // Listener pour le bouton Supprimer
        btnDelete.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new DeleteFragment())
                .addToBackStack(null)
                .commit());
*/
        btnViewOrders.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new fragment_voir_commande())
                .addToBackStack(null)
                .commit());


        // Listener pour le bouton Déconnexion
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
