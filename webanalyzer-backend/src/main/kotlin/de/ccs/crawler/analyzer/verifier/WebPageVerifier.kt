package de.ccs.crawler.analyzer.verifier

import de.ccs.crawler.domain.LinkStatus
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.Callable

class WebPageVerifier constructor(private val resourceList: List<String>) : Callable<Map<String, LinkStatus>> {

    private val log = LogFactory.getLog(WebPageVerifier::class.java)

    //TODO should be made configurable
    private val LINK_VERIFICATION_TIMEOUT = 1000

    private val resourceValidationMap = HashMap<String, LinkStatus>()

    override fun call(): Map<String, LinkStatus> {
        for (resource in resourceList) {
            val respCode: LinkStatus
            try {
                respCode = getResponseCode(resource, LINK_VERIFICATION_TIMEOUT)
                resourceValidationMap.put(resource, respCode)
            } catch (e: MalformedURLException) {
                log.error(e)
            } catch (e: IOException) {
                log.error(e)
            }
        }
        return resourceValidationMap
    }

    private fun getResponseCode(url: String, timeout: Int): LinkStatus {
        var url = url
        url = url.replaceFirst("^https".toRegex(), "http")
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = timeout
            connection.readTimeout = timeout
            connection.requestMethod = "HEAD"
            return LinkStatus(httpResponseStatusCode = connection.responseCode,
                    status = WEBPAGESTATUS.REACHABLE.toString())
        } catch (exception: IOException) {
            log.info("Error verifying link url: " + url)
        } catch (e: Exception) {
            log.info("Error verifying link url: " + url)
        }
        return LinkStatus(httpResponseStatusCode = HttpStatus.NOT_FOUND.value(),
                status = WEBPAGESTATUS.NOT_REACHABLE.toString())
    }

    enum class WEBPAGESTATUS {
        REACHABLE,
        NOT_REACHABLE
    }
}
