### 项目简介
基于 Spring Boot 和 React 的一站式用户管理系统，实现了用户注册、登录、查询管理等功能。

### 主要工作
数据库访问层开发：采用 MyBatis 和 MyBatis-Plus，复用大量通用方法，并通过继承定制了通用操作模板，大幅提升了开发效率。
统一错误处理：自定义统一的错误码，并封装了全局异常处理器，规范异常返回，隐藏多余的报错细节。
JSON 处理：对项目中的 JSON 格式化处理对象，采用双检锁单例模式进行管理，避免重复创建对象，方便集中维护和管理。
单元测试：使用 JUnit Jupiter API 的 @Test 注解和 Assertions 类实现对用户模块的单元测试，测试覆盖率达到 90%。
多环境配置：通过 Spring Boot 的多套 application-{env}.yml 配置文件实现多环境，并通过 --spring.profiles.active=prod 指定生产环境配置。
Nginx 反向代理：使用 Nginx 统一接受前端页面和后端接口请求，并通过 proxy_pass 反向代理解决跨域问题。
