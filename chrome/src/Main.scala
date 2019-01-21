package jp.kenichi.block.chrome.ext

import scala.scalajs.js, js.annotation.JSGlobal

import org.scalajs.dom

// https://github.com/lucidd/scala-js-chrome could be used
@JSGlobal("chrome") @js.native object chrome extends js.Object {
	@js.native object runtime extends js.Object {
		@js.native object onInstalled extends js.Object {
			def addListener(callback: js.Function1[js.Dictionary[js.Any], _]): Unit = js.native
		}
		@js.native object onSuspend extends js.Object {
			def addListener(callback: js.Function0[_]): Unit = js.native // details
		}
	}

	@js.native object webRequest extends js.Object {
		@js.native object onBeforeRequest extends js.Object {
			def addListener(callback: js.Function1[Details, BlockingResponse],
				filter: js.Dictionary[js.Any],
				extraInfo: js.Array[String] = js.Array[String]()): Unit = js.native

			@js.native class Details extends js.Object {
				val requestId: String = js.native
				val url: String = js.native
				val method: String = js.native
				val frameId: Int = js.native
				val parentFrameId: Int = js.native
				// TODO: requestBody
				val tabId: Int = js.native
				val `type`: String = js.native
				val initiator: js.UndefOr[String] = js.native
				val timeStamp: Double = js.native
			}
		}
	}
}

		// TODO: put this under chrome.webRequest
		class BlockingResponse extends js.Object {
			var cancel: js.UndefOr[Boolean] = js.undefined
			// TODO: redirectUrl
			// TODO: requestHeaders
			// TODO: responseHeaders
			// TODO: authCredentials
		}

object Main {
	val log = getLogger

	def main(args: Array[String]) {
		chrome.runtime.onInstalled.addListener { _ =>
			log.debug(s"onInstalled")

			chrome.webRequest.onBeforeRequest.addListener({ details =>
				// a request is considered as cancelled if at least one extension instructs to cancel the request
				log.debug(s"onBeforeRequest: ${details.method} ${details.url} ${details.`type`}")
				val resp = new /*chrome.webRequest.*/BlockingResponse
				if (details.url.startsWith("https://wrs21.winshipway.com/"))
					resp.cancel = true
				resp
			}, js.Dictionary("urls" -> js.Array("<all_urls>")),
			js.Array("blocking"))
		}
		chrome.runtime.onSuspend.addListener { () =>
			log.debug(s"onSuspend")
		}
	}
}
