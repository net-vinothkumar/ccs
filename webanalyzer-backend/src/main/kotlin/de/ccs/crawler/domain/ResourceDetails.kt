package de.ccs.crawler.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import de.ccs.crawler.analyzer.verifier.WebPageVerifier

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("link", "webPageStatus", "httpResponseCode")
data class ResourceDetails(val link: String? = null,
                           val webPageStatus: String? = null,
                           val httpResponseCode: Int = 0)