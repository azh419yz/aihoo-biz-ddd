# 项目接口按页面整理

## 1. App 启动流程 (App.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/patientUser/weChatLogin` | GET | 微信登录，获取 token 和隐私协议授权状态 |
| `/api/v2/patientUser/queryPatientUserById` | GET | 查询当前用户信息（token、手机号、隐私协议状态等） |

### 业务流程
1. 应用启动 → 调用 `weChatLogin()` 获取 token
2. 根据返回的 `allowPrivacyPolicy` 决定是否显示隐私协议弹窗
3. 应用回到前台 → 调用 `queryPatientUserById()` 同步用户信息

---

## 2. 首页 (pages/index/index.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/doctorUser/list` | GET | 获取在线医生列表 |
| `/api/v2/patientUser/allowPrivacyPolicy` | POST | 保存隐私协议授权状态 |
| `/api/v2/patientUser/checkWeChatPhone` | POST | 校验微信手机号是否可绑定 |
| `/api/v2/patientUser/bindWeChatPhone` | POST | 绑定微信手机号 |

### 业务流程
1. 页面加载 → 调用 `getDoctorList()` 获取医生列表
2. 首次进入检查隐私协议 → 弹窗
3. 用户点击"允许" → 调用 `saveAllowPrivacyPolicy()` 保存状态
4. 获取手机号授权 → 调用 `checkWeChatPhone()` 校验 → 调用 `bindWeChatPhone()` 绑定
5. 手机号被占用 → 弹出二次确认弹窗

---

## 3. 我的页面 (pages/my/my.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/patientUser/queryPatientUserById` | GET | 获取用户信息（头像、手机号、订单数、患者数） |
| `/api/v2/patientUser/allowPrivacyPolicy` | POST | 保存隐私协议授权状态 |
| `/api/v2/patientUser/checkWeChatPhone` | POST | 校验微信手机号 |
| `/api/v2/patientUser/bindWeChatPhone` | POST | 绑定微信手机号 |

### 业务流程
1. 页面加载 → 调用 `queryPatientUserById()` 获取用户信息
2. 显示头像、手机号、订单数、患者数
3. 点击"修改手机号" → 获取手机号授权 → 调用 `checkWeChatPhone()` 和 `bindWeChatPhone()`
4. 点击菜单项跳转到对应页面

---

## 4. 消息列表页 (pages/message/list.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/hosVisit/patient/visitList` | POST | 获取患者问诊订单列表（会话列表） |
| `/api/v2/im/visit` | GET | 获取问诊聊天记录 |

### 业务流程
1. 页面加载 → 调用 `getPatientVisitList()` 获取会话列表
2. 点击会话 → 进入聊天页面
3. 下拉刷新 → 重新调用 `getPatientVisitList()`

---

## 5. 聊天页面 (pages/chat/index.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/im/visit` | GET | 获取问诊聊天记录 |
| `/api/v2/im/sendMsg` | POST | 发送 IM 消息 |
| 腾讯云 IM SDK | - | 实时消息通信（WebSocket） |

### 业务流程
1. 页面加载 → 初始化 IM SDK（使用 userSig）
2. 调用 `getIMVisit()` 获取历史消息
3. 用户发送消息 → 调用 `sendMsg()` 发送
4. 实时接收消息 → 通过 IM SDK 监听

### 消息类型支持
- 文本消息 (TIMTextElem)
- 图片消息 (TIMImageElem)
- 语音消息 (TIMSoundElem)
- 问诊资料卡片（自定义消息）

---

## 6. 医生详情页 (pages/doctor/detail.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/doctorUser/doctorDetails` | GET | 获取医生详情（名字、职位、医院、资质等） |

### 业务流程
1. 页面加载 → 调用 `getDoctorDetail(id)` 获取医生详情
2. 显示医生信息、资质、擅长、简介等
3. 点击头像 → 弹出资质认证弹窗

---

## 7. 问诊流程

### 7.1 基础信息页 (pages/inquiry/basic.vue)

#### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/dArea/list` | GET | 获取省市区级联列表 |

#### 业务流程
1. 页面加载 → 调用 `getAreaList()` 获取地区数据
2. 用户填写：地区、身高、体重、既往病史、过敏史、病情描述
3. 点击"下一步" → 跳转到问卷页面

---

