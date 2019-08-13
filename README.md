# slickqa-junit-jupiter
Slick Junit 5 (jupiter) connector

To post results to Slick you will need to provide some Java opts.
-Dslick.baseurl=<Slick Instance Base URL>
-Dslick.project=<project to post to>
-Dslick.release=<release to post to>
-Dslick.build=<build to post to>
-Dslick.testplan=<name of the testplan to use>
-Dslick.scheduleTests=<tests are just scheduled, not run>
-Dslick.resulturl=<Single Test Mode.  Pass URL to be updated when a single test is run. A new result will never be created when in single test mode.  Result URL must refer to a result with the same automation ID of the test being run.>

For example:
-Dslick.baseurl=http://slick.mycompany.com -Dslick.project=automation -Dslick.release=junit4 -Dslick.build=86-4pm-suite

