package com.solvabit.phishingsmsdetector.models

import android.graphics.pdf.PdfDocument

data class YoutubeData(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: PageInfo,
    val items: List<Items>

)

data class PageInfo(
    val totalResults: Long,
    val resultsPerPage: Int
)

data class Items(
    val kind: String,
    val etag: String,
    val id: Id,
    val snippet: Snippet

)

data class Id(
    val kind: String,
    val videoId: String
)

data class Snippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String
)

data class Thumbnails(
    val default: Default,
    val medium: Default,
    val high: Default
)

data class Default(
    val url: String,
    val width: Int,
    val height: Int
)


