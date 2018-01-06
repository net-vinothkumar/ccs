package de.ccs.crawler.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("url")
data class WebPageDetail(val url : String? = null,
                         val startRecord : Int = 0,
                         val limit : Int = 5)
