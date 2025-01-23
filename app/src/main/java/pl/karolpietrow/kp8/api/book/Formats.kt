package pl.karolpietrow.kp8.api.book

import com.google.gson.annotations.SerializedName

data class Formats(
    @SerializedName("application/epub+zip") val epubZip: String = "",
    @SerializedName("application/octet-stream") val octetStream: String = "",
    @SerializedName("application/rdf+xml") val rdfXml: String = "",
    @SerializedName("application/x-mobipocket-ebook") val xMobipocketEbook: String = "",
    @SerializedName("image/jpeg") val imageJpeg: String = "",
    @SerializedName("text/html") val textHtml: String = "",
    @SerializedName("text/plain; charset=us-ascii") val textPlain: String = ""
)