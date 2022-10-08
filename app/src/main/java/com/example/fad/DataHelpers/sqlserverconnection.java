package com.example.fad.DataHelpers;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqlserverconnection {
    @SuppressLint("NewApi")
    public static Connection CONN() {

        String _user = "sa";
        String _pass = "asa123$";
        String _DB = "dest";
        String _server = ".";
        String _port = "4605";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ":" + _port + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("error", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error", e.getMessage());
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return conn;
    }
}
