package com.ttknpdev.log
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Log4j(c: Class<Any>) {
    val logBack : Logger = LoggerFactory.getLogger(c)
}
