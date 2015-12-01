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
<title>${pojo} Edit</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/${projectName}.css">
<link rel="stylesheet" href="css/jquery-ui.css">
</head>
<body>
	<div class="container">
		<div id="menu"></div>
		<form class="well" role="form">
			<div id="${idName}-row" hidden="true" class="form-group">
				<label for="id">${idName}</label> <span id="${idName}-text" class="form-control-static"></span>
				<input id="${idName}" type="hidden">
			</div>
				<%
				if (!attrList.isEmpty()) {
					attrList.each() { attrName, attrValue ->
						if (!PU.hasAnnotationForField(tmpFile, attrName, 'GeneratedValue' )) {
				%>
			<div class="form-group">
				<label for="${attrName}">${attrName}</label>
				<input id="${attrName}" type="text" class="form-control">
				<div id="${attrName}-message" class="label label-danger"></div>
			</div>
				<%		}
					}
				}
				%>
			<div class="form-group">
				<button id="save" class="btn btn-primary">Salvar</button>
				<button id="delete" class="btn btn-danger" hidden="true">Excluir</button>
				<button id="back" class="btn btn-warning">Voltar</button>
			</div>
		</form>
	</div>
	<p class="demoiselle">
		Aplicação de exemplo do Demoiselle <span id="demoiselle-version"></span>
	</p>
	<script type="text/javascript" src="js/lib/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/lib/jquery-ui.js"></script>
	<script type="text/javascript" src="js/lib/datepicker-pt-BR.js"></script>
	<script type="text/javascript" src="js/lib/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/lib/purl.js"></script>
	<script type="text/javascript" src="js/lib/bootbox.js"></script>
	<script type="text/javascript" src="js/lib/app.js"></script>
	<script type="text/javascript" src="js/proxy/metadata.js"></script>
	<script type="text/javascript" src="js/proxy/auth.js"></script>
	<script type="text/javascript" src="js/proxy/${beanLower}.js"></script>
	<script type="text/javascript" src="js/controller/menu.js"></script>
	<script type="text/javascript" src="js/controller/${beanLower}-edit.js"></script>
</body>