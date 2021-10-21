# aaa-jgeo-utils

## 说明

本仓库仅限Java语言，几何相关  
分为两种使用方式：

1. 自定义使用：AAA常用第三方Java依赖的Maven安装教程，以解决本地jar包的设备同步与多人协作问题
2. 快速打包使用：基于所有常用第三方Java依赖的，由AAA成员贡献的Java几何相关工具包

## 自定义使用（仅第三方库）
如果不需要此仓库的代码，仅需要自定义常用第三方库的Maven安装，详见：[custom-third-party](./custom-third-party.md)

## 快速使用（暂未发布）

**快速使用将通过Maven把本仓库的代码打包，和本仓库所需的第三方必要依赖一同加入你的项目依赖**

必要依赖包括：Processing, jts, [HE_Mesh](https://github.com/wblut/HE_Mesh), [iGeo](https://github.com/sghr/iGeo), Guo_Cam

上述必要依赖将默认使用各自最新版（在互相之间不发生版本冲突的情况下）

#### **Step 1.** 将JitPack repository添加到`pom.xml`的`<repositories>`中
``` xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

#### **Step 2.** 将dependency添加到`pom.xml`的`<dependencies>`中

```xml
	<dependency>
	    <groupId>com.github.Inst-AAA</groupId>
	    <artifactId>aaa-jgeo-utils</artifactId>
	    <version>Tag</version>
	</dependency>
```

## 测试例子
[test examples](./src/test/java)   
