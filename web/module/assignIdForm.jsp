<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Register Patients" otherwise="/login.htm" redirect="/module/amrsregistration/start.form"/>

<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="localHeader.jsp" %>

<script type="text/javascript">
	
	function parseDate(d) {
		var str = '';
		if (d != null) {
			
			// get the month, day, year values
			var month = "";
			var day = "";
			var year = "";
			var date = d.getDate();
			
			if (date < 10)
				day += "0";
			day += date;
			var m = d.getMonth() + 1;
			if (m < 10)
				month += "0";
			month += m;
			if (d.getYear() < 1900)
				year = (d.getYear() + 1900);
			else
				year = d.getYear();
		
			var datePattern = '<openmrs:datePattern />';
			var sep = datePattern.substr(2,1);
			var datePatternStart = datePattern.substr(0,1).toLowerCase();
			
			if (datePatternStart == 'm') /* M-D-Y */
				str = month + sep + day + sep + year
			else if (datePatternStart == 'y') /* Y-M-D */
				str = year + sep + month + sep + day
			else /* (datePatternStart == 'd') D-M-Y */
				str = day + sep + month + sep + year
			
		}
		return str;
	}
	
	function updatePatient(element) {
		var form = element.form;
		form.submit();
	}
</script>

<b class="boxHeader">Possible Matched Patient Data</b>
<div class="box">
<form id="switchPatient" method="post">
	<c:choose>
		<c:when test="${fn:length(potentialMatches) > 0}">
		    <div>
		        <table border="0" cellspacing="2" cellpadding="2">
		            <tr>
		            	<th>Use this patient?</th>
		            	<th>Identifier</th>
		            	<th>First Name</th>
		            	<th>Last Name</th>
		            	<th>DOB</th>
		            </tr>
		    		<c:forEach items="${potentialMatches}" var="person" varStatus="varStatus">
		    			<tr>
		    				<c:forEach items="${person.identifiers}" var="identifier" varStatus="varStatus">
		    					<c:if test="${varStatus.index == 0}">
			    				<td align="center">
									<input type="hidden" name="_idCardInput">
									<input type="radio" name="idCardInput" value="${identifier.identifier}" onclick="updatePatient(this)" />
	            				</td>
			    				<td>
	            				</c:if>
			    					<input type="text" name="matchedIdentifier" value="${identifier.identifier}" />
		    				</c:forEach>
			    				</td>
		    				<td>
		    					<input type="text" name="matchedGivenname" value="${person.personName.givenName}" />
		    				</td>
		    				<td>
		    					<input type="text" name="matchedLastname" value="${person.personName.familyName}" />
		    				</td>
		    				<td>
		    					<input type="text" name="matchedDob" value="${person.birthdate}" />
		    				</td>
		    			</tr>
		    		</c:forEach>
		        </table>
		    </div>
		</c:when>
		<c:otherwise>
			No potential matches found
		</c:otherwise>
	</c:choose>
</form>
</div>
<br />
<form id="patientForm" method="post">
	<%@ include file="portlets/personInfo.jsp" %>
	<br />
    <input type="submit" name="_cancel" value="<spring:message code='amrsregistration.button.startover'/>">
    &nbsp; &nbsp;
	<input type="submit" name="_target1" value="<spring:message code='amrsregistration.button.edit'/>">
	&nbsp; &nbsp;
    <input type="submit" name="_target3" value="<spring:message code='amrsregistration.button.save'/>">
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>