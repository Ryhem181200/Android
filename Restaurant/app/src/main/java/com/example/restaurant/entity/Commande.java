package com.example.restaurant.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "commandes")
public class Commande {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "client_nom")
    private String clientNom;

    @ColumnInfo(name = "date_commande")
    private String dateCommande;

    @ColumnInfo(name = "montant")
    private double montant;

    @ColumnInfo(name = "status")
    private String status; // Ex: "En cours", "Livré", "Annulé"
    @ColumnInfo(name = "tableNumber")
    private int tableNumber;

    @ColumnInfo(name = "details")
    private String details;

    @ColumnInfo(name = "url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDetails() {
        return details;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public Commande() {
    }


    @Override
    public String toString() {
        return "Commande{" +
                "clientNom='" + clientNom + '\'' +
                ", id=" + id +
                ", dateCommande='" + dateCommande + '\'' +
                ", montant=" + montant +
                ", status='" + status + '\'' +
                ", tableNumber=" + tableNumber +
                ", details='" + details + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    // Constructeur, getters et setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
