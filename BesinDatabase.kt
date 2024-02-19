﻿package com.example.besinuygulamasi.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.besinuygulamasi.Model.Besin


@Database(entities = arrayOf(Besin::class), version = 1)
//olusturdugumuz tabolari bir dizi halinde istiyor
//veritabaninda bir degisiklik olur ise versiyon degistirilir

abstract class BesinDatabase : RoomDatabase() {
    //bir veritabanina farkli tredlerden ulasilabilir ve sistemi cökertebilir

    abstract fun besinDAO() : BesinDAO
    //olusturdugumuz DAO yu getiriyoruz

    //simdi olusturdugumuz sinifi kullanarak database yi olusturalim

    // ayni anda birden fazla trad lerden ulasinca sorun olmasin diye veritabanini
    //singelton olarak yapiyoruz, böylece farkli trad lerden sadece bir objeye ulasilir
    //singelton sinifini companion object yapisi ile olusturuyoruz

    companion object{
        //@Volatile yi ekler isek olusturdugumuz intance farkli tradler de de görünür hale geiyor
     @Volatile private  var instance: BesinDatabase? = null

        private  val lock = Any()// burda ki kilidimizi denetlemek icin olusturuldu
        operator  fun invoke(context: Context) = instance ?: synchronized(lock){
            //instance ?: Atesle demek,islemleri senkronize oarak yapar
            //eger bu instance den bir obje olusturulmus ise islemi senkronize calistirir
            //olusturulmadi ise calistirma
            //instance ?: demek eger daha önce BesinDatabase nesnesi ise direk bunu döndür
            //olusturulmamis ise synchronized() baslat

            //eger database olusturulmamis ise olustur
            instance ?: databaseOlustur(context).also {
                instance = it

            }

        }


        private fun databaseOlustur(context : Context) = Room.
        databaseBuilder(context.applicationContext,
            BesinDatabase::class.java,
            name = "besinDatabase")
            .build()

    }
}
