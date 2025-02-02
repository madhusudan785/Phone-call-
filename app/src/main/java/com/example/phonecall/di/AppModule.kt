package com.example.phonecall.di


import android.content.Context
import androidx.room.Room
import com.example.phonecall.callList.data.local.AppPreferences
import com.example.phonecall.callList.data.local.CallLogDao
import com.example.phonecall.callList.data.local.CallLogDatabase
import com.example.phonecall.callList.data.local.ContactDatabase
import com.example.phonecall.callList.repository.CallLogRepository
import com.example.phonecall.callList.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContactDatabase(@ApplicationContext context: Context): ContactDatabase {
        return ContactDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideContactRepository(database: ContactDatabase): ContactRepository {
        return ContactRepository(database.contactDao())
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }
    @Provides
    @Singleton
    fun provideCallLogDatabase(@ApplicationContext context: Context): CallLogDatabase {
        return Room.databaseBuilder(
            context,
            CallLogDatabase::class.java,
            "call_log_database"
        ).build()
    }

    // ✅ Provide CallLogDao
    @Provides
    @Singleton
    fun provideCallLogDao(database: CallLogDatabase): CallLogDao {
        return database.callLogDao()
    }

    // ✅ Provide CallLog Repository
    @Provides
    @Singleton
    fun provideCallLogRepository(database: CallLogDatabase): CallLogRepository {
        return CallLogRepository(database.callLogDao())
    }

}
