package zunza.zunlog.Enum

enum class IsRead {
    TRUE,
    FALSE,
}

enum class IsLike {
    TRUE,
    FALSE,
}

enum class CacheType(
    val cacheName: String,
    val expireAfterWrite: Long,
    val maximumSize: Long
) {
    TOTAL_POST_COUNT("totalPosts", 5, 1),
    SEARCHED_POST_COUNT("searchedPosts", 5, 1000)
}
