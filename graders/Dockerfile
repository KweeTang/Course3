FROM java:openjdk-8u66-jdk

RUN apt-get update
RUN apt-get install -y zip
RUN apt-get install -y junit4
RUN apt-get install -y python
RUN apt-get install -y p7zip-full

RUN mkdir /shared
RUN mkdir /shared/submission

RUN mkdir /grader
RUN mkdir /grader/mod1
RUN mkdir /grader/mod1/basicgraph
RUN mkdir /grader/mod1/util
RUN mkdir /grader/mod1/data
RUN mkdir /grader/mod1/geography
RUN mkdir /grader/mod1/roadgraph
RUN mkdir /grader/mod2
RUN mkdir /grader/mod2/basicgraph
RUN mkdir /grader/mod2/util
RUN mkdir /grader/mod2/data
RUN mkdir /grader/mod2/geography
RUN mkdir /grader/mod2/roadgraph
RUN mkdir /grader/mod3/
RUN mkdir /grader/mod3/basicgraph
RUN mkdir /grader/mod3/util
RUN mkdir /grader/mod3/data
RUN mkdir /grader/mod3/geography
RUN mkdir /grader/mod3/roadgraph

COPY executeGrader.sh /grader/executeGrader.sh
COPY mod1/basicgraph/* /grader/mod1/basicgraph/
COPY mod1/util/* /grader/mod1/util/
COPY mod1/data/* /grader/mod1/data/
COPY mod1/geography/* /grader/mod1/geography/
COPY mod1/roadgraph/* /grader/mod1/roadgraph/
COPY mod2/basicgraph/* /grader/mod2/basicgraph/
COPY mod2/util/* /grader/mod2/util/
COPY mod2/data/* /grader/mod2/data/
COPY mod2/geography/* /grader/mod2/geography/
COPY mod2/roadgraph/* /grader/mod2/roadgraph/
COPY mod3/basicgraph/* /grader/mod3/basicgraph/
COPY mod3/util/* /grader/mod3/util/
COPY mod3/data/* /grader/mod3/data/
COPY mod3/geography/* /grader/mod3/geography/
COPY mod3/roadgraph/* /grader/mod3/roadgraph/


COPY compile_error.py /grader/compile_error.py

RUN chmod a+rwx -R /grader/

ENTRYPOINT ["./grader/executeGrader.sh"]
