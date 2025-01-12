package com.example.restaurant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurant.entity.Commande;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class CommandeListAdapter extends ArrayAdapter<Commande> {

    private final Context context;
    private final ArrayList<Commande> commandes;

    public CommandeListAdapter(@NonNull Context context, @NonNull ArrayList<Commande> commandes) {
        super(context, 0, commandes);
        this.context = context;
        this.commandes = commandes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.commande_list_item, parent, false);
        }

        Commande commande = commandes.get(position);

        ImageView imageQRCode = convertView.findViewById(R.id.imageQRCode);
        TextView clientNom = convertView.findViewById(R.id.clientNom);
        TextView dateCommande = convertView.findViewById(R.id.dateCommande);
        TextView montant = convertView.findViewById(R.id.montant);
        TextView status = convertView.findViewById(R.id.status);
        Button buttonEdit = convertView.findViewById(R.id.buttonEdit);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

        // Set data
        clientNom.setText(commande.getClientNom());
        dateCommande.setText(commande.getDateCommande());
        montant.setText(String.valueOf(commande.getMontant()));
        status.setText(commande.getStatus());

        // QR Code generation
        Bitmap qrCodeBitmap = generateQRCodeBitmap(commande.getUrl());
        if (qrCodeBitmap != null) {
            imageQRCode.setImageBitmap(qrCodeBitmap);
        } else {
            imageQRCode.setImageResource(R.drawable.ic_qr_code_placeholder); // Fallback image
        }

        // Handle edit button click
        buttonEdit.setOnClickListener(v -> {
            if (commande.getStatus().equals("En cours")) {
                buttonEdit.setText("Payer");
                commande.setStatus("Livré");
            } else if (commande.getStatus().equals("Livré")) {
                commande.setStatus("Payer");
            }
            // Notify the adapter about data changes
            notifyDataSetChanged();
        });

        // Handle delete button click
        buttonDelete.setOnClickListener(v -> {
            commandes.remove(position);
            notifyDataSetChanged(); // Refresh the list view
        });

        return convertView;
    }

    private Bitmap generateQRCodeBitmap(String data) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            Log.e("QRCodeError", "Error generating QR code: " + e.getMessage());
            return null;
        }
    }
}


