package de.ccs.crawler.analyzer.extractors

import org.jsoup.nodes.Document
import org.jsoup.nodes.DocumentType


class WebPageExtractor {

    companion object {

        fun getHtmlDocumentType(document: Document): String {

            val nods = document.childNodes()

            return nods
                        .filterIsInstance<DocumentType>()
                        .map { it }
                        .firstOrNull()
                        ?.toString()
                        ?: ""
        }

    }
}