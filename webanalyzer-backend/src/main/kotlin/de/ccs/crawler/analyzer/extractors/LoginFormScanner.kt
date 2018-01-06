package de.ccs.crawler.analyzer.extractors

import org.jsoup.nodes.Document
import org.jsoup.nodes.FormElement

class LoginFormScanner {

    companion object {

        fun containsLoginForm(document: Document): Boolean {
            val forms = document.getElementsByTag("form")
            return forms.forms()
                    .filter(this::formElementContainsLogin)
                    .any()
        }

        private fun formElementContainsLogin(it: FormElement): Boolean {
            val texts = it.select("input[type=text]").size
            val emails = it.select("input[type=email]").size
            val passwords = it.select("input[type=password]").size
            val submits = it.select("input[type=submit]").size
            val buttons = it.select("button[type=submit]").size

            return (texts == 1 || emails == 1) && passwords == 1 && (submits > 0 || buttons > 0)
        }
    }
}