package com.example.fad.DataHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fad.Rates.ratesdata;
import com.example.fad.branch.branch;
import com.example.fad.farmers.farmersdata;
import com.example.fad.milkcollection.milkdata;
import com.example.fad.settingsdata;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.fad.farmers.farmers.searchtext;
import static com.example.fad.milkcollection.milkcollection.ddate;
import static com.example.fad.milkcollection.milkcollection.dshift;
import static com.example.fad.reports.report.dfdate;
import static com.example.fad.reports.report.dfmtype;
import static com.example.fad.reports.report.dfshift;
import static com.example.fad.reports.report.drfcode;
import static com.example.fad.reports.report.dtdate;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG ="DatabaseHelper";
    private static final String DATABASE_NAME = "Database";
    private static final String TABLE_NAME="milkdata";
    private static final String slno = "slno";
    private static final String mdate= "date";
    private static final String mshift = "shift";
    private static final String mcode = "code";
    private static final String mname = "name";
    private static final String mquantity ="quantity";
    private static final String mmtype="mtype";
    private static final String mfat ="fat";
    private static final String msnf = "snf";
    private static final String mrate = "rate";
    private static final String mamount = "amount";

    private static final String FARMER_TABLE_NAME="farmerdata";
    private static final String fid = "slno";
    private static final String fmcode = "code";
    private static final String ffname = "fname";
    private static final String fmobileno = "mobileno";
    private static final String fmtype = "mtype";
    private static final String factive ="active";

    //first the thing is we should assign rate chart to farmers
    //so we need to define the rate chart id in farmer table
    //and we should declare a ratechart id in ratechart table without distrubing branch data

    private static final String RATE_TABLE_NAME="milkrates";
    private static final String rslno = "slno";
    private static final String rbcode ="bcode";
    private static final String rrcid ="rcid";
    private static final String rmtype="mtype";
    private static final Date rfdate = null;
    private static final Date rtdate = null;
    private static final String rfatmin ="fatmin";
    private static final String rfatmax ="fatmax";
    private static final String rsnfmin ="snfmin";
    private static final String rsnfmax ="snfmax";
    private static final String rtsrate ="tsrate";

    private static final String SETTINGS_TABLE_NAME="settings";
    private static final String sslno = "slno";
    private static final String sbcode="bcode";
    private static final String sname ="name";
    private static final String svalue="value";
    private static final String sactive ="active";

    private static final String BRANCH_TABLE_NAME="branch";
    private static final String bslno = "slno";
    private static final String bbcode="bcode";
    private static final String bbname ="bname";
    private static final String bfdate="fdate";
    private static final String btdate ="btdate";
    private static final String bactive ="active";
    private static final String bproductkey ="productkey";
    private static final String bdeviceid="deviceid";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    private static final String createTable = "CREATE TABLE milkdata("+"slno INTEGER PRIMARY KEY AUTOINCREMENT, " + "date DATE, "+ "shift BOOLEAN, "+ "code INTEGER, "+ "name TEXT, "+ "quantity Numeric(18,2), "+ "mtype TEXT, " + "fat Numeric(18,2), "+ "snf Numeric(18,2), "+ "rate Numeric(18,2), "+ "amount Numeric(18,2),"+" foreign key (code) references farmerdata (code) on update restrict on delete restrict)";
    private static final String createFarmerTable = "CREATE TABLE farmerdata("+"slno INTEGER PRIMARY KEY AUTOINCREMENT,code INTEGER UNIQUE, "+ "fname TEXT, "+ "mobileno TEXT, "+ "mtype TEXT, " + "active BOOLEAN) ";
    private static final String createRATETable="CREATE TABLE "+RATE_TABLE_NAME+"("+"slno INTEGER PRIMARY KEY AUTOINCREMENT, " + "bcode INTEGER,"+ "rrcid TEXT, "+ "mtype TEXT, "+ "fdate DATE, "+ "tdate DATE, "+ "fatmin Numeric(18,2), "+ "fatmax Numeric(18,2), " + "snfmin Numeric(18,2), "+ "snfmax Numeric(18,2), "+ "tsrate Numeric(18,2))";
    private static final String createdRATETable="CREATE TABLE dynamicrates("+"slno INTEGER PRIMARY KEY AUTOINCREMENT, " + "bcode INTEGER, "+ "mtype TEXT, "+ "fdate DATE, "+ "tdate DATE, "+ "fat Numeric(18,2), "+ "snf Numeric(18,2), "+ "rate Numeric(18,2))";
    private static final String createSettingsTable="CREATE TABLE "+SETTINGS_TABLE_NAME+"("+"slno INTEGER PRIMARY KEY AUTOINCREMENT, " + "bcode INTEGER, "+ "name TEXT, "+ "value TEXT, "+ "active BOOLEAN)";
    private static final String createBranchTable="CREATE TABLE "+BRANCH_TABLE_NAME+"("+"slno INTEGER PRIMARY KEY AUTOINCREMENT, " + "bcode INTEGER, "+ "bname TEXT, "+ "fdate DATE, "+ "tdate DATE, "+ "active BOOLEAN, "+ "productkey TEXT, "+ "deviceid TEXT)";
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createTable);
        db.execSQL(createFarmerTable);
        db.execSQL(createRATETable);
        db.execSQL(createdRATETable);
        db.execSQL(createSettingsTable);
        db.execSQL(createBranchTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FARMER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RATE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS dynamicrates");
        db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BRANCH_TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String code,String name,Date date,String shift,String mtype,String quantity,String fat,String snf,String rate,String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(String.valueOf(mdate),String.valueOf(date));
        contentValues.put(mshift,shift);
        contentValues.put(mcode,code);
        contentValues.put(mname,name);
        contentValues.put(mquantity,quantity);
        contentValues.put(mmtype,mtype);
        contentValues.put(mfat,fat);
        contentValues.put(msnf,snf);
        contentValues.put(mrate,rate);
        contentValues.put(mamount,amount);
        Log.d(TAG,"addData :Adding "+code+" to "+TABLE_NAME);
        long result = db.insert(TABLE_NAME,null,contentValues);
        return result != -1;
    }
    public boolean addsettings(String bcode,String name,String value,String active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(sbcode,bcode);
        contentValues.put(sname,name);
        contentValues.put(svalue,value);
        contentValues.put(sactive,active);
        long result = db.insert(SETTINGS_TABLE_NAME,null,contentValues);
        return result != -1;
    }
    public boolean updatesettings(String slno,String bcode,String name,String value,String active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(sbcode,bcode);
        contentValues.put(sname,name);
        contentValues.put(svalue,value);
        contentValues.put(sactive,active);
        long result = db.update(SETTINGS_TABLE_NAME,contentValues,"slno = " + slno,null);
        return result != -1;
    }
    public boolean updateData(String slno,String code,String name,Date date,String shift,String mtype,String quantity,String fat,String snf,String rate,String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(String.valueOf(mdate),String.valueOf(date));
        contentValues.put(mshift,shift);
        contentValues.put(mcode,code);
        contentValues.put(mname,name);
        contentValues.put(mquantity,quantity);
        contentValues.put(mmtype,mtype);
        contentValues.put(mfat,fat);
        contentValues.put(msnf,snf);
        contentValues.put(mrate,rate);
        contentValues.put(mamount,amount);
        Log.d(TAG,"Update :Updating "+code+" to "+TABLE_NAME);
        long result = db.update(TABLE_NAME,contentValues,"slno = " + slno,null);
        return result != -1;
    }
    public boolean addfData(String code,String fname,String mobileno,String mtype,String active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(fmcode,code);
        contentValues.put(ffname,fname);
        contentValues.put(fmobileno,mobileno);
        contentValues.put(fmtype,mtype);
        contentValues.put(factive,active);
        Log.d(TAG,"addData :Adding "+code+" to "+FARMER_TABLE_NAME);
        long result = db.insert(FARMER_TABLE_NAME,null,contentValues);
        return result != -1;
    }
    public boolean addbData(int bcode,String bname,Date fdate,Date tdate,String productkey,String deviceid,boolean active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(bbcode,bcode);
        contentValues.put(bbname,bname);
        contentValues.put(bfdate, String.valueOf(fdate));
        contentValues.put(btdate, String.valueOf(tdate));
        contentValues.put(bproductkey,productkey);
        contentValues.put(bdeviceid,deviceid);
        contentValues.put(bactive,active);
        Log.d(TAG,"addData :Adding "+bcode+" to "+BRANCH_TABLE_NAME);
        long result = db.insert(BRANCH_TABLE_NAME,null,contentValues);
        return result != -1;
    }
    public boolean updatefarmers(String slno,String code,String fname,String mobileno,String mtype,String active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(fmcode,code);
        contentValues.put(ffname,fname);
        contentValues.put(fmobileno,mobileno);
        contentValues.put(fmtype,mtype);
        contentValues.put(factive,active);
        Log.d(TAG,"Update :Updating "+code+" to "+FARMER_TABLE_NAME);
        long result = db.update(FARMER_TABLE_NAME,contentValues,"slno = " + slno,null);
        return result != -1;
    }
    public boolean updatebranch(int bcode,String bname,Date fdate,Date tdate,String productkey,String deviceid,boolean active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(bbcode,bcode);
        contentValues.put(bbname,bname);
        contentValues.put(bfdate, String.valueOf(fdate));
        contentValues.put(btdate, String.valueOf(tdate));
        contentValues.put(bproductkey,productkey);
        contentValues.put(bdeviceid,deviceid);
        contentValues.put(bactive,active);
        Log.d(TAG,"Update :Updating "+bcode+" to "+BRANCH_TABLE_NAME);
        long result = db.update(BRANCH_TABLE_NAME,contentValues,"slno = " + slno,null);
        return result != -1;
    }
    public ArrayList<branch> getbdata() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<branch> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + BRANCH_TABLE_NAME + " where date between '"+bfdate+"' and '"+btdate+"'", null);
        while (cursor != null && cursor.moveToNext()) {
            int bcode = cursor.getColumnIndex("bcode");
            String bname= cursor.getString(cursor.getColumnIndex("bname"));
            Date fdate = Date.valueOf(String.valueOf(cursor.getColumnIndex("fdate")));
            Date tdate = Date.valueOf(String.valueOf(cursor.getColumnIndex("tdate")));
            String productkey = cursor.getString(cursor.getColumnIndex("productkey"));
            String deviceid = cursor.getString(cursor.getColumnIndex("deviceid"));
            int active =cursor.getColumnIndex("active");
            branch bdata = new branch(bcode,bname,fdate,tdate,productkey,deviceid,active);
            arrayList.add(bdata);
        }
        return arrayList;
    }
    public boolean deletemilkrecord(String id,String code){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"delete :Deleting "+code+" from "+TABLE_NAME);
        long result = db.delete(TABLE_NAME,"slno = " + id,null);
        return result != -1;
    }
    public boolean deletefarmerrecord(String slno){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(FARMER_TABLE_NAME,"slno = " + slno,null);
        return result != -1;
    }
    public boolean deleteraterecord(String slno){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(RATE_TABLE_NAME,"slno = " + slno,null);
        return result != -1;
    }
    public boolean adddratesData(String bcode,String rcid, String mtype, Date fdate, Date tdate, String fat, String snf, String rate){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(rbcode, bcode);
            contentValues.put(rmtype, mtype);
            contentValues.put(rrcid, rcid);
            //DateFormat.getDateInstance().format(myDate);
            contentValues.put("fdate", String.valueOf(fdate));
            contentValues.put("tdate", String.valueOf(tdate));
            contentValues.put(rfatmin, fat);
            contentValues.put(rsnfmin, snf);
            contentValues.put(rtsrate, rate);
            Log.d(TAG, "addData :Adding " + bcode + " to " + RATE_TABLE_NAME);
            result = db.insert("dynamicrates", null, contentValues);

        }
        return result != -1;
    }
    public boolean addratesData(String bcode,String rcid, String mtype, Date fdate, Date tdate, String fatmin, String fatmax, String snfmin, String snfmax, String tsrate){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(rbcode, bcode);
            contentValues.put(rrcid, rcid);
            contentValues.put(rmtype, mtype);
            //DateFormat.getDateInstance().format(myDate);
            contentValues.put("fdate", String.valueOf(fdate));
            contentValues.put("tdate", String.valueOf(tdate));
            contentValues.put(rfatmin, fatmin);
            contentValues.put(rfatmax, fatmax);
            contentValues.put(rsnfmin, snfmin);
            contentValues.put(rsnfmax, snfmax);
            contentValues.put(rtsrate, tsrate);
            Log.d(TAG, "addData :Adding " + bcode + " to " + RATE_TABLE_NAME);
            result = db.insert(RATE_TABLE_NAME, null, contentValues);

        }
        return result != -1;
    }
    public boolean updateratesData(String slno, String bcode,String rcid, String mtype, Date fdate, Date tdate, String fatmin, String fatmax, String snfmin, String snfmax, String tsrate){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.isOpen()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(rbcode, bcode);
            contentValues.put(rrcid, rcid);
            contentValues.put(rmtype, mtype);
            contentValues.put(String.valueOf(rfdate), String.valueOf(fdate));
            contentValues.put(String.valueOf(rtdate), String.valueOf(tdate));
            contentValues.put(rfatmin, fatmin);
            contentValues.put(rfatmax, fatmax);
            contentValues.put(rsnfmin, snfmin);
            contentValues.put(rsnfmax, snfmax);
            contentValues.put(rtsrate, tsrate);
            Log.d(TAG,"Update :Updating "+bcode+" to "+RATE_TABLE_NAME);
            result = db.update(RATE_TABLE_NAME,contentValues,"slno = " + slno,null);
        }
        return result != -1;
    }
    public Cursor getvalidatecode(String code){
        SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor=sqLiteDatabase.rawQuery("select code from "+FARMER_TABLE_NAME+" where code='"+code+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("code")));
            cursor.moveToNext();
        }
        return cursor;
    }
    public Cursor getdata(){
        SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("date")));
            cursor.moveToNext();
        }
        return cursor;
    }
    public Cursor getfname(String code){
        SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT fname,lname from farmerdata where code='"+code+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("fname")));
            cursor.moveToNext();
        }
        return cursor;
    }
    public ArrayList<milkdata> getadata() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<milkdata> arrayList = new ArrayList<>();
        System.out.println(ddate);
        System.out.println(dshift);
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where date ='"+ddate+"' and shift='"+dshift+"'", null);
            while (cursor != null && cursor.moveToNext()) {
                try {
                String id = cursor.getString(cursor.getColumnIndex("slno"));
                Date date = Date.valueOf(cursor.getString(cursor.getColumnIndex("date")));
                String shift = cursor.getString(cursor.getColumnIndex("shift"));
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String mtype = cursor.getString(cursor.getColumnIndex("mtype"));
                String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                String fat = cursor.getString(cursor.getColumnIndex("fat"));
                String snf = cursor.getString(cursor.getColumnIndex("snf"));
                String rate = cursor.getString(cursor.getColumnIndex("rate"));
                String amount = cursor.getString(cursor.getColumnIndex("amount"));
                milkdata milkdata = new milkdata(id, date, shift, code, name, mtype, quantity, fat, snf, rate, amount);
                arrayList.add(milkdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        return arrayList;
    }
    public ArrayList<milkdata> getrdata() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<milkdata> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where date between '"+dfdate+"' and '"+dtdate+"'", null);
        while (cursor != null && cursor.moveToNext()) {
            try {
            String id = cursor.getString(cursor.getColumnIndex("slno"));
            String dat = cursor.getString(cursor.getColumnIndex("date"));
            long edate=new SimpleDateFormat("dd/MM/yyyy").parse(dat).getTime();
            java.sql.Date date=new java.sql.Date(edate);
            String shift = cursor.getString(cursor.getColumnIndex("shift"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String mtype = cursor.getString(cursor.getColumnIndex("mtype"));
            String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
            String fat = cursor.getString(cursor.getColumnIndex("fat"));
            String snf = cursor.getString(cursor.getColumnIndex("snf"));
            String rate = cursor.getString(cursor.getColumnIndex("rate"));
            String amount = cursor.getString(cursor.getColumnIndex("amount"));
            milkdata milkdata = new milkdata(id, date, shift, code, name, mtype, quantity, fat, snf, rate, amount);
            arrayList.add(milkdata);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
    public ArrayList<milkdata> getrspecificdata() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<milkdata> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where date between '"+dfdate+"' and '"+dtdate+"' and shift='"+dfshift+"' and mtype='"+dfmtype+"' and code='"+drfcode+"'", null);
        while (cursor != null && cursor.moveToNext()) {
            try {
                String id = cursor.getString(cursor.getColumnIndex("slno"));
                String dat = cursor.getString(cursor.getColumnIndex("date"));
                long edate=new SimpleDateFormat("dd/MM/yyyy").parse(dat).getTime();
                java.sql.Date date=new java.sql.Date(edate);
                String shift = cursor.getString(cursor.getColumnIndex("shift"));
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String mtype = cursor.getString(cursor.getColumnIndex("mtype"));
                String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                String fat = cursor.getString(cursor.getColumnIndex("fat"));
                String snf = cursor.getString(cursor.getColumnIndex("snf"));
                String rate = cursor.getString(cursor.getColumnIndex("rate"));
                String amount = cursor.getString(cursor.getColumnIndex("amount"));
                milkdata milkdata = new milkdata(id, date, shift, code, name, mtype, quantity, fat, snf, rate, amount);
                arrayList.add(milkdata);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
    public ArrayList<settingsdata> getsmssetting() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<settingsdata> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from settings where name=sms", null);
        while (cursor != null && cursor.moveToNext()) {
            String slno = cursor.getString(cursor.getColumnIndex("slno"));
            String bcode = cursor.getString(cursor.getColumnIndex("bcode"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String value = cursor.getString(cursor.getColumnIndex("value"));
            String active = cursor.getString(cursor.getColumnIndex("active"));
            settingsdata setting = new settingsdata(slno, bcode, name, value, active);
            arrayList.add(setting);
        }
        return arrayList;
    }
    public ArrayList<farmersdata> getfdata() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<farmersdata> arraylistf = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + FARMER_TABLE_NAME , null);
        while (cursor != null && cursor.moveToNext()) {
            String slno =cursor.getString(cursor.getColumnIndex("slno"));
            String code=cursor.getString(cursor.getColumnIndex("code"));
            String fname=cursor.getString(cursor.getColumnIndex("fname"));
            String mobileno=cursor.getString(cursor.getColumnIndex("mobileno"));
            String mtype=cursor.getString(cursor.getColumnIndex("mtype"));
            String active =cursor.getString(cursor.getColumnIndex("active"));
            farmersdata fardata= new farmersdata(slno,code,fname,mobileno,mtype,active);
            arraylistf.add(fardata);
        }
        return arraylistf;
    }
    public ArrayList<farmersdata> getfarmersearch() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<farmersdata> arraylistf = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + FARMER_TABLE_NAME +" where fname like '%"+searchtext+"%'", null);
        while (cursor != null && cursor.moveToNext()) {
            String slno = cursor.getString(cursor.getColumnIndex("slno"));
            String code=cursor.getString(cursor.getColumnIndex("code"));
            String fname=cursor.getString(cursor.getColumnIndex("fname"));
            String mobileno=cursor.getString(cursor.getColumnIndex("mobileno"));
            String mtype=cursor.getString(cursor.getColumnIndex("mtype"));
            String active =cursor.getString(cursor.getColumnIndex("active"));
            farmersdata fardata= new farmersdata(slno,code,fname,mobileno,mtype,active);
            arraylistf.add(fardata);
        }
        return arraylistf;
    }

    public ArrayList<ratesdata> getratesData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<ratesdata> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + RATE_TABLE_NAME, null);
        while (cursor != null && cursor.moveToNext()) {
            String slno = cursor.getString(cursor.getColumnIndex("slno"));
            String bcode = cursor.getString(cursor.getColumnIndex("bcode"));
            String mtype = cursor.getString(cursor.getColumnIndex("mtype"));
            String fdate = cursor.getString(cursor.getColumnIndex("fdate"));
            String tdate = cursor.getString(cursor.getColumnIndex("tdate"));
            String fatmin = cursor.getString(cursor.getColumnIndex("fatmin"));
            String fatmax = cursor.getString(cursor.getColumnIndex("fatmax"));
            String snfmin = cursor.getString(cursor.getColumnIndex("snfmin"));
            String snfmax = cursor.getString(cursor.getColumnIndex("snfmax"));
            String tsrate = cursor.getString(cursor.getColumnIndex("tsrate"));
            ratesdata rdata = new ratesdata(slno,bcode, mtype,fdate,tdate,fatmin, fatmax,snfmin, snfmax, tsrate);
            arrayList.add(rdata);
        }
        return arrayList;
    }

}
