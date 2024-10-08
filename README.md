# zunlog

#### 코틀린을 경험해보고자 게시판을 주제로 프로젝트를 진행하였습니다. 
<br>

## 1️⃣ ERD
<img src="https://github.com/user-attachments/assets/705bfc8e-db65-4843-a31f-5f327763dabf" width="500" height="500" />
<br> <br>

## 2️⃣ 구현 기능
1. 회원가입 / 로그인
2. 게시글 CRUD
3. 댓글 CRUD
4. 좋아요
5. 구독
6. 알림
<br>

## 3️⃣ 주요 구현 내용
### [EventListner와 비동기]

ApplicationEventPublisher를 통해 글 작성, 댓글 작성, 좋아요와 같은 이벤트를 발행하면, 이벤트 리스너는 해당 알림 서비스의 메서드를 호출합니다.<br>
이때, @EventListener로 등록된 리스너는 기본적으로 동기적으로 작동하므로, 글 작성 서비스는 알림 서비스의 처리가 완료될 때까지 대기하게 됩니다.<br>
이를 해결하기 위해서 리스너에 @Async 어노테이션을 사용하여 비동기적으로 작동하도록 했습니다.

```
@Async
@EventListener
fun notifyHandler(customEvent: CustomEvent) {
    when (customEvent.getType()) {
        "post" -> notificationService.postNotify(customEvent)
        "comment" -> notificationService.commentAndLikeNotify(customEvent)
        "like" -> notificationService.commentAndLikeNotify(customEvent)
    }
}
```

비동기 처리는 되었지만 문제가 또 있었습니다.<br>
@Async는 기본적으로 SimpleAsyncTaskExecutor를 사용하기 때문에 매번 새로운 스레드를 생성하고 작업이 끝나면 삭제하게 됩니다.<br>
이를 해결하기 위해서 스레드 풀을 제공하는 ThreadPoolTaskExecutor를 사용하여 스레드를 재사용하도록 했습니다.

```
@Bean
fun executor(): ThreadPoolTaskExecutor {
    return ThreadPoolTaskExecutor().apply {
        corePoolSize = 5
        maxPoolSize = 10
        queueCapacity = 20
        setThreadNamePrefix("custom-pool-")
        initialize()
    }
}
```

### [페이징]

