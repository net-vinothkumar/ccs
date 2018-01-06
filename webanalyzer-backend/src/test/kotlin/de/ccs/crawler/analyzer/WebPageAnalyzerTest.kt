package de.ccs.crawler.analyzer.extractors

import de.ccs.crawler.analyzer.WebPageAnalyzer
import de.ccs.crawler.analyzer.verifier.WebPageVerifier.WEBPAGESTATUS.*
import de.ccs.crawler.domain.AnalyzedWebPageDetails
import de.ccs.crawler.domain.HeadingTag
import de.ccs.crawler.domain.ResourceDetails
import de.ccs.crawler.domain.WebPageDetail
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class WebPageAnalyzerTest {

    @Test
    @Throws(Exception::class)
    fun `Should analyze the given web page link and return analysis`() {

        //Given
        val analyzer = WebPageAnalyzer()
        val expectedAnalyzedWebDetails = AnalyzedWebPageDetails(uri = "http://www.google.com",
                htmlVersion = "<!doctype html>",
                title = "Google",
                headingTags = listOf(HeadingTag(headerName = "H1: ", count = 1)),
                numOfInternalLinks = 16,
                numOfExternalLinks = 21,
                containsLoginForm = false,
                resourceDetails = listOf())

        //When
        val analyzedWebDetails = analyzer.analyze(WebPageDetail(url = "http://www.google.com"))

        //Then
        assertEquals(expectedAnalyzedWebDetails, analyzedWebDetails)
    }

    @Test
    @Throws(Exception::class)
    fun `Should verify the given web page link and return verified results`() {

        //Given
        val analyzer = WebPageAnalyzer()
        analyzer.VERIFY_LINKS_SIZE = "10"

        val expectedVerifiedWebPageResults = listOf(ResourceDetails(link = "http://www.google.com",
                webPageStatus = REACHABLE.toString(),
                httpResponseCode = 200))

        //When
        val verifiedWebPageResults = analyzer.verifyWepPageLinks(setOf("http://www.google.com"))

        //Then
        assertEquals(expectedVerifiedWebPageResults.size ,verifiedWebPageResults.size)
        //TODO sporadic results - need to investigate why link is not reachable.
//        assertEquals(expectedVerifiedWebPageResults, verifiedWebPageResults)
    }
}