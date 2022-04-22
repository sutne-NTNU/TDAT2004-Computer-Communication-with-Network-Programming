FROM gcc:4.9

COPY . /usr/src/oving3/server/src/'Docker_C++'

WORKDIR /usr/src/oving3/server/src/'Docker_C++'

RUN g++ -o Main Main.cpp

CMD ["./Main"]