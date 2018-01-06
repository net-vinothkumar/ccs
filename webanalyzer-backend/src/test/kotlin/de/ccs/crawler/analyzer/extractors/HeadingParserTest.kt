package de.ccs.crawler.analyzer.extractors

import de.ccs.crawler.domain.HeadingTag
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HeadingParserTest {

    @Test
    @Throws(Exception::class)
    fun `Should extract headings from web page`() {

        val html = IOUtils.toString(javaClass.classLoader.getResourceAsStream("testwebpage.html"))

        val actualHeadings = HeadingsExtractor.getNumberOfHeadingsByGroup(Jsoup.parse(html))

        val expectedHeadings = listOf(HeadingTag(headerName = "H1: ", count = 1),
                                      HeadingTag(headerName = "H2: ", count = 2),
                                      HeadingTag(headerName = "H3: ", count = 1))

        assertTrue(actualHeadings.size == 3)
        assertEquals(expectedHeadings, actualHeadings)
    }
}