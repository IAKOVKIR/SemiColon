package com.example.semicolon.sqlite_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao
import com.example.semicolon.support_features.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A database that stores SleepNight information.
 * And a global method to get access to the database.
 *
 * This pattern is pretty much the same for any database,
 * so you can reuse it.
 */
@Database(entities = [User::class, Follower::class], version = 1,  exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAOs.
     */
    abstract val userDao: UserDao
    abstract val followerDao: FollowerDao

    /**
     * Define a companion object, this allows us to add functions on the SleepDatabase class.
     *
     * For example, clients can call `SleepDatabase.getInstance(context)` to instantiate
     * a new SleepDatabase.
     */
    companion object {

        val time = Time()
        private val userArray = arrayOf(User(1, "mr_query", "0490506763", "12345678", "Chandra MrQuery",
            "I'll still fuck your bitch", "MrStealYourQuery@gmail.com", 5.0F, time.toString(), time.toString()
        ), User(2, "ice_wallow", "0490000000", "12345678", "Abbas Ice-Wallow", "I'm better than my father",
            "BetterThanFather@gmail.com", 2.8F, time.toString(), time.toString()
        ), User(3, "karama", "0490000001", "12345678", "Carl Karama", "Mr.President",
            "CallMePresident@gmail.com", 5.0F, time.toString(), time.toString()
        ), User(4, "merin", "0490000002", "12345678", "Matt Merin", "Always on the beat",
            "MattMerinOnTheBeat@gmail.com", 5.0F, time.toString(), time.toString()
        ), User(5, "ay_sir", "0490000003", "12345678", "John Carlos", "Aye, sir",
            "JohnCarlos@gmail.com", 5.0F, time.toString(), time.toString()
        ), User(6, "beverly", "0490000004", "12345678", "Patrick Beverly", "PB", "beverly@gmail.com",
            5.0F, time.toString(), time.toString()
        ), User(7, "dwade", "0490000005", "12345678", "Dwyane Wade", "flash", "wade@gmail.com",
            5.0F, time.toString(), time.toString()
        ), User(8, "phelps", "0490000006", "12345678", "Michael Phelps", "splash", "phelps@gmail.com",
            5.0F, time.toString(), time.toString()
        ), User(9, "putin", "0490000007", "12345678", "Vladimir Putin", "King of the Vodkaland", "putin@kgb.com",
            5.0F, time.toString(), time.toString()
        ), User(10, "harden13", "0490000008", "12345678", "James Harden", "The Beard",
            "jharden@gmail.com", 5.0F, time.toString(), time.toString()
        ), User(11, "king_james", "0490000009", "12345678", "LeBron James", "King James",
            "KingJames@gmail.com", 5.0F, time.toString(), time.toString()
        ))

        //test entries for FRIEND table
        private val followerArray = arrayOf(
        Follower(1, 1, 2, 0, time.toString(), time.toString()),
        Follower(2, 1, 3, 1, time.toString(), time.toString()),
            Follower(3, 2, 3, 0, time.toString(), time.toString()),
        Follower(4, 3, 1, 1, time.toString(), time.toString()),
        Follower(5, 1, 4, 1, time.toString(), time.toString()),
        Follower(6, 2, 4, 1, time.toString(), time.toString()),
        Follower(7, 3, 4, 0, time.toString(), time.toString()),
        Follower(8, 4, 3, 0, time.toString(), time.toString()),
        Follower(9, 5, 3, 0, time.toString(), time.toString()),
        Follower(10, 6, 3, 0, time.toString(), time.toString()),
        Follower(11, 7, 3, 0, time.toString(), time.toString()),
        Follower(12, 8, 3, 0, time.toString(), time.toString()),
        Follower(13, 9, 3, 0, time.toString(), time.toString()),
        Follower(14, 10, 3, 0, time.toString(), time.toString()),
        Follower(15, 11, 3, 0, time.toString(), time.toString())
        )

        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "sleep_history_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .addCallback(DbCallback(scope))
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }

        private class DbCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDb(database.userDao, database.followerDao)
                    }
                }
            }
        }

        fun populateDb(userDao: UserDao, followerDao: FollowerDao) {
            for (i in userArray)
                userDao.insert(i)

            for (i in followerArray)
                followerDao.insert(i)
        }
    }
}