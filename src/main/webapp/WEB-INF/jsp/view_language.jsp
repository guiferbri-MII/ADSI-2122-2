<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
<style>
body {
	font: Arial 12px;
	text-align: center;
}

.link {
	stroke: #ccc;
}

.node text {
	pointer-events: none;
	font: sans-serif;
}
</style>

<!-- 
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/d3/4.6.0/d3.min.js"></script>

<c:url value="/js/bubble_chart.js" var="jstlJs" />
<script src="${jstlJs}"></script>
</head>
<body>
	<h3>Language Analysis</h3>
	<%-- 			<h2>Message: ${message}</h2> --%>

	<script type="text/javascript"
		src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<div id="chart"><svg></svg></div>
	<script>
		data = ${data};
        var chart = bubbleChart().width(600).height(400);
        d3.select('#chart').datum(data).call(chart);  
	</script>
</body>

</html>