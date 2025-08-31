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
            <li data-tab="tab2"><a href="${pageContext.request.contextPath}/application/periodicalList">기간별 입회신청 내역조회</a></li>
            <li data-tab="tab3"><a href="${pageContext.request.contextPath}/application/cardList">소지 카드 내역조회</a></li>
            <li class="active" data-tab="tab4"><a href="${pageContext.request.contextPath}/application/cardDetailList">카드 상세 내역조회</a></li>
            <li data-tab="tab5"><a href="${pageContext.request.contextPath}/application/userIndex">회원 색인 조회</a></li>
        </ul>
    </div>

    <!-- 콘텐츠 -->
    <div class="tab-content active" id="tab-periodical">
        <h1>상세 카드 내역조회</h1>

        <!-- 검색 조건 -->
        <div class="form-grid single-line-full">
            <div>
                <label for="jumin">주민번호</label>
                <input type="text" id="jumin" name="jumin"/>
            </div>
            <div>
                <label for="crdNo">카드번호</label>
                <input type="text" id="crdNo" name="crdNo"/>
            </div>
            <div class="button-cell">
                <label>&nbsp;</label>
                <button type="button" onclick="searchCardDetail()">조회</button>
            </div>
        </div>

        <hr/>

        <!-- 조회 결과 -->
        <h4>조회 결과</h4>
        <div class="form-grid">
            <div>
                <label for="성명_한글">성명(한글)</label>
                <input type="text" id="hgNm" name="hgNm" readonly/>
            </div>
            <div>
                <label for="성명_영문">성명(영문)</label>
                <input type="text" id="engNm" name="engNm" readonly/>
            </div>
        </div>


        <div class="form-grid">
            <div>
                <label for="등록일자">등록일자</label>
                <input type="text" id="fstRegD" name="fstRegD" readonly/>
            </div>
            <div>
                <label for="유효기간">유효기간</label>
                <input type="text" id="vldDur" name="vldDur" readonly/>
            </div>
        </div>

        <div class="form-grid">
            <div>
                <label for="브랜드">브랜드</label>
                <input type="text" id="brd" name="brd" readonly/>
            </div>
            <div>
                <label for="카드등급">카드등급</label>
                <input type="text" id="crdGrd" name="crdGrd" readonly/>
            </div>
        </div>

        <div class="form-grid">
            <div>
                <label for="전_카드번호">전 카드번호</label>
                <input type="text" id="bfCrdNo" name="bfCrdNo" readonly/>
            </div>
            <div>
                <label for="고객번호">고객번호</label>
                <input type="text" id="custNo" name="custNo" readonly/>
            </div>
        </div>

        <div class="form-grid">
            <div>
                <label for="영업점">관리 영업점</label>
                <input type="text" id="mgtBbrn" name="mgtBbrn" readonly/>
            </div>
            <div>
                <label for="최종카드">최종 카드여부</label>
                <input type="text" id="lstCrdF" name="lstCrdF" readonly/>
            </div>
        </div>

        <div class="form-grid">
            <div>
                <label for="최종_등록일자">최종 등록일자</label>
                <input type="text" id="lstOprD" name="lstOprD" readonly/>
            </div>
        </div>

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
        document.querySelectorAll("input").forEach(el => {
            el.value = "";
        });
    }

    // 조회 기능 (단건 처리)
    function searchCardDetail() {
        const ssn = document.getElementById("jumin").value;
        const crdNo = document.getElementById("crdNo").value;

        // 유효성 체크
        if (!ssn) { alert('주민번호를 입력해주세요.'); return; }
        if (!crdNo) { alert('카드번호를 입력해주세요.'); return; }

        fetch("${pageContext.request.contextPath}/application/searchCardDetail", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ssn, crdNo })
        })
            .then(res => res.json())
            .then(result => {
                if (result.message) {
                    alert(result.message);
                    return;
                }

                // 단건 처리
                const data = (result.data && result.data[0]) || null;
                if (!data) return;

                document.getElementById("hgNm").value     = data.hgNm || '';
                document.getElementById("engNm").value    = data.engNm || '';
                document.getElementById("fstRegD").value  = data.fstRegD || '';
                document.getElementById("vldDur").value   = data.vldDur || '';
                document.getElementById("brd").value      = data.brd || '';
                document.getElementById("crdGrd").value   = data.crdGrd || '';
                document.getElementById("bfCrdNo").value  = data.bfCrdNo || '';
                document.getElementById("custNo").value   = data.custNo || '';
                document.getElementById("mgtBbrn").value  = data.mgtBbrn || '';
                document.getElementById("lstCrdF").value  = data.lstCrdF || '';
                document.getElementById("lstOprD").value  = data.lstOprD || '';
            })
            .catch(err => console.error("조회 오류:", err));
    }

</script>
</body>
</html>
