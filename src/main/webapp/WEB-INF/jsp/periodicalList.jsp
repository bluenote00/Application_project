<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>기간별 입회신청 내역조회</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: "Noto Sans KR", sans-serif;
            background-color: #f0f0f0;
            color: #333;
            display: flex;
            justify-content: center;
            padding: 40px 20px;
        }

        .container {
            width: 100%;
            max-width: 1200px;
        }

        /* 탭 스타일 */
        .tabs ul {
            display: flex;
            gap: 10px;
            list-style: none;
        }

        .tabs ul li {
            flex: 1;
        }

        .tabs ul li a {
            display: block;
            padding: 10px;
            text-align: center;
            text-decoration: none;
            background: #f6f6f6;
            border: 1px solid #e0e0e0;
            border-radius: 10px 10px 0 0;
            color: #909090;
            font-weight: bold;
        }

        .tabs ul li.active a {
            background: #fff;
            border-bottom: 1px solid #fff;
            color: #dd390d;
        }

        /* 탭 콘텐츠 */
        .tab-content {
            display: none;
            background: #fff;
            padding: 40px;
            border-radius: 20px;
            border: 1px solid #e3e3e3;
        }

        .tab-content.active {
            display: block;
        }

        h1 {
            margin-bottom: 30px;
            font-size: 24px;
            color: #333;
        }

        /* Grid Form */
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px 40px;
            margin-top: 30px;
            margin-bottom: 30px;
        }

        .form-grid label {
            display: block;
            margin-bottom: 6px;
            font-weight: 500;
        }

        .form-grid input, .form-grid select, .form-grid button {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #a8b2b9;
            font-size: 14px;
        }

        .form-grid input:focus, .form-grid select:focus {
            outline: none;
            border-color: #454e55;
        }

        .form-grid.single-line-full {
            display: grid;
            grid-template-columns: repeat(3, 1fr) 120px;
            gap: 15px 20px;
            align-items: end;
        }

        .form-grid.single-line-full .button-cell button {
            width: 100%;
        }

        .form-grid.single-line-post {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px 20px;
        }

        .post-code-wrapper input {
            width: 65%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #a8b2b9;
        }

        .post-code-wrapper button {
            width: 30%;
            padding: 8px;
        }

        .button-row {
            display: flex;
            gap: 20px;
            justify-content: flex-end;
            margin-top: 20px;
        }

        .button-row button {
            width: 120px;
            background-color: #f6f6f6;
            border: 1px solid #d3d8e3;
            cursor: pointer;
            transition: all 0.2s;
            border-radius: 8px;
            padding: 10px;
        }

        .button-row button:hover {
            background-color: #e0f0d9;
            border-color: #b5d5a9;
        }

        hr {
            border: 1px solid #dddddd;
            margin: 30px 0;
        }

        /* 테이블 디자인 */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            margin-bottom: 20px;
        }

        thead {
            background-color: #f6f6f6;
        }

        thead th {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
            color: #555;
        }

        tbody td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: center;
            color: #333;
        }

        tbody tr:nth-child(even) {
            background-color: #fafafa;
        }

        tbody tr:hover {
            background-color: #f1f9ff;
        }

        .double-input {
            display: flex;
            flex-direction: column;
        }

        .double-input div {
            display: flex;
            gap: 10px;
        }

        .double-input input {
            width: 50%;
        }

        input[readonly] {
            background-color: #f5f5f5;
            color: #666;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<section class="container">
    <!-- 탭 메뉴 -->
    <div class="tabs">
        <ul>
            <li data-tab="tab1"><a href="${pageContext.request.contextPath}/application/index">회원 입회 신청</a></li>
            <li class="active" data-tab="tab2"><a href="${pageContext.request.contextPath}/application/periodicalList">기간별 입회신청 내역조회</a></li>
            <li data-tab="tab3"><a href="${pageContext.request.contextPath}/application/cardList">소지 카드 내역조회</a></li>
            <li data-tab="tab4"><a href="${pageContext.request.contextPath}/application/cardDetailList">카드 상세 내역조회</a></li>
            <li data-tab="tab5"><a href="${pageContext.request.contextPath}/application/userIndex">회원 색인 조회</a></li>
        </ul>
    </div>

    <!-- 콘텐츠 -->
    <div class="tab-content active" id="tab-periodical">
        <h1>기간별 입회 신청 내역 조회</h1>

        <!-- 검색 조건 -->
        <form method="post">
        <div class="form-grid single-line-full">
            <div class="double-input">
                <label>기간</label>
                <div>
                    <input type="date" id="startRcvD" name="startRcvD"/>
                    <input type="date" id="endRcvD" name="endRcvD"/>
                </div>
            </div>
            <div>
                <label for="신청구분">신청구분</label>
                <select id="applClas" name="applClas">
                    <option value="">선택</option>
                    <option value="11">최초신규</option>
                    <option value="12">추가신규</option>
                    <option value="21">재발급</option>
                </select>
            </div>
            <div>
                <label for="주민번호">주민번호</label>
                <input type="text" id="ssn"
                       maxlength="14" />
            </div>
            <div class="button-cell">
                <label>&nbsp;</label>
                <button type="button" onclick="searchApplPeriod()">조회</button>
            </div>
        </div>

        <hr/>

        <!-- 조회 결과 -->
        <h4>조회 결과</h4>
        <table>
            <thead>
            <tr>
                <th>번호</th>
                <th>접수일자</th>
                <th>접수 일련번호</th>
                <th>주민번호</th>
                <th>성명(한글)</th>
                <th>성명(영문)</th>
                <th>신청구분</th>
                <th>브랜드</th>
                <th>핸드폰번호</th>
                <th>불능구분</th>
                <th>불능 사유명</th>
            </tr>
            </thead>
            <tbody>
            <!-- 데이터 삽입 영역 -->
            </tbody>
        </table>

        <hr/>
        <div class="button-row">
            <button type="button" onclick="clearForm()">초기화</button>
        </div>
    </div>

    <%--  결과값  --%>
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>

</section>
<script>
    // 현재 페이지 기준 탭 활성화
    const currentPath = window.location.pathname;
    const tabs = document.querySelectorAll(".tabs ul li a");
    tabs.forEach(a => {
        if (a.getAttribute("href") === currentPath) {
            a.parentElement.classList.add("active");
        } else {
            a.parentElement.classList.remove("active");
        }
    });

    // 초기화 버튼
    function clearForm() {
        document.querySelectorAll("form input, form select, form textarea").forEach(el => {
            if (el.type === "checkbox" || el.type === "radio") {
                el.checked = false;
            } else {
                el.value = "";
            }
        });

        // 테이블 초기화
        const tbody = document.querySelector("table tbody");
        tbody.innerHTML = "";
    }

    // 조회 기능
    function searchApplPeriod() {
        const startRcvD = document.getElementById("startRcvD").value;
        const endRcvD   = document.getElementById("endRcvD").value;
        const applClas  = document.getElementById("applClas").value;
        const ssn       = document.getElementById("ssn").value;

        // 유효성 체크
        if (!startRcvD) { alert('접수 시작일자를 입력해주세요.'); return; }
        if (!endRcvD)   { alert('접수 종료일자를 입력해주세요.'); return; }
        if (!applClas)  { alert('신청 구분을 선택해주세요.'); return; }
        if (!ssn)       { alert('주민번호를 입력해주세요.'); return; }

        fetch("${pageContext.request.contextPath}/application/searchApplPeriod", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ startRcvD, endRcvD, applClas, ssn })
        })
            .then(response => response.json())
            .then(result => {
                if (result.message) {
                    alert(result.message);
                    return;
                }
                renderTable(result.data || []);
            })
            .catch(err => console.error("조회 오류:", err));
    }

    // 테이블 렌더링
    function renderTable(list) {
        const tbody = document.querySelector("table tbody");
        tbody.innerHTML = "";

        list.forEach((row, idx) => {
            const tr = document.createElement("tr");

            const fields = [
                idx + 1,
                row.rcvD ?? '',
                row.rcvSeqNo ?? '',
                row.ssn ?? '',
                row.hgNm ?? '',
                row.engNm ?? '',
                row.applClas ?? '',
                row.brd ?? '',
                row.hdpNo ?? '',
                row.impsbClas ?? '',
                row.impsbMsg ?? ''
            ];

            fields.forEach(value => {
                const td = document.createElement("td");
                td.textContent = value;
                tr.appendChild(td);
            });

            tbody.appendChild(tr);
        });
    }
</script>

</body>
</html>
