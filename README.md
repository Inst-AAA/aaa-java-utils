# aaa-java-utils

## 说明

本仓库为Inst. AAA内部Java相关工具仓库  
分为两块内容：

1. 快速打包使用：基于所有常用第三方依赖的，由Inst. AAA成员贡献的Java相关工具代码
2. 自定义第三方库：Inst. AAA常用第三方Java依赖的Maven安装说明，以解决本地jar包的设备同步与多人协作问题

## 快速打包使用
快速使用将通过Maven把本仓库的代码打包(连同依赖)加入你的项目

本仓库已通过Github Packages部署，用法详见[com.github.Inst-AAA.aaa-java-utils](https://github.com/orgs/Inst-AAA/packages?repo_name=aaa-java-utils)

## 自定义第三方库
整理并列出一些大家在Java项目中常用的、好用的第三方依赖库（通过Maven添加）

如仅有本地文件，也可通过[JitPack](https://jitpack.io/), Github Packages等渠道打包到Maven远程仓库，便于使用

可根据需要，通过Maven自定义添加到你的项目中（不包括本仓库源码）

详见：[自定义第三方库说明](docs/custom-third-party.md)



## 关于代码维护的说明
[代码维护说明](docs/code-maintainance.md)   
