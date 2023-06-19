var realCurrentPage;
var realPageSize;

function dataViewPageLoad() {
	// Get the DataView component by its widgetVar
	var dataViewWidget = PF('realEstatesDataView');

	// Get the Paginator widget associated with the DataView
	var paginatorWidget = dataViewWidget.getPaginator();

	// Get the current page number and page size
	realCurrentPage = paginatorWidget.getCurrentPage();
	realPageSize = paginatorWidget.getRows();
}


function initSwitch() {
	if ($('.bs_switch').length) {
		$(".bs_switch").bootstrapSwitch({
			inverse : true
		});
		$('.bootstrap-switch-handle-on').html('نعم');
		$('.bootstrap-switch-handle-off').html('كلا');
	}

}