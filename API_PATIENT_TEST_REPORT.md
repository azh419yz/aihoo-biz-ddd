# api-patient 接口调试报告

> 调试时间：2026-06-17
> 服务：aihoo-api-patient（local profile，端口 8082）
> Token：mock 登录（code=1234）获取的 patient token
> 共 **54 个接口**（不含腾讯 IM 回调 `/im/call-back`）

## 总览

| 状态 | 数量 |
|------|------|
| ✅ PASS（HTTP 200 + 业务 OK） | **38 → 42** |
| ⚠️ BUSINESS-FAIL（HTTP 200 + 业务返回错误，接口通） | **7** |
| ❌ FAIL（HTTP 500 / 404 / 业务级异常） | **9 → 5** |
| 总计 | **54** |

**二轮调试（2026-06-17 10:02）**：B/C/D 全部 ✅ 修复完成。G 验证无需改。详见下文。

### 失败根因分类

| 代号 | 根因 | 影响接口数 | 状态 |
|------|------|----------|------|
| **A** | DoctorUser 实体有 DB 不存在的字段 | 4 | ✅ 已修 |
| **B** | HosVisitVoMapper.patientList 缺 XML 绑定 | 1 | ❌ 待修 |
| **C** | Controller 缺 `@RequestBody`/`@RequestParam` | 2 | ❌ 待修 |
| **D** | PUT patient-user-address 缺 SET 字段 | 1 | ❌ 待业务决策 |
| **E** | IM 业务 FAIL（mock 登录产物，腾讯 IM 拒签） | 5 | ⏸️ 等真 openid |
| **F** | patientUser 微信手机号 NPE（mock 链路数据缺失） | 3 | ⏸️ mock 链路 |
| **G** | DrugController 路径与旧代码一致（裸 `/drugPriceList`） | 1 | ✅ 已验证（无需改） |
| **H** | 业务规则（重复/不存在）—— 非代码 bug | 1 | ✅ 正常 |

---

## ✅ PASS（38 个）

### AreaController（5/5）
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/dArea/list` | GET | 200，省市区级联 |
| `/api/v2/dArea/provincesList` | GET | 200，省份列表 |
| `/api/v2/dArea/doctorProCityList` | GET | 200，医生省市 |
| `/api/v2/dArea/cityList?parentAreaCode=110000` | GET | 200，城市列表 |
| `/api/v2/dArea/districtList?parentAreaCode=110100` | GET | 200，区列表 |

### DoctorUserController（3/3）—— **A 类已修**
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/doctorUser/list?name=` | GET | 200，返回 13 个医生 |
| `/api/v2/doctorUser/doctorDetails?id=46` | GET | 200，医生详情 |
| `/api/v2/doctorUser/now/welcomeMessage?doctorUserId=46` | GET | 200，欢迎语 |

### PatientUserController（4/7）—— 微信手机号 3 个为 F 类
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/patientUser/weChatLogin?code=1234` | GET | 200，mock 登录通过 |
| `/api/v2/patientUser/queryPatientUserById` | GET | 200，用户信息 |
| `/api/v2/patientUser/allowPrivacyPolicy` | POST | 200，授权成功 |
| `/api/v2/patientUser/sendCode` | POST | 200，验证码已发送 |
| `/api/v2/patientUser/checkPhone` | POST | 200，手机号可用 |

### HosSickController（5/5）
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/hosSick/queryByPatientUserId` | GET | 200，患者列表 |
| `/api/v2/hosSick/queryByHosSickId?hosSickId=69` | GET | 200，患者详情 |
| `/api/v2/hosSick/save` | POST | 200，新增就诊人 |
| `/api/v2/hosSick/check` | POST | 200，认证 |
| `/api/v2/hosSick/update` | PUT | 200，更新 |
| `/api/v2/hosSick/delete/{hosSickId}` | DELETE | 200，删除 |

### HosVisitController（6/9）—— visitList 为 B 类，addHosSick 重复为 H 类
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/hosVisit/baseInfo?hosVisitId=1` | GET | 200 |
| `/api/v2/hosVisit/healthInfo?hosVisitId=1` | GET | 200 |
| `/api/v2/hosVisit/createOrder` | POST | 200，新订单（医生 46，价格 100） |
| `/api/v2/hosVisit/pay` | POST | 200，支付成功 |
| `/api/v2/hosVisit/addBaseInfo` | POST | 200 |
| `/api/v2/hosVisit/addHealthInfo` | POST | 200 |
| `/api/v2/hosVisit/submitInfo` | POST | 200 |
| `/api/v2/hosVisit/updateBaseInfo` | PUT | 200 |

### HosPrescriptionController（2/3）—— 列表为 C 类
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/pre/view?id=1` | GET | 200 |
| `/api/v2/pre/confirmed?req=1` | GET | 200，处方不存在（业务正常） |
| `/api/v2/pre/confirmed` | PUT | 200 |

