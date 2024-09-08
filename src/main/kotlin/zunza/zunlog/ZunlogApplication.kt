package zunza.zunlog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZunlogApplication

fun main(args: Array<String>) {
    runApplication<ZunlogApplication>(*args)
}