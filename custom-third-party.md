## 测试中

### **Step 1.** Add JitPack repository to your build file
``` xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
### **Step 2.** Choose libraries you need and add the JitPack repository to your build file
#### 1. [HE_Mesh](https://github.com/wblut/HE_Mesh)  
*version tags: 1.8.2, 2.0.3, 2.0.9, 3.0.3, 6.1.0, 2019.0.2*  
``` xml
	<dependency>
	    <groupId>com.github.archialgo</groupId>
	    <artifactId>HE_Mesh-maven-lib</artifactId>
	    <version>Tag</version>
	</dependency>
```
#### 2. [iGeo](https://github.com/sghr/iGeo)  
*version tags: 0.9.3.0, 0.9.4.1, 0.9.4.1.1 (for Processing 2)*  
``` xml
	<dependency>
	    <groupId>com.github.archialgo</groupId>
	    <artifactId>iGeo-maven-lib</artifactId>
	    <version>Tag</version>
	</dependency>
```
#### 3. Guo_Cam by [Guo Zifeng](https://github.com/guozifeng91)  
*version tags: 1.0, 1.1*  
``` xml
	<dependency>
	    <groupId>com.github.archialgo</groupId>
	    <artifactId>camera</artifactId>
	    <version>Tag</version>
	</dependency>
```
#### 4. [controlp5](https://github.com/sojamo/controlp5)  
*for version 2.2.3, 2.2.5, 2.2.6 :*  
``` xml
	<dependency>
	    <groupId>com.github.archialgo</groupId>
	    <artifactId>iGeo-maven-lib</artifactId>
	    <version>Tag</version>
	</dependency>
```
*for version 2.2.4 :*  
``` xml
	<repositories>
		<repository>
		    <id>clojars.org</id>
		    <url>https://clojars.org/repo/</url>
		</repository>
	</repositories>
```
``` xml
	<dependency>
		<groupId>controlp5</groupId>
		<artifactId>controlp5</artifactId>
		<version>2.2.4</version>
	</dependency>
```
#### 5. [BlobDetection](http://www.v3ga.net/)   
*version tag: 1.0*  
``` xml
	<dependency>
	    <groupId>com.github.archialgo</groupId>
	    <artifactId>iGeo-maven-lib</artifactId>
	    <version>Tag</version>
	</dependency>
```