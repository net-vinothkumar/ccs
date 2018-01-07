package de.ccs.crawler.validator

import org.apache.commons.validator.routines.UrlValidator

class ValidatorUtil {
    companion object {
        fun validateURL(url : String) : Boolean {

            val schemes = arrayOf(
                    "http",
                    "https"
            )

            val urlValidator = UrlValidator(schemes)
            return urlValidator.isValid(url)
        }
    }
}