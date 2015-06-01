<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>
\$(function() {
	\$("#new").focus();

	${pojo}Proxy.findAll().done(findAllOk);

	MetadataProxy.getDemoiselleVersion().done(function(data) {
		\$("#demoiselle-version").html(data);
	});

	\$("form").submit(function(event) {
		event.preventDefault();
	});

	\$("#new").click(function() {
		location.href = "${beanLower}-edit.html";
	});

	\$("#delete").click(function() {
		var ids = [];

		\$("input:checked").each(function(index, value) {
			ids.push(\$(value).val());
		});

		if (ids.length == 0) {
			bootbox.alert({
				message : "Nenhum registro selecionado"
			});
		} else {
			bootbox.confirm("Tem certeza que deseja apagar?", function(result) {
				if (result) {
					${pojo}Proxy.remove(ids).done(removeOk);
				}
			});
		}
	});
});

function findAllOk(data) {
	\$('#resultList').dataTable({
		"aoColumns" : [ {
			"aTargets" : [ 0 ],
			"mDataProp" : "${idName}",
			"mRender" : function(id) {
				return '<input id="remove-' + id + '" type="checkbox" value="' + id + '">';
			}
<%
	if (!attrList.isEmpty()) {
		attrList.each() { attrName, attrValue ->
	     def i = 0;
		 if (!PU.hasAnnotationForField(tmpFile, attrName, 'GeneratedValue' )) {
			 ++i;
			%>
		}, {
		"aTargets" : [ ${i} ],
		"mDataProp" : "${attrName}",
		"mRender" : function(data, type, full) {
			return '<a href="${beanLower}-edit.html?id=' + full.id + '">' + full.${attrName} + '</a>';
		}

	<%  }
		}
	}
%>						
		} ],
		"oLanguage" : {
			"sInfo" : "Mostrando _START_ a _END_ de _TOTAL_ registros",
			"sEmptyTable" : "Não há dados disponíveis na tabela",
			"sLengthMenu" : "Mostrar _MENU_ registros",
			"sInfoThousands" : "",
			"oPaginate" : {
				"sFirst" : "Primeiro",
				"sLast" : "Último",
				"sNext" : "Próximo",
				"sPrevious" : "Anterior"
			}
		},
		"bFilter" : false,
		"bDestroy" : true,
		"sPaginationType" : "bs_full",
		"aaData" : data,
		"bSort" : true
	});
}

function removeOk() {
	${pojo}Proxy.findAll().done(findAllOk);
}