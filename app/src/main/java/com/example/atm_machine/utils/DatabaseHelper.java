package com.example.atm_machine.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.atm_machine.models.Transaction;
import com.example.atm_machine.models.TransactionType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "atm_database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TRANSACTION_ID = "transaction_id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_PREVIOUS_BALANCE = "previous_balance";
    private static final String COLUMN_NEW_BALANCE = "new_balance";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_RECIPIENT_ACCOUNT = "recipient_account";
    private static final String COLUMN_IS_SUCCESSFUL = "is_successful";

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
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
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TRANSACTION_ID + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_PREVIOUS_BALANCE + " REAL,"
                + COLUMN_NEW_BALANCE + " REAL,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_RECIPIENT_ACCOUNT + " TEXT,"
                + COLUMN_IS_SUCCESSFUL + " INTEGER" + ")";

        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }



    public boolean saveTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TRANSACTION_ID, transaction.getTransactionId());
        values.put(COLUMN_TYPE, transaction.getType().name());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_PREVIOUS_BALANCE, transaction.getPreviousBalance());
        values.put(COLUMN_NEW_BALANCE, transaction.getNewBalance());
        values.put(COLUMN_TIMESTAMP, transaction.getTimestamp().getTime());
        values.put(COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_RECIPIENT_ACCOUNT, transaction.getRecipientAccount());
        values.put(COLUMN_IS_SUCCESSFUL, transaction.isSuccessful() ? 1 : 0);

        long result = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();

        return result != -1;
    }



    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Use column indices directly instead of getColumnIndex()
                Transaction transaction = new Transaction(
                        cursor.getString(1), // COLUMN_TRANSACTION_ID
                        TransactionType.valueOf(cursor.getString(2)), // COLUMN_TYPE
                        cursor.getDouble(3), // COLUMN_AMOUNT
                        cursor.getDouble(4), // COLUMN_PREVIOUS_BALANCE
                        cursor.getDouble(5), // COLUMN_NEW_BALANCE
                        cursor.getString(7)  // COLUMN_DESCRIPTION
                );

                transaction.setRecipientAccount(cursor.getString(8)); // COLUMN_RECIPIENT_ACCOUNT
                transaction.setSuccessful(cursor.getInt(9) == 1); // COLUMN_IS_SUCCESSFUL

                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactions;
    }


    public void clearTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, null, null);
        db.close();
    }
}