# slickqa-junit-jupiter
Slick Junit 5 (jupiter) connector

To post results to Slick you will need to provide some Java opts.

-Dslick.baseurl=&lt;Slick Instance Base URL&gt;
  
-Dslick.project=&lt;project to post to&gt;

-Dslick.release=<release to post to&gt;

-Dslick.build=<build to post to&gt;

-Dslick.testplan=<name of the testplan to use&gt;

-Dslick.scheduleTests=<tests are just scheduled, not run&gt;

-Dslick.resulturl=&lt;URL to single result&gt; *Single Test Mode.  Pass URL to be updated when a single test is run. A new result will never be created when in single    test mode.  Result URL must refer to a result with the same automation ID of the test being run.*

For example:
-Dslick.baseurl=http://slick.mycompany.com -Dslick.project=automation -Dslick.release=junit5 -Dslick.build=86-4pm-suite -Dslick.testplan=smoke-tests

