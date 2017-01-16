package com.onepiece_eren.veritabaniornekuygulama2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teyfik on 11.12.2016.
 */
public class Veritabani extends SQLiteOpenHelper {

    private static final String VERITABANI_ISMI="veritabanim";
    private static final int VERITABANI_VERSION=1;
    private static final String TABLO_ISMI="ders_takip_tablosu";

    private static final String ID="_id";
    private static final String DERS="ders";
    private static final String SORUSAYISI="soru_sayisi";
    private static final String TARIH="tarih";

    public Veritabani(Context context) {
        super(context, VERITABANI_ISMI, null, VERITABANI_VERSION);
    }



    public void onCreate(SQLiteDatabase db) {

        String tablo_olustur="CREATE TABLE "+TABLO_ISMI+
                " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DERS+" TEXT, "+
                SORUSAYISI+" INTEGER NOT NULL, "+
                TARIH+" INTEGER NOT NULL);";

        db.execSQL(tablo_olustur);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLO_ISMI);
        onCreate(db);
    }

    public long kayitEkle(Ogrenci ogrenci) {

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put(DERS,ogrenci.getDers());
        cv.put(SORUSAYISI,ogrenci.getSoru());
        cv.put(TARIH, ogrenci.getTarih());

        long id=db.insert(TABLO_ISMI,null,cv);


        db.close();

        return  id;


    }

    public List<Ogrenci> TumKayitlar() {

        SQLiteDatabase db=this.getReadableDatabase();

        String [] sutunlar=new String[]{DERS,SORUSAYISI,TARIH,ID};

        Cursor c=db.query(TABLO_ISMI, sutunlar, null, null, null, null,TARIH+" desc");
        //Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        int derssirano=c.getColumnIndex(DERS);
        int sorusirano=c.getColumnIndex(SORUSAYISI);
        int tarihsirano=c.getColumnIndex(TARIH);
        int idsirano=c.getColumnIndex(ID);


        List<Ogrenci> ogrenciList=new ArrayList<Ogrenci>();

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            Ogrenci ogrenci=new Ogrenci();

            ogrenci.setDers(c.getString(derssirano));
            ogrenci.setSoru(c.getInt(sorusirano));
            ogrenci.setTarih(c.getLong(tarihsirano));
            ogrenci.setId(c.getLong(idsirano));


            ogrenciList.add(ogrenci);



        }

        db.close();



        return ogrenciList;


    }


    /*
    public void Sil(long id) {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLO_ISMI,ID+"= ?",new String[]{String.valueOf(id)});
        db.close();
    }
    Bu sekilde de kullanabiliriz.
    */
    public void Sil(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLO_ISMI,ID+"="+id,null);
        //2 parametrede şartı yazıyoruz null yazarsak bütün tablodaki satirlar silinir
        // ID = id diyerek tablodaki ID sutununda idye eşit olan satır sildik.
        db.close();
    }

    //tüm kayıtları silmek istersek bu sekilde yaparız
    public void Sil() {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLO_ISMI,null,null);
        db.close();
    }

    public void Guncelle(long id,long tarih,String ders,int soru){

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put(DERS,ders);
        cv.put(TARIH,tarih);
        cv.put(SORUSAYISI,soru);

        //update , delete...
        db.update(TABLO_ISMI, cv, ID + "=" + id, null);
        db.close();

    }

    public List<Ogrenci> IkiTarihArasi(long tarih_ilk, long tarih_son) {

        SQLiteDatabase db=this.getReadableDatabase();

        String [] sutunlar=new String[]{DERS,SORUSAYISI,TARIH,ID};
        String [] tarihler=new String[]{String.valueOf(tarih_ilk),String.valueOf(tarih_son)};

        Cursor c=db.query(TABLO_ISMI, sutunlar,TARIH+ " BETWEEN ? AND ?", tarihler, null, null,TARIH+" desc");
        //Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        int derssirano=c.getColumnIndex(DERS);
        int sorusirano=c.getColumnIndex(SORUSAYISI);
        int tarihsirano=c.getColumnIndex(TARIH);
        int idsirano=c.getColumnIndex(ID);

        List<Ogrenci> ogrenciList=new ArrayList<Ogrenci>();

        /*
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            Ogrenci ogrenci=new Ogrenci();

            ogrenci.setDers(c.getString(derssirano));
            ogrenci.setSoru(c.getInt(sorusirano));
            ogrenci.setTarih(c.getLong(tarihsirano));
            ogrenci.setId(c.getLong(idsirano));

            ogrenciList.add(ogrenci);

        }*/

        if(c.moveToFirst()){

            do
            {
                Ogrenci ogrenci=new Ogrenci();

                ogrenci.setDers(c.getString(derssirano));
                ogrenci.setSoru(c.getInt(sorusirano));
                ogrenci.setTarih(c.getLong(tarihsirano));
                ogrenci.setId(c.getLong(idsirano));

                ogrenciList.add(ogrenci);

            }while (c.moveToNext());
            // sonraki kayıt boş olana kadar do ile devam etmemizi saglıyor



        }else
        {
            ogrenciList=null;
        }




        db.close();



        return ogrenciList;





    }
}
