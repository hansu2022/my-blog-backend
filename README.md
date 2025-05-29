# My Blog Backend

[English](#english) | [中文](#chinese)

<a id="english"></a>
## English

This project is a backend application for a personal blog, developed as a course design project. It provides RESTful APIs for managing articles, categories, comments, links, and user authentication. The application leverages Spring Boot, MyBatis-Plus, Spring Security, and Redis to deliver a robust and efficient backend solution.

### Project Structure

The project is organized into a multi-module Maven project:
* `framework`: Contains core functionalities, utilities, entities, and services shared across other modules.
* `qblog`: The main blog application module, exposing public APIs for the blog frontend.
* `admin`: (Potentially) A module for administrative functionalities, although currently it contains a placeholder `Main` class.

### Features

* **Article Management**:
    * Retrieve a list of hot articles.
    * Fetch articles based on pagination and category.
    * Get detailed information for a specific article.
* **Category Management**:
    * Retrieve a list of categories associated with published articles.
* **Comment Management**:
    * Post new comments.
    * Retrieve comments for a given article, including sub-comments.
* **Link Management**:
    * Retrieve a list of approved friendly links.
* **User Authentication & Authorization**:
    * User login and logout functionality using JWT.
    * Authentication and authorization handled by Spring Security.
    * Error handling for authentication and authorization failures.
* **Redis Integration**: Caches user login information for improved performance.
* **Global Exception Handling**: Provides centralized exception handling for consistent error responses.
* **CORS Configuration**: Allows cross-origin requests for frontend integration.
* **FastJSON Integration**: Customizes JSON serialization for improved data handling, including `Long` to `String` conversion.

### Technologies Used

* **Spring Boot**: 2.5.15 (framework module specifies 2.5.15 explicitly for web, security, and data-redis starters. The `qblog` module inherits the Spring Boot version from the parent, which is 2.7.10.)
* **MyBatis-Plus**: 3.5.5 (framework module specifies 3.5.5 explicitly)
* **Spring Security**: 2.5.15 (framework module specifies 2.5.15 explicitly)
* **Redis**: Used for caching, integrated with Spring Data Redis.
* **Fastjson**: 1.2.83 (framework module specifies 1.2.83 explicitly) for JSON processing and Redis serialization.
* **JWT (jjwt)**: 0.9.1 (framework module specifies 0.9.1 explicitly) for token-based authentication.
* **MySQL Connector/J**: 8.0.33
* **Lombok**: 1.18.32 for reducing boilerplate code.
* **Aliyun OSS**: 3.16.2 (usage not explicitly shown in the provided Java code, but the dependency is present).
* **EasyExcel**: 3.3.2 (usage not explicitly shown in provided Java code, but dependency is present).
* **Swagger2**: 2.9.2 for API documentation (usage not explicitly shown in provided Java code, but dependency is present).

### Getting Started

#### Prerequisites

* Java 8 or higher
* Maven 3.x
* MySQL 8.0.x
* Redis

#### Setup

1.  **Clone the repository**:
    ```bash
    git clone <repository_url>
    cd my-blog-backend
    ```

2.  **Database Configuration**:
    * Ensure you have a MySQL database running.
    * Update the database connection properties in `qblog/src/main/resources/application.yml` with your MySQL credentials:
        ```yaml
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/blog?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
            username: your_mysql_username
            password: your_mysql_password
        ```
    * The `dataSources.xml` file indicates a local MySQL connection on port 3306.

3.  **Redis Configuration**:
    * Ensure you have a Redis instance running. No explicit configuration is shown in `application.yml`, implying default Spring Boot Redis connection properties are used or configured elsewhere.

4.  **Build the project**:
    ```bash
    mvn clean install
    ```

5.  **Run the application**:
    Navigate to the `qblog` module and run the main application class:
    ```bash
    cd qblog
    mvn spring-boot:run
    ```
    Alternatively, you can run the `BlogApplication.java` file directly from your IDE.

The application will start on port `7777` by default.

### API Endpoints

Here are some of the key API endpoints provided by the backend:

* **Article Controller (`/article`)**
    * `GET /article/hotArticleList`: Retrieves a list of hot articles, ordered by view count.
    * `GET /article/articleList`: Parameters: `pageNum`, `pageSize`, `categoryId` (optional). Retrieves a paginated list of articles, filtered by category and ordered with "top" articles first.
    * `GET /article/{id}`: Retrieves the details of a specific article by its ID.

* **Blog Login Controller (`/login`, `/logout`)**
    * `POST /login`: Authenticates a user and returns a JWT token and user information upon successful login.
    * `POST /logout`: Logs out the current user by invalidating their session in Redis.

* **Category Controller (`/category`)**
    * `GET /category/getCategoryList`: Retrieves a list of all categories.

* **Comment Controller (`/comment`)**
    * `GET /comment/commentList`: Parameters: `articleId`, `pageNum`, `pageSize`. Retrieves a paginated list of comments for a given article, including replies.
    * `POST /comment`: Adds a new comment.

* **Link Controller (`/link`)**
    * `GET /link/getAllLink`: Retrieves a list of all approved friendly links.

### Error Handling

The application includes global exception handling to provide consistent error responses.
* `SystemException`: Custom exception for business-specific errors.
* `AuthenticationEntryPointImpl`: Handles authentication failures (e.g., missing token, invalid token, bad credentials).
* `AccessDeniedHandlerImpl`: Handles authorization failures (e.g., insufficient permissions).

Common error codes and messages are defined in `AppHttpCodeEnum`.

### Development

* **Code Style**: Standard Java conventions.
* **Lombok**: Used for getter/setter generation.
* **Mapper Interfaces**: Mapper interfaces (e.g., `ArticleMapper`, `CategoryMapper`) extend `BaseMapper` from MyBatis-Plus for simplified database operations.
* **Utilities**:
    * `BeanCopyUtils`: Utility for copying properties between Java Beans.
    * `FastJsonRedisSerializer`: Custom Redis serializer using Fastjson.
    * `JwtUtil`: Utility for generating and parsing JWT tokens.
    * `RedisCache`: Utility for interacting with Redis cache.
    * `WebUtils`: Utility for web-related operations like rendering strings to the response.

### Contributors

* CWJ (Author of generated code)

---

<a id="chinese"></a>
## 中文

本项目是一个个人博客的后端应用程序，作为课程设计项目开发。它提供了用于管理文章、分类、评论、友链和用户认证的 RESTful API。该应用程序利用 Spring Boot、MyBatis-Plus、Spring Security 和 Redis，提供了一个健壮高效的后端解决方案。

### 项目结构

项目被组织成一个多模块的 Maven 项目：
* `framework`：包含核心功能、工具、实体和在其他模块中共享的服务。
* `qblog`：主要的博客应用程序模块，对外暴露博客前端的公共 API。
* `admin`：（潜在的）用于管理功能的模块，尽管目前它只包含一个占位符 `Main` 类。

### 功能

* **文章管理**：
    * 获取热门文章列表。
    * 根据分页和分类获取文章。
    * 获取特定文章的详细信息。
* **分类管理**：
    * 获取与已发布文章关联的分类列表。
* **评论管理**：
    * 发布新评论。
    * 获取给定文章的评论列表，包括子评论。
* **友链管理**：
    * 获取所有已审核的友链列表。
* **用户认证与授权**：
    * 使用 JWT 实现用户登录和注销功能。
    * 通过 Spring Security 处理认证和授权。
    * 处理认证和授权失败的错误。
* **Redis 集成**：缓存用户登录信息以提高性能。
* **全局异常处理**：提供集中式异常处理，以实现一致的错误响应。
* **CORS 配置**：允许跨域请求以便前端集成。
* **FastJSON 集成**：自定义 JSON 序列化以改进数据处理，包括 `Long` 类型到 `String` 类型的转换。

### 使用技术

* **Spring Boot**：2.5.15 (framework 模块明确指定了 web, security, 和 data-redis 启动器版本为 2.5.15。`qblog` 模块继承了父 pom 的 Spring Boot 版本，即 2.7.10。)
* **MyBatis-Plus**：3.5.5 (framework 模块明确指定版本为 3.5.5)
* **Spring Security**：2.5.15 (framework 模块明确指定版本为 2.5.15)
* **Redis**：用于缓存，与 Spring Data Redis 集成。
* **Fastjson**：1.2.83 (framework 模块明确指定版本为 1.2.83) 用于 JSON 处理和 Redis 序列化。
* **JWT (jjwt)**：0.9.1 (framework 模块明确指定版本为 0.9.1) 用于基于令牌的认证。
* **MySQL Connector/J**：8.0.33
* **Lombok**：1.18.32 用于减少样板代码。
* **阿里云 OSS**：3.16.2 (尽管在提供的 Java 代码中未明确显示其使用，但依赖项存在)。
* **EasyExcel**：3.3.2 (尽管在提供的 Java 代码中未明确显示其使用，但依赖项存在)。
* **Swagger2**：2.9.2 用于 API 文档 (尽管在提供的 Java 代码中未明确显示其使用，但依赖项存在)。

### 快速开始

#### 前置条件

* Java 8 或更高版本
* Maven 3.x
* MySQL 8.0.x
* Redis

#### 设置

1.  **克隆仓库**：
    ```bash
    git clone <repository_url>
    cd my-blog-backend
    ```

2.  **数据库配置**：
    * 确保您的 MySQL 数据库正在运行。
    * 在 `qblog/src/main/resources/application.yml` 中更新数据库连接属性：
        ```yaml
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/blog?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
            username: your_mysql_username
            password: your_mysql_password
        ```
    * `dataSources.xml` 文件表明本地 MySQL 连接在 3306 端口。

3.  **Redis 配置**：
    * 确保您的 Redis 实例正在运行。`application.yml` 中未显示明确的配置，这意味着使用了 Spring Boot 默认的 Redis 连接属性或在其他地方进行了配置。

4.  **构建项目**：
    ```bash
    mvn clean install
    ```

5.  **运行应用程序**：
    进入 `qblog` 模块并运行主应用程序类：
    ```bash
    cd qblog
    mvn spring-boot:run
    ```
    或者，您可以直接从 IDE 运行 `BlogApplication.java` 文件。

应用程序默认将在 `7777` 端口启动。

### API 端点

以下是后端提供的一些主要 API 端点：

* **文章控制器 (`/article`)**
    * `GET /article/hotArticleList`：获取按浏览量排序的热门文章列表。
    * `GET /article/articleList`：参数：`pageNum`，`pageSize`，`categoryId`（可选）。获取文章的分页列表，按分类筛选，并按“置顶”文章优先排序。
    * `GET /article/{id}`：根据文章 ID 获取特定文章的详细信息。

* **博客登录控制器 (`/login`, `/logout`)**
    * `POST /login`：用户认证，成功登录后返回 JWT 令牌和用户信息。
    * `POST /logout`：通过使 Redis 中的用户会话失效来注销当前用户。

* **分类控制器 (`/category`)**
    * `GET /category/getCategoryList`：获取所有分类的列表。

* **评论控制器 (`/comment`)**
    * `GET /comment/commentList`：参数：`articleId`，`pageNum`，`pageSize`。获取给定文章的评论分页列表，包括回复。
    * `POST /comment`：添加新评论。

* **友链控制器 (`/link`)**
    * `GET /link/getAllLink`：获取所有已审核的友链列表。

### 错误处理

应用程序包含全局异常处理，以提供一致的错误响应。
* `SystemException`：用于业务特定错误的自定义异常。
* `AuthenticationEntryPointImpl`：处理认证失败（例如，缺少令牌、令牌无效、凭据错误）。
* `AccessDeniedHandlerImpl`：处理授权失败（例如，权限不足）。

常见的错误代码和消息在 `AppHttpCodeEnum` 中定义。

### 开发

* **代码风格**：遵循标准的 Java 规范。
* **Lombok**：用于生成 getter/setter 等方法，减少样板代码。
* **Mapper 接口**：Mapper 接口（例如 `ArticleMapper`、`CategoryMapper`）继承自 MyBatis-Plus 的 `BaseMapper`，简化数据库操作。
* **工具类**：
    * `BeanCopyUtils`：用于在 Java Bean 之间复制属性的工具。
    * `FastJsonRedisSerializer`：使用 Fastjson 的自定义 Redis 序列化器。
    * `JwtUtil`：用于生成和解析 JWT 令牌的工具。
    * `RedisCache`：用于与 Redis 缓存交互的工具。
    * `WebUtils`：用于网络相关操作（例如将字符串渲染到响应）的工具。

### 贡献者

* CWJ (生成代码的作者)
