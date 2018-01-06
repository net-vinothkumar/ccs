package de.ccs.crawler.controller

import de.ccs.crawler.analyzer.Analyzer
import de.ccs.crawler.domain.AnalyzedWebPageDetails
import de.ccs.crawler.domain.ResourceDetails
import de.ccs.crawler.domain.WebPageDetail
import de.ccs.crawler.validator.URLValidator
import de.ccs.crawler.validator.ValidatorUtil
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping
class WebPageAnalyzeController @Autowired constructor(private val analyzer: Analyzer) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.validator = URLValidator()
    }

    @PostMapping(value = "/analyze", consumes = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun analyzeWebPage(@RequestBody @Valid webPageDetail: WebPageDetail): ResponseEntity<AnalyzedWebPageDetails> {
        return ResponseEntity(analyzer.analyze(webPageDetail), OK)
    }

    @GetMapping(value = "/verifyLinks")
    fun verifyLinks(@RequestParam("url") url: String, @RequestParam("startRecord") startRecord: Int, @RequestParam("limit") limit: Int): ResponseEntity<List<ResourceDetails>> {
        if (!ValidatorUtil.validateURL(url)) {
            throw IllegalArgumentException("Not a valid url.")
        }
        return ResponseEntity(analyzer.getVerifiedLinksDetails(WebPageDetail(url = url, startRecord = startRecord, limit = limit)), OK)
    }
}
