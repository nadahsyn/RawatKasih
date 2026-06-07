package com.projekakhir.rawatkasih

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClient {

    init {
        println("SUPABASE URL = https://qocorphglexryurkzfcn.supabase.co")
    }

    val client = createSupabaseClient(
        supabaseUrl = "https://qocorphglexryurkzfcn.supabase.co",
        supabaseKey = "sb_publishable_zdZIfb_qqYQFzUeshR_jBA_vHRIgIac"
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
        install(Storage)
    }
}