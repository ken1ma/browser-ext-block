# Server Environment

1. Java 11.0.2


# Client Environment

1. Chrome 71.0.3578.98 on macOS 10.14.2


# Development Environment

In addition to the server environment above
1. [mill](http://www.lihaoyi.com/mill/) 0.3.5
	1. On macOS `brew install mill`


# Frequently Used Development Commands

1. Generate JavaScript fast

		mill dist.chrome.unpacked.fastOpt

	1. Open `chrome://extensions/`
	2. Enable `Developer mode`
	3. `Load unpacked` `dist/chrome/unpacked` directory
