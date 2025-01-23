package pl.karolpietrow.kp8.api

import pl.karolpietrow.kp8.api.book.*
import pl.karolpietrow.kp8.api.search.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @GET("/books")
    suspend fun getBookBySearch(
        @Query("search") search: String
    ) : Response<SearchModel>

    @GET("/books/{id}")
    suspend fun getBookByID(
        @Path("id") id: String
    ): Response<BookModel>

}