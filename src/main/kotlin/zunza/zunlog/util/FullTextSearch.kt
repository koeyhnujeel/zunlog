package zunza.zunlog.util

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath

class FullTextSearch {

    companion object {
        fun match(column: StringPath, value: String): BooleanExpression? {
            if (value.isEmpty()) return null

            return Expressions.numberTemplate(
                java.lang.Double::class.java,
                "FUNCTION('MATCH_AGAINST', {0}, {1})",
                column,
                value.trim().split(" ").joinToString(" ") { "+$it" }
            ).gt(0)
        }
    }
}