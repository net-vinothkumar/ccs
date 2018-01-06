package de.ccs.crawler.analyzer

import com.google.common.collect.Lists
import de.ccs.crawler.domain.AnalyzedWebPageDetails
import de.ccs.crawler.domain.ResourceDetails
import de.ccs.crawler.domain.WebPageDetail
import de.ccs.crawler.analyzer.extractors.*
import de.ccs.crawler.analyzer.verifier.WebPageVerifier
import de.ccs.crawler.domain.LinkStatus
import org.apache.commons.logging.LogFactory
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask


@Service
class WebPageAnalyzer : Analyzer {

    private val log = LogFactory.getLog(WebPageAnalyzer::class.java)

    @Value("\${webpage.analyzer.verifylinks.size}")
    lateinit var VERIFY_LINKS_SIZE: String

    @Value("\${webpage.analyzer.linkconnection.timeout}")
    lateinit var WEB_PAGE_LINK_TIMEOUT: String

    override fun analyze(webPageDetail: WebPageDetail): AnalyzedWebPageDetails {

        try {
            val doc = Jsoup.connect(webPageDetail.url).get()

            val hyperLinksCollection = HyperLinksExtractor.getHyperLinksCollection(doc)
            val numberOfLinks = HyperLinksExtractor.getNumberOfHyperMediaLinks(doc, hyperLinksCollection)

            return AnalyzedWebPageDetails(uri = webPageDetail.url, htmlVersion = WebPageExtractor.getHtmlDocumentType(doc),
                    title = WebPageTitleExtractor.getTitle(doc),
                    headingTags = HeadingsExtractor.getNumberOfHeadingsByGroup(doc),
                    numOfInternalLinks = numberOfLinks[0],
                    numOfExternalLinks = numberOfLinks[1],
                    containsLoginForm = LoginFormScanner.containsLoginForm(doc))

        } catch (e: IOException) {
            log.error(e)
        }
        return AnalyzedWebPageDetails()
    }

    override fun getVerifiedLinksDetails(webPageDetail: WebPageDetail): List<ResourceDetails> {
        try {
            val doc = Jsoup.connect(webPageDetail.url).timeout(Integer.valueOf(WEB_PAGE_LINK_TIMEOUT)).get()
            val hyperLinksCollection = HyperLinksExtractor.getHyperLinksCollection(doc)
            val remainingLinks = hyperLinksCollection.drop(webPageDetail.startRecord).toSet()
            return verifyWepPageLinks(remainingLinks.take(10).toSet())
        } catch (e: IOException) {
            log.error(e)
        }
        return listOf()
    }

    fun verifyWepPageLinks(resourceList: Set<String>): List<ResourceDetails> {

        val resourceValidationMap = HashMap<String, LinkStatus>()

        val numberOfThread = 9
        val threadPool = Executors.newFixedThreadPool(numberOfThread)

        val futureTaskList = ArrayList<FutureTask<Map<String, LinkStatus>>>()

        val subSets = Lists.partition(resourceList.take(Integer.valueOf(VERIFY_LINKS_SIZE)), numberOfThread)

        for (subList in subSets) {
            val task = WebPageVerifier(subList)
            val futureTask = FutureTask<Map<String, LinkStatus>>(task)
            threadPool.submit(futureTask)
            futureTaskList.add(futureTask)
        }

        prepareWebPageVerificationResults(futureTaskList, resourceValidationMap)

        threadPool.shutdown()

        return resourceValidationMap.
                map { it -> ResourceDetails(link = it.key,
                                            webPageStatus = it.value.status,
                                            httpResponseCode = it.value.httpResponseStatusCode) }
                .toList()
    }

    private fun prepareWebPageVerificationResults(futureTaskList: ArrayList<FutureTask<Map<String, LinkStatus>>>, resourceValidationMap: HashMap<String, LinkStatus>) {
        for (completeFutureTask in futureTaskList) {
            try {
                resourceValidationMap.putAll(completeFutureTask.get())
            } catch (e: InterruptedException) {
                log.error(e)
            } catch (e: ExecutionException) {
                log.error(e)
            }
        }
    }
}

