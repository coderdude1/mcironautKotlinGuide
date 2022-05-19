# 14 May 2022

1. DONE - Verify log statements include threadid
2. add some log statements to document thread in controller/services/etc.
3. identify names of thread micronaut uses
    1. event loop/request listener
    2. worker threads
    3. others
4. put pauses and such to cause micronaut to spin up worker threads? what is the behaviour of this
5. experiment with the annotations and (kotlin) suspend and it's effect of unblcoked io
6. add new controller to do async experiments.
7. experiment with traditional java per file for everything vs kotlin (ie controller, request/response in a sintlge
   file, etc)