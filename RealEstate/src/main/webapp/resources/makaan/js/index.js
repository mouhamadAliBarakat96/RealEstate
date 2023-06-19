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