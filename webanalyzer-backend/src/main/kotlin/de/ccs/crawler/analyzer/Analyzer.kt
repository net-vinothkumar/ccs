package de.ccs.crawler.analyzer

import de.ccs.crawler.domain.AnalyzedWebPageDetails
import de.ccs.crawler.domain.ResourceDetails
import de.ccs.crawler.domain.WebPageDetail

interface Analyzer {
    fun analyze(webPageDetail : WebPageDetail) : AnalyzedWebPageDetails
    fun getVerifiedLinksDetails(webPageDetail: WebPageDetail): List<ResourceDetails>
}