package de.ccs.crawler.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.HashMap

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "htmlVersion", "title", "headingTags", "numberOfInternalLinks", "numberOfExternalLinks", "containsLoginForm", "resourceDetails")
data class AnalyzedWebPageDetails(
        val uri: String? = null,
        val htmlVersion: String? = null,
        val title: String? = null,
        val headingTags : List<HeadingTag> = listOf(),
        val numOfInternalLinks: Int = 0,
        val numOfExternalLinks: Int = 0,
        val containsLoginForm: Boolean = false,
        val resourceDetails: List<ResourceDetails> = listOf()
)