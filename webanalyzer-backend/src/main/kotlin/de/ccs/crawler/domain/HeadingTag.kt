package de.ccs.crawler.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("headerName", "count")
data class HeadingTag(val headerName : String? = null, val count : Int? = 0)
