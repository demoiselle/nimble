<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${pojo} List</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/${projectName}.css">
<link rel="stylesheet" href="css/datatables.css">
</head>
<body>
	<div class="container">
		<div id="menu"></div>
		<p>
			<button id="new" class="btn btn-success">Novo</button>
			<button id="delete" class="btn btn-danger">Excluir</button>
		</p>
		<table id="resultList" class="datatable table table-striped table-bordered">
			<thead>
				<tr>
					<th width="3px"></th>
			<%
				if (!attrList.isEmpty()) {
					attrList.each() { attrName, attrValue ->
						if (!PU.hasAnnotationForField(tmpFile, attrName, 'GeneratedValue' )) {
				%>
					<th>${attrName}</th>
				<%		}
					}
				}
				%>
				</tr>
			</thead>
		</table>
	</div>
	<p class="demoiselle">
		Aplica\u00E7\u00E3o de exemplo do Demoiselle <span id="demoiselle-version"></span>
	</p>
	<script type="text/javascript" src="js/lib/jquery-2.1.0.min.js"></script>
	<script type="text/javascript" src="js/lib/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/lib/jquery.dataTables.js"></script>
	<script type="text/javascript" src="js/lib/datatables.js"></script>
	<script type="text/javascript" src="js/lib/bootbox.js"></script>
	<script type="text/javascript" src="js/lib/app.js"></script>
	<script type="text/javascript" src="js/proxy/metadata.js"></script>
	<script type="text/javascript" src="js/proxy/auth.js"></script>
	<script type="text/javascript" src="js/proxy/${beanLower}.js"></script>
	<script type="text/javascript" src="js/controller/menu.js"></script>
	<script type="text/javascript" src="js/controller/${beanLower}-list.js"></script>
</body>
</html>