package com.crm.corecrm.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

@Component
class RequestLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val wrappedRequest = CachingRequestWrapper(request)

        println("Incoming request:")
        println("Method: ${wrappedRequest.method}")
        println("URI: ${wrappedRequest.requestURI}")
        println("Headers:")
        wrappedRequest.headerNames.asIterator().forEachRemaining { headerName ->
            println("$headerName: ${wrappedRequest.getHeader(headerName)}")
        }

        if (wrappedRequest.method.equals("POST", ignoreCase = true) || wrappedRequest.method.equals("PUT", ignoreCase = true)) {
            val body = wrappedRequest.reader.use { it.readText() }
            println("Body: $body")
        }

        filterChain.doFilter(wrappedRequest, response)
    }
}

class CachingRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray = request.inputStream.readBytes()

    override fun getInputStream(): ServletInputStream {
        return CachedServletInputStream(cachedBody)
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(ByteArrayInputStream(cachedBody)))
    }

    private class CachedServletInputStream(private val cachedBody: ByteArray) : ServletInputStream() {
        private val inputStream = ByteArrayInputStream(cachedBody)

        override fun read(): Int = inputStream.read()

        override fun isFinished(): Boolean = inputStream.available() <= 0

        override fun isReady(): Boolean = true

        override fun setReadListener(readListener: ReadListener?) {
            // No-op
        }
    }
}