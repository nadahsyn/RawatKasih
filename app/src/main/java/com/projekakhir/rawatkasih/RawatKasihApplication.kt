package com.projekakhir.rawatkasih

import android.app.Application
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.data.local.RawatKasihDatabase

class RawatKasihApplication : Application() {
    val database: RawatKasihDatabase by lazy { RawatKasihDatabase.getDatabase(this) }
    val repository: RawatKasihRepository by lazy { RawatKasihRepository(database.dao()) }
}
