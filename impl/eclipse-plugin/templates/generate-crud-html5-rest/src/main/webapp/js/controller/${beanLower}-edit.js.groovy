<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>
\$(function() {
	\$("#delete").hide();

	if (id = App.getUrlParameterByName('${idName}')) {
		${pojo}Proxy.load(id).done(loadOk).fail(loadFailed);
	}

	MetadataProxy.getDemoiselleVersion().done(function(data) {
		\$("#demoiselle-version").html(data);
	});

	\$("form").submit(function(event) {
		event.preventDefault();
	});	
	\$("#save").click(function() {
	var data = {
<%
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue ->
		if (!PU.hasAnnotationForField(tmpFile, attrName, 'GeneratedValue' )) {
	
%>
			${attrName} : \$("#${attrName}").val(),
<%		}
	}
}
%>
		};
		if (id = \$("#${idName}").val()) {
			${pojo}Proxy.update(id, data).done(saveOk).fail(saveFailed);
		} else {
			${pojo}Proxy.insert(data).done(saveOk).fail(saveFailed);
		}
	});

	\$("#delete").click(function() {
		bootbox.confirm("Tem certeza que deseja apagar?", function(result) {
			if (result) {
				${pojo}Proxy.remove([ \$("#${idName}").val() ]).done(removeOk);
			}
		});
	});

	\$("#back").click(function() {
		history.back();
	});

<%
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue ->
		if (attrValue.equalsIgnoreCase('Date')) {
%>
	\$("#${attrName}").datepicker({
	showButtonPanel : true,
	dateFormat : "yy-dd-mm",
	regional : "pt-BR"
});
<%		}
	}
}
%>

});

function loadOk(data) {
	\$("#${idName}-row").show();
	\$("#${idName}-text").html(data.id);
	\$("#${idName}").val(data.id);
	<%
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue ->
%>
	\$("#${attrName}").val(data.${attrName});
<%
	}
}
%>	
	\$("#delete").show();
}

function loadFailed(request) {
	switch (request.status) {
		case 404:
			bootbox.alert("Você está tentando acessar um registro inexistente!", function() {
				location.href = "${beanLower}-list.html";
			});
			break;

		default:
			break;
	}
}

function saveOk(data) {
	location.href = '${beanLower}-list.html';
}

function saveFailed(request) {
	switch (request.status) {
		case 422:
			\$(\$("form input").get().reverse()).each(function() {
				var id = \$(this).attr('${idName}');
				var message = null;

				\$.each(request.responseJSON, function(index, value) {
					if (id == value.property) {
						message = value.message;
						return;
					}
				});

				if (message) {
					\$("#" + id + "-message").html(message).show();
					\$(this).focus();
				} else {
					\$("#" + id + "-message").hide();
				}
			});
			break;
		default:
			break;
	}
}
function removeOk(data) {
	location.href = '${beanLower}-list.html';
}