# birthday_slack_bot

[![Build Status](https://travis-ci.org/amcintosh/birthday_slack_bot.svg?branch=master)](https://travis-ci.org/amcintosh/birthday_slack_bot)

Parse our company birthday list and send out slack message for today's birthdays.

## Usage

Create a config file using `/resources/config.edn.sample` as a...well...sample.

By default birthday_slack_bot will look for load from `/resources/config.edn`, which is useful if using `lein run` or if it has been compiled with your configuration included, but otherwise you should use `-c FILE` to specify your configuration.

    $ java -jar birthday_slack_bot-0.1.0-standalone.jar -c /path/to/config.edn


## Options

```
  -c, --config FILE  Config File
  -v                 Logging Verbosity level
  -h, --help
```

Logs to console and to `./.birthday_slack_bot.log`. The log file will rotate with size. Default logging is `WARN`. You can turn up the logging with the verbosity flag (`-v`). For example:

    $ java -jar birthday_slack_bot-0.1.0-standalone.jar -v

will log at `INFO` and 

	$ java -jar birthday_slack_bot-0.1.0-standalone.jar -v

will log at `DEBUG`.
