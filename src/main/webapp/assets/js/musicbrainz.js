function MBQuery(options) {
	this.queryString = options.queryString;
	this.entity = options.entity;
	this.additionalParams = options.additionalParams || '';
	this.success = options.success;
	this.fail = options.fail;
	this.id = undefined; // we will get this if our search is successful
}

MBQuery.prototype.getResults = function () {
	$.when( this.searchForString() )
		.pipe(this.stringFound)
		.then(this.success)
		.fail(this.fail);
}

MBQuery.prototype.searchForString = function() {

	var GETRequestData = {
		query: '"' + this.queryString + '"',
		limit: 30
	};

	return $.ajax({
		url: 'http://musicbrainz.org/ws/2/' + this.entity,
		method: 'GET',
		data: GETRequestData,
		context: this,
		dataType: 'xml',
	});

}

MBQuery.prototype.stringFound = function(ajaxData) {

	var candidate = new MBCandidate(ajaxData, this);
	if ( !candidate.checkExactMatch() ) {
		return $.Deferred().reject('Not found');
	}
	this.id = candidate.getId();
	return this.runQuery();
	
}

MBQuery.prototype.runQuery = function() {

	return $.ajax({
		url: 'http://musicbrainz.org/ws/2/' + this.entity + '/' + this.id + this.additionalParams,
		method: 'GET',
		dataType: 'xml',
	});

}

function MBCandidate(response, query) {
	this.XML = response;
    this.query = query;
    this.matchChecked = false;
}

MBCandidate.prototype.getId = function( ) {
	var id;
	if ( ( id = $(this.XML).attr('id') ) !== undefined )
		return id;
	return $(this.XML).find(this.query.entity).attr('id');
}

MBCandidate.prototype.checkExactMatch = function() {
	
	// this function is necessary as MB sometimes returns the wrong result with the highest score.
	// it does a basic strcmp between the name of the query and each of the results obtained
	// then strips out incorrect answers from its own .XML property
	
	var currentObj = this;
	var match = false;

	$(this.XML).find(this.query.entity).each(function ( i, e ) {
		var foundName  = $(e).find('name').first().text();
		if ( stringsEq ( foundName, currentObj.query.queryString ) ) {
      console.log('match');
			currentObj.XML = e; // reassign the correct result as the candidates XML and prepare to return true
			match = true;
		}
	});

	this.matchChecked = true;
	return match;
}

function stringsEq(a, b) {
	a = $.trim(a.toLowerCase());
	b = $.trim(b.toLowerCase());
	if ( a === b ) return true;
	return false;
}
