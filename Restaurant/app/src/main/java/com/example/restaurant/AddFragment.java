package com.example.restaurant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.restaurant.entity.Commande;

import org.json.JSONObject;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class AddFragment extends Fragment {

    private double totalAmount = 0.00;
    private LinearLayout linearBill;
    private TextView tvTotal;
    private EditText edtTableNumber, edtCommandeDetails;
    private Button btnAddCommande;

    public AddFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        linearBill = view.findViewById(R.id.linearBill);
        tvTotal = view.findViewById(R.id.tvTotal);
        edtTableNumber = view.findViewById(R.id.edtTableNumber);
        edtCommandeDetails = view.findViewById(R.id.edtOrderDetails);
        btnAddCommande = view.findViewById(R.id.btnAddOrder);

        ImageView menuItemImage = view.findViewById(R.id.ivMenuItem);
        TextView menuItemName = view.findViewById(R.id.tvMenuItemName);
        TextView menuItemPrice = view.findViewById(R.id.tvMenuItemPrice);

        // deuxieme item dans le menu
        ImageView burgerItemImage = view.findViewById(R.id.burgerMenuItem);
        TextView burgerItemName = view.findViewById(R.id.burgertextView);
        TextView burgerItemPrice = view.findViewById(R.id.burgerItemPrice);

        ImageView chawarmaItemImage = view.findViewById(R.id.chawarmaMenuItem);
        TextView chawarmaItemName = view.findViewById(R.id.chawarmatextView);
        TextView chawarmaItemPrice = view.findViewById(R.id.chawarmaItemPrice);
        // Handle menu item clicks
        menuItemImage.setOnClickListener(v -> {
            String itemName = menuItemName.getText().toString();
            String itemPriceStr = menuItemPrice.getText().toString();
            double itemPrice = parsePrice(itemPriceStr);

            addItemToBill(itemName, itemPrice);
        });

        burgerItemImage.setOnClickListener(v -> {
            String name =  burgerItemName.getText().toString();
            String price = burgerItemPrice.getText().toString();

            double iprice = parsePrice(price);

            addItemToBill(name,iprice);
        });

        chawarmaItemImage.setOnClickListener(v -> {
            String name =  chawarmaItemName.getText().toString();
            String price = chawarmaItemPrice.getText().toString();

            double iprice = parsePrice(price);

            addItemToBill(name,iprice);
        });


        // Add Commande to the database
        btnAddCommande.setOnClickListener(v -> {
            if (validateInputs()) {
                try {
                    int tableNumber = Integer.parseInt(edtTableNumber.getText().toString());
                    String commandeDetails = edtCommandeDetails.getText().toString();

                    addCommandeToDatabase(tableNumber, commandeDetails);

                    Toast.makeText(getContext(), "Commande ajoutée avec succès!", Toast.LENGTH_SHORT).show();

                    // Reset fields
                    edtTableNumber.setText("");
                    edtCommandeDetails.setText("");
                    clearBill();
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Numéro de table invalide!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set touch listener on the root view
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus instanceof EditText) {
                    currentFocus.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }
            }
            return false;
        });
    }


    private boolean validateInputs() {
        if (edtTableNumber.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer le numéro de la table.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtCommandeDetails.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer les détails de la commande.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (totalAmount <= 0) {
            Toast.makeText(getContext(), "Veuillez ajouter au moins un article à la facture.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addItemToBill(String itemName, double itemPrice) {
        TextView billItem = new TextView(getContext());
        billItem.setText(String.format("%s - $%.2f", itemName, itemPrice));
        linearBill.addView(billItem);

        totalAmount += itemPrice;

        updateTotal();
    }

    private void updateTotal() {
        tvTotal.setText(String.format("Total: $%.2f", totalAmount));
    }

    private double parsePrice(String priceStr) {
        priceStr = priceStr.replace("$", "").trim();
        try {
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return 0.00;
        }
    }

    private void addCommandeToDatabase(int tableNumber, String commandeDetails) {
        Context context = getContext(); // Get the context of the fragment
        if (context == null) {
            throw new IllegalStateException("Context is null");
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("username", "Unknown User");

        // Create and populate the Commande object
        Commande commande = new Commande();
        commande.setMontant(totalAmount); // Set the total amount
        commande.setStatus("En cours");  // Set status
        commande.setDateCommande(new Date().toString()); // Set the current date as a string
        commande.setClientNom(currentUser);
        commande.setTableNumber(tableNumber);
        commande.setDetails(commandeDetails);


        // use the api to generate an invoice and return the url


        generateInvoiceForCommande(commande, url -> {
            commande.setUrl(url);
            Log.d("Generated Invoice URL", url);

            // Add the Commande to the database
            new AddCommandeTask().execute(commande);
        });
    }
    private void generateInvoiceForCommande(Commande commande, InvoiceCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL("https://app.documentero.com/api");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                // Prepare JSON payload
                JSONObject payload = new JSONObject();
                payload.put("document", "oSQt25AYJsUr6kElQwpX");
                payload.put("apiKey", "ZQMTZRA-Y2AUXHY-TJPB2TQ-KVTYP7Q");
                payload.put("format", "docx");
               /* "document": "oSQt25AYJsUr6kElQwpX",
                        "apiKey": "ZQMTZRA-Y2AUXHY-TJPB2TQ-KVTYP7Q"*/



                JSONObject data = new JSONObject();
                data.put("dateCommande", commande.getDateCommande());
                data.put("tableNumber", commande.getTableNumber());
                data.put("clientNom", commande.getClientNom());
                data.put("details", commande.getDetails());
                data.put("montant", commande.getMontant());
                payload.put("data", data);

                // Write JSON payload to the request body
                OutputStream os = connection.getOutputStream();
                os.write(payload.toString().getBytes("UTF-8"));
                os.close();

                // Read the response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse JSON response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String invoiceUrl = jsonResponse.getString("data");

                    // Call the callback with the generated URL
                    callback.onInvoiceGenerated(invoiceUrl);
                } else {
                    Log.e("API Error", "Failed to generate invoice. Response code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("API Error", "Error generating invoice", e);
            }
        }).start();
    }

    // Callback interface for asynchronous API response
    interface InvoiceCallback {
        void onInvoiceGenerated(String url);
    }
    private void clearBill() {
        linearBill.removeAllViews();
        totalAmount = 0.00;
        updateTotal();
    }

    private class AddCommandeTask extends AsyncTask<Commande, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Commande... commandes) {
            try {
                DatabaseHelper db = DatabaseHelper.getInstance(getContext());
                db.addCommande(commandes[0].getClientNom(),commandes[0].getDateCommande(),commandes[0].getMontant(),
                        commandes[0].getStatus(),commandes[0].getTableNumber(),commandes[0].getDetails(),commandes[0].getUrl());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getContext(), "Commande enregistrée avec succès!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erreur lors de l'enregistrement de la commande.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
