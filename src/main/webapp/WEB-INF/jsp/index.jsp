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
        span.checkMessage {
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

        #stlActCheck, #impsbClas, #impsbMsg {
            color : #ff0000;
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
        <form method="post" onsubmit="return subValidateForm()">
            <div class="form-grid single-line-full">
                <div>
                    <label for="주민번호">주민번호</label>
                    <input type="text" id="ssn" value="${appl.ssn}"
                           maxlength="14" oninput="filterToNumbers(this);" />
                    <input type="hidden" id="ssnOriginal" name="ssn">
                    <span id="ssnCheckMessage" class="checkMessage"></span>
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
                    <button type="button" onclick="searchAppl()">조회</button>
                </div>
            </div>

        <hr/>

        <!-- 신청 정보 -->
        <div class="form-grid">
            <div>
                <label for="신청일자">신청일자</label>
                <input type="date" id="applD" name="applD" value="${appl.applD}" readonly />
            </div>
            <div>
                <label for="신청구분">신청구분</label>
                <select id="applClas" name="applClas" value="${appl.applClas}">
                    <option value="">선택</option>
                    <option value="11">최초신규</option>
                    <option value="12">추가신규</option>
                    <option value="21">재발급</option>
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
                <input type="text" id="engNm" name="engNm" value="${appl.engNm}" oninput="allowEnglishOnly(this)" />
            </div>
            <div>
                <label for="생년월일">생년월일</label>
                <input type="text" id="birthD" name="birthD" value="${appl.birthD}" readonly />
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
                <input type="text" id="stlActCheck" name="stlActCheck" value="${appl.stlActCheck}" readonly/>
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
                <input type="text" id="emailAdr" name="emailAdr" value="${appl.emailAdr}" oninput="checkEmail(this)" />
                <span id="emailCheckMessage" class="checkMessage"></span>
            </div>
            <div>
                <label for="핸드폰">핸드폰 번호</label>
                <input type="tel" id="hdpNo" name="hdpNo" value="${appl.hdpNo}"
                oninput="filterToPhone(this)" />
            </div>
            <div>
                <label for="비밀번호">비밀번호</label>
                <input type="password" maxlength="4" id="scrtNo" name="scrtNo" value="${appl.scrtNo}"
                oninput="allowNumbersOnly(this)"/>
            </div>
        </div>

        <!-- 불능 정보 -->
        <div class="form-grid">
            <div>
                <label for="불능구분">불능 구분</label>
                <input type="text" id="impsbClas" name="impsbClas" value="${appl.impsbClas}" readonly/>
            </div>
            <div>
                <label for="불능사유명">불능 사유</label>
                <input type="text" id="impsbMsg" name="impsbMsg" value="${appl.impsbMsg}" readonly/>
                <input type="hidden" id="impsbCd" name="impsbCd" value="${appl.impsbCd}" readonly/>
            </div>
        </div>

        <hr/>

            <div class="button-row">
                <button type="submit" formaction="${pageContext.request.contextPath}/application/insertAppl">등록</button>
                <button type="submit" id="editBtn" style="display:none;"
                 formaction="${pageContext.request.contextPath}/application/updateAppl">수정</button>
                <button type="button" onclick="clearForm()">초기화</button>
            </div>
        </form>
    </div>


    <%--  결과값  --%>
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>

</section>
<script>
    let ssnChecked = false;
    let emailChecked = false;

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

    // 주민번호 유효성 검사 + 마스킹
    function filterToNumbers(input) {
        const messageSpan = document.getElementById('ssnCheckMessage');
        const birthDInput = document.getElementById('birthD');
        const hiddenInput = document.getElementById('ssnOriginal'); // hidden input (원본 저장)

        // 숫자만 입력 가능
        let numbers = input.value.replace(/[^0-9]/g, '');

        // 자리수 검사 (총 13자리여야 함, 하이픈 제외)
        if (numbers.length < 13) {
            messageSpan.textContent = '주민번호 13자리를 전부 입력해주세요';
            birthDInput.value = "";
            hiddenInput.value = ""; // 원본 초기화
            return;
        }

        // 원본값 저장 (13자리)
        hiddenInput.value = numbers;

        // 하이픈 포함 원본값 만들기
        const originalFormatted = numbers.substring(0, 6) + '-' + numbers.substring(6, 13);

        // 화면에는 8번째 자리부터 마스킹 처리
        input.value = numbers.substring(0, 6) + '-' + numbers.charAt(6) + '******';

        // 생년월일 + 성별 코드로 출생년도 계산
        const yy = parseInt(numbers.substring(0, 2), 10);
        const mm = parseInt(numbers.substring(2, 4), 10);
        const dd = parseInt(numbers.substring(4, 6), 10);
        const genderCode = numbers.charAt(6);

        // 연도 계산 (세기 구분)
        let fullYear;
        if (['1', '2', '5', '6'].includes(genderCode)) {
            fullYear = 1900 + yy;
        } else if (['3', '4', '7', '8'].includes(genderCode)) {
            fullYear = 2000 + yy;
        } else {
            messageSpan.textContent = '올바른 주민번호를 입력해주세요';
            birthDInput.value = "";
            hiddenInput.value = "";
            return;
        }

        // 생년월일 input에 값 세팅
        birthDInput.value = fullYear + String(mm).padStart(2, '0') + String(dd).padStart(2, '0');

        // 나이 계산
        const today = new Date();
        let age = today.getFullYear() - fullYear;
        if (today.getMonth() + 1 < mm || (today.getMonth() + 1 === mm && today.getDate() < dd)) age--;

        if (age < 19) {
            messageSpan.textContent = '미성년자는 신청할 수 없습니다';
            hiddenInput.value = "";
            return;
        }

        // 날짜 유효성 체크
        const birthDate = new Date(fullYear, mm - 1, dd);
        if (birthDate.getFullYear() !== fullYear || birthDate.getMonth() + 1 !== mm || birthDate.getDate() !== dd) {
            messageSpan.textContent = '유효하지 않은 생년월일입니다';
            hiddenInput.value = "";
            return;
        }

        // 체크디지트 검증
        const multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];
        let sum = 0;
        for (let i = 0; i < 12; i++) {
            sum += parseInt(numbers.charAt(i), 10) * multipliers[i];
        }
        const checkDigit = (11 - (sum % 11)) % 10;
        if (checkDigit !== parseInt(numbers.charAt(12), 10)) {
            messageSpan.textContent = '주민번호 검증에 실패했습니다';
            hiddenInput.value = "";
            return;
        }

        // 모든 검사 통과
        messageSpan.textContent = '';
        ssnChecked = true;
    }


    // 신청 날짜는 현재 날짜로 디폴트 처리
        window.addEventListener("DOMContentLoaded", () => {
            const applD = document.getElementById("applD");
            if (!applD.value) {
                const today = new Date().toISOString().split("T")[0];
                applD.value = today;
            }
        });

        // 영문 이름 입력칸 영문만 입력 가능
        function allowEnglishOnly(input) {
            input.value = input.value.replace(/[^a-zA-Z\s]/g, '');
        }

        // 핸드폰 번호 입력시 하이픈 자동 추가
        function filterToPhone(input) {
            // 숫자만 남기기
            let numbers = input.value.replace(/[^0-9]/g, '');

            if (numbers.length <= 3) {
                input.value = numbers;
            } else if (numbers.length <= 7) {
                input.value = numbers.substring(0, 3) + '-' + numbers.substring(3);
            } else {
                input.value = numbers.substring(0, 3) + '-' + numbers.substring(3, 7) + '-' + numbers.substring(7, 11);
            }
        }

        // 비밀번호 입력시, 숫자만 가능
        function allowNumbersOnly(input) {
            input.value = input.value.replace(/[^0-9]/g, '');
        }

        // 이메일 유효성 검사
        function checkEmail() {
            const messageSpan = document.getElementById('emailCheckMessage');

            const emailInput = document.getElementById('emailAdr').value.trim();
            const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

            if (!emailRegex.test(emailInput)) {
                messageSpan.textContent = '올바른 이메일 주소를 작성해주세요.';
                return false;
            }

            messageSpan.textContent = '';
            emailChecked = true;
        }

        // DB 저장 시 '-' 제거
        document.querySelector("form").addEventListener("submit", function() {
            const hdpNoInput = document.getElementById("hdpNo");
            hdpNoInput.value = hdpNoInput.value.replace(/-/g, '');
        });


    // 조회 기능
    function searchAppl() {
        const ssn = document.getElementById("ssnOriginal").value;
        const rcvD = document.getElementById("rcvD").value;
        const rcvSeqNo = document.getElementById("rcvSeqNo").value;

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

        fetch("${pageContext.request.contextPath}/application/searchAppl", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ssn, rcvD, rcvSeqNo })
        })
            .then(res => res.json())
            .then(data => {
                if (data.message) {
                    alert(data.message);
                } else {
                    for (let key in data) {
                        const el = document.getElementById(key);
                        if (el) el.value = data[key];
                    }

                    // 수정 버튼 노출 조건 체크
                    if (data.impsbCd === "02" || data.impsbCd === "03") {
                        document.getElementById("editBtn").style.display = "inline-block";
                    } else {
                        document.getElementById("editBtn").style.display = "none";
                    }
                }
            })
            .catch(err => console.error("조회 에러:", err));
    }

    // 등록 시 데이터 유효성 체크
    function subValidateForm() {
        const ssn = document.getElementById("ssn").value;
        const applClas = document.getElementById("applClas").value;
        const brd = document.getElementById("brd").value;
        const hgNm = document.getElementById("hgNm").value;
        const engNm = document.getElementById("engNm").value;
        const stlDd = document.getElementById("stlDd").value;
        const stlMtd = document.getElementById("stlMtd").value;
        const bnkCd = document.getElementById("bnkCd").value;
        const stlAct = document.getElementById("stlAct").value;
        const stmtSndMtd = document.getElementById("stmtSndMtd").value;
        const billadrZip = document.getElementById("billadrZip").value;
        const billadrAdr1 = document.getElementById("billadrAdr1").value;
        const billadrAdr2 = document.getElementById("billadrAdr2").value;
        const emailAdr = document.getElementById("emailAdr").value;
        const hdpNo = document.getElementById("hdpNo").value;
        const scrtNo = document.getElementById("scrtNo").value;

        if (!ssn) {
            alert('주민번호를 입력해주세요.');
            return false;
        }
        if (!ssnChecked) {
            alert('올바른 주민등록 번호를 입력해주세요.');
            return false;
        }
        if (!applClas) {
            alert('신청 구분을 선택해주세요');
            return false;
        }
        if (!brd) {
            alert('브랜드를 선택해주세요');
            return false;
        }
        if (!hgNm) {
            alert('한글 이름을 입력해주세요');
            return false;
        }
        if (!engNm) {
            alert('영문 이름을 입력해주세요');
            return false;
        }
        if (!stlDd) {
            alert('결제 일자를 입력해주세요');
            return false;
        }
        if (!stlMtd) {
            alert('결제 방법을 선택해주세요');
            return false;
        }
        if (!bnkCd) {
            alert('결제 은행을 선택해주세요');
            return false;
        }
        if (!stlAct) {
            alert('결제 계좌를 입력해주세요');
            return false;
        }
        if (!stmtSndMtd) {
            alert('청구서 발송 방법을 선택해주세요');
            return false;
        }
        if (!billadrZip) {
            alert('우편번호를 입력해주세요');
            return false;
        }
        if (!billadrAdr1) {
            alert('주소를 입력해주세요');
            return false;
        }
        if (!billadrAdr2) {
            alert('상세 주소를 입력해주세요');
            return false;
        }
        if (!emailAdr) {
            alert('이메일 주소를 입력해주세요');
            return false;
        }
        if (!emailChecked) {
            alert('올바른 이메일 주소를 입력해주세요.');
            return false;
        }
        if (!hdpNo) {
            alert('핸드폰 번호를 입력해주세요');
            return false;
        }
        if (!scrtNo) {
            alert('비밀번호를 입력해주세요');
            return false;
        }
            return true;
    }

</script>
</body>
</html>
