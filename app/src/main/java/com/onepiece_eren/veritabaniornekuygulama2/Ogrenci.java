package com.onepiece_eren.veritabaniornekuygulama2;

/**
 * Created by Teyfik on 11.12.2016.
 */
public class Ogrenci {

    private String Ders;
    private int Soru;
    private long Tarih;
    //milisaniye cinsinden kaydedilir tarih
    private long Id;

    public Ogrenci() {
    }

    public Ogrenci( String ders, int soru, long tarih) {
        setTarih(tarih);
        setDers(ders);
        setSoru(soru);
    }

    public void setId(long id){Id =id;}
    public Long getId(){return Id;}

    public String getDers() {
        return Ders;
    }

    public void setDers(String ders) {
        Ders = ders;
    }

    public int getSoru() {
        return Soru;
    }

    public void setSoru(int soru) {
        Soru = soru;
    }

    public long getTarih() {
        return Tarih;
    }

    public void setTarih(long tarih) {
        Tarih = tarih;
    }
}
