package com.onepiece_eren.veritabaniornekuygulama2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;

import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int DIALOG_HAKKINDA=1;
    private static final int DIALOG_DERS=2;
    private static final int DIALOG_TARIH=3;

    private TableLayout tablo;
    private TextView tv;
    private ActionMode actionMode;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv= (TextView) findViewById(R.id.tv);
        tablo = (TableLayout) findViewById(R.id.tablo);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ab));

        try{
            Listele();
        }catch (Exception e) {
            Toast.makeText(MainActivity.this, "Herhangi bir kayıt bulunamadı.", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){

            case R.id.action_settings:

                return  true;

            case R.id.ekle:

                showDialog(DIALOG_DERS);

            return  true;

            case R.id.paylas:

                paylasMesaj(tv.getText());

                return  true;

            case R.id.tarih:

                showDialog(DIALOG_TARIH);

                return  true;

            case R.id.hakkinda:

                showDialog(DIALOG_HAKKINDA);

                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;
        switch (id){

            case DIALOG_HAKKINDA:
                dialog=new Dialog(MainActivity.this);
                dialog.setTitle("Hakkinda");
                dialog.setContentView(R.layout.hakkinda);
                break;

            case DIALOG_DERS:
                dialog=getEkleDialog();
                break;

            case DIALOG_TARIH:
                dialog=getIkiTarihArasi();
                break;

            default:
                dialog = null;
        }
        return dialog;
    }

    private Dialog getIkiTarihArasi() {

        LayoutInflater inflater=LayoutInflater.from(this);
        View layout=inflater.inflate(R.layout.iki, null);

        Button kaydet= (Button) layout.findViewById(R.id.kaydet);
        Button vazgec= (Button) layout.findViewById(R.id.vazgec);
        final DatePicker dp_ilk= (DatePicker) layout.findViewById(R.id.dp_ilk);
        final DatePicker dp_son= (DatePicker) layout.findViewById(R.id.dp_son);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("İki Tarih Arası");
        builder.setView(layout);

        final AlertDialog dialog=builder.create();

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                int gun_ilk = dp_ilk.getDayOfMonth();
                int ay_ilk = dp_ilk.getMonth() + 1;
                int yil_ilk = dp_ilk.getYear();



                Date date = null;
                try {
                    date = df.parse(gun_ilk + "/" + ay_ilk + "/" + yil_ilk);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long tarih_ilk = date.getTime();



                int gun_son = dp_son.getDayOfMonth();
                int ay_son = dp_son.getMonth() + 1;
                int yil_son = dp_son.getYear();



                Date date_son = null;
                try {
                    date_son = df.parse(gun_son + "/" + ay_son + "/" + yil_son);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long tarih_son = date_son.getTime();


                Veritabani db=new Veritabani(getApplicationContext());


                List<Ogrenci> ogrenciList=new ArrayList<Ogrenci>();
                ogrenciList=db.IkiTarihArasi(tarih_ilk,tarih_son);



                /*
                *
                *
                * Durum raporu burada oluşturulacak
                *
                * Kayıtlar burada listelenecek
                *
                * Silme ve güncelleme işlemleri de burada kodlanacak
                *
                *
                *
                */



                dialog.dismiss();


            }
        });

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;

    }

    private Dialog getEkleDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.ders_ekle,null);

        Button kaydet = (Button) layout.findViewById(R.id.kaydet);
        Button vazgec = (Button) layout.findViewById(R.id.vazgec);
        final EditText soru = (EditText) layout.findViewById(R.id.et);
        final Spinner sp= (Spinner) layout.findViewById(R.id.sp);
        final DatePicker datepicker = (DatePicker) layout.findViewById(R.id.dp);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kayıt Ekle");
        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int gun = datepicker.getDayOfMonth();
                    int ay = datepicker.getMonth() + 1;
                    int yil = datepicker.getYear();

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    Date date = null;

                    try {
                        date = df.parse(gun + "/" + ay + "/" + yil);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long tarih = date.getTime();

                    int pozisyon = sp.getSelectedItemPosition();
                    String ders = (String) sp.getItemAtPosition(pozisyon);

                    int soru_sayisi = Integer.valueOf(soru.getText().toString());

                    Ogrenci ogrenci = new Ogrenci(ders, soru_sayisi, tarih);

                    Veritabani dp = new Veritabani(getApplicationContext());
                    long id = dp.kayitEkle(ogrenci);

                    if (id == -1) {
                        Toast.makeText(MainActivity.this, "Kayit sırasında bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Kayit işlemi başarılı.", Toast.LENGTH_SHORT).show();
                    }

                    Listele();
                    //yeni bir kayıt yaptıktan sonra hemen listelemesi icin ekledik listele methodunu

                    dialog.dismiss();
                }catch (Exception e){

                    Toast.makeText(MainActivity.this, "Lütfen soru sayısını giriniz", Toast.LENGTH_SHORT).show();

                }
            }
        });

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;
    }
    private void Listele() {

        //listeyi temizledik
        tablo.removeAllViews();


        Veritabani db = new Veritabani(getApplicationContext());

        List<Ogrenci> ogrenciList = new ArrayList<Ogrenci>();
        ogrenciList=db.TumKayitlar();

        //toplam ve ortalamasorusayisi--------------------------------------------------------------

        long en_kucuk=ogrenciList.get(ogrenciList.size()-1).getTarih();
        long en_buyuk=ogrenciList.get(0).getTarih();
            //1.ve sonuncu tarihleri alıp arasındaki gün farkını bulup toplam soru sayısına bölüyoruz

        Date fark = new Date(en_buyuk-en_kucuk);
            // burdaki fark long türünden normal gösterimi icin asagıda algoritmayı kullandık
        int fark_normalgosterimi=((fark.getYear() % 70)*365) +  (fark.getMonth()%30) + (fark.getDate()-1);
        fark_normalgosterimi++;
            //en buyuk ve en kucuku ekledik
        int toplam_soru=0;

        for(Ogrenci ogrenci:ogrenciList){

            toplam_soru=toplam_soru+ogrenci.getSoru();
        }

        int ortalama_soru=toplam_soru/fark_normalgosterimi;

        if (ortalama_soru>=100)
        {
            tv.setText("Tebrikler! Günlük 100 soru hedefini aştınız. \n Toplam çözülen soru sayisi: "+toplam_soru
                     +"\nGünlük ortalama soru sayisi "+ortalama_soru);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.parseColor("#123456"));
        }else{
            tv.setText("Maalesef günlük 100 soru hedefinizi gerçekleştiremediniz. \n Toplam çözülen soru sayisi: "+toplam_soru
                    +"\nGünlük ortalama soru sayisi "+ortalama_soru);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.RED);
        }







        //------------------------------------------------------------------------------------------

        for(final Ogrenci ogrenci:ogrenciList){

            TableRow satir= new TableRow(getApplicationContext());
            satir.setGravity(Gravity.CENTER);
            //satir.setOrientation(TableRow.HORIZONTAL);

            TextView tv_tarih=new TextView(getApplicationContext());
            tv_tarih.setPadding(2, 2, 2, 2);
            tv_tarih.setTextColor(Color.WHITE);

            SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
            Date date=new Date(ogrenci.getTarih());
            tv_tarih.setText(df.format(date) + "   ");


            TextView tv_ders=new TextView(getApplicationContext());
            tv_ders.setPadding(2, 2, 2, 2);
            tv_ders.setTextColor(Color.WHITE);
            tv_ders.setText(ogrenci.getDers() + "   ");

            TextView tv_soru=new TextView(getApplicationContext());
            tv_soru.setPadding(2, 2, 2, 2);
            tv_soru.setTextColor(Color.WHITE);
            tv_soru.setText(String.valueOf(ogrenci.getSoru()));

            satir.addView(tv_tarih);
            satir.addView(tv_ders);
            satir.addView(tv_soru);

            tablo.addView(satir);



            satir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    id=ogrenci.getId();

                    if(actionMode!=null){
                        return false;
                    }
                    MyActionModeCallback callback=new MyActionModeCallback();
                    actionMode=startActionMode(callback);
                    v.setSelected(true);

                    return true;
                }
            });




        }

    }
    private void paylasMesaj(CharSequence mesaj) {

        Intent paylasIntent=new Intent(Intent.ACTION_SEND);
        paylasIntent.setType("text/plain");
        paylasIntent.putExtra(Intent.EXTRA_TEXT,mesaj);
        startActivity(Intent.createChooser(paylasIntent,"Paylaşın!!!"));

    }

    class MyActionModeCallback implements ActionMode.Callback
    {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()){

                case R.id.sil:

                    Veritabani db=new Veritabani(getApplicationContext());
                    db.Sil(id);
                    Listele();
                    mode.finish();
                    return true;

                case R.id.duzenle:

                    mode.finish();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            actionMode=null;
        }
    }

}
