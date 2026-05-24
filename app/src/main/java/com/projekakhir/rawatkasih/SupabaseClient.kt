package com.projekakhir.rawatkasih

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://yrnvshekvmkjehfeoocc.supabase.co/rest/v1/",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlybnZzaGVrdm1ramVoZmVvb2NjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzk2MTM5OTIsImV4cCI6MjA5NTE4OTk5Mn0.aeDq7LxQfwLzV9AS5QqUvXpSDdQuf2UBRMldbccs7pM"
    ) {
        install(Postgrest)
    }
}