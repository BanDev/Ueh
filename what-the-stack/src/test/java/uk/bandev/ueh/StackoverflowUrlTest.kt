package uk.bandev.ueh

import org.junit.Test

internal class StackoverflowUrlTest {
    @Test
    fun shouldEncodeSearchStringCorrectly() {
        val searchQuery = "test search"
        val url = generateStackoverflowSearchUrl(searchQuery)
        assert(!url.contains(' '))
    }
}