### 7.2 问卷页面 (pages/inquiry/questionnaire.vue)

#### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/hosVisit/addHealthInfo` | POST | 添加健康信息（问卷答案） |

#### 业务流程
1. 页面加载 → 显示问卷表单
2. 用户填写问卷
3. 点击"下一步" → 调用 `addHealthInfo()` 保存问卷数据

---

### 7.3 上传资料页 (pages/inquiry/upload.vue)

#### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/file/upload` | POST | 上传文件（舌照、面照、病历等） |
| `/api/v2/hosVisit/addBaseInfo` | POST | 添加基本状况（包含上传的图片URL） |

#### 业务流程
1. 用户选择图片 → 调用 `uploadFile()` 上传到服务器
2. 获得图片 URL → 保存到本地状态
3. 点击"提交" → 调用 `addBaseInfo()` 提交所有资料

#### 上传的资料类型
- 舌照（舌苔、舌底）
- 面照
- 历史病历/处方单（最多9张）

---

## 8. 患者信息页 (pages/patient/list.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/hosSick/queryByPatientUserId` | GET | 获取患者列表 |
| `/api/v2/hosSick/check` | POST | 认证患者（根据姓名和身份证号） |
| `/api/v2/hosSick/save` | POST | 新增患者 |
| `/api/v2/patientUser/allowPrivacyPolicy` | POST | 保存隐私协议授权状态 |
| `/api/v2/patientUser/checkWeChatPhone` | POST | 校验微信手机号 |
| `/api/v2/patientUser/bindWeChatPhone` | POST | 绑定微信手机号 |

### 业务流程
1. 页面加载 → 调用 `queryByPatientUserId()` 获取患者列表
2. 点击"新建患者" → 跳转到新增患者页面
3. 填写患者信息 → 调用 `checkPatient()` 认证 → 调用 `savePatient()` 保存

### 患者信息字段
- 姓名（必填）
- 身份证号（必填）
- 性别（由认证接口返回）
- 生日（由认证接口返回）

---

## 9. 购药订单页 (pages/order/list.vue)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/mdtOrder/orderList` | GET | 获取购药订单列表（分页） |
| `/api/v2/patientUser/allowPrivacyPolicy` | POST | 保存隐私协议授权状态 |
| `/api/v2/patientUser/checkWeChatPhone` | POST | 校验微信手机号 |
| `/api/v2/patientUser/bindWeChatPhone` | POST | 绑定微信手机号 |

### 业务流程
1. 页面加载 → 调用 `orderList(page, limit)` 获取订单列表
2. 下拉刷新 → 重新调用 `orderList(1, limit)`
3. 上拉加载更多 → 调用 `orderList(page+1, limit)`
4. 点击订单 → 进入订单详情页

### 订单字段
- 订单标题
- 金额
- 药房名称
- 订单时间
- 订单状态（已退款等）

---

## 10. 问诊订单相关接口 (service/hosVisit.ts)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/hosVisit/createOrder` | POST | 创建问诊订单 |
| `/api/v2/hosVisit/pay` | POST | 问诊订单支付 |
| `/api/v2/hosVisit/addHosSick` | POST | 问诊单绑定患者 |
| `/api/v2/hosVisit/addBaseInfo` | POST | 添加问诊资料—基本状况 |
| `/api/v2/hosVisit/addHealthInfo` | POST | 添加问诊资料—健康信息 |
| `/api/v2/hosVisit/submitInfo` | POST | 提交问诊资料状态 |
| `/api/v2/hosVisit/baseInfo` | GET | 获取问诊资料—基本状况 |
| `/api/v2/hosVisit/healthInfo` | GET | 获取问诊资料—健康状况 |
| `/api/v2/hosVisit/patient/visitList` | POST | 获取患者问诊订单列表 |

### 业务流程
1. 选择医生 → 调用 `createOrder()` 创建问诊订单
2. 支付 → 调用 `pay()` 支付
3. 选择患者 → 调用 `addHosSick()` 绑定患者
4. 填写基本信息 → 调用 `addBaseInfo()` 保存
5. 填写问卷 → 调用 `addHealthInfo()` 保存
6. 提交 → 调用 `submitInfo()` 标记完成
7. 进入聊天 → 调用 `getPatientVisitList()` 获取会话列表

---

