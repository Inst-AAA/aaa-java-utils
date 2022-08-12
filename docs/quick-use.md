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

#### **Step 2.** 将dependency添加到`pom.xml`的`<dependencies>`中，并根据项目需要更改对应的版本Tag

```xml
	<dependency>
	    <groupId>com.github.Inst-AAA</groupId>
	    <artifactId>aaa-java-utils</artifactId>
	    <version>Tag</version>
	</dependency>
```