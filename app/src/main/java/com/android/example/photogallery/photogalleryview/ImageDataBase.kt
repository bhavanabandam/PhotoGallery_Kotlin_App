package com.android.example.photogallery.photogalleryview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImageEntity::class], version = 1, exportSchema = false)
abstract class ImageDataBase : RoomDatabase() {
    abstract val imageDatabaseDao: ImageDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ImageDataBase? = null

        fun getInstance(context: Context): ImageDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImageDataBase::class.java,
                        "image_list_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance

            }
        }
    }

}