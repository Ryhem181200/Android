package com.example.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.restaurant.entity.Commande;

import java.util.ArrayList;

public class fragment_voir_commande extends Fragment {

    public fragment_voir_commande() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voir_commande, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listViewCommandes = view.findViewById(R.id.listViewCommandes);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // Retrieve all commandes
        ArrayList<Commande> commandes = dbHelper.getAllCommandes();

        if (commandes.isEmpty()) {
            Toast.makeText(getContext(), "Aucune commande trouvée.", Toast.LENGTH_SHORT).show();
        } else {
            CommandeListAdapter adapter = new CommandeListAdapter(getContext(), commandes);
            listViewCommandes.setAdapter(adapter);
        }
    }
}
