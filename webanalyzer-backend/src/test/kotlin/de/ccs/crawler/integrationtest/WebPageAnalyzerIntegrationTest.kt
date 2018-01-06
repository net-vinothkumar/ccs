package de.ccs.crawler.integrationtest

import de.ccs.crawler.analyzer.verifier.WebPageVerifier.WEBPAGESTATUS.*
import de.ccs.crawler.domain.AnalyzedWebPageDetails
import de.ccs.crawler.domain.HeadingTag
import de.ccs.crawler.domain.ResourceDetails
import de.ccs.crawler.domain.WebPageDetail
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebPageAnalyzerIntegrationTest {
    @Value("\${local.server.port}")
    private val localServerPort: Int = 0

    var testRestTemplate: RestTemplate? = null

    @Before
    fun setUp() {
        testRestTemplate = RestTemplate()
    }

    @Test
    fun `Should analyze the given link and return web page details`() {

        //Given
        val webPageDetail = WebPageDetail(url = "http://www.google.com")

        val expectedAnalyzedWebDetails = AnalyzedWebPageDetails(uri = "http://www.google.com",
                htmlVersion = "<!doctype html>",
                title = "Google",
                headingTags = listOf(HeadingTag(headerName = "H1: ", count = 1)),
                numOfInternalLinks = 16,
                numOfExternalLinks = 21,
                containsLoginForm = false,
                resourceDetails = listOf())

        //When
        val analyzedWebpageResponse = testRestTemplate!!.postForEntity("http://localhost:$localServerPort/analyze", webPageDetail, AnalyzedWebPageDetails::class.java)

        //Then
        assertThat(analyzedWebpageResponse.statusCode).isEqualTo(OK)
        assertThat(analyzedWebpageResponse.body.uri).isEqualTo(expectedAnalyzedWebDetails.uri)
        assertThat(analyzedWebpageResponse.body.htmlVersion).isEqualTo(expectedAnalyzedWebDetails.htmlVersion)
        assertThat(analyzedWebpageResponse.body.title).isEqualTo(expectedAnalyzedWebDetails.title)
        assertThat(analyzedWebpageResponse.body.headingTags).isEqualTo(expectedAnalyzedWebDetails.headingTags)
        assertThat(analyzedWebpageResponse.body.numOfExternalLinks).isEqualTo(expectedAnalyzedWebDetails.numOfExternalLinks)
        assertThat(analyzedWebpageResponse.body.numOfInternalLinks).isEqualTo(expectedAnalyzedWebDetails.numOfInternalLinks)
        assertThat(analyzedWebpageResponse.body.containsLoginForm).isEqualTo(expectedAnalyzedWebDetails.containsLoginForm)
    }

    @Test
    fun `Should verify the links for the given web page and return results`() {

        //Given
        val url = "http://www.google.com"
        val startRecord = 0
        val limit = 10

        val expectedVerifiedWebPageResults = listOf(ResourceDetails(link = "https://www.google.de/intl/de_de/policies/privacy/?fg=1",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 200),
                ResourceDetails(link = "https://www.google.de/webhp?hl=de&dcr=0&sa=X&ved=0ahUKEwj0qfilrMbYAhXIESwKHVI7AXMQPAgD",
                        webPageStatus = REACHABLE.toString(), httpResponseCode = 200), ResourceDetails(link = "https://mail.google.com/mail/?tab=wm",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 302), ResourceDetails(link = "https://www.google.de/preferences?hl=de"
                , webPageStatus = REACHABLE.toString(), httpResponseCode = 301), ResourceDetails(link = "https://www.google.de/intl/de/options/"
                , webPageStatus = REACHABLE.toString(), httpResponseCode = 200), ResourceDetails(link = "https://www.google.de/imghp?hl=de&tab=wi",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 200), ResourceDetails(link = "https://www.google.de/intl/de_de/policies/terms/?fg=1",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 200), ResourceDetails(link = "https://support.google.com/websearch/answer/186645?hl=de",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 301), ResourceDetails(link = "https://www.google.de/?gfe_rd=cr&dcr=0&ei=r1RSWrS3A-uCX4_xsKAL&gws_rd=ssl#",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 200), ResourceDetails(link = "https://accounts.google.com/ServiceLogin?hl=de&passive=true&continue=https://www.google.de/%3Fgfe_rd%3Dcr%26dcr%3D0%26ei%3Dr1RSWrS3A-uCX4_xsKAL%26gws_rd%3Dssl",
                webPageStatus = REACHABLE.toString(), httpResponseCode = 302))

        //When
        val verifiedWebPageResults = testRestTemplate!!.exchange("http://localhost:$localServerPort/verifyLinks?url={url}&startRecord={startRecord}&limit={limit}", HttpMethod.GET,
                null, referenceType<List<ResourceDetails>>(), url, startRecord, limit)

//        val sortedActualList = verifiedWebPageResults.body.sortedWith(compareBy(ResourceDetails::link, ResourceDetails::webPageStatus, ResourceDetails::httpResponseCode))
//        val sortedexpectedList = expectedVerifiedWebPageResults.sortedWith(compareBy(ResourceDetails::link, ResourceDetails::webPageStatus, ResourceDetails::httpResponseCode))

        //Then
        assertThat(verifiedWebPageResults.statusCode).isEqualTo(OK)
        assertThat(expectedVerifiedWebPageResults.size.equals(verifiedWebPageResults.body.size)).isTrue()
//        assertThat(sortedexpectedList).isEqualTo(sortedActualList) //TODO need to investigate to verify the specific results.
    }

    private inline fun <reified T : Any> referenceType(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}
}