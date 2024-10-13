package zunza.zunlog.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import zunza.zunlog.repository.PostRepository

@Component
class PageUpdateScheduler(
    private val pageInfo: PageInfo,
    private val postRepository: PostRepository
) {

//    @Scheduled(fixedRate = 300000)
    fun updateTotalElements() {
        val totalElements = postRepository.count()
        pageInfo.updatedTotalElements(totalElements)
    }
}