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
COPY mod3/part1/* /grader/mod3/part1/

COPY compile_error.py /grader/compile_error.py

RUN chmod a+rwx -R /grader/

ENTRYPOINT ["./grader/executeGrader.sh"]
