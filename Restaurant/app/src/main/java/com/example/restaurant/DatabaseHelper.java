package com.example.restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.restaurant.entity.Commande;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurant_two.db";
    private static final int DATABASE_VERSION = 3;

    // Table `users`
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Table `commandes`
    private static final String TABLE_COMMANDES = "commandes";
    private static final String COLUMN_COMMANDE_ID = "id";
    private static final String COLUMN_CLIENT_NOM = "client_nom";
    private static final String COLUMN_DATE_COMMANDE = "date_commande";
    private static final String COLUMN_MONTANT = "montant";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TABLE_NUMBER = "table_number";
    private static final String COLUMN_DETAILS = "details";

    private static final String COLUMN_URL = "url";
    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT);";
        db.execSQL(createUsersTable);

        // Add some sample users
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password) VALUES ('admin', '12345');");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password) VALUES ('user', 'password123');");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMANDES);

        String createCommandesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMANDES + " (" +
                COLUMN_COMMANDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLIENT_NOM + " TEXT, " +
                COLUMN_DATE_COMMANDE + " TEXT, " +
                COLUMN_MONTANT + " REAL, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_TABLE_NUMBER + " INTEGER, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_URL + " TEXT);";

        db.execSQL(createCommandesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMANDES);

        onCreate(db);
    }

    // Method to authenticate user
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to add a new commande
    public void addCommande(String clientNom, String dateCommande, double montant, String status, int tableNumber, String details, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENT_NOM, clientNom);
        values.put(COLUMN_DATE_COMMANDE, dateCommande);
        values.put(COLUMN_MONTANT, montant);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_TABLE_NUMBER, tableNumber); // New field
        values.put(COLUMN_DETAILS, details);
        values.put(COLUMN_URL, url);   // New field
// New field

        try {
            db.insert(TABLE_COMMANDES, null, values);
            Log.d("DatabaseHelper", "Commande added successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while adding commande", e);
        } finally {
            db.close();
        }
    }

    // Retrieve all commandes, including the new fields
    public ArrayList<Commande> getAllCommandes() {
        ArrayList<Commande> commandesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_COMMANDES, null);
            if (cursor.moveToFirst()) {
                do {
                    Commande commande = new Commande();
                    commande.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMMANDE_ID)));
                    commande.setClientNom(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NOM)));
                    commande.setDateCommande(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_COMMANDE)));
                    commande.setMontant(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MONTANT)));
                    commande.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                    commande.setTableNumber(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TABLE_NUMBER))); // New field
                    commande.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS)));
                    commande.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL)));// New field
                    commandesList.add(commande);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while retrieving commandes", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return commandesList;
    }

    // Update a commande's status
    public void updateCommandeStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);

        try {
            db.update(TABLE_COMMANDES, values, COLUMN_COMMANDE_ID + "=?", new String[]{String.valueOf(id)});
            Log.d("DatabaseHelper", "Commande status updated successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while updating commande status", e);
        } finally {
            db.close();
        }
    }

    // Delete a commande
    public void deleteCommande(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_COMMANDES, COLUMN_COMMANDE_ID + "=?", new String[]{String.valueOf(id)});
            Log.d("DatabaseHelper", "Commande deleted successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while deleting commande", e);
        } finally {
            db.close();
        }
    }
}
