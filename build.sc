import mill._, scalalib._, scalajslib._

trait CommonScalaModule extends ScalaModule {
	def scalaVersion = "2.12.8"
	def scalacOptions = Seq(
		"-deprecation",
		"-feature",
		"-unchecked",
	)
}

object chrome extends ScalaJSModule with CommonScalaModule {
	def scalaJSVersion = "0.6.26"
	def scalacOptions = super.scalacOptions() ++ Seq(
		"-P:scalajs:sjsDefinedByDefault", // opt-in for the new semantics of Scala.js 1.x
	)
	def ivyDeps = Agg(
		//ivy"org.log4s::log4s::1.6.1", // log4s-1.6.1 suffers https://github.com/Log4s/log4s/issues/23
		ivy"org.scala-js::scalajs-dom::0.9.6",
		ivy"io.github.cquiroz::scala-java-time::2.0.0-RC1", // https://github.com/cquiroz/scala-java-time
		ivy"io.github.cquiroz::scala-java-time-tzdb::2.0.0-RC1_2018f", // avoid exception: Unknown time-zone ID: Asia/Tokyo
		/*
			cquiroz/scala-java-time is based on http://www.threeten.org/threetenbp/

			https://github.com/scala-js/scala-js-java-time is very incomplete and
			lacks LocalDateTime and DateTimeFormatter as of 2017-04:
			https://github.com/scala-js/scala-js-java-time/issues/7
		*/
	)
}

object dist extends Module {
	object chrome extends Module {
		object unpacked extends Module {
			def fastOpt = T { pack(build.chrome.fastOpt().path) }
			def fullOpt = T { pack(build.chrome.fullOpt().path) }
			def pack(jsPath: os.Path) {
				val outDir = os.pwd / "dist" / "chrome" / "unpacked"
				os.makeDir.all(outDir)

				val confDir = build.chrome.millSourcePath / "conf"
				for (file <- os.list.stream(confDir))
					if (!file.last.startsWith(".")) // ignore .DS_Store
						os.copy.over(file, outDir / file.relativeTo(confDir))
				os.copy.over(jsPath, outDir / "background.js")
			}
		}
	}
}
