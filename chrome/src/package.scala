package jp.kenichi.block.chrome

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import scala.scalajs.js
import org.scalajs.dom

package object ext {
	/*
		Simple replacement for org.log4s.getLogger

		log4s-1.6.1 suffers https://github.com/Log4s/log4s/issues/23
		global `Error` causes `TypeError: Right-hand side of 'instanceof' is not callable`
		on the following code from //kitchen.juicer.cc/?color=foo

			} catch (e) {
				e = e instanceof Error ? e : "load :: " + e,
				this.logging(e)
			}

		Also the timePart is always in UTC
		and the fraction separator is `,` rather than `.` unlike logback/log4j2
		as in `2019-01-03 09:20:26,505 INFO  {} package.class - message`
	*/
	def getLogger = new Logger("")

	val dynamicConsole = js.Dynamic.global.console
	def consoleDebug(message: String) = {
		if (!js.isUndefined(dynamicConsole.debug))
			dynamicConsole.debug(message)
		else
			dom.console.log(message)
	}
	// console.trace() could be used so that Scala source is shown in the stack trace but it doesn't take an exception

	class Logger(name: String) {
		val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

		def render(level: String, message: String) = s"${LocalDateTime.now.format(dateTimeFormatter)} $level $name - $message"

		def debug(message: String) {
			consoleDebug(render("DEBUG", message))
		}
		def debug(ex: Throwable)(message: String) {
			consoleDebug(render("DEBUG", s"$message\n$ex${ex.getStackTrace.mkString("\n\t", "\n\t", "")}"))
		}

		def info(message: String) {
			dom.console.info(render("INFO ", message))
		}
		def info(ex: Throwable)(message: String) {
			dom.console.info(render("INFO ", s"$message\n$ex${ex.getStackTrace.mkString("\n\t", "\n\t", "")}"))
		}

		def warn(message: String) {
			dom.console.warn(render("WARN ", message))
		}
		def warn(ex: Throwable)(message: String) {
			dom.console.warn(render("WARN ", s"$message\n$ex${ex.getStackTrace.mkString("\n\t", "\n\t", "")}"))
		}

		def error(message: String) {
			dom.console.error(render("ERROR", message))
		}
		def error(ex: Throwable)(message: String) {
			dom.console.error(render("ERROR", s"$message\n$ex${ex.getStackTrace.mkString("\n\t", "\n\t", "")}"))
		}
	}
}

