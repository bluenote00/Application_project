<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>입회신청서</title>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

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
            padding: 60px;
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
            margin-top: 50px;
            margin-bottom: 50px;
        }

        .form-grid label {
            display: block;
            margin-bottom: 6px;
            font-weight: 500;
        }

        .form-grid input,
        .form-grid select,
        .form-grid button {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #a8b2b9;
            font-size: 14px;
        }

        .form-grid input:focus,
        .form-grid select:focus {
            outline: none;
            border-color: #454e55;
        }

        .form-grid2 {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 2fr));
            gap: 20px 40px;
            margin-top: 50px;
            margin-bottom: 50px;
        }

        .form-grid2 label {
            display: block;
            margin-bottom: 6px;
            font-weight: 500;
        }

        .form-grid2 input,
        .form-grid2 select,
        .form-grid2 button {
            width: 31%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #a8b2b9;
            font-size: 14px;
        }

        .form-grid2 input:focus,
        .form-grid2 select:focus {
            outline: none;
            border-color: #454e55;
        }

        /* 버튼 행 */
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
        }

        .button-row button:hover {
            background-color: #e0f0d9;
            border-color: #b5d5a9;
        }

        hr {
            border: 1px solid #dddddd;
            margin: 30px 0;
        }

        /* 주민번호/접수일자/접수번호/조회 버튼 한 줄 */
        span#ssnCheckMessage {
            font-size: 13px;
            color: #ff0000;
        }

        .form-grid.single-line-full {
            display: grid;
            grid-template-columns: repeat(3, 1fr) 120px; /* 버튼 고정 폭 */
            gap: 15px 20px;
        }

        .form-grid.single-line-full .button-cell button {
            width: 100%;
        }

        /* 우편번호 + 버튼 옆 배치 */
        .form-grid.single-line-post {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px 20px;
        }

        .post-code-wrapper input {
            width: 68%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #a8b2b9;
        }

        .post-code-wrapper button {
            flex: none;
            width: 30%;
            padding: 8px;
        }

        .button-row button {
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #a8b2b9;
            font-size: 14px;
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
    <div class="tabs">
        <ul>
            <li class="active" data-tab="tab1"><a href="${pageContext.request.contextPath}/application/index">회원 입회 신청</a></li>
            <li data-tab="tab2"><a href="${pageContext.request.contextPath}/application/periodicalList">기간별 입회신청 내역조회</a></li>
            <li data-tab="tab3"><a href="${pageContext.request.contextPath}/application/cardList">소지 카드 내역조회</a></li>
            <li data-tab="tab4"><a href="${pageContext.request.contextPath}/application/cardDetailList">카드 상세 내역조회</a></li>
            <li data-tab="tab5"><a href="${pageContext.request.contextPath}/application/userIndex">회원 색인 조회</a></li>
        </ul>
    </div>

    <div class="tab-content active" id="tab1">
        <h1>회원 입회 신청</h1>

        <!-- 주민번호, 접수일자, 접수번호 조회 -->
        <form method="post">
            <div class="form-grid single-line-full">
                <div>
                    <label for="주민번호">주민번호</label>
                    <input type="text" id="ssn" name="ssn" value="${appl.ssn}"
                           maxlength="14" oninput="filterToNumbers(this)" />
                    <span id="ssnCheckMessage"></span>
                </div>
                <div>
                    <label for="접수일자">접수일자</label>
                    <input type="date" id="rcvD" name="rcvD" value="${appl.rcvD}" />
                </div>
                <div>
                    <label for="접수번호">접수 일련 번호</label>
                    <input type="text" id="rcvSeqNo" name="rcvSeqNo" value="${appl.rcvSeqNo}" />
                </div>
                <div class="button-cell">
                    <label>&nbsp;</label>
                    <button type="submit" formaction="${pageContext.request.contextPath}/application/searchAppl"
                    onclick="validateForm()">조회</button>
                </div>
            </div>

        <hr/>

        <!-- 신청 정보 -->
        <div class="form-grid">
            <div>
                <label for="신청일자">신청일자</label>
                <input type="date" id="applD" name="applD" value="${appl.applD}" />
            </div>
            <div>
                <label for="신청구분">신청구분</label>
                <select id="applClas" name="applClas" value="${appl.applClas}">
                    <option value="">선택</option>
                    <option value="11">최초신규</option>
                    <option value="12">추가신규</option>
                    <option value="21">추가재발급</option>
                </select>
            </div>
            <div>
                <label for="브랜드">브랜드</label>
                <select id="brd" name="brd" value="${appl.brd}">
                    <option value="">선택</option>
                    <option value="1">MASTER</option>
                    <option value="2">VISA</option>
                    <option value="3">JCB</option>
                </select>
            </div>
        </div>

        <!-- 성명/생년월일 -->
        <div class="form-grid">
            <div>
                <label for="성명_한글">성명(한글)</label>
                <input type="text" id="hgNm" name="hgNm" value="${appl.hgNm}" />
            </div>
            <div>
                <label for="성명_영어">성명(영어)</label>
                <input type="text" id="engNm" name="engNm" value="${appl.engNm}" />
            </div>
            <div>
                <label for="생년월일">생년월일</label>
                <input type="date" id="birthD" name="birthD" value="${appl.birthD}" />
            </div>
        </div>

        <!-- 결제 정보 -->
        <div class="form-grid">
            <div>
                <label for="결제일자">결제일자</label>
                <select id="stlDd" name="stlDd" value="${appl.stlDd}">
                    <option value="">선택</option>
                    <option value="05">매달 5일</option>
                    <option value="10">매달 10일</option>
                    <option value="15">매달 15일</option>
                    <option value="20">매달 20일</option>
                    <option value="25">매달 25일</option>
                    <option value="99">매달 말일</option>
                </select>
            </div>
            <div>
                <label for="결제방법">결제 방법</label>
                <select id="stlMtd" name="stlMtd" value="${appl.stlMtd}">
                    <option value="">선택</option>
                    <option value="1">지로</option>
                    <option value="2">자동이체</option>
                    <option value="3">CMS</option>
                </select>
            </div>
            <div>
                <label for="결제은행">결제은행</label>
                <select id="bnkCd" name="bnkCd" value="${appl.bnkCd}">
                    <option value="">선택</option>
                    <option value="002">산업은행</option>
                    <option value="003">기업은행</option>
                    <option value="004">국민은행</option>
                    <option value="005">외환은행</option>
                    <option value="007">수협중앙회</option>
                    <option value="011">농협중앙회</option>
                    <option value="012">농협단위조합</option>
                    <option value="016">축협중앙회</option>
                    <option value="017">축협단위조합</option>
                    <option value="020">우리은행</option>
                    <option value="023">제일은행</option>
                    <option value="026">신한은행</option>
                    <option value="027">시티은행</option>
                </select>
            </div>
        </div>

        <!-- 결제 정보 -->
        <div class="form-grid">
            <div>
                <label for="결제계좌">결제계좌</label>
                <input type="text" id="stlAct" name="stlAct" value="${appl.stlAct}" />
            </div>
            <div>
                <label for="결제계좌">결제계좌 확인 여부</label>
                <input type="text" id="stlActCheck" name="stlActCheck" readonly/>
            </div>
            <div>
                <label for="청구서 발송 방법">청구서 발송 방법</label>
                <select id="stmtSndMtd" name="stmtSndMtd" value="${appl.stmtSndMtd}">
                    <option value="">선택</option>
                    <option value="1">우편</option>
                    <option value="2">E-MAIL</option>
                    <option value="3">청구서 사절</option>
                </select>
            </div>
        </div>

        <!-- 우편번호, 우편번호 확인 버튼, 주소, 상세주소 -->
        <div class="form-grid">
            <div>
                <div class="post-code-wrapper">
                    <label for="우편번호">우편번호</label>
                    <input type="text" id="billadrZip" name="billadrZip" value="${appl.billadrZip}" placeholder="우편번호" />
                    <button type="button" onclick="PostCode()">우편번호 찾기</button>
                </div>
            </div>
            <div>
                <label for="주소">주소</label>
                <input type="text" id="billadrAdr1" name="billadrAdr1" value="${appl.billadrAdr1}" />
            </div>
            <div>
                <label for="상세주소">상세 주소</label>
                <input type="text" id="billadrAdr2" name="billadrAdr2" value="${appl.billadrAdr2}" />
            </div>
        </div>

        <!-- 이메일/핸드폰/비밀번호 -->
        <div class="form-grid">
            <div>
                <label for="이메일">이메일</label>
                <input type="email" id="emailAdr" name="emailAdr" value="${appl.emailAdr}" />
            </div>
            <div>
                <label for="핸드폰">핸드폰 번호</label>
                <input type="tel" id="hdpNo" name="hdpNo" value="${appl.hdpNo}" />
            </div>
            <div>
                <label for="비밀번호">비밀번호</label>
                <input type="password" id="scrtNo" name="scrtNo" value="${appl.scrtNo}" />
            </div>
        </div>

        <!-- 불능 정보 -->
        <div class="form-grid">
            <div>
                <label for="불능구분">불능 구분</label>
                <input type="text" id="impsbClas" name="impsbClas" value="${appl.impsbClas}" readonly/>
            </div>
            <div>
                <label for="불능사유명">불능 사유명</label>
                <input type="text" id="impsbCd" name="impsbCd" value="${appl.impsbCd}" readonly/>
            </div>
        </div>

        <hr/>

        <div class="button-row">
            <button type="submit" formaction="${pageContext.request.contextPath}/application/insertAppl">등록</button>
            <button type="submit">수정</button>
            <button type="button" onclick="clearForm()">초기화</button>
        </div>
        </form>
    </div>


</section>

<script>
    <%-- 탭메뉴 css --%>
    const currentPath = window.location.pathname;
    const tabs = document.querySelectorAll(".tabs ul li");

    tabs.forEach(tab => {
        const link = tab.querySelector("a").getAttribute("href");
        if (link === currentPath) {
            tab.classList.add("active");
        } else {
            tab.classList.remove("active");
        }
    });

    <%-- 초기화 버튼 --%>
    function clearForm() {
        document.querySelectorAll("form input, form select, form textarea").forEach(el => {
            if (el.type === "checkbox" || el.type === "radio") {
                el.checked = false;
            } else {
                el.value = "";
            }
        });
    }

    function PostCode() {
        new daum.Postcode({
            oncomplete: function (data) {
                document.getElementById('billadrZip').value = data.zonecode;
                document.getElementById('billadrAdr1').value = data.address;
                document.getElementById('billadrAdr2').focus();
            }
        }).open();
    }

    function validateForm() {
        const ssn = document.getElementById('ssn').value;
        const rcvD = document.getElementById('rcvD').value;
        const rcvSeqNo = document.getElementById('rcvSeqNo').value;

        if (!ssn) {
            alert('주민번호를 입력해주세요.');
            return false;
        }

        if (!rcvD) {
            alert('접수 일자를 입력해주세요.');
            return false;
        }

        if (!rcvSeqNo) {
            alert('접수 일련 번호를 입력해주세요.');
            return false;
        }
        return true;
    }

    // 주민번호 유효성 검사
    function filterToNumbers(input) {
        const messageSpan = document.getElementById('ssnCheckMessage');

        var regex = /^[0-9]{6}(?:0[1-9]|1[0-2])(?:0[1-9]|[1-2][0-9]|3[0-1])[0-9]{6}$|^[0-9]{7}(?:0[1-9]|1[0-2])(?:0[1-9]|[1-2][0-9]|3[0-1])[0-9]{3}$|^[0-9]{13}$/;

        // 숫자만 남기기 (하이픈 제외)
        let numbers = input.value.replace(/[^0-9]/g, '');

        // 자리 자리 검사
        if (numbers.length >= 3) {
            const thirdDigit = numbers.charAt(2);
            const sevenDigit = numbers.charAt(6);
            if (thirdDigit !== '0' && thirdDigit !== '1' && sevenDigit !== '1'
                && sevenDigit !== '2' && sevenDigit !== '3' && sevenDigit !== '4') {
                messageSpan.textContent = '올바른 주민번호를 입력해주세요';
            } else {
                messageSpan.textContent = '';
            }
        }

        // 하이픈 추가
        if (numbers.length > 6) {
            input.value = numbers.substring(0, 6) + '-' + numbers.substring(6, 13);
        } else {
            input.value = numbers;
        }

        // 주민번호 자릿수 유효성 검사
        if (numbers.length !== 13) {
            messageSpan.textContent = '주민번호 13자리를 전부 입력해주세요';
        } else {
            // 세 번째 자리 유효성 통과했을 경우에만 메시지 제거
            if (numbers.charAt(2) === '0' || numbers.charAt(2) === '1'
                || numbers.charAt(6) === '1' || numbers.charAt(6) === '2'
                || numbers.charAt(6) === '3' || numbers.charAt(6) === '4') {
                messageSpan.textContent = '';
            }
        }
    }
</script>
</body>
</html>
