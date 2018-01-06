package de.ccs.crawler.validator

import de.ccs.crawler.domain.WebPageDetail
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.apache.commons.validator.routines.UrlValidator;

class URLValidator : Validator {

    override fun supports(clazz: Class<*>?) = WebPageDetail::class.java == clazz

    override fun validate(target: Any?, errors: Errors?) {
        val webPageDetail = target as WebPageDetail

        if(webPageDetail.url != null && !ValidatorUtil.validateURL(webPageDetail.url)){
            errors!!.rejectValue("url", "url.invalid")
        }
    }
}