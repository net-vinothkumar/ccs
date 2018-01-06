package de.ccs.crawler.analyzer.extractors

import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginFormScannerTest {

    @Test
    @Throws(Exception::class)
    fun `Should contain the login form`() {
        val html = IOUtils.toString(javaClass.classLoader.getResourceAsStream("testloginform.html"))
        assertTrue(LoginFormScanner.containsLoginForm((Jsoup.parse(html))))
    }

    @Test
    @Throws(Exception::class)
    fun `Should not contain the login form`() {
        val html = IOUtils.toString(javaClass.classLoader.getResourceAsStream("testwebpage.html"))
        assertFalse(LoginFormScanner.containsLoginForm(Jsoup.parse(html)))
    }
}