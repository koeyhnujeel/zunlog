package zunza.zunlog.scheduler

import org.springframework.stereotype.Component

@Component
class PageInfo(
    totalElements: Long = 0
) {
    var totalElements = totalElements
        protected set

    fun updatedTotalElements(totalElements: Long) {
        this.totalElements = totalElements
    }
}