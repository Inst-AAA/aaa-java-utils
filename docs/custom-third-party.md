## 第一类：Maven中央仓库或其他Maven仓库已有的常用第三方库
**版本号** 请根据需要从mvnrepository链接中选择版本号，替换“Tag” ↓
#### 1. [Processing (core)](https://processing.org/)
Tag: 3.3.7 及以前 https://mvnrepository.com/artifact/org.processing/core
``` xml
    <dependency>
        <groupId>org.processing</groupId>
        <artifactId>core</artifactId>
        <version>Tag</version>
    </dependency>
```
Tag: 3.3.7 以后 https://mvnrepository.com/artifact/quil/processing-core
``` xml
    <dependency>
        <groupId>quil</groupId>
        <artifactId>processing-core</artifactId>
        <version>Tag</version>
    </dependency>
```
``` xml
    <repository>
        <id>clojars.org</id>
        <url>https://clojars.org/repo/</url>
    </repository>
```
#### 2. GlueGen Runtime (Processing 必需)
Tag: 2.3.2 及以前 https://mvnrepository.com/artifact/org.jogamp.gluegen/gluegen-rt-main
``` xml
    <dependency>
        <groupId>org.jogamp.gluegen</groupId>
        <artifactId>gluegen-rt-main</artifactId>
        <version>Tag</version>
    </dependency>
```
#### 3. JOGL (Processing 必需)
Tag: 2.3.2 及以前 https://mvnrepository.com/artifact/org.jogamp.jogl/jogl-all-main
``` xml
    <dependency>
        <groupId>org.jogamp.jogl</groupId>
        <artifactId>jogl-all-main</artifactId>
        <version>Tag</version>
    </dependency>
```
#### 4. [jts](https://github.com/locationtech/jts)
Tag: 1.15.0 及以后 https://mvnrepository.com/artifact/org.locationtech.jts/jts-core
``` xml
    <dependency>
        <groupId>org.locationtech.jts</groupId>
        <artifactId>jts-core</artifactId>
        <version>Tag</version>
    </dependency>
```
Tag: 1.14.0 https://mvnrepository.com/artifact/com.vividsolutions/jts-core
``` xml
    <dependency>
        <groupId>com.vividsolutions</groupId>
        <artifactId>jts-core</artifactId>
        <version>Tag</version>
    </dependency>
```
Tag: 1.13 及以前 https://mvnrepository.com/artifact/com.vividsolutions/jts
``` xml
    <dependency>
        <groupId>com.vividsolutions</groupId>
        <artifactId>jts</artifactId>
        <version>Tag</version>
    </dependency>
```
#### 5. [Gurobi](https://github.com/locationtech/jts)
PS：Gurobi超乱，这里只放最新版和老版本  
Tag: 9.1.1 最新 https://mvnrepository.com/artifact/com.gurobi/gurobi-jar?repo=jena-bio-libs-release-oss
``` xml
    <dependency>
        <groupId>com.gurobi</groupId>
        <artifactId>gurobi-jar</artifactId>
        <version>9.1.1</version>
    </dependency>
```
``` xml
    <repository>
        <id>Jena Bio</id>
        <url>https://bio.informatik.uni-jena.de/repository/libs-release-oss/</url>
    </repository>
```
Tag: 7.0.1 及以前 https://mvnrepository.com/artifact/com.gurobi/gurobi
``` xml
    <dependency>
        <groupId>com.gurobi</groupId>
        <artifactId>gurobi</artifactId>
        <version>Tag</version>
    </dependency>
```
``` xml
    <repository>
        <id>cogcomp.org</id>
        <url>http://cogcomp.org/m2repo/</url>
    </repository>
```
## 第二类：其他第三方库，目前已被手动整理至JitPack的Maven远程仓库
#### **Step 1.** 将JitPack repository添加到`pom.xml`的`<repositories>`中
``` xml
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
```
#### **Step 2.** 将dependency添加到`pom.xml`的`<dependencies>`中，并根据项目需要更改对应的版本Tag
#### 1. [HE_Mesh](https://github.com/wblut/HE_Mesh)  
Tags: 1.8.2, 2.0.3, 2.0.9, 3.0.3, 6.1.0, 2019.0.2
``` xml
    <dependency>
        <groupId>com.github.archialgo</groupId>
        <artifactId>HE_Mesh-maven-lib</artifactId>
        <version>Tag</version>
    </dependency>
```
#### 2. [iGeo](https://github.com/sghr/iGeo)  
Tags: 0.9.3.0, 0.9.4.1, 0.9.4.1.1 (for Processing 2) 
``` xml
    <dependency>
        <groupId>com.github.archialgo</groupId>
        <artifactId>iGeo-maven-lib</artifactId>
        <version>Tag</version>
    </dependency>
```
#### 3. Guo_Cam by [Guo Zifeng](https://github.com/guozifeng91)  
Tags: 1.0, 1.1（左右键互换）
``` xml
    <dependency>
        <groupId>com.github.archialgo</groupId>
        <artifactId>camera</artifactId>
        <version>Tag</version>
    </dependency>
```
#### 4. [controlp5](https://github.com/sojamo/controlp5)  
Tags: 2.2.3, 2.2.5, 2.2.6
``` xml
    <dependency>
        <groupId>com.github.archialgo</groupId>
        <artifactId>iGeo-maven-lib</artifactId>
        <version>Tag</version>
    </dependency>
```
Tag: 2.2.4 
``` xml
    <repository>
        <id>clojars.org</id>
        <url>https://clojars.org/repo/</url>
    </repository>
```
``` xml
    <dependency>
        <groupId>controlp5</groupId>
        <artifactId>controlp5</artifactId>
        <version>2.2.4</version>
    </dependency>
```
#### 5. [BlobDetection](http://www.v3ga.net/)   
Tag: 1.0
``` xml
    <dependency>
        <groupId>com.github.archialgo</groupId>
        <artifactId>iGeo-maven-lib</artifactId>
        <version>1.0</version>
    </dependency>
```