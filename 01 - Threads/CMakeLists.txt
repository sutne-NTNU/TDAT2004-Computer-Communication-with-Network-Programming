cmake_minimum_required(VERSION 3.15)
project(Nettverksprogrammering)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fsanitize=thread -pthread -std=c++1y -Wall -Wextra")

add_executable(Nettverksprogrammering main.cpp)