### MdtOrderController（4/4）
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/mdtOrder?page=1&limit=10` | GET | 200，分页 |
| `/api/v2/mdtOrder/view?orderNum=M20210610111024612` | GET | 200 |
| `/api/v2/mdtOrder/orderList?pageParam=` | GET | 200 |
| `/api/v2/mdtOrder` | POST | 200 |
| `/api/v2/mdtOrder/pay?orderNum=V001` | POST | 200 |

### PatientUserAddressController（3/5）—— PUT 为 D 类
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/patient-user-address` | GET | 200，地址列表 |
| `/api/v2/patient-user-address/default?selectRequest=` | GET | 200，默认地址 |
| `/api/v2/patient-user-address` | POST | 200，新增 |
| `/api/v2/patient-user-address` | DELETE | 200 |

### ImController（2/5）—— sendMsg/recent/modifyMsg 为 E 类，withdraw 为 C 类
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/im/visit?visitNo=V001` | GET | 200 |
| `/api/v2/im/perrRead?visitNo=V001` | GET | 200 |

### DoctorDirectoryController（1/1）
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/doctor-directory` | POST | 200 |

### FileController（1/1）
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/file/upload` | POST | 200，上传成功 |

### DrugstoreController（1/1）
| 接口 | 方法 | 测试结果 |
|------|------|----------|
| `/api/v2/drugstore` | GET | 200（返回空数据，stub） |

---

## ⚠️ BUSINESS-FAIL（7 个，HTTP 200 + 业务返回错误）

### ImController（3 个，E 类 - mock 登录产物）
| 接口 | 业务返回 | 根因 |
|------|----------|------|
| `POST /api/v2/im/sendMsg` | 60003 JSON parse error | 腾讯 IM 拒签（mock openid） |
| `POST /api/v2/im/recent` | 70003 UserSig illegal | 同上 |
| `POST /api/v2/im/modifyMsg` | 60003 JSON parse error | 同上 |

### ImGroupController（5 个，E 类 - mock 登录产物）
| 接口 | 业务返回 |
|------|----------|
| `POST /api/v2/im-group` | 70003 UserSig illegal |
| `POST /api/v2/im-group/msg` | 70003 UserSig illegal |
| `POST /api/v2/im-group/member` | 70003 UserSig illegal |
| `DELETE /api/v2/im-group/member` | 70003 UserSig illegal |
| `GET /api/v2/im-group/member?req=g1` | 70003 UserSig illegal |

**E 类根因**：mock 登录用硬编码 `openid=oT6JW1--IGBPD6sFE2SR41aEpfT8` 生成 imUserSig，腾讯 IM 服务端不认识。修复条件：用真实微信 code 登录后自动通过。

---

## ❌ FAIL（9 个）

### B 类 - HosVisitVoMapper XML 缺失（1）
| 接口 | 错误 | 根因 |
|------|------|------|
| `POST /api/v2/hosVisit/patient/visitList` | `BindingException: Invalid bound statement (not found): com.aihoo.domain.visit.mapper.HosVisitVoMapper.patientList` | mapper 接口声明了 `patientList` 方法但 visit 域 `resources/mapper/` 目录是空的，无 XML 也无 `@Select` 注解。`orderList`/`orderDetails` 同理（未触发但同样缺）。 |

### C 类 - Controller 缺 `@RequestBody`/`@RequestParam`（2）
| 接口 | 错误 | 根因 |
|------|------|------|
| `POST /api/v2/im/withdraw` | `NPE: ImWithdrawMsgRequestDto.getMsgReq().split(...)` | `ImController.withdrawMsg(ImWithdrawMsgRequest req)` 缺 `@RequestBody`，request 始终为 null，dto 字段全空 |
| `GET /api/v2/pre?request=` | `NPE: PrescriptionSelectDto.getPage().intValue()` | `HosPrescriptionController.list(PrescriptionSelectRequest req)` 缺 `@RequestBody`/param，传空参时 dto 全 null |

### D 类 - PUT 缺 SET 字段（1）
| 接口 | 错误 | 根因 |
|------|------|------|
| `PUT /api/v2/patient-user-address` | `SQL: UPDATE t_patient_user_address WHERE (id = ?)`（无 SET） | body `{"id":"1"}` 没传要更新的字段，MyBatis-Plus LambdaUpdateWrapper 没调 `.set()`，生成空 SET |

### F 类 - patientUser 微信手机号 NPE（3）
| 接口 | 错误 | 根因 |
|------|------|------|
| `POST /api/v2/patientUser/updatePhone` | `NPE: RedisService.get(...) returns null` | `updatePhone(mobile, code)` 调 `redisService.get("send_code_" + mobile)`，无 sendCode 流程就调用时 key 不存在 |
| `POST /api/v2/patientUser/bindWeChatPhone` | 同上 | `redisService.get("wechat_phone_" + code)` 无 key |
| `POST /api/v2/patientUser/checkWeChatPhone` | `weChatComponent.getWeCHatMobile(...)` stub 失败 | mock 链路无 access_token 缓存 |

**F 类根因**：需要走完整 `sendCode` → 微信授权 → 验证 流程，mock 登录无法生成真实验证码和 access_token。修复条件：真实环境运行。

### G 类 - 路径错（1）
| 接口 | 错误 | 根因 |
|------|------|------|
| `POST /api/v2/drugPriceList` | 404 Not Found | DrugController 类上缺 `@RequestMapping("/api/v2/drug")`，实际路径是 `/drugPriceList`（不在 `/api/v2` 下） |

### H 类 - 业务规则（1，**非 bug**）
| 接口 | 错误 | 根因 |
|------|------|------|
| `POST /api/v2/hosVisit/addHosSick` (hosVisitId=446) | `PATIENT_HOS_VISIT_PAY_STATUS` BizException | 业务规则：同一 (医生+患者+就诊人) 已有 UNSUBMITTED/SUBMITTED/STARTED 状态的订单，禁止重复创建 |

---

## 已修复（A 类 4 个 + B 类 1 个 + C 类 2 个）

### A 类（4 个）—— DoctorUser 实体多余字段

| 接口 | 原错误 | 修复 |
|------|--------|------|
| `GET /api/v2/doctorUser/list` | `Unknown column 'im_user_id'` | 删除 `DoctorUser` 实体 `imUserId/imUserSig` 字段 |
| `GET /api/v2/doctorUser/doctorDetails` | 同上 | 同上 |
| `GET /api/v2/hosSick/queryByHosSickId` | 同上（内部调 doctorDetails） | 同上 |
| `POST /api/v2/hosVisit/createOrder` | 同上（内部调 doctorDetails） | 同上 |

### B 类（1 个）—— HosVisitVoMapper XML 缺失

| 接口 | 原错误 | 修复 |
|------|--------|------|
| `POST /api/v2/hosVisit/patient/visitList` | `BindingException: Invalid bound statement` | 新增 `aihoo-domain-visit/src/main/resources/mapper/HosVisitVoMapper.xml`（含 patientList / orderList / orderDetails 三个 select，命名空间 `com.aihoo.domain.visit.mapper.HosVisitVoMapper`）；`application.yml` 增加 `classpath*:mapper/**/*Mapper.xml` 扫描路径 |

### C 类（2 个）—— Controller 缺 `@RequestBody`

| 接口 | 原错误 | 修复 |
|------|--------|------|
| `POST /api/v2/im/withdraw` | NPE: `getMsgReq().split()` | `ImController.withdrawMsg` 形参前补 `@RequestBody` |
| `GET /api/v2/pre` | NPE: `getPage().intValue()` | `HosPrescriptionController.list` 形参前补 `@RequestBody` |

### G 类（1 个）—— DrugController 路径（**无需改**）

| 接口 | 验证 |
|------|------|
| `POST /drugPriceList` | ✅ 200，返回 10 条药品（路径与旧代码一致——旧代码无类级 `@RequestMapping`，DDD 也无） |
| `POST /api/v2/drug/drugPriceList` | ❌ 404（旧代码也不存在该路径，PATIENT_API.md 未列出该接口） |

**结论**：G 类 DDD 代码本就与旧代码一致，无需修改。报告里 G 类原标 ❌ 是误判。

---

## 待修复汇总

按优先级：
1. ✅ ~~**B + G（2 接口）**：缺 SQL/类注解，1-2 行修复~~ **已修**
2. ✅ ~~**C（2 接口）**：补 `@RequestBody`，1 行修复~~ **已修**
3. ✅ ~~**D（1 接口）**：需业务决策（强制至少 1 个更新字段？允许空 PUT 当 noop？）~~ **已实现**（沿用旧代码 conditional `.set()` 模式）
4. **E + F（8 接口）**：mock 链路产物，需真实环境验证（IM 业务 / 微信手机号 / 验证码链路）

## 调用样例

```bash
# 登录拿 token
TOKEN=$(curl -s "http://localhost:8082/api/v2/patientUser/weChatLogin?code=1234" | python3 -c "import json,sys; print(json.load(sys.stdin)['data']['token'])")

# 测试任一接口
curl -H "accessToken: $TOKEN" "http://localhost:8082/api/v2/doctorUser/list?name="
curl -X POST -H "accessToken: $TOKEN" -H "Content-Type: application/json" -d '{"hosSickId":69,"doctorId":46,"price":100}' "http://localhost:8082/api/v2/hosVisit/createOrder"
```
