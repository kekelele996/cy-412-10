# SmartEstate 智慧社区物业管理系统

SmartEstate 是前后端分离的智慧社区物业管理系统。业主可报修、缴费、查看公告、维护房产信息；物业和管理员可处理工单、发布公告、查看收费与操作日志。

## 技术栈

- 前端：Vue 3 + TypeScript + Vite + Pinia + Element Plus + ECharts
- 后端：Spring Boot 3 + Java 17 + MyBatis-Plus + MySQL 8
- 认证与权限：JWT + RBAC
- 部署：Docker Compose，前端 Nginx 反向代理 `/api` 到后端

## 启动方式

```bash
cp .env.example .env
docker compose up -d --build
```

- 前端：http://localhost:18412
- 后端健康检查：http://localhost:19412/api/health
- MySQL：localhost:3306

停止：

```bash
docker compose down
```

本地静态验证：

```bash
cd frontend && npm install && npm run build
cd ../backend && mvn -DskipTests package
```

## 目录说明

```text
frontend/             Vue3 前端应用
backend/              Spring Boot 后端应用
database/init.sql     MySQL 初始化脚本
docker-compose.yml    一键启动编排
.env.example          环境变量模板
```

## 核心页面

- `/dashboard`：物业工作台，展示待处理工单、本月收费、公告与 ECharts 趋势图。
- `/repairs`：报修管理，筛选、分配、推进工单。
- `/payments`：费用缴纳，账单列表与支付宝沙箱模拟支付。
- `/announcements`：社区公告，置顶、分类、详情与发布入口。
- `/profile`：个人中心，头像、昵称、房产绑定。

## 共享组件

- `StatCard`：工作台统计卡片。
- `RepairStatusBadge`：工作台与报修管理共用。
- `RepairCard`：报修管理工单卡。
- `PaymentItem`：缴费账单项。
- `AnnouncementCard`：公告列表项。
- `EmptyState`：多个列表页共用。
- `AvatarUploader`：个人中心头像上传。
- `PermissionButton`：RBAC 按钮显隐。

## 横切关注点

- RBAC：数据库 `roles`、`permissions`、`role_permissions`，后端 `JwtInterceptor`、`RbacInterceptor`、`PermissionService`，前端 `router/guards.ts`、`PermissionButton.vue`、`authStore.ts`、`types/permission.ts`。
- 操作日志：数据库 `operation_logs`，后端 `OperationLogInterceptor`、`OperationLogService`、`LogUtil`，前端 `/dashboard` 内管理员日志区。
- 接口限流：后端 `RateLimitInterceptor`，登录、缴费等敏感接口启用。

## 枚举出现位置

### RepairStatus

值：`pending`、`assigned`、`processing`、`done`、`closed`

- 前端常量：`frontend/src/constants/repair.ts`
- 前端类型：`frontend/src/types/repair.ts`
- 前端组件：`frontend/src/components/common/RepairCard.vue`
- 前端组件：`frontend/src/components/common/RepairStatusBadge.vue`
- 前端页面：`frontend/src/pages/Dashboard.vue`
- 前端页面：`frontend/src/pages/Repairs.vue`
- 前端 Store：`frontend/src/stores/repairStore.ts`
- 前端 Hook：`frontend/src/hooks/useRepairStats.ts`
- 前端 API：`frontend/src/api/repair.ts`
- 前端格式化：`frontend/src/utils/roleText.ts`
- 后端常量：`backend/src/main/java/com/smartestate/constants/RepairConstants.java`
- 后端日志模板：`backend/src/main/java/com/smartestate/constants/LogTemplates.java`
- 后端消息：`backend/src/main/java/com/smartestate/constants/Messages.java`
- 后端错误码：`backend/src/main/java/com/smartestate/common/ErrorCode.java`
- 后端实体：`backend/src/main/java/com/smartestate/entity/Repair.java`
- 后端 Service：`backend/src/main/java/com/smartestate/service/impl/RepairServiceImpl.java`
- 后端 Controller：`backend/src/main/java/com/smartestate/controller/RepairController.java`
- 后端格式化：`backend/src/main/java/com/smartestate/utils/CommonFormatter.java`
- 数据库约束：`database/init.sql` 中 `repairs.status ENUM(...)`

### UserRole

值：`resident`、`staff`、`admin`

- 前端常量：`frontend/src/constants/user.ts`
- 前端类型：`frontend/src/types/user.ts`
- 前端权限类型：`frontend/src/types/permission.ts`
- 前端路由权限：`frontend/src/router/index.ts`
- 前端路由守卫：`frontend/src/router/guards.ts`
- 前端按钮显隐：`frontend/src/components/common/PermissionButton.vue`
- 前端 Store：`frontend/src/stores/authStore.ts`
- 前端 Hook：`frontend/src/hooks/useAuth.ts`
- 前端 Hook：`frontend/src/hooks/usePermission.ts`
- 前端页面：`frontend/src/pages/Profile.vue`
- 前端格式化：`frontend/src/utils/roleText.ts`
- 后端常量：`backend/src/main/java/com/smartestate/constants/UserConstants.java`
- 后端权限常量：`backend/src/main/java/com/smartestate/constants/Permissions.java`
- 后端日志模板：`backend/src/main/java/com/smartestate/constants/LogTemplates.java`
- 后端实体：`backend/src/main/java/com/smartestate/entity/User.java`
- 后端 RBAC：`backend/src/main/java/com/smartestate/interceptor/RbacInterceptor.java`
- 后端 Service：`backend/src/main/java/com/smartestate/service/impl/PermissionServiceImpl.java`
- 后端 Controller：`backend/src/main/java/com/smartestate/controller/UserController.java`
- 后端格式化：`backend/src/main/java/com/smartestate/utils/CommonFormatter.java`
- 数据库约束：`database/init.sql` 中 `users.role ENUM(...)`、`roles`、`role_permissions`

## 低内聚高耦合说明

项目刻意保留多处重复常量和横向引用：`LogUtil` 被 controller/service/interceptor 引用，`LogTemplates` 集中 25 条以上模板，权限码同时分散在数据库、后端常量、前端路由 meta、按钮组件与 Store。新增 Repair 状态时需同步修改数据库枚举、前后端 constants、类型、状态机、组件、日志模板、错误码、格式化和统计逻辑。
