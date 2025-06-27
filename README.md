# 企業級員工差勤管理系統 (後端)

這是一個使用 Spring Boot 搭建的後端伺服器，為一個現代化的人事管理系統提供 RESTful API 服務。專案不僅包含基礎的 CRUD 功能，更著重於實現真實世界中的複雜業務邏輯，如多階段審核工作流與角色權限管理。

## 專案亮點與設計決策

這個專案最大的價值，在於將理論知識應用於解決複雜的業務需求。在開發過程中，我針對幾個核心問題進行了深入的設計與優化：

#### 1. **權限系統架構：從簡易到可擴展的演進**
* **挑戰**：如何設計一個能適應未來業務擴展的權限系統？
* **初期方案**：在 `employees` 表中使用單一 `role` 欄位。
* **演進與優化**：意識到初期方案的局限性後，主動將其**重構**為業界標準的 **RBAC (Role-Based Access Control) 多對多模型**。建立了獨立的 `roles` 與 `employee_roles` 資料表，使系統可以動態新增角色，並讓員工擁有多重身份，大幅提升了系統的靈活性與可維護性。

#### 2. **多階段動態審核工作流設計**
* **挑戰**：如何實現一個有「先後順序」的、非線性的審核流程？
* **解決方案**：沒有採用複雜的 JPQL 查詢，而是透過在 `leave_approval` 表中引入了 **`PENDING`** 和 **`WAITING`** 兩種狀態來巧妙地管理流程。當第一階段審核通過後，由 `Service` 層的業務邏輯負責將下一階段的狀態從 `WAITING` 更新為 `PENDING`，從而「啟動」下一關。
* **優勢**：這個設計讓資料庫查詢保持高效簡潔，同時將複雜的流程控制邏輯集中在 `Service` 層，使系統狀態清晰、易於追蹤和擴展。

#### 3. **資料庫與 ORM 最佳實踐**
* **命名慣例**：全面採用 `snake_case` 蛇式命名法來設計資料庫 Schema，以解決跨作業系統的大小寫敏感度問題，並完美契合 Spring Data JPA 的預設命名策略，大幅簡化了 Entity 的註解。
* **資料一致性**：在所有涉及多個資料表寫入的操作中（如建立假單與審核步驟），都使用 **`@Transactional`** 註解來確保資料庫操作的原子性，防止產生不一致的「髒資料」。
* **密碼安全**：實作了**加鹽雜湊 (Salted Hashing)** 機制來儲存使用者密碼，為每位使用者產生獨立的 `salt`，有效防止彩虹表攻擊。

#### 4. **專業的 API 設計**
* **RESTful 風格**：所有 API 端點都遵循 RESTful 設計原則，使用名詞而非動詞，並善用標準的 HTTP 方法 (GET, POST, PUT)。
* **統一回應格式**：設計了 `BaseApiResponse` 來統一所有 API 的 JSON 回應結構，並使用 `ResponseEntity` 來精準控制 HTTP 狀態碼（`201`, `400`, `401`, `403`），提升了 API 的專業性和前端整合的便利性。

## 技術棧 (Technology Stack)
* **語言**: Java 17+
* **框架**: Spring Boot 3, Spring Data JPA
* **資料庫**: MySQL 8
* **工具**: Lombok, Maven

## 本機環境啟動指南
1.  確保已安裝 Java 21 和 Maven。
2.  確保已安裝並啟動 MySQL 8 伺服器。
3.  在 MySQL 中建立名為 `javapj` 的資料庫。
4.  將專案根目錄下的 `Dump20250617.sql` 檔案匯入到您的 `javapj` 資料庫中。
5.  修改 `src/main/resources/application.properties` 中的資料庫連線資訊（用戶名、密碼）。
6.  執行 `mvn spring-boot:run` 或直接在您的 IDE 中啟動主應用程式。
7.  後端伺服器將會運行在 `http://localhost:5000`。

## API 端點預覽
| 方法 | 路徑 | 功能說明 |
| :--- | :--- | :--- |
| `POST` | `/api/login` | 員工登入 |
| `GET` | `/api/user` | 獲取當前登入者資訊 |
| `GET` | `/api/employees` | 查詢員工列表（可依部門篩選） |
| `POST` | `/api/employees` | 新增員工 |
| `POST`| `/api/leave-applications` | 員工提交請假申請 |
| `GET` | `/api/approvals/pending`| 主管獲取待審核列表 |
| `POST`| `/api/approvals/{id}/{action}` | 主管執行審核操作 |
| `GET` | `/api/options/...` | 獲取各類下拉選單選項 |
