package de.ccs.crawler.analyzer.extractors

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.apache.commons.logging.LogFactory
import org.jsoup.nodes.Document
import java.net.MalformedURLException
import java.net.URL

class HyperLinksExtractor {

    companion object {

        private val log = LogFactory.getLog(HyperLinksExtractor::class.java)

        fun getHyperLinksCollection(document: Document): Set<String> {

            val hyperLinks = document.select("a[href]")
                    .map { it.attr("abs:href") }
                    .filter<String> { null != it }.toSet()

            return hyperLinks
        }

        fun getNumberOfHyperMediaLinks(document: Document, hyperlinkCollection: Set<String>): IntArray {

            val numberOfLinks = IntArray(2)
            var internalLinks = 0
            var externalLinks = 0

            try {
                val aURL = URL(document.baseUri())
                val domain = aURL.host

                hyperlinkCollection.forEach { link ->
                    if (link.contains(domain))
                        internalLinks++
                    else
                        externalLinks++
                }
                numberOfLinks[0] = internalLinks
                numberOfLinks[1] = externalLinks

            } catch (e: MalformedURLException) {
                log.error(e)
            }
            return numberOfLinks
        }
    }
}