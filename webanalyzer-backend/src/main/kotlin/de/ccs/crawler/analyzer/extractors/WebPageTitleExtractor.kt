package de.ccs.crawler.analyzer.extractors

import org.jsoup.nodes.Document

class WebPageTitleExtractor {
    companion object {
        fun getTitle(document: Document): String {
            if (document.title() == null) return "Title is not set"
            return document.title()
        }
    }
}