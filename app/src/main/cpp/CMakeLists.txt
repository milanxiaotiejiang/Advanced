# 声明要求的cmake最低版本
cmake_minimum_required(VERSION 3.6)

#表示要编译的源文件，所有的源文件都要罗列到此处。set 设置变量，变量名SOURCE_FILES自定义。
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_POSITION_INDEPENDENT_CODE TRUE)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}  ${WARNING_FLAGS}  -std=gnu11 -pthread -marm ")#指定编译器
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++11 -pthread ")# C++ 编译器
#set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS_RELEASE} -Wl,--hash-style=gun")


if (${ANDROID_ABI} STREQUAL "x86")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++11 -pthread  -D__X86__")
else ()
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++11 -pthread -D__ARM__ ")
endif ()

#表示生成静态链接库libassociaiton.a，由${PROJECT_SOURCE_DIR}代表的文件生成。
#语法：ADD_LIBRARY(libname [SHARED|STATIC]
#SHARED 表示生成动态库， STATIC表示生成静态库。
# 创建并命名库，将其设置为静态的或共享，并提供其源代码的相对路径。
# 你可以定义多个library库，并使用CMake来构建。
# Gradle会自动将包共享库关联到你的apk程序。
# 添加库
add_library(
        # 设置库的名称
        threadhook
        # 将库设置为共享库。
        SHARED
        # 为源文件提供一个相对路径。
        threadHook.cpp
)

#添加库
add_library(sockethook SHARED socketHook.cpp)

add_subdirectory(linker)
#指定头文件的搜索路径，相当于指定gcc的-I参数
include_directories(linker)

#导入系统库
# （导入系统库）搜索指定预先构建的库和存储路径变量。因为CMake包括系统库搜索路径中默认情况下,只需要指定想添加公共NDK库的名称，在CMake验证库之前存在完成构建
find_library(
        # 设置path变量的名称
        log-lib
        # 在CMake定位前指定的NDK库名称
        log)

#链接目标库
# （链接目标库）指定库CMake应该链接到目标库中，可以链接多个库，比如定义库，构建脚本，预先构建的第三方库或者系统库
target_link_libraries(
        # 指定目标库
        threadhook linker
        # 目标库到日志库的链接 包含在NDK
        ${log-lib})

#链接目标库
target_link_libraries(sockethook linker ${log-lib})
