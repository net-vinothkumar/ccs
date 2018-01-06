package de.ccs.crawler.analyzer.extractors

import de.ccs.crawler.domain.HeadingTag
import org.jsoup.nodes.Document

class HeadingsExtractor {

    companion object {

        fun getNumberOfHeadingsByGroup(document: Document): List<HeadingTag> {

            val headerTagsMap = LinkedHashMap<String, Int>()
            (1..6).forEach { i ->
                val numberOfHeadings = document.body().getElementsByTag("H$i").size
                if (numberOfHeadings > 0) {
                    headerTagsMap.put("H$i: ", numberOfHeadings)
                }
            }
            return headerTagsMap.map { headerTag -> HeadingTag(headerName = headerTag.key, count = headerTag.value) }.toList()
        }
    }
}