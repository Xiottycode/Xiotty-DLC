cmake_minimum_required(VERSION 3.22.1)

project("xiotty_dlc")

add_library(
    native_bridge
    SHARED
    native_bridge.cpp
    source.cpp
)

find_library(log-lib log)

target_link_libraries(
    native_bridge
    ${log-lib}
)
