package de.ccs.crawler.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.ccs.crawler.analyzer.Analyzer
import de.ccs.crawler.analyzer.verifier.WebPageVerifier.WEBPAGESTATUS.*
import de.ccs.crawler.domain.AnalyzedWebPageDetails
import de.ccs.crawler.domain.HeadingTag
import de.ccs.crawler.domain.ResourceDetails
import de.ccs.crawler.domain.WebPageDetail
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class WebPageAnalyzeControllerTest {

    lateinit var mvc: MockMvc

    @Mock
    lateinit var analyzer : Analyzer

    @InjectMocks
    lateinit var webPageAnalyzeController : WebPageAnalyzeController

    @Before
    fun setUp() {
        initMocks(this)
        mvc = standaloneSetup(webPageAnalyzeController).build()
    }

    @Test
    @Throws(Exception::class)
    fun `Should analyze the given url and return web page details`() {

        //Given
        val webPageDetail = WebPageDetail(url = "http://www.test.com")

        val createdAnalyzedWebPageDetails = AnalyzedWebPageDetails(uri = "http://www.test.com",
                htmlVersion = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
                title = "test title",
                headingTags = listOf(HeadingTag(headerName = "H1: ", count = 1)),
                numOfExternalLinks = 1,
                numOfInternalLinks = 1,
                containsLoginForm = true)

        `given`(analyzer.analyze(webPageDetail)).willReturn(createdAnalyzedWebPageDetails)

        //When
        mvc.perform(post("/analyze").contentType(MediaType.APPLICATION_JSON_UTF8).
                content(asJsonString(webPageDetail))).
                andExpect(status().isOk).
                andExpect(content().json(asJsonString(createdAnalyzedWebPageDetails)))

        //Then
        `verify`(analyzer, times(1)).analyze(webPageDetail)
    }

    @Test
    @Throws(Exception::class)
    fun `Should verify the links for the given web page and return results`() {

        //Given
        val url = "http://www.test.com"
        val startRecord = 0
        val limit = 10
        val webPageDetail = WebPageDetail(url = url, startRecord = startRecord, limit = limit)

        val resourceDetail = ResourceDetails(link = "http://www.test.com/internallink",
                webPageStatus = REACHABLE.toString() , httpResponseCode = 200)

        val verifiedResourceDetails : List<ResourceDetails> = listOf(resourceDetail)

        `given`(analyzer.getVerifiedLinksDetails(webPageDetail)).willReturn(verifiedResourceDetails)

        //When
        mvc.perform(get("/verifyLinks?url={url}&startRecord={startRecord}&limit={limit}", url, startRecord, limit)).
                andExpect(status().isOk).
                andExpect(content().json(asJsonString(verifiedResourceDetails)))

        //Then
        `verify`(analyzer, times(1)).getVerifiedLinksDetails(webPageDetail)
    }

    private fun asJsonString(obj: Any): String {
        try {
            val objectMapper = ObjectMapper()
            return objectMapper.writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
