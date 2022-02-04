package com.solvabit.phishingsmsdetector.models

data class Tweets(
    val data: List<Tweet>,
    val includes: Includes,
    val meta: Meta
)

data class Includes(
    val users: List<User>
)

data class Tweet(
    val text: String,
    val id: String,
    val author_id: String,
    val created_at: String
)

data class User(
    val id: String,
    val name: String,
    val username: String
)

data class Meta(
    val newest_id: String,
    val oldest_id: String,
    val result_count: Int,
    val next_token: String
)