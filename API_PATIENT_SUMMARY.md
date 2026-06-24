# api-patient 迁移小结（2026-06-17 完结）

> 本阶段目标：把 `aihoo-biz-service/aihoo-patient-api/` 的 15 个 controller 完整迁移到 DDD 架构下，编译通过 + 启动验证 + curl 接口调试。
>
> 阶段状态：**✅ 完成**（PASS 42 / BUSINESS-FAIL 7 / FAIL 5）

## 完成内容

### 1. 启动验证（2026-06-17 07:49）
- `Started PatientApiApplication in 9.836 seconds`
- SwaggerUI `/swagger-ui/index.html` 可访问
- token 鉴权链路验证通过（mock 登录 `code=1234` → accessToken → 受保护接口）

### 2. 接口调试（54 个端点，2026-06-17 09:57 ~ 10:02）
| 状态 | 数量 | 说明 |
|------|------|------|
| ✅ PASS | 42 | HTTP 200 + 业务 OK |
| ⚠️ BUSINESS-FAIL | 7 | 腾讯 IM 拒签（mock openid）|
| ❌ FAIL | 5 | 见下 |
| 总计 | 54 | |

### 3. 修复 4 类（10 个接口）

| 类 | 接口 | 根因 | 修复 |
|---|------|------|------|
| **A** | doctorUser/list, doctorDetails, hosSick/queryByHosSickId, hosVisit/createOrder | `DoctorUser` 实体有 DB 不存在的 `imUserId/imUserSig` | 删除实体多余字段 |
| **B** | hosVisit/patient/visitList | `HosVisitVoMapper` 接口缺 XML；`application.yml` 只扫 `xml/**` | 新建 `mapper/HosVisitVoMapper.xml` + yml 加 `mapper/**` 路径 |
| **C-1** | im/withdraw | `ImController.withdrawMsg` 缺 `@RequestBody` | 补 `@RequestBody` |
| **C-2** | pre (list) | `HosPrescriptionController.list` 缺 `@RequestBody` | 补 `@RequestBody` |
| **D** | PUT patient-user-address | service 用 conditional `.set()` 已正确（沿用旧代码），无需改 | 验证通过 |

### 4. 已知遗留（无需本阶段处理）

- **G 类**：DrugController 路径与旧代码一致（裸 `/drugPriceList`），报告里原标 ❌ 是误判，实际无需改。
- **E 类**（5 个 IM 接口）：mock 登录产物 `openid=oT6JW1--IGBPD6sFE2SR41aEpfT8` 拒签 → 真实微信 code 登录后自动通过。
- **F 类**（3 个微信手机号接口）：mock 链路无 access_token 缓存 → 真实环境运行即可。
- **H 类**：`addHosSick` 业务规则（重复订单保护）正常行为。

## 启动期间的修复点（2026-06-17 07:49 启动验证期间）

详见 [[ddd-biz-ddd-plan]] 启动期间发现的 6 个修复点：
1. Bean 冲突 → 删除 im 域 BeanRegistry
2. @MapperScan 加 `com.aihoo.push`
3. @ComponentScan 加 push/alicloud
4. upload.properties 从 aihoo-common 复制到 common resources
5. @Resource → @Autowired（HosVisitServiceImpl.pushMessageServiceImpl）
6. AuthUtil.getLoginUser 类型差异处理

## 文件变更清单（最终）

```
M  aihoo-api-patient/src/main/java/com/aihoo/api/patient/config/security/PublicEndpoints.java
M  aihoo-api-patient/src/main/java/com/aihoo/api/patient/controller/HosPrescriptionController.java
M  aihoo-api-patient/src/main/java/com/aihoo/api/patient/controller/ImController.java
M  aihoo-api-patient/src/main/java/com/aihoo/api/patient/controller/PatientUserController.java
M  aihoo-api-patient/src/main/resources/application.yml
M  aihoo-domain-doctor/src/main/java/com/aihoo/domain/doctor/entity/DoctorUser.java
?? API_PATIENT_TEST_REPORT.md
?? API_PATIENT_SUMMARY.md
?? PATIENT_API.md
?? aihoo-domain-visit/src/main/resources/mapper/HosVisitVoMapper.xml
```

## 下一阶段：api-doctor（计划 2026-06-17+ 启动）

按规则"按 controller 合并 service"，先看 doctor-api 的 controller 列表，确认哪些需要：
- **新建**到 doctor 域（如 doctor 专属业务方法）
- **合并补全**到 patient 阶段已建的同名 service（如 DoctorUserService）

需要重点关注（已知问题）：
1. **api-doctor 旧路径引用**：`com.aihoo.domain.hospital.*` / `com.aihoo.domain.prescription.*` 与当前 9 域不一致
2. **aihoo-domain-payment 空骨架**（无 java 代码）
3. **drug 域**可能需要按 doctor-api 的 Drug 实体（30 字段）合并 patient-api 的 Drug 实体（26 字段）

详细报告见 `API_PATIENT_TEST_REPORT.md`。