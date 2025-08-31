<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>소지 카드 내역조회</title>
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
    </style>
</head>
<body>
<section class="container">
    <!-- 탭 메뉴 -->
    <div class="tabs">
        <ul>
            <li data-tab="tab1"><a href="${pageContext.request.contextPath}/application/index">회원 입회 신청</a></li>
            <li data-tab="tab2"><a href="${pageContext.request.contextPath}/application/periodicalList">기간별 입회신청 내역조회</a></li>
            <li  class="active" data-tab="tab3"><a href="${pageContext.request.contextPath}/application/cardList">소지 카드 내역조회</a></li>
            <li data-tab="tab4"><a href="${pageContext.request.contextPath}/application/cardDetailList">카드 상세 내역조회</a></li>
            <li data-tab="tab5"><a href="${pageContext.request.contextPath}/application/userIndex">회원 색인 조회</a></li>
        </ul>
    </div>


    <div class="tab-content active" id="tab-card-list">
        <h1>소지 카드 내역조회</h1>

        <!-- 검색 조건 -->
        <div class="form-grid single-line-full">
            <div>
                <label for="ssn">주민번호</label>
                <input type="text" id="ssn" name="ssn"/>
            </div>
            <div>
                <label for="crdNo">카드번호</label>
                <input type="text" id="crdNo" name="crdNo"/>
            </div>
            <div class="button-cell">
                <label>&nbsp;</label>
                <button type="button" onclick="searchCardList()">조회</button>
            </div>
        </div>

        <hr/>

        <!-- 고객 정보 -->
        <h4>고객 정보</h4>
        <div class="form-grid">
            <div><label>성명(한글)</label><input id="hgNm" readonly/></div>
            <div><label>핸드폰 번호</label><input id="hdpNo" readonly/></div>
            <div><label>등록일자</label><input id="fstRegD" readonly/></div>
        </div>

        <!-- 결제 정보 -->
        <h4>결제 정보</h4>
        <div class="form-grid">
            <div><label>결제방법</label><input id="stlMtd" readonly/></div>
            <div><label>결제은행</label><input id="bnkCd" readonly/></div>
            <div><label>결제계좌</label><input id="stlAct" readonly/></div>
        </div>
        <div class="form-grid">
            <div><label>결제일자</label><input id="stlDd" readonly/></div>
            <div><label>청구서 발송방법</label><input id="stmtSndMtd" readonly/></div>
            <div><label>이메일</label><input id="emailAdr" readonly/></div>
        </div>
        <div class="form-grid">
            <div><label>우편번호</label><input id="billZip" readonly/></div>
            <div><label>주소</label><input id="billAdr1" readonly/></div>
            <div><label>상세주소</label><input id="billAdr2" readonly/></div>
        </div>

        <!-- 카드 정보 테이블 -->
        <h4>카드 정보</h4>
        <table>
            <thead>
            <tr>
                <th>No</th><th>카드번호</th><th>성명(영문)</th><th>유효기간</th><th>브랜드</th><th>카드등급</th>
                <th>발행일자</th><th>전 카드번호</th><th>최종 카드 여부</th><th>관리영업점</th>
            </tr>
            </thead>
            <tbody id="cardTableBody">
            <tr><td colspan="10">조회된 데이터가 없습니다.</td></tr>
            </tbody>
        </table>

        <div class="button-row">
            <button type="button" onclick="clearForm()">초기화</button>
        </div>
    </div>
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

    const contextPath = "${pageContext.request.contextPath}";

    function clearForm() {
        document.querySelectorAll("input").forEach(el => el.value = "");
        document.getElementById("cardTableBody").innerHTML = '<tr><td colspan="10">조회된 데이터가 없습니다.</td></tr>';
    }

    // 카드 리스트 조회
    function searchCardList() {
        const ssn = document.getElementById("ssn").value.trim();
        const crdNo = document.getElementById("crdNo").value.trim();

        if (!ssn) { alert("주민번호를 입력해주세요."); return; }
        if (!crdNo) { alert("카드번호를 입력해주세요."); return; }

        fetch(`${contextPath}/application/searchCardList`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ssn, crdNo })
        })
            .then(res => res.json())
            .then(result => {
                if (result.message) {
                    alert(result.message);
                    clearForm();
                    return;
                }

                const data = (result.data && result.data[0]) || null;
                if (!data) {
                    clearForm();
                    return;
                }

                console.log("조회 데이터:", data);

                // 고객 정보 (INPUT)
                document.getElementById("hgNm").value = data.hgNm || '';
                document.getElementById("hdpNo").value = data.hdpNo || '';
                document.getElementById("fstRegD").value = data.regD || '';

                // 결제 정보 (BILL)
                document.getElementById("stlMtd").value = data.stlMtd || '';
                document.getElementById("bnkCd").value = data.bnkCd || '';
                document.getElementById("stlAct").value = data.stlAct || '';
                document.getElementById("stlDd").value = data.stlDd || '';
                document.getElementById("stmtSndMtd").value = data.stmtSndMtd || '';
                document.getElementById("emailAdr").value = data.emailAdr || '';
                document.getElementById("billZip").value = data.billZip || '';
                document.getElementById("billAdr1").value = data.billAdr1 || '';
                document.getElementById("billAdr2").value = data.billAdr2 || '';

                // 테이블 렌더링
                renderTable([data]);
            })
            .catch(err => {
                console.error("조회 오류:", err);
                alert("조회 중 오류가 발생했습니다.");
            });
    }

    // 테이블 렌더링
    function renderTable(list) {
        const tbody = document.getElementById("cardTableBody");
        tbody.innerHTML = "";

        list.forEach((row, idx) => {
            const tr = document.createElement("tr");

            const fields = [
                idx + 1,
                row.crdNo ?? '',
                row.engNm ?? '',
                row.vldDur ?? '',
                row.brd ?? '',
                row.crdGrd ?? '',
                row.fstRegD ?? '',
                row.bfCrdNo ?? '',
                row.lstCrdF === true || row.lstCrdF === '1' ? 'Y' : 'N',
                row.mgtBbrn ?? ''
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