## 11. IM 相关接口 (service/im.ts)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/im/sendMsg` | POST | 发送 IM 消息 |
| `/api/v2/im/visit` | GET | 获取问诊聊天记录 |

### 业务流程
1. 初始化 IM SDK → 使用 `queryPatientUserById()` 返回的 `userSig`
2. 发送消息 → 调用 `sendMsg()` 或通过 IM SDK 发送
3. 接收消息 → 通过 IM SDK 监听或调用 `getIMVisit()` 获取历史记录

---

## 12. 地区接口 (service/area.ts)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/dArea/list` | GET | 获取省市区级联列表 |

### 使用场景
- 问诊基础信息页面的地区选择
- 患者信息页面的地区选择

---

## 13. 文件上传接口 (service/file.ts)

### 核心接口
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v2/file/upload` | POST | 上传文件（图片等） |

### 使用场景
- 问诊资料上传（舌照、面照、病历等）
- 用户头像上传

### 请求参数
- `file`: 二进制文件
- `accessToken`: 请求头中的 token

### 响应格式
```json
{
  "code": 200,
  "msg": "success",
  "data": "https://...",  // 文件 URL
  "success": true
}
```

---

## 接口调用流程图

```
App 启动
  ├─ weChatLogin() → 获取 token
  ├─ queryPatientUserById() → 获取用户信息
  └─ 根据 allowPrivacyPolicy 决定是否显示隐私协议

首页
  ├─ getDoctorList() → 获取医生列表
  ├─ 隐私协议授权流程
  │  ├─ saveAllowPrivacyPolicy()
  │  ├─ checkWeChatPhone()
  │  └─ bindWeChatPhone()
  └─ 跳转到我的页面

我的页面
  ├─ queryPatientUserById() → 获取用户信息
  ├─ 修改手机号流程
  │  ├─ checkWeChatPhone()
  │  └─ bindWeChatPhone()
  └─ 菜单导航

消息列表
  ├─ getPatientVisitList() → 获取会话列表
  └─ 点击会话进入聊天

聊天页面
  ├─ 初始化 IM SDK
  ├─ getIMVisit() → 获取历史消息
  ├─ sendMsg() → 发送消息
  └─ IM SDK 实时接收消息

医生详情
  └─ getDoctorDetail() → 获取医生详情

问诊流程
  ├─ 基础信息页
  │  └─ getAreaList() → 获取地区列表
  ├─ 问卷页
  │  └─ addHealthInfo() → 保存问卷
  ├─ 上传资料页
  │  ├─ uploadFile() → 上传图片
  │  └─ addBaseInfo() → 保存资料
  └─ 进入聊天
     └─ getPatientVisitList() → 获取会话

患者信息页
  ├─ queryByPatientUserId() → 获取患者列表
  ├─ 新增患者流程
  │  ├─ checkPatient() → 认证患者
  │  └─ savePatient() → 保存患者
  └─ 隐私协议授权流程

购药订单页
  ├─ orderList() → 获取订单列表
  └─ 隐私协议授权流程
```

---

## 常用接口参数总结

### 认证相关
- `weChatLogin()`: 无参数，自动调用 uni.login 获取 code
- `queryPatientUserById()`: 无参数
- `checkWeChatPhone(code)`: code 来自微信手机号授权
- `bindWeChatPhone(code)`: code 来自微信手机号授权

### 患者相关
- `queryByPatientUserId()`: 无参数
- `checkPatient({name, idCard, id?, sex?, birthday?})`: 认证患者
- `savePatient({id?, name, idCard, sex, birthday})`: 保存患者

### 问诊相关
- `createOrder(hosSickId, doctorId, price)`: 创建订单
- `addBaseInfo(params)`: 添加基本信息
- `addHealthInfo(params)`: 添加健康信息
- `submitInfo(hosVisitId, hosSickId)`: 提交资料

### 文件相关
- `uploadFile(filePath)`: 上传文件

### 消息相关
- `sendMsg(params)`: 发送消息
- `getIMVisit(visitNo)`: 获取聊天记录

---


## API 基础信息

- **基础 URL**: `https://localhost:8082`
- **API 版本**: v2
- **认证方式**: Token（请求头 `accessToken`）
- **响应格式**: JSON
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {...},
    "success": true,
    "timestamp": 1234567890
  }
  ```
